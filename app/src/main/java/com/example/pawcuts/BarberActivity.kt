package com.example.pawcuts

import MenuBarBarberFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pawcuts.fragments.barberFragments.CalendarAppointmentsPetBarberFragment
import com.example.pawcuts.fragments.barberFragments.MakeEventBarberFragment
import com.example.pawcuts.fragments.barberFragments.ProfileBarberFragment
import com.example.pawcuts.fragments.barberFragments.ReviewsBarberScreenFragment
import com.example.pawcuts.fragments.barberFragments.ShowPetInfoFragment
import com.example.pawcuts.interfaces.CallBackBackButtonPress
import com.example.pawcuts.interfaces.CallBackDataUserFound
import com.example.pawcuts.interfaces.CallBackGeocodeListener
import com.example.pawcuts.interfaces.CallBackMenuBarberButtonsPress
import com.example.pawcuts.interfaces.CallBackShowPetInfoCardAndCreateEventBarberScreen
import com.example.pawcuts.interfaces.CallBackUpdateProfile
import com.example.pawcuts.interfaces.CallBackUploadImage
import com.example.pawcuts.models.AppointmentBarber
import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.UserType
import com.example.pawcuts.utilities.AccountManager
import com.example.pawcuts.utilities.CalendarManager
import com.example.pawcuts.utilities.Constants
import com.example.pawcuts.utilities.DataUsersManager
import com.example.pawcuts.utilities.ImageProfileManager
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson


class BarberActivity : AppCompatActivity() {
    //FRAMES
    private lateinit var MainActivity_FRAME_SPACEUP  :FrameLayout
    private lateinit var MainActivity_FRAME_top  : FrameLayout
    private lateinit var MainActivity_FRAME_down :FrameLayout

    //fragments
    private lateinit var calendarAppointmentsPetBarberFragment: CalendarAppointmentsPetBarberFragment
    private val menuBarBarberFragment: MenuBarBarberFragment = MenuBarBarberFragment()
    private val makeEventBarberFragment:MakeEventBarberFragment  = MakeEventBarberFragment()
    private lateinit var profileBarberFragment: ProfileBarberFragment
    private lateinit var reviewsBarberScreenFragment: ReviewsBarberScreenFragment
    private lateinit var showPetInfoFragment: ShowPetInfoFragment
    //call backs
    private lateinit var callBackGeocodeListener: CallBackGeocodeListener
    private lateinit var callBackMenuBarberButtonsPress: CallBackMenuBarberButtonsPress
    private lateinit var callBackUploadImage: CallBackUploadImage
    private lateinit var callBackDataUserFound: CallBackDataUserFound
    private lateinit var callBackUpdateProfileActivity: CallBackUpdateProfile
    private lateinit var callBackShowPetInfoCardAndCreateEventBarberScreen : CallBackShowPetInfoCardAndCreateEventBarberScreen
    private lateinit var callBackButtonBack: CallBackBackButtonPress

