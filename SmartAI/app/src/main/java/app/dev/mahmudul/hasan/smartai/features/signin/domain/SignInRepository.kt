package app.dev.mahmudul.hasan.smartai.features.signin.domain

import com.appdevmhr.dpi_sai.di.Resourse
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface SignInRepository {
    suspend fun signIn(email: String, password: String): Flow<Resourse<String>>
    suspend fun checkIsStudentOrTeacher(currentUser : String):Flow<Resourse<String>>
}