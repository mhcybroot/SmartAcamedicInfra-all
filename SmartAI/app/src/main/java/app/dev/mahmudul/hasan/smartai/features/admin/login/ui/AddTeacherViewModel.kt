package app.dev.mahmudul.hasan.smartai.features.admin.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dev.mahmudul.hasan.smartai.features.admin.addTeacher.domain.AddTeacherRepository
import app.dev.smartacademicinfrastructure.TeacherDataModel
import com.appdevmhr.dpi_sai.di.doOnFailure
import com.appdevmhr.dpi_sai.di.doOnLoading
import com.appdevmhr.dpi_sai.di.doOnSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddTeacherViewModel(private val addTeacherRepository: AddTeacherRepository) : ViewModel() {

    private val _addTeacherState = MutableStateFlow(AddTeacherState())
    val addTeacherState = _addTeacherState
    private val _saveTeacherInfoState = MutableStateFlow(SaveTeacherInfoState())
    val saveTeacherInfoState = _saveTeacherInfoState

    fun addTeacher(email: String, password: String) {
        viewModelScope.launch {
            val result = addTeacherRepository.addTeacher(email, password)
            result.doOnLoading {
                _addTeacherState.value = AddTeacherState(isLoading = true)
            }.doOnFailure {
                _addTeacherState.value = AddTeacherState(error = it.message!!)
            }.doOnSuccess {
                _addTeacherState.value = AddTeacherState(data = it)
            }.collect {
                println("AddTeacherViewModel.addTeacher $it")
            }
        }
    }
    fun saveTeacherInfo(teacherDataModel: TeacherDataModel) {
        viewModelScope.launch {
            val result = addTeacherRepository.saveTeacherInfo(teacherDataModel)
            result.doOnLoading {
                _saveTeacherInfoState.value = SaveTeacherInfoState(isLoading = true)
            }.doOnFailure {
                _saveTeacherInfoState.value = SaveTeacherInfoState(error = it.message!!)
            }.doOnSuccess {
                _saveTeacherInfoState.value = SaveTeacherInfoState(data = it)
            }.collect {
                println("AddTeacherViewModel.saveTeacherInfo $it")
            }
        }

    }

}

data class AddTeacherState(
    val data: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)

data class SaveTeacherInfoState(
    val data: Boolean = false,
    val isLoading: Boolean = false,
    val error: String = ""
)