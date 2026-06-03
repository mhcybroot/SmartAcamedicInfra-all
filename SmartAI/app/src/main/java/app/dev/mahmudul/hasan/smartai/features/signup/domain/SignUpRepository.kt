package app.dev.mahmudul.hasan.smartai.features.signup.domain

import app.dev.smartacademicinfrastructure.StudentDataModel
import com.appdevmhr.dpi_sai.di.Resourse
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface SignUpRepository {
    suspend fun signUp(email: String, password: String): Flow<Resourse<String>>
    suspend fun saveStudentInfo(studentDataModel: StudentDataModel): Flow<Resourse<Boolean>>
}