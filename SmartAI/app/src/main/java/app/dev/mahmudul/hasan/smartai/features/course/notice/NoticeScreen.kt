package app.dev.mahmudul.hasan.smartai.features.course.notice

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.dev.mahmudul.hasan.smartai.component.StudentTemp
import app.dev.mahmudul.hasan.smartai.component.TeacherTemp
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun NoticeTeacherScreen(modifier: Modifier = Modifier, courseCode: String,) {
    TeacherTemp(courseCode =courseCode,  collection ="NoticeSheet" , subCollection = "Notice")
}
@Destination
@Composable
fun NoticeStudentScreen(modifier: Modifier = Modifier, courseCode: String,) {
    StudentTemp(courseCode =courseCode,  collection ="NoticeSheet" , subCollection = "Notice")
}