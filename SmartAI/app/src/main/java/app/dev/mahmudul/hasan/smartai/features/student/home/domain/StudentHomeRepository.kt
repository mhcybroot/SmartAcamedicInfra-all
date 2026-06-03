package app.dev.mahmudul.hasan.smartai.features.student.home.domain

import app.dev.smartacademicinfrastructure.CourseModel
import app.dev.smartacademicinfrastructure.StudentDataModel
import com.appdevmhr.dpi_sai.di.Resourse
import kotlinx.coroutines.flow.Flow

interface StudentHomeRepository {
    suspend fun getStudentCourses (studentDataModel: StudentDataModel): Flow<Resourse<List<CourseModel>>>
    suspend fun getStudentProfile (studentId: String): Flow<Resourse<StudentDataModel>>
    suspend fun studentSignOut()
}