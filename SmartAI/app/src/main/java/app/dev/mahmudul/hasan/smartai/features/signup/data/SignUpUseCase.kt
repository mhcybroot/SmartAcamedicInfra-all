package app.dev.mahmudul.hasan.smartai.features.signup.data

import app.dev.mahmudul.hasan.smartai.features.signup.domain.SignUpRepository
import app.dev.smartacademicinfrastructure.StudentDataModel
import com.appdevmhr.dpi_sai.di.Resourse
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class SignUpUseCase(
    private val repository: SignUpRepository
) {
    suspend fun signUp(email: String, password: String): Flow<Resourse<String>> =
        repository.signUp(email, password)

    suspend fun saveStudentInfo(studentDataModel: StudentDataModel): Flow<Resourse<Boolean>> =
        repository.saveStudentInfo(studentDataModel)
}