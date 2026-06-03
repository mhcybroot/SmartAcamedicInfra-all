package app.dev.mahmudul.hasan.smartai.component

import android.content.Intent
import android.media.RouteListingPreference.Item
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.dpi_sai.component.SpacerHeight
import app.dev.mahmudul.hasan.smartai.features.course.attendance.data.AttendanceModel
import app.dev.mahmudul.hasan.smartai.features.course.attendance.ui.AttendanceViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date


@Composable
fun StudentTemp(courseCode: String, vm: AttendanceViewModel= koinViewModel(), collection: String, subCollection: String) {
    val getAttendanceState = vm.getAttendanceState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        vm.getAttendance(courseCode,collection,subCollection)
    }
    Column(
        modifier = Modifier.padding()
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
//            LottieAnim(anim = R.raw., )
            Text("Loading")
        }
    }
}
@Composable
fun ItemList(modifier: Modifier = Modifier, items: List<AttendanceModel>) {
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
    ) {
        items(items) { item ->
            SingleItem(attendanceModel = item)

        }
    }
}


@Composable
fun SingleItem(attendanceModel: AttendanceModel) {
    val context = LocalContext.current
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(attendanceModel.url)) }
    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                context.startActivity(intent)
            },
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "OPENED : ${attendanceModel.date}" , fontSize = 20.sp)
            SpacerHeight(10)
            Text(text = "DEAD LINE : ${attendanceModel.deadLine}", fontSize = 20.sp)
            SpacerHeight(10)
        }
    }
}

fun getDate(): String {
    var result = ""
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
        result = current.format(formatter).toString()

    } else {
        var date = Date()
        val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
        result = formatter.format(date)
    }
    return result
}