package app.dev.mahmudul.hasan.smartai.features.admin.login.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.dpi_sai.component.InputField
import app.dev.mahmudul.hasan.smartai.R
import app.dev.mahmudul.hasan.smartai.features.destinations.AddTeacherScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@Destination
@Composable
fun AdminLoginScreen(destination: DestinationsNavigator) {
    var email = remember {
        mutableStateOf("")
    }

    var password = remember {
        mutableStateOf("")
    }
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ADMIN AREA",
            fontSize = 40.sp,
            fontWeight = FontWeight.W900,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        InputField("Email", email.value) {
            email.value = it
        }
        Spacer(modifier = Modifier.height(20.dp))
        InputField("Password", password.value) {
            password.value = it
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(onClick = {
            if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
                if (email.value == "1979" && password.value == "1979") {
                    destination.navigate(AddTeacherScreenDestination){
                        popUpTo(AddTeacherScreenDestination){
                            inclusive = true
                        }
                    }
                } else {
                    email.value = "Invalid Credentials"
                    password.value = "Invalid Credentials"

                }
            }
        }) {
            Text(text = "Login" , modifier = Modifier.fillMaxWidth(0.8F), textAlign = TextAlign.Center)
        }
    }

}