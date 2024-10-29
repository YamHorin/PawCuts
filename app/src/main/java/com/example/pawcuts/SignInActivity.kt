package com.example.pawcuts

import android.content.Intent
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
import com.example.pawcuts.interfaces.CallBackSignIn
import com.example.pawcuts.interfaces.CallBackUserTypeFound
import com.example.pawcuts.utilities.Constants
import com.example.pawcuts.utilities.DataUsersManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import kotlinx.coroutines.launch
import java.util.UUID

class SignInActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var accountManager: AccountManager
    private var uidFireBase:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        accountManager = AccountManager.getInstance()
        accountManager.callBackSignIn = object :CallBackSignIn
        {
            override fun success() {
                uidFireBase = accountManager.getUidCurrentUser()
                updateUI()
            }

            override fun failure(errorCode: String) {
                Toast.makeText(
                    this@SignInActivity,
                    errorCode,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        binding.SignInActivityFABGoBack.setOnClickListener({moveToStartScreen()})
        binding.SignInActivityMTVSignUp.setOnClickListener({moveToSingUpScreen()})
        binding.SignInActivityEFABSignIn.setOnClickListener({singInUser()})
        binding.signInActivityEFABSignInGoogle.setOnClickListener({singInUserGoogle()})
    }

    private fun moveToStartScreen() {
        val intent = Intent(this@SignInActivity, LoginActivity::class.java);
        startActivity(intent)
        finish()
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
                        accountManager.getUidCurrentUser()
                        uidFireBase = accountManager.getUidCurrentUser()
                        updateUI()
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
        val userEmail  = binding.SignInActivityTILEmail.getEditText()?.getText().toString();
        val userPassword = binding.SignInActivityTILPassword.getEditText()?.getText().toString();
        accountManager.signInUserEmailPassword(userEmail ,userPassword,this)



    }

    private fun updateUI() {
        Log.d("updateUi" ,"next screen :)")
        //check if the user a barber or a pet owner
        val dataManger =DataUsersManager.getInstance()
        dataManger.callBackUserTypeFound = object:CallBackUserTypeFound
        {
            override fun userBarber() {
                val intent = Intent(this@SignInActivity, BarberActivity::class.java);
                val bundle = Bundle()
                bundle.putString(Constants.KEYS.UID_KEY, uidFireBase)
                Log.d("updateUi" ,"barber "+uidFireBase)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }

            override fun userPetOwner() {
                val intent = Intent(this@SignInActivity, PetOwnerActivity::class.java);
                val bundle = Bundle()
                bundle.putString(Constants.KEYS.UID_KEY, uidFireBase)
                Log.d("updateUi" ,"pet owner "+uidFireBase)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }

            override fun userNotFound() {
                Toast.makeText(this@SignInActivity, "user was not found in the database",Toast.LENGTH_SHORT,).show()
            }

        }
        dataManger.getUserType(uidFireBase)

    }

    private fun moveToSingUpScreen() {
        val intent = Intent(this, SignUpAllUsersActivity::class.java);
        startActivity(intent)
        finish()
    }
}