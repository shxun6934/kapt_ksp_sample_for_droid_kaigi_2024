package com.example.ksp_sample.user.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ksp_sample.KspSampleApplication
import com.example.ksp_sample.db.dao.UserDao
import com.example.ksp_sample.db.entity.User
import com.example.ksp_sample.view_model_factory.ViewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@ViewModelFactory
class UserListViewModel(app: KspSampleApplication) : ViewModel() {

    private val userDao: UserDao = app.db.userDao()

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

    // TODO: 削除メソッド
}
