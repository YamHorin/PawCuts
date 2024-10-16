package com.example.pawcuts

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pawcuts.utilities.AccountManager
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.pawcuts.databinding.ActivitySignInBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import java.util.UUID

class SignInActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        accountManager = AccountManager.getInstance()
        initViews()

    }



    private fun initViews() {
        binding.SignInActivityMTVSignUp.setOnClickListener({moveToSingUpScreen()})
        binding.SignInActivityEFABSignIn.setOnClickListener({singInUser()})
        binding.signInActivityEFABSignInGoogle.setOnClickListener({singInUserGoogle()})
    }

    private fun singInUserGoogle() {
        val credentialManager = CredentialManager.create(this)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId("730997873293-p12ejf20v9v15j7kp02uvpsu6fjq7bno.apps.googleusercontent.com")
            .setAutoSelectEnabled(true)
            .setNonce(UUID.randomUUID().toString())
            .build()

        val request:GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
                try{

                    val result = credentialManager.getCredential(request = request, context = this@SignInActivity)
                    if (result.credential is CustomCredential && result.credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                        accountManager.signInWithCredential(googleIdTokenCredential.idToken)
                        updateUI(accountManager.currentUser())
                    } else {
                        Log.e("sing in User Google ERROR", "UNEXPECTED_CREDENTIAL")
                    }
                }
                catch (e:GetCredentialException)
                {
                    Log.e("GetCredentialException",e.errorMessage.toString())
                }
        }
    }




    private fun singInUser() {
        Log.d("singInUser","start here")
        val email = binding.SignInActivityTIETEmail.text.toString()
        val password  = binding.SignInActivityTIETPassword.text.toString()
        val currentUser: FirebaseUser?
        currentUser = accountManager.signInUserEmailPassword(email ,password,this)
        if(currentUser!=null)
        {
            updateUI(currentUser)
        }
        else
            Toast.makeText(
               this,
                "Authentication failed.",
                Toast.LENGTH_SHORT,
            ).show()

    }

    private fun updateUI(user: FirebaseUser?) {
        // TODO:get user information
        Log.d("updateUi" ,"next screen :)")

    }

    private fun moveToSingUpScreen() {
        TODO("Not yet implemented")
    }
}