package app.dev.mahmudul.hasan.smartai.features.admin.addTeacher.domain

import app.dev.smartacademicinfrastructure.StudentDataModel
import app.dev.smartacademicinfrastructure.TeacherDataModel
import com.appdevmhr.dpi_sai.di.Resourse
import kotlinx.coroutines.flow.Flow

interface AddTeacherRepository {
    suspend fun addTeacher(email: String, password: String): Flow<Resourse<String>>
    suspend fun saveTeacherInfo(teacherDataModel: TeacherDataModel): Flow<Resourse<Boolean>>
}