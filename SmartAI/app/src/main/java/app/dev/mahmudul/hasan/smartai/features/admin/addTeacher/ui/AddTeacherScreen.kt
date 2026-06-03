package app.dev.mahmudul.hasan.smartai.features.admin.addTeacher.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dev.dpi_sai.component.DropDownSpiner
import app.dev.dpi_sai.component.InputField
import app.dev.dpi_sai.component.SpacerHeight
import app.dev.mahmudul.hasan.smartai.R
import app.dev.mahmudul.hasan.smartai.component.LottieAnim
import app.dev.mahmudul.hasan.smartai.component.ResultShowInfo
import app.dev.mahmudul.hasan.smartai.features.admin.login.ui.AddTeacherViewModel
import app.dev.mahmudul.hasan.smartai.utils.Utils.departmentList
import app.dev.mahmudul.hasan.smartai.utils.Utils.shiftList
import app.dev.smartacademicinfrastructure.TeacherDataModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
fun AddTeacherScreen(
    destination: DestinationsNavigator,
    vm: AddTeacherViewModel = koinViewModel()
) {
    val addTeacherState by vm.addTeacherState.collectAsStateWithLifecycle()
    val saveTeacherInfoState by vm.saveTeacherInfoState.collectAsStateWithLifecycle()
    val teacherName = remember {
        mutableStateOf("")
    }
    val teacherEmail = remember {
        mutableStateOf("")
    }
    val teacherPassword = remember {
        mutableStateOf("")
    }
    val teacherPhoneNumber = remember {
        mutableStateOf("")
    }
    val teacherRegistrationNumber = remember {
        mutableStateOf("")
    }
    val selectedDepartment = remember {
        mutableStateOf(departmentList.first())
    }
    val selectedShift = remember {
        mutableStateOf(shiftList.first())
    }
    val info = remember {
        mutableStateOf("Sign Up")
    }
    val isInfo = remember {
        mutableStateOf(false)
    }
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SpacerHeight(height = 100)

            Text(
                text = "Register as a Teacher",
                fontSize = 30.sp,
                fontWeight = FontWeight.W900,
                textAlign = TextAlign.Center
            )
            SpacerHeight(height = 50)
            InputField(name = "Full Name", value = teacherName.value) {
                teacherName.value = it
            }
            SpacerHeight(height = 10)
            InputField(name = "Email", value = teacherEmail.value) {
                teacherEmail.value = it
            }
            SpacerHeight(height = 10)
            InputField(name = "Password", value = teacherPassword.value) {
                teacherPassword.value = it
            }
            SpacerHeight(height = 10)
            InputField(name = "Phone Number", value = teacherPhoneNumber.value) {
                teacherPhoneNumber.value = it
            }
            SpacerHeight(height = 10)
            InputField(name = "Registration Number", value = teacherRegistrationNumber.value) {
                teacherRegistrationNumber.value = it
            }
            SpacerHeight(height = 10)
            DropDownSpiner(items = departmentList, selecedItem = selectedDepartment)
            SpacerHeight(height = 30)
            DropDownSpiner(items = shiftList, selecedItem = selectedShift)
            SpacerHeight(height = 30)
            OutlinedButton(onClick = {
                isInfo.value = false
                if (teacherName.value.isNotEmpty() && teacherEmail.value.isNotEmpty() && teacherPassword.value.isNotEmpty() && teacherPhoneNumber.value.isNotEmpty() && teacherRegistrationNumber.value.isNotEmpty()) {
                    vm.addTeacher(teacherEmail.value, teacherPassword.value)
                }else{
                    isInfo.value = true
                    info.value = "Please Fill All Fields"
                }
            }, modifier = Modifier.fillMaxWidth(0.8F)) {

                Text(text = "ADD TEACHER")
            }
            SpacerHeight(height = 30)
            ResultShowInfo(
                visible = addTeacherState.error.isNotEmpty(),
                data = addTeacherState.error
            )
            ResultShowInfo(
                visible = saveTeacherInfoState.error.isNotEmpty(),
                data = saveTeacherInfoState.error
            )
            ResultShowInfo(visible = isInfo.value, data = info.value)
            ResultShowInfo(visible = saveTeacherInfoState.data, data = "Teacher Added Successfully")
            if (addTeacherState.isLoading || saveTeacherInfoState.isLoading) {
//                LottieAnim(anim = R.raw.loading, )
                Text("Loading")
            }
            SpacerHeight(height = 100)
        }
    }
    if (addTeacherState.data.isNotEmpty()) {
        val teacherDataModel = TeacherDataModel(
            teacherName = teacherName.value,
            teacherEmail = teacherEmail.value,
            teacherPassword = teacherPassword.value,
            teacherPhone = teacherPhoneNumber.value,
            teacherRegNo = teacherRegistrationNumber.value,
            teacherDepartment = selectedDepartment.value,
            teacherShift = selectedShift.value,
            teacherID = addTeacherState.data,
            photo = ""
        )
        vm.saveTeacherInfo(teacherDataModel)
    }
}