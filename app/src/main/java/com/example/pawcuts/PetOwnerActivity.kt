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
import com.example.pawcuts.fragments.petOwnerFragments.FavoritesFragment
import com.example.pawcuts.fragments.petOwnerFragments.FindPetBarbersFragment
import com.example.pawcuts.fragments.petOwnerFragments.MapsFragment
import com.example.pawcuts.fragments.petOwnerFragments.ProfileUserFragment
import com.example.pawcuts.fragments.petOwnerFragments.CalenderOfThePetOwnerFragment
import com.example.pawcuts.fragments.petOwnerFragments.menuPetOwnerFragment
import com.example.pawcuts.interfaces.CallBackDataUserFound
import com.example.pawcuts.interfaces.CallBackGoogleMapAndBarberCard
import com.example.pawcuts.interfaces.CallBackMenuPetOwnerButtonsPress
import com.example.pawcuts.interfaces.CallBackShowBarberCardAndFavoriteBarber
import com.example.pawcuts.interfaces.CallBackUpdateProfile
import com.example.pawcuts.interfaces.CallBackUploadImage
import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.PetOwner
import com.example.pawcuts.models.UserType
import com.example.pawcuts.utilities.AccountManager
import com.example.pawcuts.utilities.Constants
import com.example.pawcuts.utilities.DataUsersManager
import com.example.pawcuts.utilities.ImageProfileManager
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

class PetOwnerActivity : AppCompatActivity() {
    //FRAMES
   private lateinit var PetOwnerActivity_FRAME_SPACEUP   : FrameLayout
   private lateinit var PetOwnerActivity_FRAME_top  :FrameLayout
   private lateinit var PetOwnerActivity_FRAME_down  :FrameLayout
   //fragments
   private lateinit var mapsFragment:MapsFragment
   private lateinit var favoritesFragment:FavoritesFragment
   private lateinit var calenderOfThePetOwnerFragment: CalenderOfThePetOwnerFragment
   private var menuPetOwnerFragment: menuPetOwnerFragment = menuPetOwnerFragment()
   private lateinit var profileUserFragment:ProfileUserFragment
   private lateinit var findPetBarbersFragment:FindPetBarbersFragment
   //callBacks
   private lateinit var callBackMenuPetOwnerButtonsPress: CallBackMenuPetOwnerButtonsPress
   private lateinit var callBackShowBarberCardAndFavoriteBarber: CallBackShowBarberCardAndFavoriteBarber
   private lateinit var callBackUpdateProfile: CallBackUpdateProfile
   private lateinit var callBackMap:CallBackGoogleMapAndBarberCard
   //values we will use
   private var petOwnerObj: PetOwner = PetOwner.Builder().location("{\"latitude\":32.0777087,\"longitude\":34.8015509}").builder()
   private lateinit var uid:String
   private var dataUsersManager = DataUsersManager.getInstance()
   private var accountManager = AccountManager.getInstance()
   private var imageManager = ImageProfileManager.getInstance()
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private var locationCurrent :LatLng = LatLng(0.0 , 0.0)

