package com.example.ksp_sample.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ksp_sample.db.AppDatabase
import com.example.ksp_sample.db.entity.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var db: AppDatabase

    private lateinit var userDao: UserDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun getAllUsers_butNoExist_returnsEmptyList() = runTest {
        val actualUsers = userDao.getAll().first()
        assertEquals(emptyList<User>(), actualUsers)
    }

    @Test
    @Throws(IOException::class)
    fun getAllUsers_returnsAllUsers() = runTest {
        val users = listOf(
            User(1, "test1"),
            User(2, "test2"),
            User(3, "test3")
        )
        users.forEach {  user ->
            userDao.insert(user)
        }

        val actualUsers = userDao.getAll().first()
        assertEquals(users, actualUsers)
    }

    @Test
    @Throws(IOException::class)
    fun getUserById_butNoExist_returnsNull() = runTest {
        val actualUser = userDao.getById(id = 1).first()
        assertEquals(null, actualUser)
    }

    @Test
    @Throws(IOException::class)
    fun getUserById_returnsUser() = runTest {
        val expectUser = User(1, "test")
        userDao.insert(expectUser)

        val actualUser = userDao.getById(id = expectUser.id).first()!!
        assertEquals(expectUser, actualUser)
    }

    @Test
    @Throws(IOException::class)
    fun deleteUser_returnsNull() = runTest {
        val expectUser = User(1, "test")
        userDao.insert(expectUser)

        userDao.delete(expectUser)

        val actualUser = userDao.getById(id = expectUser.id).first()
        assertEquals(null, actualUser)
    }

    @Test
    @Throws(IOException::class)
    fun updateUser_returnsUpdatedUser() = runTest {
        val expectName = "new name"

        val oldUser = User(1, "test")
        userDao.insert(oldUser)
        userDao.update(oldUser.copy(name = expectName))

        val actualUser = userDao.getById(id = oldUser.id).first()!!
        assertEquals(expectName, actualUser.name)
    }
}
