package com.example.ksp_sample.user.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ksp_sample.KspSampleApplication
import com.example.ksp_sample.ui.theme.KspSampleTheme
import kotlinx.coroutines.launch

class CreateUserActivity : ComponentActivity() {

    companion object {

        fun intent(context: Context): Intent = Intent(context, CreateUserActivity::class.java)
    }

    private val viewModel: CreateUserViewModel by viewModels {
        CreateUserViewModelFactory(application as KspSampleApplication)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            KspSampleTheme {
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                val keyboardController = LocalSoftwareKeyboardController.current

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Create User")
                            },
                            modifier = Modifier.shadow(1.dp),
                            navigationIcon = {
                                IconButton(
                                    onClick = { finish() }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        )
                    },
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) { innerPadding ->
                    val insertResult by viewModel.insertResult.collectAsState()

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(20.dp)
                    ) {
                        var name by remember { mutableStateOf("") }

                        TextField(
                            value = name,
                            onValueChange = {
                                name = it
                            },
                            label = {
                                Text(
                                    text = "User Name",
                                    fontSize = 14.sp
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { name = "" }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "clear"
                                    )
                                }
                            },
                            isError = insertResult is CreateUserViewModel.InsertResult.Failure,
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    viewModel.insertUser(name)
                                }
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                keyboardController?.hide()
                                viewModel.insertUser(name)
                            }
                        ) {
                            Text("Add")
                        }
                    }

                    LaunchedEffect(insertResult) {
                        when (insertResult) {
                            is CreateUserViewModel.InsertResult.Success -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "User added successfully"
                                    )
                                }
                            }
                            is CreateUserViewModel.InsertResult.Failure -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        (insertResult as CreateUserViewModel.InsertResult.Failure).exception.message ?: "Failed to add user"
                                    )
                                }
                            }
                            else -> return@LaunchedEffect
                        }
                    }
                }
            }
        }
    }
}
