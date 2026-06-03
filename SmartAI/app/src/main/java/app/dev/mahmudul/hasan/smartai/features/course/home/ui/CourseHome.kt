package app.dev.mahmudul.hasan.smartai.features.course.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.sharp.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.dpi_sai.component.SpacerHeight
import app.dev.mahmudul.hasan.smartai.R
import app.dev.mahmudul.hasan.smartai.component.ChatItemRow
import app.dev.mahmudul.hasan.smartai.component.ResultShowLoading
import app.dev.mahmudul.hasan.smartai.features.course.home.data.MessageItemModel
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun CourseHome(
    vm: CourseHomeViewModel = koinViewModel(),
    courseID: String,
    role: String
) {
    val courseDataState by vm.courseDataState.collectAsState()
    val messageListState by vm.messageListState.collectAsState()
    val storeMessageState by vm.storeMessageState.collectAsState()


    val firebaseAuth = koinInject<FirebaseAuth>()
//    val defaultOptions = JitsiMeetConferenceOptions.Builder()
//        .setFeatureFlag("welcomepage.enabled", false)
//        .build()
//
//    val options = JitsiMeetConferenceOptions.Builder()
//        .setRoom("DPI_VIRTUAL_CLASS_${courseDataState.data}_DEVELOPER_MAHMUDUL_HASAN")
//        .setSubject(courseDataState.data.courseName)
//        .build()

    LaunchedEffect(courseID) {
        if (role == "Teacher") {
            vm.getTeacherCourseData(courseID)
        } else {
            vm.getStudentCourseData(courseID)
        }
    }



    LaunchedEffect(key1 = courseDataState.data) {
        vm.getMessages(courseDataState.data)

    }


    println(messageListState.data)
    val context = LocalContext.current
    TopAppBar(
        title = { Text("") },
        actions = {
            IconButton(onClick = {
//                JitsiMeet.setDefaultConferenceOptions(defaultOptions)
//                JitsiMeetActivity.launch(context, options)

            }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "meeting")
            }
            IconButton(onClick = {

            }) {
                Icon(Icons.Default.Settings, contentDescription = "meeting")
            }

        },
    )

    if (courseDataState.isLoading) {
        ResultShowLoading()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        MessageList(messageListState.data, firebaseAuth.currentUser!!.uid)
        SpacerHeight(height = 20)
        SendMessageRow(courseId = courseID, vm,role)
    }


}

@Composable
fun MessageList(items: List<MessageItemModel>, uid: String) {
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight(0.7F)
            .fillMaxWidth(),
    ) {
        items(items) { item ->
            ChatItemRow(
                chatItem = item,
                photo = "",
                uid = uid,
            )
        }
    }
}


@Composable
fun SendMessageRow(
    courseId: String,
    vm: CourseHomeViewModel,
    role: String
) {

    val storeMessageState by vm.storeMessageState.collectAsState()
    val courseDataState by vm.courseDataState.collectAsState()
    val message = remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = storeMessageState.data) {
        message.value = ""
    }
    SpacerHeight(height = 10)
    Row(
        modifier = Modifier
            .fillMaxHeight(0.25F)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Spacer(modifier = Modifier.width(20.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.8F)
                .fillMaxHeight(),
            value = message.value,
            onValueChange = {
                message.value = it
            },
            textStyle = TextStyle(
                fontSize = 20.sp
            ),

            label = { Text(text = "Enter Your Message") },
            leadingIcon = {
                Icon(
                    Icons.Default.MailOutline,
                    contentDescription = "Favorite",
                )
            },
            singleLine = false,

            )
        IconButton(onClick = {
            if (role == "Teacher") {
                vm.StoreTeacherMessage(message.value, courseModel = courseDataState.data)
            } else {
                vm.StoreStudentMessage(message.value, courseModel = courseDataState.data)
            }
        }, Modifier.size(width = 100.dp, height = 50.dp)) {
            Icon(
                Icons.Sharp.ArrowForward,
                contentDescription = "send",
                modifier = Modifier.size(width = 100.dp, height = 50.dp),
            )
        }
    }

}
