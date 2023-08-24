package com.example.quiztopia.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.quiztopia.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class AboutPage : AppCompatActivity() {
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_page)
        setUpViews()
    }

    private fun setUpViews() {
        setUpDrawerLayout()
    }

    @SuppressLint("CutPasteId")
    private fun setUpDrawerLayout() {
        setSupportActionBar(findViewById<MaterialToolbar>(R.id.subAppBar))
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, findViewById<DrawerLayout>(R.id.subDrawer),
            R.string.app_name,
            R.string.app_name
        )
        actionBarDrawerToggle.syncState()
        val obj1 = findViewById<NavigationView>(R.id.navigationView)
        val obj2 = findViewById<DrawerLayout>(R.id.subDrawer)
        obj1.setNavigationItemSelectedListener {
            if (it.itemId == R.id.btnHome) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            } else if (it.itemId == R.id.btnProfile) {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            }
            obj2.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}