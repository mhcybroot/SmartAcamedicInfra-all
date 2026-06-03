package app.dev.mahmudul.hasan.smartai.features.signup.data

import app.dev.mahmudul.hasan.smartai.di.BaseRepository
import app.dev.mahmudul.hasan.smartai.features.signup.domain.SignUpRepository
import app.dev.smartacademicinfrastructure.StudentDataModel
import com.appdevmhr.dpi_sai.di.Resourse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class SignUpRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : SignUpRepository , BaseRepository() {

    override suspend fun signUp(email: String, password: String): Flow<Resourse<String>> =
       safeApiCall {
           val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
           Resourse.Success(result.user!!.uid)
       }
    override suspend fun saveStudentInfo(studentDataModel: StudentDataModel): Flow<Resourse<Boolean>> =safeApiCall {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            studentDataModel.studentID = uid
            firestore.collection("StudentList").document(uid).set(studentDataModel).await()
            Resourse.Success(true)
        } else {
            Resourse.Failure(Exception("User not found"))
        }
    }
}