package com.example.quiztopia.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import com.example.quiztopia.R
import com.example.quiztopia.models.Question
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.example.quiztopia.adapters.OptionAdapter
import com.example.quiztopia.models.Quiz
import com.google.gson.Gson

class QuestionActivity : AppCompatActivity() {

    lateinit var quiz: Quiz
    var quizzes: MutableList<Quiz>? = null
    var questions: MutableMap<String, Question>? = null
    var index = 1
    var overrideID = 0
    var no: Long = 0
    private lateinit var timer: CountDownTimer
    private lateinit var textViewTimer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        textViewTimer = findViewById(R.id.Timer)
        setUpFirestore()
        setUpEventListener()
    }

    private fun startCountdown(durationMillis: Long) {
        //val question = questions!!["question$index"]
        //val Time : Long = questions!!.size.toLong()

        timer = object : CountDownTimer(durationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val minutes = secondsRemaining / 60
                val seconds = secondsRemaining % 60
                textViewTimer.text = String.format("%02d:%02d", minutes, seconds)

                if (millisUntilFinished <= 0) {
                    onFinish() // Call onFinish when the time is up
                }
            }

            override fun onFinish() {
                Toast.makeText(applicationContext, "Time Up", Toast.LENGTH_SHORT).show()
                textViewTimer.text = "00:00"
                // Handle countdown finished event here
                simulateButtonClick()
            }
        }
        timer.start();
    }

    private fun simulateButtonClick() {
        // Assuming you have a button named "submitButton"
        overrideID = 1
        findViewById<Button>(R.id.btnSubmit).performClick()
    }

    private fun setUpEventListener() {

        findViewById<Button>(R.id.btnPrevious).setOnClickListener {
            index--
            bindViews()
        }
        findViewById<Button>(R.id.btnNext).setOnClickListener {
            index++
            bindViews()
        }
        findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            val json = Gson().toJson(quizzes!![0])
            quiz = Gson().fromJson<Quiz>(json, Quiz::class.java)
            var count = 0
            for (entry in quiz.questions.entries) {
                val question = entry.value
                if (question.userAnswer == "") {
                    count++;
                }
            }
            if (count != 0 && overrideID == 0)
                Toast.makeText(applicationContext, "Answer all Questions", Toast.LENGTH_SHORT)
                    .show()
            else {
                Log.d("FINALQUIZ", questions.toString())
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("QUIZ", json)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setUpFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        var date = intent.getStringExtra("DATE")

        if (date != null) {
            Log.d("DATE", date)
            firestore.collection("quizzes").whereEqualTo("title", date)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot != null && !querySnapshot.isEmpty) {
                        quizzes = querySnapshot.toObjects(Quiz::class.java)
                        questions = quizzes!![0].questions
                        bindViews()
                        startCountdown(45000 * no)
                    } else {
                        // No matches found, close the activity
                        Toast.makeText(applicationContext, "No Quiz on Date", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors that occur during the query
                    Log.e("Firestore", "Error getting quiz documents", exception)
                }
        }

    }


    private fun bindViews() {
        findViewById<Button>(R.id.btnPrevious).visibility = View.GONE
        findViewById<Button>(R.id.btnNext).visibility = View.GONE
        findViewById<Button>(R.id.btnSubmit).visibility = View.GONE

        val question = questions!!["question$index"]
        question?.let {
            findViewById<TextView>(R.id.description).text = question.description
            val optionAdapter = OptionAdapter(this, it)
            val obj1 = findViewById<RecyclerView>(R.id.optionList)
            obj1.layoutManager = LinearLayoutManager(this)
            obj1.adapter = optionAdapter
            obj1.setHasFixedSize(true)
        }


        no = questions!!.size.toLong()

        if (index == 1) {
            findViewById<Button>(R.id.btnNext).visibility = View.VISIBLE
        } else if (index == questions!!.size) {

            findViewById<Button>(R.id.btnPrevious).visibility = View.VISIBLE
            findViewById<Button>(R.id.btnSubmit).visibility = View.VISIBLE
        } else {
            findViewById<Button>(R.id.btnPrevious).visibility = View.VISIBLE
            findViewById<Button>(R.id.btnNext).visibility = View.VISIBLE
        }

    }
}