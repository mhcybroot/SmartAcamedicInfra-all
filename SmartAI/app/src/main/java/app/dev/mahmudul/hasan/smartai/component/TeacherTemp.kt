package app.dev.mahmudul.hasan.smartai.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import app.dev.dpi_sai.component.InputField
import app.dev.dpi_sai.component.SpacerHeight
import app.dev.mahmudul.hasan.smartai.R
import app.dev.mahmudul.hasan.smartai.features.course.attendance.data.AttendanceModel
import app.dev.mahmudul.hasan.smartai.features.course.attendance.ui.AttendanceViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherTemp(courseCode: String, vm: AttendanceViewModel = koinViewModel(), collection: String, subCollection: String) {

    val putAttendanceState = vm.putAttendanceState.collectAsState()
    val getAttendanceState = vm.getAttendanceState.collectAsState()
    val openDialog = remember {
        mutableStateOf(false)
    }
    val url = remember {
        mutableStateOf("")

    }
    val deadLine = remember {
        mutableStateOf("")
    }


    LaunchedEffect(key1 = openDialog.value) {
        vm.getAttendance(courseCode,collection,subCollection)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            IconButton(onClick = {
                openDialog.value = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "")
            }
        }
    ) { innerPadding ->
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false }) {
                Surface(
                    modifier = Modifier

                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SpacerHeight(20)
                        Text("ADD", fontSize = 30.sp, fontWeight = FontWeight.W800)
                        SpacerHeight(40)
                        InputField(name = "Dead Line", value = deadLine.value) {
                            deadLine.value = it
                        }
                        SpacerHeight(16)
                        InputField(name = "URL", value = url.value) {
                            url.value = it
                        }
                        SpacerHeight(16)
                        OutlinedButton(onClick = {
                            val date = getDate()
                            val attendanceModel = AttendanceModel(
                                courseCode = courseCode,
                                deadLine = deadLine.value,
                                url = url.value,
                                date = date
                            )
                            vm.putAttendance(attendanceModel,collection,subCollection)

                        }) {
                            Text("Add")
                        }
                        SpacerHeight(16)

                        ResultShowInfo(
                            visible = putAttendanceState.value.error.isNotEmpty(),
                            data = putAttendanceState.value.error
                        )
                        SpacerHeight(16)

                        ResultShowInfo(
                            visible = putAttendanceState.value.data,
                            data = "$subCollection Added Successfully"
                        )
                        SpacerHeight(16)

                        if (putAttendanceState.value.isLoading) {
//                            LottieAnim(anim = R.raw.loading, )
                            Text("Loading")
                        }
                        if (putAttendanceState.value.data) {
                            openDialog.value = false
                        }

                    }
                }
            }
        } else {


            Column(
                modifier = Modifier
                        .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpacerHeight(16)
                Text("$subCollection List", fontSize = 30.sp, fontWeight = FontWeight.W900)
                ItemList(items = getAttendanceState.value.data)
                ResultShowInfo(
                    visible = getAttendanceState.value.error.isNotEmpty(),
                    data = getAttendanceState.value.error
                )
                SpacerHeight(16)
                ResultShowInfo(
                    visible = getAttendanceState.value.data.isNotEmpty(),
                    data = "$subCollection Loaded Successfully"
                )
                SpacerHeight(16)
                if (getAttendanceState.value.isLoading) {
//                    LottieAnim(anim = R.raw.loading, )
                    Text("Loading")
                }
            }
        }
    }
}
