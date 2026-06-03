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
import app.dev.mahmudul.hasan.smartai.features.destinations.AttendanceStudentDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.ClassTestStudentScreenDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.JobSubmitStudentScreenDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.SessionalSubmissionStudentScreenDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.SettingsStudentDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@Destination
@Composable
fun SettingsStudent(modifier: Modifier = Modifier,destination: DestinationsNavigator,couseCode: String) {
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
            destination.navigate(AttendanceStudentDestination(courseCode = couseCode)) {
                popUpTo(SettingsStudentDestination) {
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
            destination.navigate(JobSubmitStudentScreenDestination(courseCode = couseCode)) {
                popUpTo(SettingsStudentDestination) {
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
            destination.navigate(ClassTestStudentScreenDestination(courseCode = couseCode)) {
                popUpTo(SettingsStudentDestination) {
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

            destination.navigate(SessionalSubmissionStudentScreenDestination(courseCode = couseCode)) {
                popUpTo(SettingsStudentDestination) {
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