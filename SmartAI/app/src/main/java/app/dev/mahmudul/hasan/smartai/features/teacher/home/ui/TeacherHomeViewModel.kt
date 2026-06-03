package app.dev.mahmudul.hasan.smartai.features.teacher.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dev.mahmudul.hasan.smartai.features.teacher.home.domain.TeacherHomeRepository
import app.dev.smartacademicinfrastructure.CourseModel
import com.appdevmhr.dpi_sai.di.doOnFailure
import com.appdevmhr.dpi_sai.di.doOnLoading
import com.appdevmhr.dpi_sai.di.doOnSuccess
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TeacherHomeViewModel(
    private val repository: TeacherHomeRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _teacherHomeState = MutableStateFlow(TeacherHomeState())
    val teacherHomeState = _teacherHomeState
    private val _firebaseAuth = auth
    val firebaseAuth = _firebaseAuth

    init {
        getTeacherCourses()
    }

    fun getTeacherCourses() {
        viewModelScope.launch {
            val result = repository.getTeacherCourses()
            result.doOnLoading {
                _teacherHomeState.value = TeacherHomeState(isLoading = true)
            }.doOnFailure {
                _teacherHomeState.value = TeacherHomeState(error = it.message!!)
            }.doOnSuccess {
                _teacherHomeState.value = TeacherHomeState(data = it)
            }.collect {
                println("TeacherHomeViewModel.getTeacherCourses $it")
            }
        }
    }
}

data class TeacherHomeState(
    val data: List<CourseModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)