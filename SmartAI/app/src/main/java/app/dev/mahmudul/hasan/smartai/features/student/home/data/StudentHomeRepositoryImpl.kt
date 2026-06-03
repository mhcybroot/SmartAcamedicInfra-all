package app.dev.mahmudul.hasan.smartai.features.student.home.data

import app.dev.mahmudul.hasan.smartai.di.BaseRepository
import app.dev.mahmudul.hasan.smartai.features.student.home.domain.StudentHomeRepository
import app.dev.smartacademicinfrastructure.CourseModel
import app.dev.smartacademicinfrastructure.StudentDataModel
import com.appdevmhr.dpi_sai.di.Resourse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class StudentHomeRepositoryImpl(private val firestore: FirebaseFirestore,private val firebaseAuth: FirebaseAuth) : StudentHomeRepository,
    BaseRepository() {
    override suspend fun getStudentCourses(studentDataModel: StudentDataModel): Flow<Resourse<List<CourseModel>>>  =safeApiCall {
        val result =  firestore.collection("CourseData").document(studentDataModel.session).collection(studentDataModel.department)
            .document(studentDataModel.semester).collection(studentDataModel.shift).document(studentDataModel.group).collection("Courses").get()
            .await().toObjects(CourseModel::class.java)
        Resourse.Success(result)
    }

    override suspend fun getStudentProfile(studentId: String): Flow<Resourse<StudentDataModel>> {
        return safeApiCall {
            val data = firestore.collection("StudentList").document(studentId).get().await().toObject(StudentDataModel::class.java)
            if (data != null) {
                Resourse.Success(data)
            } else {
                Resourse.Failure(Exception("No Data Found"))
            }
        }
    }
    override suspend fun studentSignOut() {
        firebaseAuth.signOut()
    }
}