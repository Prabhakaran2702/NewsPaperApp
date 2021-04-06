package com.prabhakaran.khaleejtimes_.ui.splash

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import com.prabhakaran.khaleejtimes_.R
import com.prabhakaran.khaleejtimes_.data.local.prefrences.AppPreferences
import com.prabhakaran.khaleejtimes_.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val prefrences =
            AppPreferences(context = this)

        prefrences.theme.asLiveData().observe(this, Observer{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        })

        object: CountDownTimer(5000, 1000){
            override fun onFinish() {
                val goToMainActivity = Intent(applicationContext, MainActivity::class.java)
                val activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    this@SplashActivity,
                    app_logo,
                    "app_logo"
                )


                startActivity(goToMainActivity, activityOptions.toBundle())

                lifecycle.addObserver(object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event == Lifecycle.Event.ON_STOP) {
                            lifecycle.removeObserver(this)
                            finish()
                        }
                    }
                })

            }

            override fun onTick(millisUntilFinished: Long) {

            }

        }.start()
    }
}