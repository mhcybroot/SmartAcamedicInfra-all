package app.dev.mahmudul.hasan.smartai.features.course.attendance.domain

import app.dev.mahmudul.hasan.smartai.features.course.attendance.data.AttendanceModel
import com.appdevmhr.dpi_sai.di.Resourse
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {

    suspend fun putAttendanceSheet(attendanceModel: AttendanceModel,collection: String,subCollection: String) :Flow<Resourse<Boolean>>
    suspend fun getAttendanceSheet(courseCode: String,collection: String,subCollection: String) :Flow<Resourse<List<AttendanceModel>>>
}