package app.dev.mahmudul.hasan.smartai.features.course.attendance.ui

import androidx.compose.runtime.Composable
import app.dev.mahmudul.hasan.smartai.component.StudentTemp
import app.dev.mahmudul.hasan.smartai.component.TeacherTemp
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun AttendanceStudent(courseCode: String){
    StudentTemp(courseCode =courseCode,  collection ="AttendanceSheet" , subCollection = "Attendance")
}