package com.example.ksp_sample.user.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ksp_sample.KspSampleApplication
import com.example.ksp_sample.db.entity.User
import com.example.ksp_sample.view_model_factory.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@ViewModelFactory
class UserDetailViewModel(
    saveHandle: SavedStateHandle,
    app: KspSampleApplication
) : ViewModel() {

    enum class Args {
        ID
    }

    private val id = saveHandle.get<Int>(Args.ID.name)!!

    private val userDao = app.db.userDao()

    val user: StateFlow<User?> get() = userDao.getById(id)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    sealed interface UpdateResult {
        data object Loading : UpdateResult
        data object Success : UpdateResult
        data class Failure(val exception: Throwable) : UpdateResult
    }

    private val _updateResult: MutableStateFlow<UpdateResult> = MutableStateFlow(UpdateResult.Loading)
    val updateResult: StateFlow<UpdateResult> get() = _updateResult.asStateFlow()

    fun updateUser(user: User) {
        _updateResult.value = UpdateResult.Loading

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                if (user.name.isBlank()) throw IllegalArgumentException("Name is blank")

                userDao.update(user)
            }.fold(
                onSuccess = {
                    _updateResult.value = UpdateResult.Success
                },
                onFailure = {
                    _updateResult.value = UpdateResult.Failure(it)
                }
            )
        }
    }
}
