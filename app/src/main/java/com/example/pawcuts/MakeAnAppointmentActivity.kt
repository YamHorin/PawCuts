package com.example.pawcuts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pawcuts.fragments.appointmentFragments.BarberCardFragment
import com.example.pawcuts.fragments.appointmentFragments.CalendarAloneFragment
import com.example.pawcuts.fragments.appointmentFragments.FragmentCalendarAppointmentsPetOwner
import com.example.pawcuts.fragments.appointmentFragments.MakeAReviewFragment
import com.example.pawcuts.fragments.barberFragments.ReviewsBarberScreenFragment
import com.example.pawcuts.interfaces.CallBackBackButtonPress
import com.example.pawcuts.interfaces.CallBackCalendar
import com.example.pawcuts.interfaces.CallBackMakeAppointment
import com.example.pawcuts.interfaces.CallBackMakeReview
import com.example.pawcuts.interfaces.CallBackShowReviewsAndAppointments
import com.example.pawcuts.models.Barber
import com.example.pawcuts.models.BarberType
import com.example.pawcuts.models.MakeAnAppointmentLevels
import com.example.pawcuts.models.PetOwner
import com.example.pawcuts.models.PetOwnerType
import com.example.pawcuts.models.Review
import com.example.pawcuts.utilities.AccountManager
import com.example.pawcuts.utilities.CalendarManager
import com.example.pawcuts.utilities.Constants
import com.example.pawcuts.utilities.DataUsersManager
import com.google.gson.Gson

class MakeAnAppointmentActivity : AppCompatActivity() {
    //Frames
    private lateinit var MakeANAppointment_FRAME_SPACEUP:FrameLayout
    private lateinit var MakeANAppointment_FRAME_top: FrameLayout
    private lateinit var MakeANAppointment_FRAME_down:FrameLayout
    //fragments
    private lateinit var reviewsFragment: ReviewsBarberScreenFragment
    private lateinit var barberCardFragment: BarberCardFragment
    private lateinit var calendarAloneFragment: CalendarAloneFragment
    private lateinit var fragmentCalendarAppointmentsPetOwner: FragmentCalendarAppointmentsPetOwner
    private lateinit var makeReviewFragment: MakeAReviewFragment
    //callbacks
    private lateinit var callBackMakeReview: CallBackMakeReview //makeReviewFragment
    private lateinit var callBackShowReviewsAndAppointments: CallBackShowReviewsAndAppointments//barberCardFragment
    private lateinit  var CallBackCalendar: CallBackCalendar//CalendarAloneFragment
    private lateinit var callBackMakeAppointMent: CallBackMakeAppointment//FragmentCalendarAppointmentsPetOwner
    private lateinit var callBackBackButtonPress: CallBackBackButtonPress//CalendarAloneFragment,barberCardFragment

    //utilities
    private val dataUsersManager:DataUsersManager = DataUsersManager.getInstance()
    private val calendarManager:CalendarManager = CalendarManager.getInstance()
    private val gson: Gson = Gson()
    //values
    private lateinit var currentPetOwner: PetOwner
    private var currentLevel:MakeAnAppointmentLevels  = MakeAnAppointmentLevels.barberCard
    private lateinit var currentBarber: Barber

