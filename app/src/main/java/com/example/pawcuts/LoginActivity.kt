package com.example.pawcuts

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView

class LoginActivity : AppCompatActivity() {

    private lateinit var LoginActivity_BUTTON_SignIn : ExtendedFloatingActionButton
    private lateinit var LoginActivity_BUTTON_SignUp:ExtendedFloatingActionButton
    private lateinit var LoginActivity_ACIV_imageDoggy: AppCompatImageView
    private lateinit var LoginActivity_MTV_Welcome: MaterialTextView
    private lateinit var LoginActivity_MTV_INFO:MaterialTextView
    val linkImageDoggy: String = "https://i.imgur.com/esxP1j4.png"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        findViews()
        initViews()

    }

    private fun findViews() {
        LoginActivity_BUTTON_SignIn = findViewById(R.id.LoginActivity_BUTTON_SingIn)
        LoginActivity_BUTTON_SignUp = findViewById(R.id.LoginActivity_BUTTON_SingUp)
        LoginActivity_ACIV_imageDoggy = findViewById(R.id.LoginActivity_ACIV_imageDoggy)
        LoginActivity_MTV_Welcome = findViewById(R.id.LoginActivity_MTV_Welcome)
        LoginActivity_MTV_INFO = findViewById(R.id.LoginActivity_MTV_INFO)
    }

    private fun initViews() {
        Glide
            .with(this)
            .load(linkImageDoggy)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(LoginActivity_ACIV_imageDoggy)
        LoginActivity_BUTTON_SignIn.setOnClickListener({moveToSignINScreen()})
        LoginActivity_BUTTON_SignUp.setOnClickListener({moveToSignUpScreen()})

    }

    //TODO: finish the activity
    private fun moveToSignUpScreen() {
        val intent = Intent(this, SignUpAllUsersActivity::class.java);
        startActivity(intent)
        finish()
    }
    //TODO: finish the activity
    private fun moveToSignINScreen() {
        val intent = Intent(this, SignInActivity::class.java);
        startActivity(intent)
        finish()
    }
}