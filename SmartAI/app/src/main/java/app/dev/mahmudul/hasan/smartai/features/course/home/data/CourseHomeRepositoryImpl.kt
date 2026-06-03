package app.dev.mahmudul.hasan.smartai.features.course.home.data

import android.os.Build
import androidx.annotation.RequiresApi
import app.dev.mahmudul.hasan.smartai.di.BaseRepository
import app.dev.mahmudul.hasan.smartai.features.course.home.domain.CourseHomeRepository
import app.dev.smartacademicinfrastructure.CourseModel
import app.dev.smartacademicinfrastructure.StudentDataModel
import com.appdevmhr.dpi_sai.di.Resourse
import com.appdevmhr.dpi_sai.di.getDataAsync
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CourseHomeRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,

    ) : CourseHomeRepository, BaseRepository() {

    override suspend fun getStudentCourseData(courseID: String): Flow<Resourse<CourseModel>> =
        safeApiCall {
            val currentUser = firebaseAuth.currentUser!!.uid
            val studentData =
                firestore.collection("StudentList").document(currentUser).get().await()
                    .toObject(StudentDataModel::class.java)
            val result = firestore.collection("CourseData").document(studentData!!.session)
                .collection(studentData.department).document(studentData.semester)
                .collection(studentData.shift).document(studentData.group).collection("Courses")
                .document(courseID).get().await()
            Resourse.Success(result.toObject(CourseModel::class.java)!!)
        }

    override suspend fun getTeacherCourseData(courseID: String): Flow<Resourse<CourseModel>> =
        safeApiCall {
            val currentUser = firebaseAuth.currentUser!!.uid
            val result = firestore.collection("TeachersList").document(currentUser)
                .collection("Courses").document(courseID).get().await()
            Resourse.Success(result.toObject(CourseModel::class.java)!!)
        }

    override suspend fun getMessages(courseModel: CourseModel): Flow<Resourse<List<MessageItemModel>>> =
        safeApiCall {
            val reference = firebaseDatabase.getReference("Courses").child(courseModel.courseDepartment)
                .child(courseModel.courseSession).child(courseModel.courseSemester).child(courseModel.courseShift)
                .child(courseModel.courseGroup).child(courseModel.courseCode)
            val snapshot = reference.getDataAsync()
            val messageList = mutableListOf<MessageItemModel>()
            for (data in snapshot.children) {
                val message = data.getValue(MessageItemModel::class.java)
                messageList.add(message!!)
            }
            Resourse.Success(messageList.reversed())

        }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun StoreStudentMessage(
        message: String, courseModel: CourseModel
    ): Flow<Resourse<Boolean>> = flow {
try{
        if (message.isEmpty()) return@flow
        val currentUser = firebaseAuth.currentUser!!.uid
        val studentData = firestore.collection("StudentList").document(currentUser).get().await()
            .toObject(StudentDataModel::class.java)!!
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a")
        val formattedDateTime = currentDateTime.format(formatter)
        val reference = firebaseDatabase.getReference("Courses").child(courseModel.courseDepartment)
            .child(courseModel.courseSession).child(courseModel.courseSemester).child(courseModel.courseShift)
            .child(courseModel.courseGroup).child(courseModel.courseCode).child(
                formattedDateTime
            )
        val messageItemModel = MessageItemModel(
            message = message,
            uid = currentUser,
            senderName = studentData.name,
            time = formattedDateTime
        )
        reference.setValue(messageItemModel).await()
        Resourse.Success(true)
        }catch (error:Exception){
            Resourse.Failure(error)
    println(error)
        }
    }
 @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun StoreTeacherMessage(
        message: String, courseModel: CourseModel
    ): Flow<Resourse<Boolean>> = safeApiCall {

//        if (message.isEmpty()) return@safeApiCall Resourse.Failure(Exception("Message is Empty"))
        val currentUser = firebaseAuth.currentUser!!.uid
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a")
        val formattedDateTime = currentDateTime.format(formatter)
        val reference = firebaseDatabase.getReference("Courses").child(courseModel.courseDepartment)
            .child(courseModel.courseSession).child(courseModel.courseSemester).child(courseModel.courseShift)
            .child(courseModel.courseGroup).child(courseModel.courseCode).child(
                formattedDateTime
            )
        val messageItemModel = MessageItemModel(
            message = message,
            uid = currentUser,
            senderName = courseModel.courseTeacherName,
            time = formattedDateTime
        )
        reference.setValue(messageItemModel).await()
        Resourse.Success(true)
    }


}