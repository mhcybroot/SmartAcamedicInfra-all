package app.dev.mahmudul.hasan.smartai.features.teacher.home.domain

import app.dev.smartacademicinfrastructure.CourseModel
import com.appdevmhr.dpi_sai.di.Resourse
import kotlinx.coroutines.flow.Flow

interface TeacherHomeRepository {
    suspend fun getTeacherCourses() : Flow<Resourse<List<CourseModel>>>
}