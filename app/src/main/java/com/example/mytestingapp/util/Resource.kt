package com.example.mytestingapp.util

data class Resource<out T>(val status: Status, val data: T?, val errorCode: Int, val message: String?) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, 1,null)
        }

        fun <T> error(msg: String, errorCode: Int, data: T?): Resource<T> {
            return Resource(Status.ERROR, data,errorCode, msg)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, 0,null)
        }


    }
}

