package com.example.ksp_sample.user.list

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ksp_sample.KspSampleApplication
import com.example.ksp_sample.db.entity.User
import com.example.ksp_sample.user.create.CreateUserActivity
import com.example.ksp_sample.ui.theme.KspSampleTheme
import com.example.ksp_sample.user.detail.UserDetailActivity

class UserListActivity : ComponentActivity() {

    private val viewModel: UserListViewModel by viewModels {
        UserListViewModelFactory(application as KspSampleApplication)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            KspSampleTheme {
                val context = LocalContext.current

                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                startActivity(
                                    CreateUserActivity.intent(context)
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add new user"
                            )
                        }
                    }
                ) { innerPadding ->
                    val uiState by viewModel.uiState.collectAsState()

                    when (uiState) {
                        is UserListViewModel.UiState.Loading -> {
                            Column(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(64.dp)
                                )
                            }
                        }
                        is UserListViewModel.UiState.Empty -> {
                            Column(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "No users found",
                                    modifier = Modifier.size(64.dp),
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "No users found",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        is UserListViewModel.UiState.Success -> {
                            val users = (uiState as UserListViewModel.UiState.Success).users

                            LazyColumn(
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                items(users) { user ->
                                    // TODO: 横スワイプで削除
                                    UserItem(
                                        modifier = Modifier
                                            .clickable {
                                                startActivity(
                                                    UserDetailActivity.intent(
                                                        context = context,
                                                        id = user.id
                                                    )
                                                )
                                            }
                                            .padding(20.dp),
                                        user = user
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(),
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun UserItem(
        modifier: Modifier = Modifier,
        user: User
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.id.toString(),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = user.name,
                modifier = Modifier.weight(1f),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
