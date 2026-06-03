package app.dev.mahmudul.hasan.smartai.features.student.profile.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import app.dev.dpi_sai.component.DropDownSpiner
import app.dev.dpi_sai.component.InputField
import app.dev.dpi_sai.component.SpacerHeight
import app.dev.mahmudul.hasan.smartai.R
import app.dev.mahmudul.hasan.smartai.component.LottieAnim
import app.dev.mahmudul.hasan.smartai.component.ResultShowInfo
import app.dev.mahmudul.hasan.smartai.features.destinations.StudentHomeScreenDestination
import app.dev.mahmudul.hasan.smartai.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
@Destination
fun StudentProfile(
    vm: StudentProfileViewModel = koinViewModel(),
    destination: DestinationsNavigator
) {
    val studentProfileState by vm.studentProfileState.collectAsState()
    val updateStudentProfile by vm.updateStudentProfileState.collectAsState()
    val firebaseAuth = koinInject<FirebaseAuth>()
    val studentName = remember {
        mutableStateOf("")
    }
    val studentRollNumber = remember {
        mutableStateOf("")
    }
    val studentRegistrationNumber = remember {
        mutableStateOf("")
    }
    val selectedSemester = remember {
        mutableStateOf(Utils.semesterList.first())
    }
    val selectedDepartment = remember {
        mutableStateOf(Utils.departmentList.first())
    }
    val selectedSession = remember {
        mutableStateOf(Utils.sessionList.first())
    }
    val selectedGroup = remember {
        mutableStateOf(Utils.groupList.first())
    }
    val selectedShift = remember {
        mutableStateOf(Utils.shiftList.first())
    }

    val isLoding = remember {
        mutableStateOf(false)
    }
    val signUpButtonText = remember {
        mutableStateOf("Update Profile")
    }
    BackHandler {
        destination.navigate(StudentHomeScreenDestination) {
            popUpTo(StudentHomeScreenDestination) {
                inclusive = true
            }
        }
    }

    if (studentProfileState.data != null) {
        val student = studentProfileState.data
        studentName.value = student.name
        studentRollNumber.value = student.rollNumber
        studentRegistrationNumber.value = student.registrationNumber
        selectedSession.value = student.session
        selectedSemester.value = student.semester
        selectedDepartment.value = student.department
        selectedShift.value = student.shift
        selectedGroup.value = student.group
    }
    LaunchedEffect(key1 = updateStudentProfile.data) {
            vm.getStudentProfile(firebaseAuth.currentUser!!.uid)
    }
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SpacerHeight(height = 50)

            Text(
                text = "UPDATE PROFILE",
                fontSize = 40.sp,
                fontWeight = FontWeight.W900,
                textAlign = TextAlign.Center
            )
            SpacerHeight(height = 50)
            InputField(name = "Name",
                value = studentName.value,
                onChange = { studentName.value = it })
            SpacerHeight(height = 10)
            InputField(name = "Roll Number",
                value = studentRollNumber.value,
                onChange = { studentRollNumber.value = it })
            SpacerHeight(height = 10)
            InputField(name = "Registration Number",
                value = studentRegistrationNumber.value,
                onChange = { studentRegistrationNumber.value = it })
            SpacerHeight(height = 10)
            DropDownSpiner(items = Utils.sessionList, selecedItem = selectedSession)
            SpacerHeight(height = 30)
            DropDownSpiner(items = Utils.semesterList, selecedItem = selectedSemester)
            SpacerHeight(height = 30)
            DropDownSpiner(items = Utils.departmentList, selecedItem = selectedDepartment)
            SpacerHeight(height = 30)
            DropDownSpiner(items = Utils.shiftList, selecedItem = selectedShift)
            SpacerHeight(height = 30)
            DropDownSpiner(items = Utils.groupList, selecedItem = selectedGroup)
            SpacerHeight(height = 30)
            OutlinedButton(
                onClick = {
                    isLoding.value = true
                    val updateProfileMap = mapOf(
                        "name" to studentName.value,
                        "rollNumber" to studentRollNumber.value,
                        "registrationNumber" to studentRegistrationNumber.value,
                        "semester" to selectedSemester.value,
                        "department" to selectedDepartment.value,
                        "session" to selectedSession.value,
                        "group" to selectedGroup.value,
                        "shift" to selectedShift.value
                    )
                    vm.updateStudentProfile(updateProfileMap)

                },
            ) {
                Text(
                    text = signUpButtonText.value,
                    modifier = Modifier.fillMaxWidth(0.8F),
                    textAlign = TextAlign.Center
                )
            }
            SpacerHeight(height = 30)
            ResultShowInfo(
                visible = studentProfileState.error.isNotEmpty(),
                data = "Error : ${studentProfileState.error}"
            )
            ResultShowInfo(
                visible = updateStudentProfile.error.isNotEmpty(),
                data = "Error : ${updateStudentProfile.error}"
            )

            if (studentProfileState.isLoading || updateStudentProfile.isLoading) {

                Text("Loading")
            }
            SpacerHeight(height = 120)

        }
    }


}