package app.dev.mahmudul.hasan.smartai.features.signup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import app.dev.mahmudul.hasan.smartai.features.destinations.SignUpScreenDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.StudentHomeScreenDestination
import app.dev.mahmudul.hasan.smartai.utils.Utils.departmentList
import app.dev.mahmudul.hasan.smartai.utils.Utils.groupList
import app.dev.mahmudul.hasan.smartai.utils.Utils.semesterList
import app.dev.mahmudul.hasan.smartai.utils.Utils.sessionList
import app.dev.mahmudul.hasan.smartai.utils.Utils.shiftList
import app.dev.smartacademicinfrastructure.StudentDataModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
fun SignUpScreen(destination: DestinationsNavigator, vm: SignUpViewModel = koinViewModel()) {

    val signUpState by vm.signUpState.collectAsState()
    val saveStudentInfoState by vm.saveStudentInfoState.collectAsState()
    var studentName by remember {
        mutableStateOf("")
    }
    var studentEmail by remember {
        mutableStateOf("")
    }
    var studentPassword by remember {
        mutableStateOf("")
    }
    var studentRollNumber by remember {
        mutableStateOf("")
    }
    var studentRegistrationNumber by remember {
        mutableStateOf("")
    }

    val selectedSemester = remember {
        mutableStateOf(semesterList.first())
    }
    val selectedDepartment = remember {
        mutableStateOf(departmentList.first())
    }
    val selectedSession = remember {
        mutableStateOf(sessionList.first())
    }
    val selectedGroup = remember {
        mutableStateOf(groupList.first())
    }
    val selectedShift = remember {
        mutableStateOf(shiftList.first())
    }

    var isLoding by remember {
        mutableStateOf(false)
    }
    var isInfo by remember {
        mutableStateOf(false)
    }


    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SpacerHeight(height = 50)

            Text(
                text = "SIGN UP",
                fontSize = 40.sp,
                fontWeight = FontWeight.W900,
                textAlign = TextAlign.Center
            )
            SpacerHeight(height = 50)
            InputField(name = "Name",
                value = studentName,
                onChange = { studentName = it })
            SpacerHeight(height = 10)
            InputField(name = "Email",
                value = studentEmail,
                onChange = { studentEmail = it })
            SpacerHeight(height = 10)
            InputField(name = "Password",
                value = studentPassword,
                onChange = { studentPassword = it })
            SpacerHeight(height = 10)
            InputField(name = "Roll Number",
                value = studentRollNumber,
                onChange = { studentRollNumber = it })
            SpacerHeight(height = 10)
            InputField(name = "Registration Number",
                value = studentRegistrationNumber,
                onChange = { studentRegistrationNumber = it })
            SpacerHeight(height = 10)
            DropDownSpiner(items = sessionList, selecedItem = selectedSession)
            SpacerHeight(height = 30)
            DropDownSpiner(items = semesterList, selecedItem = selectedSemester)
            SpacerHeight(height = 30)
            DropDownSpiner(items = departmentList, selecedItem = selectedDepartment)
            SpacerHeight(height = 30)
            DropDownSpiner(items = shiftList, selecedItem = selectedShift)
            SpacerHeight(height = 30)
            DropDownSpiner(items = groupList, selecedItem = selectedGroup)
            SpacerHeight(height = 30)
            OutlinedButton(
                onClick = {
                    isInfo= false
                    if (studentEmail.isNotEmpty() && studentPassword.isNotEmpty() && studentName.isNotEmpty() && studentRollNumber.isNotEmpty() && studentRegistrationNumber.isNotEmpty()) {
                        isLoding = true
                        vm.signUp(email = studentEmail, password = studentPassword)
                    } else {
                        isInfo = true
                    }

                },
            ) {
                Text(
                    text = "Sign Up",
                    modifier = Modifier.fillMaxWidth(0.8F),
                    textAlign = TextAlign.Center
                )
            }
            SpacerHeight(height = 30)
            ResultShowInfo(visible = signUpState.error.isNotEmpty(), data = signUpState.error)
            ResultShowInfo(
                visible = saveStudentInfoState.error.isNotEmpty(),
                data = saveStudentInfoState.error
            )
            ResultShowInfo(
                visible = saveStudentInfoState.data,
                data = "Sign Up Successfully"
            )
            ResultShowInfo(
                visible = isInfo,
                data = "Please fill all the fields"
            )
            if (signUpState.isLoading || saveStudentInfoState.isLoading) {
//                LottieAnim(anim = R.raw.loading, )
                Text("Loading")

            }
            SpacerHeight(height = 120)

            if (signUpState.data.isNotEmpty()) {
                val studentDataModel = StudentDataModel(
                    name = studentName,
                    email = studentEmail,
                    rollNumber = studentRollNumber,
                    registrationNumber = studentRegistrationNumber,
                    semester = selectedSemester.value,
                    department = selectedDepartment.value,
                    session = selectedSession.value,
                    shift = selectedShift.value,
                    group = selectedGroup.value,
                    studentID = signUpState.data,
                    password = studentPassword,
                    photo = "",
                )
                vm.saveStudentInfo(studentDataModel)
            }

            if (saveStudentInfoState.data) {
                destination.navigate(StudentHomeScreenDestination) {
                    popUpTo(SignUpScreenDestination) {
                        inclusive = true
                    }
                }
            }
        }
    }
}