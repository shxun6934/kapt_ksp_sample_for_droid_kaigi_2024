package com.example.ksp_sample.user.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ksp_sample.db.AppDatabase
import com.example.ksp_sample.db.dao.UserDao
import com.example.ksp_sample.db.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(db: AppDatabase) : ViewModel() {

    private val userDao: UserDao = db.userDao()

    sealed interface UiState {
        data object Loading : UiState
        data object Empty : UiState
        data class Success(val users: List<User>) : UiState
    }

    val uiState: StateFlow<UiState> get() = userDao.getAll()
        .map { users ->
            if (users.isEmpty()) {
                UiState.Empty
            } else {
                UiState.Success(users)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UiState.Loading
        )

    sealed interface DeleteResult {
        data object Loading : DeleteResult
        data object Success : DeleteResult
        data class Failure(val exception: Throwable) : DeleteResult
    }

    private val _deleteResult: MutableStateFlow<DeleteResult> = MutableStateFlow(DeleteResult.Loading)
    val deleteResult: StateFlow<DeleteResult> get() = _deleteResult.asStateFlow()

    fun deleteUser(user: User) {
        _deleteResult.value = DeleteResult.Loading

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                userDao.delete(user)
            }.fold(
                onSuccess = {
                    _deleteResult.value = DeleteResult.Success
                },
                onFailure = {
                    _deleteResult.value = DeleteResult.Failure(it)
                }
            )
        }
    }
}