    //utilities
    private val accountManager:AccountManager =AccountManager.getInstance()
    private val dataManager:DataUsersManager = DataUsersManager.getInstance()
    private val imageManager:ImageProfileManager = ImageProfileManager.getInstance()
    private val calendarManager:CalendarManager = CalendarManager.getInstance()
    //values we need
    private var barber: Barber = Barber()
    private lateinit var uidBarber:String
    private var gson:Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_barber)
            findView()
        val bundle: Bundle? = intent.extras
        callBackDataUserFound = object :CallBackDataUserFound
        {
            override fun dataIsReady(value: Any) {
                if (value is Barber)
                {
                    barber = value
                }
            }

            override fun exceptionData() {
               Log.e("CallBackDataUserFound" , "Data was not loading")
            }

        }
        dataManager.callBackDataUserFound = callBackDataUserFound
        uidBarber = bundle?.getString(Constants.KEYS.UID_KEY).toString()
        Log.d("updateUi" ,uidBarber)
        dataManager.getUserDataBarber(uidBarber)
        makeFragmentsAndCallBacks()
        initViews()



        }

    private fun findView() {
        MainActivity_FRAME_SPACEUP = findViewById(R.id.MainActivity_FRAME_SPACEUP)
        MainActivity_FRAME_top = findViewById(R.id.MainActivity_FRAME_top)
        MainActivity_FRAME_down = findViewById(R.id.MainActivity_FRAME_down)
    }

    private fun makeFragmentsAndCallBacks() {
        calendarAppointmentsPetBarberFragment = CalendarAppointmentsPetBarberFragment.init(uidBarber)
        profileBarberFragment = ProfileBarberFragment.init(barber)
        reviewsBarberScreenFragment = ReviewsBarberScreenFragment.init(uidBarber)

        callBackButtonBack = object : CallBackBackButtonPress{
            override fun goBack() {
                supportFragmentManager.beginTransaction().replace(R.id.MainActivity_FRAME_top,calendarAppointmentsPetBarberFragment).commit()
            }

        }
        callBackGeocodeListener = object :CallBackGeocodeListener
        {
            override fun onGeocodeSuccess(location: LatLng) {
               dataManager.updateLocationUser(uidBarber , UserType.petBarber , gson.toJson(location))
            }

            override fun onGeocodeFailure(exception: Exception) {
                Toast.makeText(
                    this@BarberActivity,
                    "Location didn't found",
                    Toast.LENGTH_SHORT).show()
            }

        }

        callBackMenuBarberButtonsPress = object :CallBackMenuBarberButtonsPress
        {
            override fun profile() {
                profileBarberFragment = ProfileBarberFragment.init(barber)
                profileBarberFragment.callBackUpdateProfile  = callBackUpdateProfileActivity
                supportFragmentManager.beginTransaction().replace(R.id.MainActivity_FRAME_top,profileBarberFragment).commit()
            }

            override fun reviews() {
                //generate  reviews for testing
                //DataGenerator.generateReviews(uidBarber)
                //reviewsBarberScreenFragment = ReviewsBarberScreenFragment.init(barber.name , uidBarber)
                supportFragmentManager.beginTransaction().replace(R.id.MainActivity_FRAME_top,reviewsBarberScreenFragment).commit()
            }

            override fun logOut() {
                accountManager.signOut()
                moveIntoLoginScreen()
            }

            override fun appointments() {
                supportFragmentManager.beginTransaction().replace(R.id.MainActivity_FRAME_top,calendarAppointmentsPetBarberFragment).commit()

            }

        }

        callBackUploadImage = object :CallBackUploadImage
        {
            override fun onSuccess(downloadUrl: String) {
                dataManager.updateProfilePhotoUser(uidBarber , UserType.petBarber , downloadUrl)
            }

            override fun onFailure(exception: Exception) {
                Toast.makeText(
                    this@BarberActivity,
                    "upload photo failed",
                    Toast.LENGTH_SHORT).show()
                Log.e("callBackUploadImage" , exception.cause.toString())
            }



        }

        callBackUpdateProfileActivity = object :CallBackUpdateProfile
        {
            override fun saveChanges() {
                val barberAfterChanges:Barber = profileBarberFragment.getUserInput()
                if (barberAfterChanges.email!=barber.email)
                    accountManager.changeEmail(barberAfterChanges.email)
                val passwordUser = profileBarberFragment.getPassword()
                if(passwordUser!=null)
                    accountManager.changePassword(passwordUser)
                barber = barberAfterChanges
                if (profileBarberFragment.isTheProfilePhotoChanged)
                    imageManager.uploadImage(uidBarber , profileBarberFragment.getUserProfile())
                //it will overwrite on the old data
                dataManager.writeNewUserPetBarberDataBase(barberAfterChanges)
                val intent = Intent(this@BarberActivity, AnimationActivity::class.java);
                val bundle = Bundle()
                val key:String = "update user barber"
                bundle.putString(Constants.KEYS.ANIMATION_KEY, key)
                bundle.putString(Constants.KEYS.UID_KEY, uidBarber)
                Log.d("updateUi" ,uidBarber)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }

        }
        callBackShowPetInfoCardAndCreateEventBarberScreen = object :CallBackShowPetInfoCardAndCreateEventBarberScreen
        {
            override fun showInfo(event: AppointmentBarber) {
                showPetInfoFragment = ShowPetInfoFragment.init(event)
                showPetInfoFragment.callBackButtonBack = callBackButtonBack
                supportFragmentManager.beginTransaction().replace(R.id.MainActivity_FRAME_top,showPetInfoFragment).commit()
            }

            override fun showScreenAddEvent() {
                supportFragmentManager.beginTransaction().replace(R.id.MainActivity_FRAME_top,makeEventBarberFragment).commit()
            }


            override fun AddEventBarberScreen() {
                val arrDate:ArrayList<Int> = makeEventBarberFragment.getUserDate()
                val events=  makeEventBarberFragment.getEvents()
                for (event in events)
                    calendarManager.createEventInPetBarberCalendar(uidBarber , year = arrDate[2] , month= arrDate[1] , day = arrDate[0] , event)
                val intent = Intent(this@BarberActivity, AnimationActivity::class.java);
                val bundle = Bundle()
                val key:String = "event Barber"
                bundle.putString(Constants.KEYS.ANIMATION_KEY, key)
                bundle.putString(Constants.KEYS.UID_KEY, barber.uidFireBase)
                Log.d("updateUi" ,key)
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }




        }
        makeEventBarberFragment.callBackEvent = callBackShowPetInfoCardAndCreateEventBarberScreen
        profileBarberFragment.callBackGeocodeListener = callBackGeocodeListener
        menuBarBarberFragment.callBackMenuBarberButtonsPress =  callBackMenuBarberButtonsPress
        imageManager.callBackUploadImage = callBackUploadImage
        profileBarberFragment.callBackUpdateProfile  = callBackUpdateProfileActivity
        calendarAppointmentsPetBarberFragment.callBackShowPetInfoCardAndCreateEventBarberScreen =  callBackShowPetInfoCardAndCreateEventBarberScreen


    }

    private fun moveIntoLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java);
        startActivity(intent)
        finish()
    }

    private fun initViews() {
        supportFragmentManager.beginTransaction().add(R.id.MainActivity_FRAME_top , calendarAppointmentsPetBarberFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.MainActivity_FRAME_down ,menuBarBarberFragment ).commit()

    }


}
