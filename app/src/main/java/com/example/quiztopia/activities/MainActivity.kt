package com.example.quiztopia.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztopia.R
import com.example.quiztopia.adapters.QuizAdapter
import com.example.quiztopia.models.Quiz
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale


class MainActivity : AppCompatActivity() {
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var adapter: QuizAdapter
    private var quizList = mutableListOf<Quiz>()
    lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpViews()
    }

    fun setUpViews() {
        setUpFireStore()
        setUpDrawerLayout()
        setUpRecyclerView()
        setUpDatePicker()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setUpDatePicker() {
        val btnObj = findViewById<FloatingActionButton>(R.id.btnDatePicker)
        btnObj.setOnClickListener{
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                Log.d("DATEPICKER", datePicker.headerText)
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                Locale.getDefault()
                val date =dateFormatter.format(java.sql.Date(it))
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("DATE", date)
                startActivity(intent)
            }
            datePicker.addOnNegativeButtonClickListener {
                Log.d("DATEPICKER", datePicker.headerText)
            }
            datePicker.addOnCancelListener {
                Log.d("DATEPICKER", "Date Picker Cancelled")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpFireStore() {
        firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("quizzes")
        collectionReference.addSnapshotListener{ value, error ->
            if( value == null || error != null) {
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            Log.d("DATA", value.toObjects(Quiz::class.java).toString())
            quizList.clear()
            quizList.addAll(value.toObjects(Quiz::class.java))
            adapter.notifyDataSetChanged()

        }
    }


    private fun setUpRecyclerView() {
        adapter = QuizAdapter(this, quizList)
        val obj = findViewById<RecyclerView>(R.id.quizRecyclerView)
        obj.layoutManager = GridLayoutManager(this, 2)
        obj.adapter = adapter
    }

    @SuppressLint("CutPasteId")
    fun setUpDrawerLayout() {
        setSupportActionBar(findViewById<MaterialToolbar>(R.id.appBar))
        actionBarDrawerToggle = ActionBarDrawerToggle(this, findViewById<DrawerLayout>(R.id.mainDrawer),
            R.string.app_name,
            R.string.app_name
        )
        actionBarDrawerToggle.syncState()
        val obj1 = findViewById<NavigationView>(R.id.navigationView)
        val obj2 = findViewById<DrawerLayout>(R.id.mainDrawer)
        obj1.setNavigationItemSelectedListener {
            if(it.itemId==R.id.btnProfile) {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            }
            else if(it.itemId==R.id.btnAboutUs) {
                val intent = Intent(this, AboutPage::class.java)
                startActivity(intent)
                //finish()
            }
            obj2.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}