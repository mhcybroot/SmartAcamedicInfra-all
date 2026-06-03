package app.dev.mahmudul.hasan.smartai.features.course.details

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.dpi_sai.component.SpacerHeight
import app.dev.mahmudul.hasan.smartai.component.ChatItemRow
import app.dev.mahmudul.hasan.smartai.features.course.CourseTeacherHome
import app.dev.mahmudul.hasan.smartai.features.course.home.data.MessageItemModel
import app.dev.mahmudul.hasan.smartai.features.destinations.CourseDetailsDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.CourseTeacherHomeDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.SettingsStudentDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.SettingsTeacherDestination
import app.dev.mahmudul.hasan.smartai.ui.theme.SmartAITheme
import app.dev.mahmudul.hasan.smartai.utils.Utils.userRole
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun CourseDetails(
    destination: DestinationsNavigator,
    courseId: String,
    courseName: String,
    teacherId: String,
    session: String,
    department: String,
    shift: String,
    semester: String,
    group: String,
    role: String,
    userName: String
) {

    val message = remember {
        mutableStateOf("")
    }


    val db = FirebaseDatabase.getInstance()
    val currentUser = Firebase.auth.currentUser?.uid
    val referenc = db.getReference("Courses").child(department).child(session)
        .child(semester).child(shift).child(group).child(courseId)
    val courseInfo: MutableState<List<MessageItemModel>> = remember { mutableStateOf(emptyList()) }
    val context = LocalContext.current.applicationContext

    val options = JitsiMeetConferenceOptions.Builder()
        .setRoom("DPI_VIRTUAL_CLASS_$courseId$courseName$teacherId$session$department$shift$semester$group _DEVELOPER_MAHMUDUL_HASAN")
        .setFeatureFlag("welcomepage.enabled", false)
        .setSubject(courseName)
        .build()

    val coroutineScope = rememberCoroutineScope()






    getCourseData(referenc, courseInfo)
    Surface {

        TopAppBar(
            title = { Text(courseName) },
            actions = {
                IconButton(onClick = {
                    JitsiMeetActivity.launch(context, options)
                }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "meeting")
                }
                IconButton(onClick = {
                    if (userRole.first() == role) {
                        destination.navigate(SettingsTeacherDestination(courseId)){
                            popUpTo(CourseDetailsDestination){
                                inclusive = true
                            }
                        }
                    } else {
                        destination.navigate(SettingsStudentDestination(courseId)){
                            popUpTo(CourseDetailsDestination){
                                inclusive = true
                            }
                        }
                    }
                }) {
                    Icon(Icons.Default.Settings, contentDescription = "meeting")
                }

            },
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            MessageList(courseInfo.value.reversed(), teacherId)
            Spacer(modifier = Modifier.width(20.dp))

            sendMessageRow(
                message = message,
                courseId = courseId,
                teacherId = teacherId,
                department = department,
                semester = semester,
                session = session,
                shift = shift,
                group = group,
                coroutineScope,
                db, role,
                userName
            )

        }
    }


}


fun getCourseData(referenc: DatabaseReference, courseInfo: MutableState<List<MessageItemModel>>) {

    referenc.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val messages = mutableListOf<MessageItemModel>()
            snapshot.children.forEach { data ->
                val message = data.getValue(MessageItemModel::class.java)
                if (message != null) {
                    messages.add(message)
                }
            }
            courseInfo.value = messages
        }

        override fun onCancelled(error: DatabaseError) {
            courseInfo.value = emptyList()
        }
    })
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun sendMessageRow(
    message: MutableState<String>,
    courseId: String,
    teacherId: String,
    department: String,
    semester: String,
    session: String,
    shift: String,
    group: String,
    coroutineScope: CoroutineScope,
    db: FirebaseDatabase,
    role: String,
    userName: String
) {
    SmartAITheme {
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

                coroutineScope.launch {
                    if (message.value.isNotEmpty()) {
                        storeMessage(
                            message,
                            courseId,
                            teacherId,
                            department,
                            semester,
                            session,
                            shift,
                            group,
                            db,
                            role,
                            userName

                        )
                    }
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


}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat")
suspend fun storeMessage(
    message: MutableState<String>,
    courseCode: String,
    uid: String,
    department: String,
    semester: String,
    session: String,
    shift: String,
    group: String,
    firebaseDatabase: FirebaseDatabase,
    role: String,
    userName: String
) {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a")
    val formattedDateTime = currentDateTime.format(formatter)


    val reference =
        firebaseDatabase.getReference("Courses").child(department).child(session).child(semester)
            .child(shift).child(group).child(courseCode).child(
                formattedDateTime
            )

    val messageItem =
        MessageItemModel(message.value, "${uid}", "${formattedDateTime}", "${userName}")
    if (userName.isNotEmpty()) {
        reference.setValue(messageItem).addOnSuccessListener {
            message.value = ""
        }.addOnFailureListener {
            message.value = "Failed to send message"
        }
    } else {
        message.value = "name is empty send message"
    }


}
