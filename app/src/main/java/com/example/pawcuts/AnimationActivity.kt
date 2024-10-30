package com.example.pawcuts

import android.animation.Animator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.pawcuts.utilities.Constants
import com.google.android.material.textview.MaterialTextView

class AnimationActivity : AppCompatActivity() {
    private lateinit var animationView: LottieAnimationView
    private lateinit var AnimationActivity_MTV: MaterialTextView
    private var uid: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_animation)
        animationView = findViewById(R.id.AnimationActivity_animationView)
        AnimationActivity_MTV = findViewById(R.id.AnimationActivity_MTV)
        val bundle: Bundle? = intent.extras
        val animation: String = bundle?.getString(Constants.KEYS.ANIMATION_KEY).toString()

        Log.d("updateUi", animation)

        when (animation) {

            "event Barber"->
            {
                uid = bundle?.getString(Constants.KEYS.UID_KEY).toString()
                animationView.setAnimation(R.raw.calender)
                AnimationActivity_MTV.text = buildString { append( "calender updated") }
                animationView.resumeAnimation()
                animationView.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        // pass
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        val intent = Intent(this@AnimationActivity, BarberActivity::class.java);
                        val bundle = Bundle()
                        bundle.putString(Constants.KEYS.UID_KEY, uid)
                        Log.d("updateUi", uid)
                        intent.putExtras(bundle)
                        startActivity(intent)
                        finish()
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        //pass
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                        //pass
                    }

                })
            }
            "calendar event" -> {
                uid = bundle?.getString(Constants.KEYS.UID_KEY).toString()
                animationView.setAnimation(R.raw.calender)
                AnimationActivity_MTV.text = buildString { append( "The session has been set successfully") }
                animationView.resumeAnimation()
                animationView.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        // pass
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        val intent = Intent(this@AnimationActivity, PetOwnerActivity::class.java);
                        val bundle = Bundle()
                        bundle.putString(Constants.KEYS.UID_KEY, uid)
                        Log.d("updateUi", uid)
                        intent.putExtras(bundle)
                        startActivity(intent)
                        finish()
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        //pass
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                        //pass
                    }

                })
            }
            "null"-> {
                animationView.setAnimation(R.raw.pets)
                AnimationActivity_MTV.text = buildString { append("Paw Cuts") }
                AnimationActivity_MTV.textSize =35F
                animationView.repeatCount = 2 // Set repeat count to 2
                animationView.resumeAnimation()
                animationView.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        // pass
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        val intent = Intent(this@AnimationActivity, LoginActivity::class.java);
                        val bundle = Bundle()
                        intent.putExtras(bundle)
                        startActivity(intent)
                        finish()
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        //pass
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                        //pass
                    }

                })

            }
            "update user pet owner"->
            {
                uid = bundle?.getString(Constants.KEYS.UID_KEY).toString()
                animationView.setAnimation(R.raw.user)
                AnimationActivity_MTV.text = buildString { append( "profile updated") }
                animationView.resumeAnimation()
                animationView.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        // pass
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        val intent = Intent(this@AnimationActivity, PetOwnerActivity::class.java);
                        val bundle = Bundle()
                        bundle.putString(Constants.KEYS.UID_KEY, uid)
                        Log.d("updateUi", uid)
                        intent.putExtras(bundle)
                        startActivity(intent)
                        finish()
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        //pass
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                        //pass
                    }

                })
            }
            "update user barber"->
            {
                uid = bundle?.getString(Constants.KEYS.UID_KEY).toString()
                animationView.setAnimation(R.raw.user)
                AnimationActivity_MTV.text = buildString { append( "profile updated") }
                animationView.resumeAnimation()
                animationView.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        // pass
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        val intent = Intent(this@AnimationActivity, BarberActivity::class.java);
                        val bundle = Bundle()
                        bundle.putString(Constants.KEYS.UID_KEY, uid)
                        Log.d("updateUi", uid)
                        intent.putExtras(bundle)
                        startActivity(intent)
                        finish()
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        //pass
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                        //pass
                    }

                })
            }




        }
    }
}


