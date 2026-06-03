package app.dev.mahmudul.hasan.smartai.features.signin.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dev.dpi_sai.component.InputField
import app.dev.dpi_sai.component.SpacerHeight
import app.dev.mahmudul.hasan.smartai.R
import app.dev.mahmudul.hasan.smartai.component.LottieAnim
import app.dev.mahmudul.hasan.smartai.component.ResultShowInfo
import app.dev.mahmudul.hasan.smartai.features.destinations.AdminLoginScreenDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.SignInScreenDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.SignUpScreenDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.StudentHomeScreenDestination
import app.dev.mahmudul.hasan.smartai.features.destinations.TeacherHomeScreenDestination
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import okhttp3.internal.wait
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
@Destination
@RootNavGraph(start = true)
fun SignInScreen(destination: DestinationsNavigator, vm: SignInViewModel = koinViewModel()) {
    val signInState by vm.signInState.collectAsStateWithLifecycle()
    val checkRoleState by vm.checkRoleState.collectAsState()
    val firebaseAuth = koinInject<FirebaseAuth>()
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var isInfo by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = signInState.isLoading) {
        if (firebaseAuth.currentUser != null) {
            vm.checkRole(firebaseAuth.currentUser!!.uid)
        }
    }

    LazyColumn(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Box(modifier = Modifier
                    .fillMaxSize()
                  ) {
                Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                ) {

                    SpacerHeight(height = 50)
                    Text(
                            text = "SIGN IN",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.W900,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                    )
                    SpacerHeight(height = 40)
//                    LottieAnim(R.raw.sign)
                    Text("Please Wait ....")

                    Spacer(modifier = Modifier.height(50.dp))
                    InputField("Email", email) {
                        email = it
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    InputField(name = "Password", value = password) {
                        password = it
                    }

                    SpacerHeight(height = 10)
                    if (signInState.isLoading || checkRoleState.isLoading) {
//                        LottieAnim(anim = R.raw.loading)
                        Text("Loading")
                    } else {
                        OutlinedButton(onClick = {
                            isLoading = true
                            isInfo = false
                            vm.signIn(email, password)
                        }, modifier = Modifier.fillMaxWidth(0.8F)) {
                            Text(
                                    text = "Login",
                                    fontSize = 20.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(0.4F)
                            )
                        }
                    }



                    Spacer(modifier = Modifier.height(30.dp))
                    ResultShowInfo(visible = signInState.data.isNotEmpty(), data = "Login Successful")
                    Spacer(modifier = Modifier.height(30.dp))
                    ResultShowInfo(data = signInState.error, visible = signInState.error.isNotEmpty())
                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                            text = "No account? Sign up",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.clickable {
                                destination.navigate(SignUpScreenDestination)
                            }
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                            text = "Admin ? Please click here",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.clickable {
                                destination.navigate(AdminLoginScreenDestination) {
                                    popUpTo(SignInScreenDestination) {
                                        inclusive = true
                                    }
                                }
                            }
                    )

                    SpacerHeight(height = 100)
                }
            }

        }
    }
    when (checkRoleState.data) {
        "Student" -> {
            destination.navigate(StudentHomeScreenDestination) {
                popUpTo(SignInScreenDestination) {
                    inclusive = true
                }
            }
        }

        "Teacher" -> {
            destination.navigate(TeacherHomeScreenDestination) {
                popUpTo(SignInScreenDestination) {
                    inclusive = true
                }
            }
        }
    }
}


