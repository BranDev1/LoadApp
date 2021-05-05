package com.udacity

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        //reference to notification manager
        val notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll()

        // getting reference to shared preferences
        val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val fileName = sharedPref.getString(getString(R.string.saved_file_name_key), "null")
        val status = sharedPref.getBoolean(getString(R.string.saved_file_status_key), false)

        //set text and status according to preference value
        fileName_text.text = fileName
        if (status) {
            status_text.text = getString(R.string.success)
        } else {
            status_text.text = getString(R.string.fail)
            status_text.setTextColor(getColor(R.color.red))
        }

        okButton.setOnClickListener {
            //finishing detail activity
            finish()
        }
    }

}
