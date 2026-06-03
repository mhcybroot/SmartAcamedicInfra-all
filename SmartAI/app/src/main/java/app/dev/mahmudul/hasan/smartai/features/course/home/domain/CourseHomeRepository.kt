package app.dev.mahmudul.hasan.smartai.features.course.home.domain

import androidx.compose.runtime.MutableState
import app.dev.mahmudul.hasan.smartai.features.course.home.data.MessageItemModel
import app.dev.smartacademicinfrastructure.CourseModel
import app.dev.smartacademicinfrastructure.StudentDataModel
import com.appdevmhr.dpi_sai.di.Resourse
import kotlinx.coroutines.flow.Flow

interface CourseHomeRepository {
    suspend fun getStudentCourseData(courseID:String) : Flow<Resourse<CourseModel>>
    suspend fun getTeacherCourseData(courseID:String) : Flow<Resourse<CourseModel>>
    suspend fun getMessages(courseModel: CourseModel) : Flow<Resourse<List<MessageItemModel>>>
    suspend fun StoreStudentMessage(message:String, courseModel: CourseModel,) : Flow<Resourse<Boolean>>
    suspend fun StoreTeacherMessage(message:String, courseModel: CourseModel,) : Flow<Resourse<Boolean>>
}