    //hash map of appointments types
    val typesMap = HashMap<BarberType ,List<PetOwnerType>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_make_an_appointment)
        val bundle: Bundle? = intent.extras
        currentBarber = gson.fromJson(bundle?.getString(Constants.KEYS.Barber_KEY).toString(),Barber::class.java)
        currentPetOwner = gson.fromJson(bundle?.getString(Constants.KEYS.Pet_OWNER_KEY).toString(),PetOwner::class.java)
        findViews()
        makeHashMap()
        makeFragmentsAnfCallBacks()
        initViews()
    }

    private fun makeHashMap() {
        typesMap[BarberType.dogBarber] = listOf( PetOwnerType.dog , PetOwnerType.both)
        typesMap[BarberType.catBarber] = listOf(PetOwnerType.cat , PetOwnerType.both)
        typesMap[BarberType.catAndDogBarber] = listOf(PetOwnerType.both , PetOwnerType.cat , PetOwnerType.dog)


    }

    private fun initViews() {
        supportFragmentManager.beginTransaction().add(R.id.MakeANAppointment_FRAME_top,barberCardFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.MakeANAppointment_FRAME_down,reviewsFragment).commit()
    }

    private fun makeFragmentsAnfCallBacks() {
        reviewsFragment = ReviewsBarberScreenFragment.init(currentBarber.uidFireBase)
        barberCardFragment = BarberCardFragment.init(currentBarber ,currentPetOwner.uidFireBase)
        calendarAloneFragment = CalendarAloneFragment()
        fragmentCalendarAppointmentsPetOwner = FragmentCalendarAppointmentsPetOwner.init(currentBarber , currentPetOwner)
        makeReviewFragment  = MakeAReviewFragment()

        callBackMakeReview = object :CallBackMakeReview{
            override fun insert() {
                val review:Review = makeReviewFragment.getUserInput()
                dataUsersManager.putNewReviewForBarber(currentBarber.uidFireBase , review)
                Toast.makeText(this@MakeAnAppointmentActivity , "The review was successfully entered" , Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction().replace(R.id.MakeANAppointment_FRAME_down,reviewsFragment).commit()

            }


        }
        callBackShowReviewsAndAppointments = object :CallBackShowReviewsAndAppointments{
            override fun reviews(showReviews: Boolean) {
                when(showReviews)
                {
                    true -> supportFragmentManager.beginTransaction().replace(R.id.MakeANAppointment_FRAME_down,reviewsFragment).commit()
                    false -> supportFragmentManager.beginTransaction().replace(R.id.MakeANAppointment_FRAME_down,makeReviewFragment).commit()
                }

            }

            override fun appointment() {
                if (typesMap[currentBarber.type]?.contains(currentPetOwner.animalType) == true)
                {
                    currentLevel = MakeAnAppointmentLevels.events
                    supportFragmentManager.beginTransaction().replace(R.id.MakeANAppointment_FRAME_down,fragmentCalendarAppointmentsPetOwner).commit()
                    supportFragmentManager.beginTransaction().replace(R.id.MakeANAppointment_FRAME_top,calendarAloneFragment).commit()
                }
                else
                {
                    Toast.makeText(this@MakeAnAppointmentActivity , "this barber does not handle your type of animal" ,Toast.LENGTH_SHORT).show()
                }
            }


        }
        CallBackCalendar = object :CallBackCalendar{
            override fun showEvents(year: Int, month: Int, day: Int) {
                fragmentCalendarAppointmentsPetOwner.updateEvents(year ,month ,day)
            }


        }

        callBackBackButtonPress = object :CallBackBackButtonPress
        {
            override fun goBack() {
                when(currentLevel)
                {
                    MakeAnAppointmentLevels.barberCard -> {
                        val intent = Intent(this@MakeAnAppointmentActivity, PetOwnerActivity::class.java);
                        val bundle = Bundle()
                        bundle.putString(Constants.KEYS.UID_KEY, currentPetOwner.uidFireBase)
                        Log.d("updateUi" ,currentPetOwner.uidFireBase)
                        intent.putExtras(bundle)
                        startActivity(intent)
                        finish()
                    }
                    MakeAnAppointmentLevels.events -> {
                        currentLevel  =MakeAnAppointmentLevels.barberCard
                        supportFragmentManager.beginTransaction().replace(R.id.MakeANAppointment_FRAME_top,barberCardFragment).commit()
                        supportFragmentManager.beginTransaction().replace(R.id.MakeANAppointment_FRAME_down,reviewsFragment).commit()
                    }
                }
            }


        }
        callBackMakeAppointMent = object :CallBackMakeAppointment{
            override fun make(
                barber: Barber,
                petOwner: PetOwner,
                time: String,
                day: Int,
                month: Int,
                year: Int) {

                    calendarManager.createEventAppointment(
                        barber,
                        petOwner,
                        time,
                        day = day,
                        month = month,
                        year = year)
                moveIntoAnimation()
            }
        }
        //attach callbacks
        makeReviewFragment.callBackMakeReview = callBackMakeReview
        barberCardFragment.callBackShowReviewsAndAppointments = callBackShowReviewsAndAppointments
        barberCardFragment.callBackBack = callBackBackButtonPress
        calendarAloneFragment.callBackBackButtonPress = callBackBackButtonPress
        calendarAloneFragment.CallBackCalendar = CallBackCalendar
        fragmentCalendarAppointmentsPetOwner.callBackMakeAppointMent = callBackMakeAppointMent

    }

    private fun moveIntoAnimation() {
        val intent = Intent(this@MakeAnAppointmentActivity, AnimationActivity::class.java);
        val bundle = Bundle()
        val key:String = "calendar event"
        bundle.putString(Constants.KEYS.ANIMATION_KEY, key)
        bundle.putString(Constants.KEYS.UID_KEY, currentPetOwner.uidFireBase)
        Log.d("updateUi" ,key)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun findViews() {
        MakeANAppointment_FRAME_SPACEUP = findViewById(R.id.MakeANAppointment_FRAME_SPACEUP)
        MakeANAppointment_FRAME_top = findViewById(R.id.MakeANAppointment_FRAME_top)
        MakeANAppointment_FRAME_down = findViewById(R.id.MakeANAppointment_FRAME_down)

    }
}