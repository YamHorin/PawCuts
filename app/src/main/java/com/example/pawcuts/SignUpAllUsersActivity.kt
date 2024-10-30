package com.example.pawcuts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.pawcuts.interfaces.CallBackSingUpScreenButtonsPress
import com.example.pawcuts.fragments.signUpFragments.ProgressBarSingUpScreenFragment
import com.example.pawcuts.fragments.signUpFragments.SignUpAllUsersFragment
import com.example.pawcuts.fragments.signUpFragments.SignUpGoogleFragment
import com.example.pawcuts.fragments.signUpFragments.SignUpMoreDetailsFragment
import com.example.pawcuts.fragments.signUpFragments.SignUpPetBarberFragment
import com.example.pawcuts.fragments.signUpFragments.SignUpPetOwnerFragment
import com.example.pawcuts.interfaces.CallBackGeocodeListener
import com.example.pawcuts.interfaces.CallBackSignIn
import com.example.pawcuts.interfaces.CallBackSignUpGoogle
import com.example.pawcuts.interfaces.CallBackUploadImage
import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.PetOwner
import com.example.pawcuts.models.SignUpPhases
import com.example.pawcuts.models.UserType
import com.example.pawcuts.utilities.AccountManager
import com.example.pawcuts.utilities.CalendarManager
import com.example.pawcuts.utilities.Constants
import com.example.pawcuts.utilities.DataUsersManager
import com.example.pawcuts.utilities.ImageProfileManager
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.UUID


class SignUpAllUsersActivity : AppCompatActivity() {

    //frames
    private lateinit var signUPActivity_FRAME_top : FrameLayout
    private lateinit var signUPActivity_FRAME_down : FrameLayout
    private lateinit var  singUPActivity_FRAME_SPACEUP:FrameLayout


    //fragments
    private lateinit var signUpAllUsersFragment:SignUpAllUsersFragment
    private lateinit var signUpPetBarberFragment: SignUpPetBarberFragment
    private lateinit var signUpGoogleFragment: SignUpGoogleFragment
    private lateinit var signUpMoreDetailsFragment: SignUpMoreDetailsFragment
    private lateinit var progressBarSingUpScreenFragment: ProgressBarSingUpScreenFragment
    private lateinit var signUpPetOwnerFragment: SignUpPetOwnerFragment

