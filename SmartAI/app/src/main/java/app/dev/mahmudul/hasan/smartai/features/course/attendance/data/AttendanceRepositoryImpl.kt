package app.dev.mahmudul.hasan.smartai.features.course.attendance.data

import app.dev.mahmudul.hasan.smartai.di.BaseRepository
import app.dev.mahmudul.hasan.smartai.features.course.attendance.domain.AttendanceRepository
import com.appdevmhr.dpi_sai.di.Resourse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class AttendanceRepositoryImpl(
    private val firestore: FirebaseFirestore
) : AttendanceRepository, BaseRepository() {
    override suspend fun putAttendanceSheet(
        attendanceModel: AttendanceModel
        ,collection: String,subCollection: String
    ): Flow<Resourse<Boolean>> = safeApiCall {
        if (attendanceModel.date.isEmpty() || attendanceModel.url.isEmpty() || attendanceModel.deadLine.isEmpty() || attendanceModel.courseCode.isEmpty()) {
            Resourse.Failure(Exception("Data Not Found"))
        } else {
            firestore.collection(collection).document(attendanceModel.courseCode)
                .collection(subCollection).document(attendanceModel.date).set(attendanceModel)
                .await()
            Resourse.Success(true)
        }
    }

    override suspend fun getAttendanceSheet(courseCode: String,collection: String,subCollection: String): Flow<Resourse<List<AttendanceModel>>> {
        return safeApiCall {

            val data = firestore.collection(collection).document(courseCode)
                .collection(subCollection).orderBy("deadLine").get().await()
            val attendanceModel = data.toObjects(AttendanceModel::class.java)
            Resourse.Success(attendanceModel)
        }
    }
}
//AttendanceSheet
//Attendance