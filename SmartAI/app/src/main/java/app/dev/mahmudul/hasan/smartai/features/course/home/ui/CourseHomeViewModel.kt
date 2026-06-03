package app.dev.mahmudul.hasan.smartai.features.course.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dev.mahmudul.hasan.smartai.features.course.home.data.MessageItemModel
import app.dev.mahmudul.hasan.smartai.features.course.home.domain.CourseHomeRepository
import app.dev.smartacademicinfrastructure.CourseModel
import com.appdevmhr.dpi_sai.di.doOnFailure
import com.appdevmhr.dpi_sai.di.doOnLoading
import com.appdevmhr.dpi_sai.di.doOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourseHomeViewModel(
    private val courseHomeRepository: CourseHomeRepository
) : ViewModel() {
    private var _courseData = MutableStateFlow<CourseDataState>(CourseDataState())
    val courseDataState = _courseData.asStateFlow()

    private var _messageList = MutableStateFlow<GetMessageState>(GetMessageState())
    val messageListState = _messageList.asStateFlow()

    private var _storeMessage = MutableStateFlow<StoreMessageState>(StoreMessageState())
    val storeMessageState = _storeMessage.asStateFlow()


    fun getStudentCourseData(courseID: String) {
        viewModelScope.launch {
            val result = courseHomeRepository.getStudentCourseData(courseID)
            result.doOnLoading {
                _courseData.value = CourseDataState(isLoading = true)
            }.doOnFailure {
                _courseData.value = CourseDataState(error = it.message!!)
            }.doOnSuccess {
                _courseData.value = CourseDataState(data = it)
            }.collect {

            }

        }
    }

    fun getTeacherCourseData(courseID: String) {
        viewModelScope.launch {
            val result = courseHomeRepository.getTeacherCourseData(courseID)
            result.doOnLoading {
                _courseData.value = CourseDataState(isLoading = true)
            }.doOnFailure {
                _courseData.value = CourseDataState(error = it.message!!)
            }.doOnSuccess {
                _courseData.value = CourseDataState(data = it)
            }.collect {

            }

        }
    }

    fun getMessages(courseModel: CourseModel) {
        viewModelScope.launch {
            val result = courseHomeRepository.getMessages(courseModel)
            result.doOnLoading {
                _messageList.value = GetMessageState(isLoading = true)
            }.doOnFailure {
                _messageList.value = GetMessageState(error = it.message?.toString()!!)
            }.doOnSuccess {
                _messageList.value = GetMessageState(data = it)
            }.collect {

            }

        }
    }

    fun StoreStudentMessage(message: String, courseModel: CourseModel) {
        viewModelScope.launch {
            val result = courseHomeRepository.StoreStudentMessage(message, courseModel)
            result.doOnLoading {
                _storeMessage.value = StoreMessageState(isLoading = true)
            }.doOnFailure {
                _storeMessage.value = StoreMessageState(error = it.message!!)
            }.doOnSuccess {
                _storeMessage.value = StoreMessageState(data = it)
            }.collect {

            }

        }

    }

    fun StoreTeacherMessage(message: String, courseModel: CourseModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = courseHomeRepository.StoreStudentMessage(message, courseModel)
            result.doOnLoading {
                _storeMessage.value = StoreMessageState(isLoading = true)
            }.doOnFailure {
                _storeMessage.value = StoreMessageState(error = it.message!!)
            }.doOnSuccess {
                _storeMessage.value = StoreMessageState(data = it)
            }.collect {

            }

        }

    }


}

data class CourseDataState(
    val data: CourseModel = CourseModel(),
    val error: String = "",
    val isLoading: Boolean = false
)

data class GetMessageState(
    val data: List<MessageItemModel> = emptyList<MessageItemModel>(),
    val error: String = "",
    val isLoading: Boolean = false
)

data class StoreMessageState(
    val data: Boolean = false,
    val error: String = "",
    val isLoading: Boolean = false
)
