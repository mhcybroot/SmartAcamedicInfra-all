package app.dev.mahmudul.hasan.smartai.features.student.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dev.mahmudul.hasan.smartai.features.student.home.domain.StudentHomeRepository
import app.dev.mahmudul.hasan.smartai.features.student.profile.domain.StudentProfileRepository
import app.dev.smartacademicinfrastructure.StudentDataModel
import com.appdevmhr.dpi_sai.di.Resourse
import com.appdevmhr.dpi_sai.di.doOnFailure
import com.appdevmhr.dpi_sai.di.doOnLoading
import com.appdevmhr.dpi_sai.di.doOnSuccess
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentProfileViewModel(
    private val studentProfileRepository: StudentProfileRepository,
    private val studentHomeRepository: StudentHomeRepository,
    firebaseAuth: FirebaseAuth
) : ViewModel() {

    private var _studentProfile = MutableStateFlow<StudentProfileState>(StudentProfileState())
    val studentProfileState = _studentProfile.asStateFlow()
    private var _updateStudentProfile = MutableStateFlow<UpdateStudentProfileState>(UpdateStudentProfileState())
    val updateStudentProfileState = _updateStudentProfile.asStateFlow()

    init {
        getStudentProfile(firebaseAuth.currentUser?.uid ?: "")
    }

    fun getStudentProfile(studentId: String) {
        viewModelScope.launch {
           val result= studentHomeRepository.getStudentProfile(studentId)
            result.doOnLoading {
                _studentProfile.value = StudentProfileState(isLoading = true)
            }.doOnSuccess {
                _studentProfile.value = StudentProfileState(data = it)
            }.doOnFailure {
                _studentProfile.value = StudentProfileState(error = it.message!!)
            }.collect{}
        }
    }

    fun updateStudentProfile(updateStudentMap: Map<String,String>) {
        viewModelScope.launch {
           val result = studentProfileRepository.updateStudentProfile(updateStudentMap)
            result.doOnLoading {
                _updateStudentProfile.value = UpdateStudentProfileState(isLoading = true)
            }.doOnSuccess {
                _updateStudentProfile.value = UpdateStudentProfileState(data = true)
            }.doOnFailure {
                _updateStudentProfile.value = UpdateStudentProfileState(error = it.message!!)
            }.collect{}
        }
    }
}
data class StudentProfileState(
    val data: StudentDataModel = StudentDataModel(),
    val error: String = "",
    val isLoading: Boolean = false
)
data class UpdateStudentProfileState(
    val data: Boolean = false,
    val error: String = "",
    val isLoading: Boolean = false
)
