package ru.levrost.vk_test_2024_summer.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.levrost.vk_test_2024_summer.R

class MainActivity : AppCompatActivity() {
    private var fragment: MainFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = supportFragmentManager.findFragmentById(R.id.top_container) as? MainFragment

        if (fragment == null) {
            fragment = MainFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.top_container, fragment!!)
                .commit()
        }

    }
}