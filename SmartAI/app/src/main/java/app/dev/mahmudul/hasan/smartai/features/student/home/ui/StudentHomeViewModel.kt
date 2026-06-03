package app.dev.mahmudul.hasan.smartai.features.student.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dev.mahmudul.hasan.smartai.features.student.home.domain.StudentHomeRepository
import app.dev.smartacademicinfrastructure.CourseModel
import app.dev.smartacademicinfrastructure.StudentDataModel
import com.appdevmhr.dpi_sai.di.Resourse
import com.appdevmhr.dpi_sai.di.doOnFailure
import com.appdevmhr.dpi_sai.di.doOnLoading
import com.appdevmhr.dpi_sai.di.doOnSuccess
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentHomeViewModel(
    private val studentHomeRepository: StudentHomeRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private var _studentProfile = MutableStateFlow<StudentProfileState>(StudentProfileState())
    val studentProfileState = _studentProfile.asStateFlow()

    private var _studentCourses = MutableStateFlow<StudentCoursesState>(StudentCoursesState())
    val studentCoursesState = _studentCourses.asStateFlow()

    init {
        getStudentProfile(firebaseAuth.currentUser?.uid ?: "")

    }

    fun getStudentProfile(studentId: String) {
        viewModelScope.launch {
           val result = studentHomeRepository.getStudentProfile(studentId)
            result.doOnLoading {
                _studentProfile.value = StudentProfileState(isLoading = true)
            }.doOnFailure {
                _studentProfile.value = StudentProfileState(error = it.message!!)
            }.doOnSuccess {
                _studentProfile.value = StudentProfileState(data = it)
            }.collect{}
        }
    }

    fun getStudentCourses(studentDataModel: StudentDataModel) {
        viewModelScope.launch {
            val result = studentHomeRepository.getStudentCourses(studentDataModel)
            result.doOnLoading {
                _studentCourses.value = StudentCoursesState(isLoading = true)
            }.doOnFailure {
                _studentCourses.value = StudentCoursesState(error = it.message!!)
            }.doOnSuccess {
                _studentCourses.value = StudentCoursesState(data = it)
            }.collect{}
        }
    }
    fun studentSignOut() {
        viewModelScope.launch {
            studentHomeRepository.studentSignOut()
        }
    }

}
data class StudentProfileState(
    val data: StudentDataModel = StudentDataModel(),
    val error: String = "",
    val isLoading: Boolean = false
)
data class StudentCoursesState(
    val data: List<CourseModel> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)
