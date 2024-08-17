package com.example.ksp_sample.user.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ksp_sample.ui.theme.KspSampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailActivity : ComponentActivity() {

    companion object {

        fun intent(
            context: Context,
            id: Int
        ): Intent = Intent(context, UserDetailActivity::class.java)
            .putExtra(UserDetailViewModel.Args.ID.name, id)
    }

    private val viewModel: UserDetailViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            KspSampleTheme {
                var showBottomSheet by remember { mutableStateOf(false) }

                val keyboardController = LocalSoftwareKeyboardController.current
                
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("User Detail")
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
                    }
                ) { innerPadding ->
                    val user by viewModel.user.collectAsState()

                    val updateResult by viewModel.updateResult.collectAsState()

                    Column(
                        modifier = Modifier.padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "id",
                                modifier = Modifier.weight(0.2f),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = user?.id.toString(),
                                modifier = Modifier.weight(0.8f),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(
                            modifier = Modifier
                                .clickable {
                                    showBottomSheet = true
                                }
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "name",
                                modifier = Modifier.weight(0.2f),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = user?.name ?: "",
                                modifier = Modifier.weight(0.8f),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showBottomSheet = false },
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                var name by remember { mutableStateOf(user?.name ?: "") }

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
                                    isError = updateResult is UserDetailViewModel.UpdateResult.Failure,
                                    singleLine = true,
                                    maxLines = 1,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            keyboardController?.hide()
                                            user?.let {
                                                viewModel.updateUser(it.copy(name = name))
                                            }
                                        }
                                    )
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        keyboardController?.hide()
                                        user?.let {
                                            viewModel.updateUser(it.copy(name = name))
                                        }
                                    }
                                ) {
                                    Text("Update")
                                }
                            }
                        }
                    }

                    LaunchedEffect(updateResult) {
                        if (updateResult is UserDetailViewModel.UpdateResult.Success) {
                            showBottomSheet = false
                        }
                    }
                }
            }
        }
    }
}
