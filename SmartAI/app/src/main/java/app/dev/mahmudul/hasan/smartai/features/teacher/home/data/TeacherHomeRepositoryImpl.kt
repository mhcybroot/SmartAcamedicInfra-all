package app.dev.mahmudul.hasan.smartai.features.teacher.home.data

import app.dev.mahmudul.hasan.smartai.di.BaseRepository
import app.dev.mahmudul.hasan.smartai.features.teacher.home.domain.TeacherHomeRepository
import app.dev.smartacademicinfrastructure.CourseModel
import com.appdevmhr.dpi_sai.di.Resourse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class TeacherHomeRepositoryImpl(private val firestore: FirebaseFirestore,private val firebaseAuth: FirebaseAuth) : TeacherHomeRepository ,BaseRepository(){
    override suspend fun getTeacherCourses(): Flow<Resourse<List<CourseModel>>> =safeApiCall {
        val currentUser = firebaseAuth.currentUser!!.uid
        val result = firestore.collection("TeachersList").document(currentUser)
            .collection("Courses").get().await()
        if (result.isEmpty){
            return@safeApiCall Resourse.Failure(Exception("No Course Found"))
        }
        Resourse.Success(result.toObjects(CourseModel::class.java))
    }
}