    private val gson : Gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pet_owner)
        val bundle: Bundle? = intent.extras
        uid = bundle?.getString(Constants.KEYS.UID_KEY).toString()
        Log.d("updateUi" ,uid)

        //callback for dataManger

        dataUsersManager.callBackDataUserFound = object : CallBackDataUserFound
        {
            override fun dataIsReady(value: Any) {
               if (value is PetOwner)
                   petOwnerObj = value
            }

            override fun exceptionData() {
                Log.e("CallBackDataUserFound" , "Data was not loading")
            }


        }
        dataUsersManager.getUserDataPetOwner(uid)
        findViews()
        makeFragmentsAndCallBacks()
        requestLocationFromUser()
        initViews()
        }

    private fun initViews() {
        requestLocationFromUser()

        supportFragmentManager.beginTransaction().add(R.id.PetOwnerActivity_FRAME_top , calenderOfThePetOwnerFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.PetOwnerActivity_FRAME_down ,menuPetOwnerFragment ).commit()

    }

    private fun makeFragmentsAndCallBacks() {
        calenderOfThePetOwnerFragment =CalenderOfThePetOwnerFragment.init(uid)
        findPetBarbersFragment = FindPetBarbersFragment.init(gson.fromJson(petOwnerObj.location,LatLng::class.java),uid )
        favoritesFragment = FavoritesFragment.init(gson.fromJson(petOwnerObj.location,LatLng::class.java),uid)
        callBackMenuPetOwnerButtonsPress = object :CallBackMenuPetOwnerButtonsPress
        {

            override fun profile() {
                profileUserFragment = ProfileUserFragment.init(petOwnerObj)
                profileUserFragment.callBackUpdateProfile = callBackUpdateProfile
                supportFragmentManager.beginTransaction().replace(R.id.PetOwnerActivity_FRAME_top,profileUserFragment).commit()

            }

            override fun favorites() {

                supportFragmentManager.beginTransaction().replace(R.id.PetOwnerActivity_FRAME_top,favoritesFragment).commit()
            }

            override fun barbers() {
                findPetBarbersFragment = FindPetBarbersFragment.init(gson.fromJson(petOwnerObj.location,LatLng::class.java),uid )
                findPetBarbersFragment.callBackMap = callBackMap
                supportFragmentManager.beginTransaction().replace(R.id.PetOwnerActivity_FRAME_top,findPetBarbersFragment).commit()
            }

            override fun appointments() {
                supportFragmentManager.beginTransaction().replace(R.id.PetOwnerActivity_FRAME_top,calenderOfThePetOwnerFragment).commit()
            }

            override fun logOut() {
                accountManager.signOut()
                moveIntoLoginScreen()
            }


        }
        menuPetOwnerFragment.callBackMenuPetOwnerButtonsPress = callBackMenuPetOwnerButtonsPress

        callBackUpdateProfile = object :CallBackUpdateProfile
        {
            override fun saveChanges() {
                val petOwnerAfterChanges:PetOwner = profileUserFragment.getUserInput()

                val passwordUser = profileUserFragment.getPassword()
                if(passwordUser!=null)
                    accountManager.changePassword(passwordUser)

                if (profileUserFragment.isTheProfilePhotoChanged)
                    imageManager.uploadImage(petOwnerObj.uidFireBase , profileUserFragment.getUserProfile())

                if (petOwnerAfterChanges.email != petOwnerObj.email)
                    accountManager.changeEmail(petOwnerAfterChanges.email)


                petOwnerObj = petOwnerAfterChanges
                //it will overwrite on the old data
                dataUsersManager.writeNewUserPetOwnerDataBase(petOwnerAfterChanges)
                val intent = Intent(this@PetOwnerActivity, AnimationActivity::class.java);
                val bundle = Bundle()
                val key:String = "update user pet owner"
                bundle.putString(Constants.KEYS.ANIMATION_KEY, key)
                bundle.putString(Constants.KEYS.UID_KEY, uid)
                Log.d("updateUi" ,uid)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()

            }

        }

        callBackMap = object :CallBackGoogleMapAndBarberCard
        {
            override fun show() {
                mapsFragment = MapsFragment.init(petOwnerObj.location)
                supportFragmentManager.beginTransaction().replace(R.id.PetOwnerActivity_FRAME_top,mapsFragment).commit()
            }

            override fun BarberCard(barber: Barber) {
                val intent = Intent(this@PetOwnerActivity, MakeAnAppointmentActivity::class.java);
                val bundle = Bundle()
                bundle.putString(Constants.KEYS.Barber_KEY, gson.toJson(barber))
                bundle.putString(Constants.KEYS.Pet_OWNER_KEY, gson.toJson(petOwnerObj))
                Log.d("updateUi", uid)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }

        }
        imageManager.callBackUploadImage = object : CallBackUploadImage {
            override fun onSuccess(downloadUrl: String) {
                dataUsersManager.updateProfilePhotoUser(uid , UserType.petOwner , downloadUrl)
            }

            override fun onFailure(exception: Exception) {
                Toast.makeText(this@PetOwnerActivity , "upload profile photo failed" ,Toast.LENGTH_SHORT).show()
            }

        }
        favoritesFragment.callBackMap = callBackMap
        findPetBarbersFragment.callBackMap = callBackMap
    }




    private fun moveIntoLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java);
        startActivity(intent)
        finish()
    }

    private fun findViews() {
        PetOwnerActivity_FRAME_SPACEUP = findViewById(R.id.PetOwnerActivity_FRAME_SPACEUP)
        PetOwnerActivity_FRAME_top = findViewById(R.id.PetOwnerActivity_FRAME_top)
        PetOwnerActivity_FRAME_down = findViewById(R.id.PetOwnerActivity_FRAME_down)
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
                        petOwnerObj.location = gson.toJson(locationCurrent)
                        dataUsersManager.updateLocationUser(uid , UserType.petOwner , petOwnerObj.location)

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
