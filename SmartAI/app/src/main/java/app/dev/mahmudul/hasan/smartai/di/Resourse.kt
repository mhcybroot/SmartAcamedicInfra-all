package com.appdevmhr.dpi_sai.di

import app.dev.mahmudul.hasan.smartai.features.course.home.data.MessageItemModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

sealed class Resourse<out R> {
    data class Success<out R>(val result: R) : Resourse<R>()
    data class Failure(val exception: Throwable) : Resourse<Nothing>()
    object Loading : Resourse<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$result]"
            is Failure -> "Failure[exception=${exception.message}]"
            Loading -> "Loading"
        }
    }

}

fun <T, R> Resourse<T>.map(mapper: (T) -> R): Resourse<R> {
    return when (this) {
        is Resourse.Success -> Resourse.Success(mapper(result))
        is Resourse.Failure -> Resourse.Failure(exception)
        Resourse.Loading -> Resourse.Loading
    }
}

fun <T> Flow<Resourse<T>>.doOnSuccess(action: suspend (value: T) -> Unit): Flow<Resourse<T>> =
    transform {
        if (it is Resourse.Success) {
            action(it.result)
        }
        return@transform emit(it)
    }

fun <T> Flow<Resourse<T>>.doOnFailure(action: suspend (value: Throwable) -> Unit): Flow<Resourse<T>> = transform {
    if (it is Resourse.Failure) {
        action(it.exception)
    }
    return@transform emit(it)
}

fun <T> Flow<Resourse<T>>.doOnLoading(action: suspend () -> Unit): Flow<Resourse<T>> = transform {
    if (it is Resourse.Loading) {
        action()
    }
    return@transform emit(it)
}

suspend fun DatabaseReference.getDataAsync(): DataSnapshot {
    return suspendCancellableCoroutine { continuation ->
        this.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                continuation.resume(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }
}

suspend fun DatabaseReference.observeDataChanges(): List<MessageItemModel> {
    return suspendCancellableCoroutine { continuation ->
        this.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = mutableListOf<MessageItemModel>()
                for (data in snapshot.children) {
                    val message = data.getValue(MessageItemModel::class.java)
                    messageList.add(message!!)
                }
                continuation.resume(messageList)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }
}
