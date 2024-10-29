package com.example.pawcuts.utilities

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.pawcuts.interfaces.CallBackUploadImage
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File

class ImageProfileManager private constructor(context: Context){
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    var callBackUploadImage: CallBackUploadImage? = null
    companion object {
        @Volatile
        private var instance: ImageProfileManager? = null

        fun init(context: Context): ImageProfileManager {
            return instance ?: synchronized(this) {

                instance ?: ImageProfileManager(context).also { instance = it }
            }
        }

        fun getInstance(): ImageProfileManager {
            return instance
                ?: throw IllegalStateException("ImageProfileManager must be initialized by calling init(context) before use.")
        }
    }
    fun uploadImage(userName: String, uri: Uri)
    {
        uploadImageProcess(userName , uri)
    }
     private  fun uploadImageProcess(userName: String, uri: Uri){
        val ref = createReference(userName)
        Log.d("uploadImageFUNCTION", "Reference: $ref")
        val uploadTask = ref.putFile(uri)
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    Log.d("uploadImageFUNCTION", "task upload .exception: $task.exception")
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("uploadImageFUNCTION", "task downloadUrl : ${task.result}")
                callBackUploadImage?.onSuccess(task.result.toString())
            } else {
                Log.d("uploadImageFUNCTION", "task downloadUrl .exception: $task.exception")
                task.exception?.let { callBackUploadImage?.onFailure(it) }
            }
        }

    }

    fun getImageURL(uid: String) {
        val ref = createReference(uid)
        ref.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("uploadImageFUNCTION", "task downloadUrl : ${task.result}")
                callBackUploadImage?.onSuccess(task.result.toString())
            } else {
                Log.d("uploadImageFUNCTION", "task downloadUrl .exception: $task.exception")
                task.exception?.let { callBackUploadImage?.onFailure(it) }
            }
        }
    }

    private fun createReference(userName:String): StorageReference {
        return storageRef.child("images/$userName.jpg")
    }
}