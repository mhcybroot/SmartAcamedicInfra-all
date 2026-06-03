package app.dev.mahmudul.hasan.smartai.features.course.attendance.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dev.mahmudul.hasan.smartai.features.course.attendance.data.AttendanceModel
import app.dev.mahmudul.hasan.smartai.features.course.attendance.domain.AttendanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.appdevmhr.dpi_sai.di.doOnFailure
import com.appdevmhr.dpi_sai.di.doOnLoading
import com.appdevmhr.dpi_sai.di.doOnSuccess

class AttendanceViewModel(private val repository: AttendanceRepository) : ViewModel() {

    private var _putAttendanceState = MutableStateFlow<PutAttendanceState>(PutAttendanceState())
    val putAttendanceState = _putAttendanceState.asStateFlow()

    private var _getAttendanceState = MutableStateFlow<GetAttendanceState>(GetAttendanceState())
    val getAttendanceState = _getAttendanceState.asStateFlow()
    fun getAttendance(courseCode: String,collection: String,subCollection: String) {
        viewModelScope.launch {
            val result = repository.getAttendanceSheet(courseCode,collection,subCollection)
            result.doOnLoading {
                _getAttendanceState.value = GetAttendanceState(isLoading = true)
            }.doOnFailure {
                _getAttendanceState.value = GetAttendanceState(error = it.message!!)
            }.doOnSuccess {
                _getAttendanceState.value = GetAttendanceState(data = it)
            }.collect {
                println("AttendanceViewModel.getAttendance $it")
            }
        }
    }
    fun putAttendance(attendanceModel: AttendanceModel,collection: String,subCollection: String) {
        viewModelScope.launch {
            val result = repository.putAttendanceSheet(attendanceModel,collection,subCollection)
            result.doOnLoading {
                _putAttendanceState.value = PutAttendanceState(isLoading = true)
            }.doOnFailure {
                _putAttendanceState.value = PutAttendanceState(error = it.message!!)
            }.doOnSuccess {
                _putAttendanceState.value = PutAttendanceState(data = it)
            }.collect {
                println("AttendanceViewModel.putAttendance $it")
            }
        }
    }
}

data class PutAttendanceState(
    val data: Boolean = false,
    val error: String = "",
    val isLoading: Boolean = false
)
data class GetAttendanceState(
    val data: List<AttendanceModel> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)
