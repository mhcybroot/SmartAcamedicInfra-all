package app.dev.mahmudul.hasan.smartai.features.course

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import app.dev.dpi_sai.component.DropDownSpiner
import app.dev.dpi_sai.component.InputField
import app.dev.dpi_sai.component.SpacerHeight
import app.dev.mahmudul.hasan.smartai.R
import app.dev.mahmudul.hasan.smartai.component.LottieAnim
import app.dev.mahmudul.hasan.smartai.utils.Utils.departmentList
import app.dev.mahmudul.hasan.smartai.utils.Utils.groupList
import app.dev.mahmudul.hasan.smartai.utils.Utils.semesterList
import app.dev.mahmudul.hasan.smartai.utils.Utils.sessionList
import app.dev.mahmudul.hasan.smartai.utils.Utils.shiftList
import app.dev.smartacademicinfrastructure.CourseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.clearBackStack
import com.ramcosta.composedestinations.navigation.navigate
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@Destination
@Composable
fun AddCourse(destination: DestinationsNavigator,) {

    val subjectName = remember {
        mutableStateOf("")
    }
    val subjectCode = remember {
        mutableStateOf("")
    }
    val courseSemester = remember {
        mutableStateOf(semesterList.first())
    }
    val courseShift = remember {
        mutableStateOf(shiftList.first())
    }
    val courseGroup = remember {
        mutableStateOf(groupList.first())
    }
    val courseSession = remember {
        mutableStateOf(sessionList.first())
    }
    val courseDepartment = remember {
        mutableStateOf(departmentList.first())
    }
    val classTestTotalNumber = remember {
        mutableStateOf("")
    }
    val jobTotalNumber = remember {
        mutableStateOf("")
    }
    val midTotalNumber = remember {
        mutableStateOf("")
    }
    val quizTestTotalNumber = remember {
        mutableStateOf("")
    }
    val sessionalTotalNumber = remember {
        mutableStateOf("")
    }
    val addCourseButton = remember {
        mutableStateOf("Add Course")
    }
    val isLoading = remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    val context  = LocalContext.current.applicationContext
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            SpacerHeight(height = 100)
            Text(
                text = "ADD COURSE",
                fontSize = 40.sp,
                fontWeight = FontWeight.W900,
                textAlign = TextAlign.Center
            )
            SpacerHeight(height = 50)
            InputField(
                name = "Subject Name",
                value = subjectName.value,
            ) {
                subjectName.value = it
            }
            SpacerHeight(height = 10)
            InputField(
                name = "Subject Code",
                value = subjectCode.value,
            ) {
                subjectCode.value = it
            }
            SpacerHeight(height = 10)
            DropDownSpiner(items = sessionList, selecedItem = courseSession)
            SpacerHeight(height = 30)
            DropDownSpiner(items = semesterList, selecedItem = courseSemester)
            SpacerHeight(height = 30)
            DropDownSpiner(items = departmentList, selecedItem = courseDepartment)
            SpacerHeight(height = 30)
            DropDownSpiner(items = shiftList, selecedItem = courseShift)
            SpacerHeight(height = 30)
            DropDownSpiner(items = groupList, selecedItem = courseGroup)
            SpacerHeight(height = 30)
            InputField(
                name = "Class Test Total Number",
                value = classTestTotalNumber.value,
            ) {
                classTestTotalNumber.value = it
            }
            SpacerHeight(height = 10)
            InputField(
                name = "Job Total Number",
                value = jobTotalNumber.value,
            ) {
                jobTotalNumber.value = it
            }
            SpacerHeight(height = 10)
            InputField(
                name = "Mid Total Number",
                value = midTotalNumber.value,
            ) {
                midTotalNumber.value = it
            }
            SpacerHeight(height = 10)
            InputField(
                name = "Quiz Test Total Number",
                value = quizTestTotalNumber.value,
            ) {
                quizTestTotalNumber.value = it
            }
            SpacerHeight(height = 10)
            InputField(
                name = "Sessional Total Number",
                value = sessionalTotalNumber.value,
            ) {
                sessionalTotalNumber.value = it
            }
            SpacerHeight(height = 10)
            OutlinedButton(onClick = {
                coroutineScope.launch {
                    checkCourseFieldIsEmpty(
                        subjectName,
                        subjectCode,
                        courseSemester,
                        courseShift,
                        courseGroup,
                        courseSession,
                        courseDepartment,
                        classTestTotalNumber,
                        jobTotalNumber,
                        midTotalNumber,
                        quizTestTotalNumber,
                        sessionalTotalNumber,
                        addCourseButton,
                        isLoading,
                        destination
                    )
                }

            }, modifier = Modifier.fillMaxWidth(0.8F)) {
                Text(
                    text = addCourseButton.value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
            SpacerHeight(height = 20)
            if (isLoading.value) {
//                LottieAnim(anim = R.raw.loading, )
                Text("Loading")
                SpacerHeight(height = 10)
                Text(
                    text = "Please wait",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
            SpacerHeight(height = 100)


        }
    }

}

suspend fun checkCourseFieldIsEmpty(
    subjectName: MutableState<String>,
    subjectCode: MutableState<String>,
    courseSemester: MutableState<String>,
    courseShift: MutableState<String>,
    courseGroup: MutableState<String>,
    courseSession: MutableState<String>,
    courseDepartment: MutableState<String>,
    classTestTotalNumber: MutableState<String>,
    jobTotalNumber: MutableState<String>,
    midTotalNumber: MutableState<String>,
    quizTestTotalNumber: MutableState<String>,
    sessionalTotalNumber: MutableState<String>,
    addCourseButton: MutableState<String>,
    loading: MutableState<Boolean>,
    destination: DestinationsNavigator
) {
    if (subjectName.value.isEmpty()) {
        addCourseButton.value = "Subject Name is required"
    } else if (subjectCode.value.isEmpty()) {
        addCourseButton.value = "Subject Code is required"
    } else if (classTestTotalNumber.value.isEmpty()) {
        addCourseButton.value = "Class Test Total Number is required"
    } else if (jobTotalNumber.value.isEmpty()) {
        addCourseButton.value = "Job Total Number is required"
    } else if (midTotalNumber.value.isEmpty()) {
        addCourseButton.value = "Mid Total Number is required"
    } else if (quizTestTotalNumber.value.isEmpty()) {
        addCourseButton.value = "Quiz Test Total Number is required"
    } else if (sessionalTotalNumber.value.isEmpty()) {
        addCourseButton.value = "Sessional Total Number is required"
    } else {
        addCourseButton.value = "Adding Course"
        loading.value = true
        val firebaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val courseData = CourseModel(
            courseName = subjectName.value,
            courseCode = subjectCode.value,
            courseSemester = courseSemester.value,
            courseShift = courseShift.value,
            courseGroup = courseGroup.value,
            courseSession = courseSession.value,
            courseDepartment = courseDepartment.value,
            courseCT = classTestTotalNumber.value,
            courseJob = jobTotalNumber.value,
            courseQuiz = quizTestTotalNumber.value,
            courseSessional = sessionalTotalNumber.value,
            courseTeacherName = getCourseTeacherName(firebaseAuth, firestore),
            courseTeacherEmail = firebaseAuth.currentUser!!.email!!,
            courseTeacherID = firebaseAuth.currentUser!!.uid,
            courseMid = midTotalNumber.value
        )
        storeCourseDataToFirestore(firestore, courseData, addCourseButton, loading, destination)
    }
}

fun storeCourseDataToFirestore(
    firestore: FirebaseFirestore,
    courseData: CourseModel,
    addCourseButton: MutableState<String>,
    loading: MutableState<Boolean>,
    destination: DestinationsNavigator,
) {

    firestore.collection("CourseData").document(courseData.courseSession)
        .collection(courseData.courseDepartment).document(courseData.courseSemester)
        .collection(courseData.courseShift).document(courseData.courseGroup).collection("Courses")
        .document(courseData.courseCode).set(courseData)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                addCourseToTeacherProfile(firestore, courseData, addCourseButton, loading, destination)

            } else {
                addCourseButton.value = "Something went wrong"
                loading.value = false
            }
        }
}

fun addCourseToTeacherProfile(
    firestore: FirebaseFirestore,
    courseData: CourseModel,
    addCourseButton: MutableState<String>,
    loading: MutableState<Boolean>,
    destination: DestinationsNavigator
) {
    firestore.collection("TeachersList").document(courseData.courseTeacherID).collection("Courses")
        .document(courseData.courseCode).set(courseData)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                destination.popBackStack()
                addCourseButton.value = "Course Added"
                loading.value = false
            } else {
                addCourseButton.value = "Something went wrong"
                loading.value = false
            }
        }
}

suspend fun getCourseTeacherName(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore): String {
    val result =
        firestore.collection("TeachersList").document(firebaseAuth.currentUser!!.uid).get().await()
    return result.getString("teacherName")!!
}
