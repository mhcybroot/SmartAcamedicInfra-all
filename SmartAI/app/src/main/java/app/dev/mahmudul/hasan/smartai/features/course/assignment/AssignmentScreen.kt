package app.dev.mahmudul.hasan.smartai.features.course.assignment

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.dev.mahmudul.hasan.smartai.component.StudentTemp
import app.dev.mahmudul.hasan.smartai.component.TeacherTemp
import com.ramcosta.composedestinations.annotation.Destination


@Destination
@Composable
fun AssignmentTeacherScreen(modifier: Modifier = Modifier,courseCode: String,) {
    TeacherTemp(courseCode =courseCode,  collection ="AssignmentSheet" , subCollection = "Assignment")
}


@Destination
@Composable
fun AssignmentStudentScreen(modifier: Modifier = Modifier,courseCode: String,) {
    StudentTemp(courseCode =courseCode,  collection ="AssignmentSheet" , subCollection = "Assignment")
}