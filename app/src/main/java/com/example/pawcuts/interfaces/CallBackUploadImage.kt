package com.example.pawcuts.interfaces

interface CallBackUploadImage {
    fun onSuccess(downloadUrl: String)
    fun onFailure(exception: Exception)

}