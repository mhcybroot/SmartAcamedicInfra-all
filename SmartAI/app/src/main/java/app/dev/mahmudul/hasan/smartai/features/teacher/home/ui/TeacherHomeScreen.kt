package app.dev.mahmudul.hasan.smartai.features.teacher.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dev.dpi_sai.component.SpacerHeight
import app.dev.mahmudul.hasan.smartai.R
import app.dev.mahmudul.hasan.smartai.component.LottieAnim
import app.dev.mahmudul.hasan.smartai.features.destinations.AddCourseDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.CourseDetailsDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.SignInScreenDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.TeacherHomeScreenDestination
import app.dev.mahmudul.hasan.smartai.utils.Utils
import app.dev.smartacademicinfrastructure.CourseModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Destination
@Composable
fun TeacherHomeScreen(
        destination: DestinationsNavigator,
        vm: TeacherHomeViewModel = koinViewModel()
) {
    val teacherHomeState by vm.teacherHomeState.collectAsStateWithLifecycle()
    val auth = vm.firebaseAuth
    println(teacherHomeState)
    Surface {
        Scaffold(floatingActionButton = {
            AddCourseFloatingActionButton(destination)
        }
        ) {
            it
            Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
            ) {
                TopAppBar(title = { }, actions = {
                    IconButton(onClick = {
                        auth.signOut()
                        destination.navigate(SignInScreenDestination) {
                            popUpTo(TeacherHomeScreenDestination) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(
                                painter = painterResource(id = R.drawable.logout),
                                contentDescription = "logout",
                        )
                    }
                })
                if (teacherHomeState.isLoading) {

                    Text("Loading")
                } else {
                    TeacherCourseList(
                            items = teacherHomeState.data,
                            destination = destination,
                            role = "teacher"
                    )
                }


            }


        }
    }
}

@Composable
fun TeacherCourseList(items: List<CourseModel>, destination: DestinationsNavigator, role: String) {
    LazyColumn(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items) { item ->
            TeacherCourseRow(item, destination)
        }
    }
}


@Composable
fun TeacherCourseRow(item: CourseModel, destination: DestinationsNavigator) {

    Card(
            modifier = Modifier

                    .fillMaxWidth(0.9F),
    ) {
        Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                          .padding(horizontal = 18.dp)
                        .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SpacerHeight(height = 40)
            Text(
                    text = "SUBJECT NAME :  ${item.courseName.toUpperCase(Locale.ROOT)}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W900
            )
            SpacerHeight(height = 20)
            Text(
                    text = "DEPARTMENT :  ${item.courseDepartment.toUpperCase(Locale.ROOT)}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
            )
            SpacerHeight(height = 10)
            Text(
                    text = "SESSION :  ${item.courseSession.toUpperCase(Locale.ROOT)}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
            )
            SpacerHeight(height = 10)
            Text(
                    text = "SHIFT :  ${item.courseShift.toUpperCase(Locale.ROOT)}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
            )
            SpacerHeight(height = 10)
            Text(
                    text = "GROUP :  ${item.courseGroup.toUpperCase(Locale.ROOT)}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
            )
            SpacerHeight(height = 20)
            OutlinedButton(
                    onClick = {
                        destination.navigate(
                                CourseDetailsDestination(
                                        item.courseCode,
                                        item.courseName,
                                        item.courseTeacherID,
                                        item.courseSession,
                                        item.courseDepartment,
                                        item.courseShift,
                                        item.courseSemester,
                                        item.courseGroup,
                                        Utils.userRole.first(),
                                        item.courseTeacherName
                                )
                        )
                    },
                    modifier = Modifier
                            .fillMaxWidth(0.8F),
            ) {
                Text(
                        text = "Let's Go To  ${item.courseName.toUpperCase()} ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(30.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = "Enter to Course")
            }
            SpacerHeight(height = 40)
        }
    }
    SpacerHeight(height = 20)

}


@Composable
fun AddCourseFloatingActionButton(destination: DestinationsNavigator) {
    FloatingActionButton(onClick = {
        destination.navigate(AddCourseDestination)
    }) {
        Icon(Icons.Default.Add, contentDescription = "Add Course")
    }
}


