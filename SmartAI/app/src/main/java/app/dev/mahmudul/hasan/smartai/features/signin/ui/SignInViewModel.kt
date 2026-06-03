package app.dev.mahmudul.hasan.smartai.features.signin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dev.mahmudul.hasan.smartai.features.signin.data.SignInUseCase
import app.dev.mahmudul.hasan.smartai.features.signin.domain.SignInRepository
import com.appdevmhr.dpi_sai.di.Resourse
import com.appdevmhr.dpi_sai.di.doOnFailure
import com.appdevmhr.dpi_sai.di.doOnLoading
import com.appdevmhr.dpi_sai.di.doOnSuccess
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInRepository: SignInRepository,
    private val useCase: SignInUseCase
) : ViewModel() {

    private val _signInState: MutableStateFlow<SignInState> =
        MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    private val _checkRoleState: MutableStateFlow<CheckRoleState> =
        MutableStateFlow(CheckRoleState())
    val checkRoleState = _checkRoleState.asStateFlow()


    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result = useCase.signIn(email, password)
            result.doOnSuccess {
                _signInState.value = SignInState(data = it)
            }.doOnFailure {
                _signInState.value = SignInState(error = it.message!!)
            }.doOnLoading {
                _signInState.value = SignInState(isLoading = true)
            }.collect {}
        }
    }


    fun checkRole(currentUser:String) {
        viewModelScope.launch {
            val result = signInRepository.checkIsStudentOrTeacher(currentUser)
            result.doOnSuccess {
                _checkRoleState.value = CheckRoleState(data = it)
            }.doOnFailure {
                _checkRoleState.value = CheckRoleState(error = it.message!!)
            }.doOnLoading {
                _checkRoleState.value = CheckRoleState(isLoading = true)
            }.collect{}
        }
    }

}

data class SignInState(
    val data: String = "",
    val error: String = "",
    val isLoading: Boolean = false
)
data class CheckRoleState(
    val data: String = "",
    val error: String = "",
    val isLoading: Boolean = false
)