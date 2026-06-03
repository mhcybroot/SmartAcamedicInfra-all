package app.dev.mahmudul.hasan.smartai.di

import com.appdevmhr.dpi_sai.di.Resourse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepository {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Resourse<T> ): Flow<Resourse<T>> = flow {
        try {
            emit(Resourse.Loading)
            val result = apiCall()
            if(result is Resourse.Failure) throw result.exception
            emit(result)
        } catch (e: Exception) {
            emit(Resourse.Failure(e))
        }
    }.catch {
        it.printStackTrace()
        emit(Resourse.Failure(it))
    }.flowOn(Dispatchers.IO)
}