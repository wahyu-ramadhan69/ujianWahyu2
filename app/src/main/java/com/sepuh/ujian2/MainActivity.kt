package com.sepuh.ujian2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sepuh.ujian2.fragment.UserFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = UserFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}