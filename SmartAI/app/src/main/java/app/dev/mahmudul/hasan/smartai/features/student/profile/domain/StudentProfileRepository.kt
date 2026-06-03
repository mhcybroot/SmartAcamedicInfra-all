package app.dev.mahmudul.hasan.smartai.features.student.profile.domain

import app.dev.smartacademicinfrastructure.StudentDataModel
import com.appdevmhr.dpi_sai.di.Resourse
import kotlinx.coroutines.flow.Flow

interface StudentProfileRepository {
    suspend fun updateStudentProfile(updateStudentMap: Map<String,String>): Flow<Resourse<Boolean>>
}