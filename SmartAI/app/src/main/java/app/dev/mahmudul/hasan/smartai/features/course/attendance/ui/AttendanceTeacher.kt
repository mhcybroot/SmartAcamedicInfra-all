package app.dev.mahmudul.hasan.smartai.features.course.attendance.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.dpi_sai.component.SpacerHeight
import app.dev.mahmudul.hasan.smartai.component.StudentTemp
import app.dev.mahmudul.hasan.smartai.component.TeacherTemp
import app.dev.mahmudul.hasan.smartai.features.course.attendance.data.AttendanceModel
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AttendanceTeacher(modifier: Modifier = Modifier,courseCode: String,) {
    TeacherTemp(courseCode =courseCode,  collection ="AttendanceSheet" , subCollection = "Attendance")

}

