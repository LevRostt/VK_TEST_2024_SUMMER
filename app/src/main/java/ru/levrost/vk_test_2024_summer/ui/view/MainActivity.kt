package ru.levrost.vk_test_2024_summer.ui.view

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.levrost.vk_test_2024_summer.R
import ru.levrost.vk_test_2024_summer.debugLog

class MainActivity : AppCompatActivity() {
    private var fragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = supportFragmentManager.findFragmentById(R.id.top_container)

        if (fragment == null) {
            fragment = MainFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.top_container, fragment!!)
                .commit()
        }

    }
}