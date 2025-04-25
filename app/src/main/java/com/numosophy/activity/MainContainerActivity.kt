package com.numosophy.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.numosophy.R
import com.numosophy.databinding.ActivityMainContainerBinding
import com.numosophy.fragment.DashboardFragment
import com.numosophy.fragment.ExportFragment
import com.numosophy.fragment.GroupFragment
import com.numosophy.fragment.SalesFragment
import com.numosophy.fragment.SettingsFragment
import com.numosophy.utility.NumosophyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainContainerActivity : BaseActivity() {
    private lateinit var binding : ActivityMainContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_main_container)
        replaceFragment(DashboardFragment())

        binding.bottomNavView.setOnItemReselectedListener {
            when(it.itemId) {
                R.id.dash -> replaceFragment(DashboardFragment())
                R.id.sales -> replaceFragment(SalesFragment())
                R.id.group -> replaceFragment(GroupFragment())
                R.id.export -> replaceFragment(ExportFragment())
                R.id.settings -> replaceFragment(SettingsFragment())

                else -> {

                }
            }
            true
        }



        val db = NumosophyDatabase.getDatabase(this)
        val userDao = db.userDao()

        // ✅ Run a simple query to confirm database creation
        CoroutineScope(Dispatchers.IO).launch {
            val userCount = userDao.getUserCount() // Query to check database existence
            Log.d("RoomTest", "Database initialized. Current users count: $userCount")
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}