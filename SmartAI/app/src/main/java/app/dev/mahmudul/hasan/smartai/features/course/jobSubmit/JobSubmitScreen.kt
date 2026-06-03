package app.dev.mahmudul.hasan.smartai.features.course.jobSubmit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.dev.mahmudul.hasan.smartai.component.StudentTemp
import app.dev.mahmudul.hasan.smartai.component.TeacherTemp
import com.ramcosta.composedestinations.annotation.Destination
@Destination
@Composable
fun JobSubmitTeacherScreen(modifier: Modifier = Modifier, courseCode: String,) {
    TeacherTemp(courseCode =courseCode,  collection ="JobSubmitSheet" , subCollection = "JobSubmit")

}
@Destination
@Composable
fun JobSubmitStudentScreen(modifier: Modifier = Modifier, courseCode: String,) {
    StudentTemp(courseCode =courseCode,  collection ="JobSubmitSheet" , subCollection = "JobSubmit")
}