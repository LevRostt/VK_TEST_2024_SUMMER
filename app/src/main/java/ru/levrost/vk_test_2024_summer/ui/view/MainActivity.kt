package ru.levrost.vk_test_2024_summer.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.levrost.vk_test_2024_summer.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.top_container, MainFragment())
            .commit()
    }
}