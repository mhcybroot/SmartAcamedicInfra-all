package app.dev.mahmudul.hasan.smartai.features.signup.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dev.mahmudul.hasan.smartai.features.signup.data.SignUpUseCase
import app.dev.mahmudul.hasan.smartai.features.signup.domain.SignUpRepository
import app.dev.smartacademicinfrastructure.StudentDataModel
import com.appdevmhr.dpi_sai.di.doOnFailure
import com.appdevmhr.dpi_sai.di.doOnLoading
import com.appdevmhr.dpi_sai.di.doOnSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: SignUpRepository) : ViewModel() {

    private var _signUpState = MutableStateFlow<SignUpState>(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    private var _saveStudentInfoState = MutableStateFlow<SaveStudentInfoState>(SaveStudentInfoState())
    val saveStudentInfoState = _saveStudentInfoState.asStateFlow()

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.signUp(email, password)
            result.doOnLoading {
                _signUpState.value = SignUpState(isLoading = true)
            }.doOnFailure {
                _signUpState.value = SignUpState(error = it.message!!)
            }.doOnSuccess {
                _signUpState.value = SignUpState(data = it)
            }.collect {
                println("SignUpViewModel.signUp $it")
            }
        }
    }

    fun saveStudentInfo(studentDataModel: StudentDataModel) {
        viewModelScope.launch {
            val result = repository.saveStudentInfo(studentDataModel)
            result.doOnLoading {
                _saveStudentInfoState.value = SaveStudentInfoState(isLoading = true)
            }.doOnFailure {
                _saveStudentInfoState.value = SaveStudentInfoState(error = it.message!!)
            }.doOnSuccess {
                _saveStudentInfoState.value = SaveStudentInfoState(data = it)
            }.collect {
                println("SignUpViewModel.saveStudentInfo $it")
            }
        }
    }
}

data class SignUpState(
    val data: String = "",
    val error: String = "",
    val isLoading: Boolean = false
)

data class SaveStudentInfoState(
    val data: Boolean = false,
    val error: String = "",
    val isLoading: Boolean = false
)