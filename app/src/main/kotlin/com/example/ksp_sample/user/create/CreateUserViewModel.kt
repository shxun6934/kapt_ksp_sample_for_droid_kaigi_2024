package com.example.ksp_sample.user.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ksp_sample.db.AppDatabase
import com.example.ksp_sample.db.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(db: AppDatabase) : ViewModel() {

    private val userDao = db.userDao()

    sealed interface InsertResult {
        data object Loading : InsertResult
        data object Success : InsertResult
        data class Failure(val exception: Throwable) : InsertResult
    }

    private val _insertResult: MutableStateFlow<InsertResult> = MutableStateFlow(InsertResult.Loading)
    val insertResult: StateFlow<InsertResult> = _insertResult.asStateFlow()

    fun insertUser(name: String) {
        _insertResult.value = InsertResult.Loading

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                if (name.isBlank()) throw IllegalArgumentException("Name is blank")

                userDao.insert(User(name = name))
            }.fold(
                onSuccess = {
                    _insertResult.value = InsertResult.Success
                },
                onFailure = {
                    _insertResult.value = InsertResult.Failure(it)
                }
            )
        }
    }
}
