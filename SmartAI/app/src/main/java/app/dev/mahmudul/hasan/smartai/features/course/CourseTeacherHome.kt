package app.dev.mahmudul.hasan.smartai.features.course

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun CourseTeacherHome(
    virtualCourse: String
) {
    val clipboardManager: androidx.compose.ui.platform.ClipboardManager =
        LocalClipboardManager.current
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "1. Copy Room \n2. Open Chrome \n3. Go to meet.jit.si \n4. Paste Room \n5. Start Meeting.\n6. Thank you Continue with your class.")
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = {
            clipboardManager.setText(AnnotatedString(text = virtualCourse))
            Toast.makeText(context,clipboardManager.getText().toString(), Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "COPY ROOM ")
        }
    }
}

