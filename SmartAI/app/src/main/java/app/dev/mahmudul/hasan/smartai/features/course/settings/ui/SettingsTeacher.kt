package app.dev.mahmudul.hasan.smartai.features.course.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import app.dev.dpi_sai.component.SpacerHeight
import app.dev.mahmudul.hasan.smartai.R
import app.dev.mahmudul.hasan.smartai.features.destinations.AttendanceTeacherDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.ClassTestTeacherScreenDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.JobSubmitTeacherScreenDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.NoticeTeacherScreenDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.SessionalSubmissionTeacherScreenDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.SettingsTeacherDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@Destination
@Composable
fun SettingsTeacher(modifier: Modifier = Modifier,destination: DestinationsNavigator,courseCode: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            fontSize = 40.sp,
            fontWeight = FontWeight.W900,
            textAlign = TextAlign.Center
        )
        SpacerHeight(height = 50)
        OutlinedButton(onClick = {
            destination.navigate(NoticeTeacherScreenDestination(courseCode = courseCode)) {
                popUpTo(SettingsTeacherDestination) {
                    inclusive = true
                }
            }
        }, modifier = Modifier.fillMaxWidth(0.8F)) {
            Text(
                text = "Notice",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        SpacerHeight(height = 30)  
        OutlinedButton(onClick = {
            destination.navigate(AttendanceTeacherDestination(courseCode = courseCode)) {
                popUpTo(SettingsTeacherDestination) {
                    inclusive = true
                }
            }
        }, modifier = Modifier.fillMaxWidth(0.8F)) {
            Text(
                text = "Attendance",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        SpacerHeight(height = 30)
        OutlinedButton(onClick = {
            destination.navigate(JobSubmitTeacherScreenDestination(courseCode = courseCode)) {
                popUpTo(SettingsTeacherDestination) {
                    inclusive = true
                }
            }
        }, modifier = Modifier.fillMaxWidth(0.8F)) {
            Text(
                text = "Job Submission",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        SpacerHeight(height = 30)
        OutlinedButton(onClick = {
            destination.navigate(ClassTestTeacherScreenDestination(courseCode = courseCode)) {
                popUpTo(SettingsTeacherDestination) {
                    inclusive = true
                }
            }
        }, modifier = Modifier.fillMaxWidth(0.8F)) {
            Text(
                text = "Class Test",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        SpacerHeight(height = 30)
        OutlinedButton(onClick = {

            destination.navigate(SessionalSubmissionTeacherScreenDestination(courseCode = courseCode)) {
                popUpTo(SettingsTeacherDestination) {
                    inclusive = true
                }
            }
        }, modifier = Modifier.fillMaxWidth(0.8F)) {
            Text(
                text = "Sessional Submission",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }




    }
}

