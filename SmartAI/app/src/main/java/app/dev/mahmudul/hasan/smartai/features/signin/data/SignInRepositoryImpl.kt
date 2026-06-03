package app.dev.mahmudul.hasan.smartai.features.signin.data

import app.dev.mahmudul.hasan.smartai.di.BaseRepository
import app.dev.mahmudul.hasan.smartai.features.signin.domain.SignInRepository
import com.appdevmhr.dpi_sai.di.Resourse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class SignInRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : SignInRepository, BaseRepository() {
    override suspend fun signIn(email: String, password: String): Flow<Resourse<String>> =
        safeApiCall {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resourse.Success(result.user?.uid!!)
        }

    override suspend fun checkIsStudentOrTeacher(currentUser: String): Flow<Resourse<String>> =
        safeApiCall {
            var role = ""
            val s = firestore.collection("StudentList").document(currentUser).get().await()
            if (s.exists()) {
                role = "Student"
            } else {
                val t = firestore.collection("TeachersList").document(currentUser).get().await()
                if (t.exists()) {
                    role = "Teacher"
                } else {
                    Resourse.Failure(Exception("Unknown User Found"))
                }
            }
            Resourse.Success(role)
        }


//    override suspend fun signIn(email: String, password: String): Resourse<FirebaseUser> {
//        return try {
//
//            Resourse.Success(result.user!!)
//        } catch (e: Exception) {
//            Resourse.Failure(e)
//        }
//    }

//    override suspend fun checkIsStudentOrTeacher(user: FirebaseUser): Resourse<String> {
//        return try {
//            var role = ""
//            val s = firestore.collection("StudentList").document(user.uid).get().await()
//            if (s.exists()) {
//                role = "Student"
//            } else {
//                val t = firestore.collection("TeachersList").document(user.uid).get().await()
//                if (t.exists()) {
//                    role = "Teacher"
//                } else {
//                    role = "Unknown"
//                }
//            }
//            Resourse.Success(role)
//        } catch (e: Exception) {
//            Resourse.Failure(e)
//        }
//    }
}