    //values we will use in this activity
    private var currentScreen:SignUpPhases = SignUpPhases.EmailAndPassword
    private var userType:UserType = UserType.none
    private var emailUser:String = ""
    private var uidUser:String =""
    private var gson: Gson =Gson()
    private lateinit var accountManager: AccountManager
    private lateinit var dataUsersManager: DataUsersManager
    private lateinit var calendarManager: CalendarManager
    private lateinit var imageProfileManager: ImageProfileManager
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var locationCurrent :LatLng
    private lateinit var callBackSingUpScreenButtonsPress:CallBackSingUpScreenButtonsPress

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sing_up_all_users)
        findViews()
        calendarManager = CalendarManager.getInstance()
        dataUsersManager = DataUsersManager.getInstance()
        accountManager = AccountManager.getInstance()
        imageProfileManager = ImageProfileManager.getInstance()
        makeFragmentsAndCallBacks()
        initViews()
    }

    private fun makeFragmentsAndCallBacks() {
        signUpAllUsersFragment = SignUpAllUsersFragment()
        signUpPetBarberFragment = SignUpPetBarberFragment()
        signUpGoogleFragment = SignUpGoogleFragment()
        signUpMoreDetailsFragment = SignUpMoreDetailsFragment()
        progressBarSingUpScreenFragment = ProgressBarSingUpScreenFragment()
        signUpPetOwnerFragment = SignUpPetOwnerFragment()
        accountManager.callBackSignIn = object :CallBackSignIn {

            override fun success() {
                uidUser = accountManager.getUidCurrentUser()
                makeRefConnectionIntoDataBaseAndCalendar(userType)

                Log.d("callBackSignIn ", "sign in: success")
            }

            override fun failure(errorCode: String) {
                if (errorCode=="ERROR_INVALID_CREDENTIAL")
                    Toast.makeText(
                        this@SignUpAllUsersActivity,
                        "ERROR: wrong email/password",
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    Toast.makeText(
                        this@SignUpAllUsersActivity,
                        errorCode,
                        Toast.LENGTH_SHORT
                    ).show()

                throw IllegalArgumentException()

            }
        }

        callBackSingUpScreenButtonsPress = object : CallBackSingUpScreenButtonsPress {
            override fun nextScreen() {
                try {
                    processTheCurrentScreen(currentScreen)
                    nextScreen(currentScreen)
                } catch (e: IllegalArgumentException) {
                    Toast.makeText(
                        this@SignUpAllUsersActivity,
                        "Please fill in all the details",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

            override fun goBackScreen(screenName: SignUpPhases) {
                goBackScreenActivity(screenName)
            }

            override fun signUpGoogle() {
                val credentialManager = CredentialManager.create(this@SignUpAllUsersActivity)
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    //TODO:make this private
                    .setServerClientId("730997873293-p12ejf20v9v15j7kp02uvpsu6fjq7bno.apps.googleusercontent.com")
                    .setAutoSelectEnabled(true)
                    .setNonce(UUID.randomUUID().toString())
                    .build()

                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                lifecycleScope.launch {
                    try {

                        val result = credentialManager.getCredential(
                            request = request,
                            context = this@SignUpAllUsersActivity
                        )
                        if (result.credential is CustomCredential && result.credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(result.credential.data)
                            accountManager.signUpUserWithCredential(
                                googleIdTokenCredential.idToken,
                                this@SignUpAllUsersActivity
                            )
                            uidUser = accountManager.getUidCurrentUser()
                        } else {
                            Log.e("sing in User Google ERROR", "UNEXPECTED_CREDENTIAL")
                        }
                    } catch (e: GetCredentialException) {
                        Log.e("GetCredentialException", e.errorMessage.toString())
                    }
                }

            }

        }
        signUpAllUsersFragment.callBackSingUpScreenButtonsPress = callBackSingUpScreenButtonsPress
        signUpPetBarberFragment.callBackSingUpScreenButtonsPress = callBackSingUpScreenButtonsPress
        signUpGoogleFragment.callBackSingUpScreenButtonsPress = callBackSingUpScreenButtonsPress
        signUpMoreDetailsFragment.callBackSingUpScreenButtonsPress =
            callBackSingUpScreenButtonsPress
        progressBarSingUpScreenFragment.callBackNextButtonPress = callBackSingUpScreenButtonsPress
        signUpPetOwnerFragment.callBackSingUpScreenButtonsPress = callBackSingUpScreenButtonsPress

        imageProfileManager.callBackUploadImage = object : CallBackUploadImage {
            override fun onSuccess(downloadUrl: String) {

                dataUsersManager.updateProfilePhotoUser(uidUser,userType ,downloadUrl )
            }

            override fun onFailure(exception: Exception) {
                Toast.makeText(
                    this@SignUpAllUsersActivity,
                    "Upload failed: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("MainActivity", "Upload failed", exception)
            }

        }
        accountManager.google = object : CallBackSignUpGoogle {
            override fun done() {
                //get email
                emailUser = accountManager.getEmailCurrentUser()
                accountManager.getUidCurrentUser()
                //change fragment
                supportFragmentManager.beginTransaction().replace(R.id.SingUPActivity_FRAME_top,signUpGoogleFragment).commit()
                //change current screen
                currentScreen =SignUpPhases.GoogleAccount
            }

        }
        signUpPetBarberFragment.callBackGeocodeListener = object:CallBackGeocodeListener{
            override fun onGeocodeSuccess(location: LatLng) {
                dataUsersManager.updateLocationUser(uidUser ,userType, gson.toJson(location))
            }

            override fun onGeocodeFailure(exception: java.lang.Exception) {
                Log.d("onGeocodeFailure" , exception.cause.toString())
            }

        }

    }




    private fun goBackScreenActivity(screenRightNow:SignUpPhases) {
        when(screenRightNow)
        {
            SignUpPhases.EmailAndPassword -> {
                goBackToStartScreen()
            }
            SignUpPhases.GoogleAccount -> {
                supportFragmentManager.beginTransaction().replace(R.id.SingUPActivity_FRAME_top,signUpAllUsersFragment).commit()
                progressBarSingUpScreenFragment.progressBarChange(1)
                currentScreen = SignUpPhases.EmailAndPassword

            }
            SignUpPhases.petBarber -> {
                supportFragmentManager.beginTransaction().replace(R.id.SingUPActivity_FRAME_top,signUpAllUsersFragment).commit()
                progressBarSingUpScreenFragment.progressBarChange(1)
                currentScreen = SignUpPhases.EmailAndPassword


            }
            SignUpPhases.petOwner -> {
                supportFragmentManager.beginTransaction().replace(R.id.SingUPActivity_FRAME_top,signUpAllUsersFragment).commit()
                progressBarSingUpScreenFragment.progressBarChange(1)
                currentScreen = SignUpPhases.EmailAndPassword


            }
            SignUpPhases.moreDetails -> {
                when(userType)
                {
                    UserType.petOwner -> {
                        supportFragmentManager.beginTransaction().replace(R.id.SingUPActivity_FRAME_top,signUpPetOwnerFragment).commit()
                        progressBarSingUpScreenFragment.progressBarChange(2)
                        currentScreen = SignUpPhases.petOwner


                    }
                    UserType.petBarber -> {
                        supportFragmentManager.beginTransaction().replace(R.id.SingUPActivity_FRAME_top,signUpPetBarberFragment).commit()
                        progressBarSingUpScreenFragment.progressBarChange(2)
                        currentScreen = SignUpPhases.petBarber

                    }
                    UserType.none -> return
                }

            }
        }
    }

    private fun goBackToStartScreen() {
        val intent = Intent(this, LoginActivity::class.java);
        startActivity(intent)
        finish()
    }

    private fun initViews() {
        requestLocationFromUser()
        supportFragmentManager.beginTransaction().add(R.id.SingUPActivity_FRAME_top , signUpAllUsersFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.SingUPActivity_FRAME_down ,progressBarSingUpScreenFragment ).commit()

    }

    private fun requestLocationFromUser() {
        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    requestLocationUpdates()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    requestLocationUpdates()
                } else -> {
                Toast.makeText(this, "Access to mobile phone location is necessary", Toast.LENGTH_LONG).show()
            }
            }
        }
        //get permission  location
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun findViews() {
        singUPActivity_FRAME_SPACEUP = findViewById(R.id.SingUPActivity_FRAME_SPACEUP)
        signUPActivity_FRAME_top = findViewById(R.id.SingUPActivity_FRAME_top)
        signUPActivity_FRAME_down = findViewById(R.id.SingUPActivity_FRAME_down)

    }

    private fun makeRefConnectionIntoDataBaseAndCalendar(type:UserType)
    {
        when(type)
        {
            UserType.petOwner -> dataUsersManager.makeRefConnectionPetOwner(uidUser)
            UserType.petBarber -> dataUsersManager.makeRefConnectionBarber(uidUser)
            UserType.none -> return
        }
        calendarManager.createNewCalendarForNewUser(uidUser , type)

    }
    private fun processTheCurrentScreen(screen:SignUpPhases)
    {
        when(screen)
        {
            SignUpPhases.EmailAndPassword -> {
                try {
                    signUpAllUsersFragment.checkUserInput()
                    val arr: Array<String> =signUpAllUsersFragment.getInfoUser()
                    val email = arr[0]
                    val password =arr[1]
                    when( arr[2])
                    {
                        "barber"-> userType = UserType.petBarber
                        "owner"-> userType = UserType.petOwner
                    }
                    //dataUsersManager.makeListBarbers()
                    emailUser = email
                    accountManager.signUpUserEmailPassword(email, password , this)

                }catch (e:Exception)
                {
                    Log.e("signUpAllUsersFragment" , e.message.toString())
                    Toast.makeText(
                        this,
                        "Authentication failed ",
                        Toast.LENGTH_SHORT,
                    ).show()
                }

            }
            SignUpPhases.GoogleAccount -> {
                signUpGoogleFragment.checkUserInput()
                userType = signUpGoogleFragment.getUserType()
                uidUser = accountManager.getUidCurrentUser()
                makeRefConnectionIntoDataBaseAndCalendar(userType)

            }
            SignUpPhases.petBarber -> {
                signUpPetBarberFragment.checkUserInput()
                val user:Barber = signUpPetBarberFragment.getUserInfoPetBarber(emailUser , uidUser)
                imageProfileManager.uploadImage(uidUser, signUpPetBarberFragment.getUserProfile())
                dataUsersManager.writeNewUserPetBarberDataBase(user)
            }

            SignUpPhases.petOwner -> {
                signUpPetOwnerFragment.checkUserInput()

                val user:PetOwner = signUpPetOwnerFragment.getUserInfoPetOwner(emailUser , uidUser)
                //profile pic
                imageProfileManager.uploadImage(user.uidFireBase , signUpPetOwnerFragment.getUserProfile())
                //location
                user.location = gson.toJson(locationCurrent)
                dataUsersManager.writeNewUserPetOwnerDataBase(user)
            }
            SignUpPhases.moreDetails -> {
            dataUsersManager.updateMoreDetailsUser(uidUser ,userType,signUpMoreDetailsFragment.getMoreDetails())
            }

        }
    }

    private fun nextScreen(screen:SignUpPhases)
    {
        when(screen)
        {
            SignUpPhases.EmailAndPassword -> {
                //update ui down
                progressBarSingUpScreenFragment.progressBarChange(2)
                //change fragments
                //+update current screen
                when(userType)
                {
                    UserType.petOwner -> {
                        supportFragmentManager.beginTransaction().replace(R.id.SingUPActivity_FRAME_top,signUpPetOwnerFragment).commit()
                        currentScreen = SignUpPhases.petOwner
                    }
                    UserType.petBarber -> {
                        supportFragmentManager.beginTransaction().replace(R.id.SingUPActivity_FRAME_top,signUpPetBarberFragment).commit()
                        currentScreen = SignUpPhases.petBarber

                    }
                    UserType.none -> return
                }
            }
            SignUpPhases.GoogleAccount -> {
                //update ui down
                progressBarSingUpScreenFragment.progressBarChange(2)
                //replace screen top
                when(userType)
                {
                    UserType.petOwner -> {
                        supportFragmentManager.beginTransaction().replace(R.id.SingUPActivity_FRAME_top,signUpPetOwnerFragment).commit()
                        currentScreen = SignUpPhases.petOwner
                    }
                    UserType.petBarber -> {
                        supportFragmentManager.beginTransaction().replace(R.id.SingUPActivity_FRAME_top,signUpPetBarberFragment).commit()
                        currentScreen = SignUpPhases.petBarber
                    }
                    UserType.none -> return
                }


            }
            SignUpPhases.petBarber -> {
                //update ui down
                progressBarSingUpScreenFragment.progressBarChange(3)
                supportFragmentManager.beginTransaction().replace(R.id.SingUPActivity_FRAME_top,signUpMoreDetailsFragment).commit()
                currentScreen = SignUpPhases.moreDetails


            }
            SignUpPhases.petOwner -> {
                //update ui down
                progressBarSingUpScreenFragment.progressBarChange(3)
                supportFragmentManager.beginTransaction().replace(R.id.SingUPActivity_FRAME_top,signUpMoreDetailsFragment).commit()
                currentScreen = SignUpPhases.moreDetails
            }
            SignUpPhases.moreDetails -> {
                moveToMainScreen()
            }
        }
    }

    private fun moveToMainScreen() {
        if (userType == UserType.petOwner)
        {
            //put user in FireStore dataBase
            calendarManager.createNewCalendarForNewUser(uidUser , userType)
            val intent = Intent(this, PetOwnerActivity::class.java);
            val bundle = Bundle()
            bundle.putString(Constants.KEYS.UID_KEY, uidUser)
             intent.putExtras(bundle)
            startActivity(intent)
            finish()

        }
        else
        {

            //put user in FireStore dataBase
            calendarManager.createNewCalendarForNewUser(uidUser , userType)
            val intent = Intent(this, BarberActivity::class.java);
            val bundle = Bundle()
            bundle.putString(Constants.KEYS.UID_KEY, uidUser)
             intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }
    }



    private fun requestLocationUpdates() {

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
        } else {
            // Permissions already granted, get the location
            val currentLocationRequest = CurrentLocationRequest.Builder().build()
            fusedLocationClient.getCurrentLocation(currentLocationRequest, null)
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        locationCurrent = LatLng(location.latitude, location.longitude)
                        Log.d("locationCurrent", "Location: $locationCurrent") // Log the location
                    } else {
                        Log.d("locationCurrent", "****error could get location locationCurrent =null")
                    }
                }
                .addOnFailureListener { exception: Exception ->
                    Log.e("LocationError", "Error getting location", exception)
                }
        }
    }

}