package app.dev.mahmudul.hasan.smartai.features.signin.data

import app.dev.mahmudul.hasan.smartai.features.signin.domain.SignInRepository
import com.appdevmhr.dpi_sai.di.Resourse
import com.appdevmhr.dpi_sai.di.map
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SignInUseCase(private val repository: SignInRepository) {
    suspend fun signIn(email: String, password: String):Flow<Resourse<String>>  {
       return  repository.signIn(email, password).map { m ->
           m.map {
                it
          }
       }

    }

    suspend fun checkIsStudentOrTeacher(currentUser : String):Flow<Resourse<String>> {
        return  repository.checkIsStudentOrTeacher(currentUser)
    }
}