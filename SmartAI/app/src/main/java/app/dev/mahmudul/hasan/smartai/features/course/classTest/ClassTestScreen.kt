package app.dev.mahmudul.hasan.smartai.features.course.classTest

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.dev.mahmudul.hasan.smartai.component.StudentTemp
import app.dev.mahmudul.hasan.smartai.component.TeacherTemp
import com.ramcosta.composedestinations.annotation.Destination


@Destination
@Composable

fun ClassTestTeacherScreen(modifier: Modifier = Modifier, courseCode: String,) {

    TeacherTemp(courseCode =courseCode,  collection ="ClassTestSheet" , subCollection = "ClassTest")
}
@Destination
@Composable
fun ClassTestStudentScreen(modifier: Modifier = Modifier, courseCode: String,) {
    StudentTemp(courseCode =courseCode,  collection ="ClassTestSheet" , subCollection = "ClassTest")
}