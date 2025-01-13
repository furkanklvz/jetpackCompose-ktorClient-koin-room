package com.klavs.football

sealed class Resource<T>(val data: T? = null, val message: Int? = null) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(message: Int) : Resource<T>(message = message)
    class Loading<T> : Resource<T>()
    class Idle<T> : Resource<T>()
}