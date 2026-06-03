package app.dev.mahmudul.hasan.smartai.features.course.sessionalSubmission

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.dev.mahmudul.hasan.smartai.component.StudentTemp
import app.dev.mahmudul.hasan.smartai.component.TeacherTemp
import com.ramcosta.composedestinations.annotation.Destination


@Destination
@Composable
fun SessionalSubmissionTeacherScreen(modifier: Modifier = Modifier, courseCode: String,) {
    TeacherTemp(courseCode =courseCode,  collection ="SessionalSubmissionSheet" , subCollection = "SessionalSubmission")

}
@Destination
@Composable
fun SessionalSubmissionStudentScreen(modifier: Modifier = Modifier, courseCode: String,) {
    StudentTemp(courseCode =courseCode,  collection ="SessionalSubmissionSheet" , subCollection = "SessionalSubmission")
}