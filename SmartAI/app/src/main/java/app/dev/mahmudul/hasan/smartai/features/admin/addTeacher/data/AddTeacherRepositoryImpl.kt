package app.dev.mahmudul.hasan.smartai.features.admin.addTeacher.data

import app.dev.mahmudul.hasan.smartai.di.BaseRepository
import app.dev.mahmudul.hasan.smartai.features.admin.addTeacher.domain.AddTeacherRepository
import app.dev.smartacademicinfrastructure.StudentDataModel
import app.dev.smartacademicinfrastructure.TeacherDataModel
import com.appdevmhr.dpi_sai.di.Resourse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class AddTeacherRepositoryImpl(private val firebaseAuth: FirebaseAuth,private val firestore: FirebaseFirestore) : AddTeacherRepository,
    BaseRepository() {
    override suspend fun addTeacher(email: String, password: String): Flow<Resourse<String>> =
        safeApiCall {
            val result =
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Resourse.Success(result.user!!.uid)
        }

    override suspend fun saveTeacherInfo(teacherDataModel: TeacherDataModel): Flow<Resourse<Boolean>> =
        safeApiCall {
            firestore.collection("TeachersList").document(teacherDataModel.teacherID).set(teacherDataModel).await()
            Resourse.Success(true)
        }
}