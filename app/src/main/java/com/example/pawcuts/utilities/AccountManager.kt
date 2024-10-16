package com.example.pawcuts.utilities

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.pawcuts.interfaces.CallBackSignUpGoogle
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import java.lang.ref.WeakReference

class AccountManager private constructor(context: Context) {
    private val auth: FirebaseAuth = Firebase.auth
    private val contextRef = WeakReference(context)
    var google:CallBackSignUpGoogle? = null
    companion object {
        @Volatile
        private var instance: AccountManager? = null

        fun init(context: Context): AccountManager {
            return instance ?: synchronized(this) {

                instance ?: AccountManager(context).also { instance = it }
            }
        }

        fun getInstance(): AccountManager {
            return instance
                ?: throw IllegalStateException("AccountManager must be initialized by calling init(context) before use.")
        }

    }
    fun signInWithCredential(idToken:String)
    {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential)
    }
    fun firebaseAuth(): FirebaseAuth {
        return auth
    }

    fun getAccountInFireBase(): FirebaseUser? {
        return auth.currentUser
    }
    fun changePassword(password:String)
    {
        val user = auth.currentUser
        user!!.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("change Email user", "User email address updated.")
                }
            }
    }

    fun changeEmail(email:String)
    {
        val user = auth.currentUser
        user!!.verifyBeforeUpdateEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("change Email user", "User email address updated.")
                }
            }
    }

    fun signInUserEmailPassword(emailUser:String , passwordUser:String , activity: Activity): FirebaseUser? {
        auth.signInWithEmailAndPassword(emailUser, passwordUser).addOnCompleteListener(activity){ task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("sign In User Email Password", "signInWithEmail:success")
            } else {
                // If sign in fails, display a message to the user.
                Log.w("sign In User Email Password", "signInWithEmail:failure", task.exception)

            }
        }
        return auth.currentUser
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }
    fun signUpUserWithCredential(idToken: String , activity: Activity)
    {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("signUpUserWithCredential", "signInWithCredential:success")
                    google?.done()
                    } else {
                    // If sign in fails, display a message to the user.
                    Log.w("signUpUserWithCredential", "signInWithCredential:failure", task.exception)
                }
            }

    }
    fun getEmailCurrentUser(): String {
        return auth.currentUser?.email.toString()
    }

    fun signUpUserEmailPassword(email: String, password: String, activity: Activity) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("signUpUserEmailPassword", "createUserWithEmail:success")
                    val user = auth.currentUser
                } else {
                    // TODO:If sign in fails, display a message to the user.
                    Log.w("signUpUserEmailPassword", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity,
                        "Authentication failed: ${task.exception?.cause} ",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

    }

    fun getUidCurrentUser(): String {
        Log.d("getUidCurrentUser","${auth.currentUser?.uid}")
        return auth.currentUser?.uid.toString()
    }


}