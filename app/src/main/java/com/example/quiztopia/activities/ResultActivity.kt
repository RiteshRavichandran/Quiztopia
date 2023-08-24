package com.example.quiztopia.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import com.example.quiztopia.R
import com.example.quiztopia.models.Quiz
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import org.w3c.dom.Text

class ResultActivity : AppCompatActivity() {
    lateinit var quiz: Quiz
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setUpViews()
    }

    private fun setUpViews() {
        val quizData = intent.getStringExtra("QUIZ")
        quiz = Gson().fromJson<Quiz>(quizData, Quiz::class.java)
        calculateScore()
        setAnswerView()
        setUpDrawerLayout()
    }

    @SuppressLint("CutPasteId")
    fun setUpDrawerLayout() {
        setSupportActionBar(findViewById<MaterialToolbar>(R.id.subAppBar))
        actionBarDrawerToggle = ActionBarDrawerToggle(this, findViewById<DrawerLayout>(R.id.subDrawer),
            R.string.app_name,
            R.string.app_name
        )
        actionBarDrawerToggle.syncState()
        val obj1 = findViewById<NavigationView>(R.id.navigationView)
        val obj2 = findViewById<DrawerLayout>(R.id.subDrawer)
        obj1.setNavigationItemSelectedListener {
            if(it.itemId==R.id.btnHome) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            }
            else if(it.itemId==R.id.btnProfile) {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            else if(it.itemId==R.id.btnAboutUs) {
                val intent = Intent(this, AboutPage::class.java)
                startActivity(intent)
                finish()
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

    private fun setAnswerView() {
        val builder = StringBuilder("")
        for (entry in quiz.questions.entries) {
            val question = entry.value
            builder.append("<font color'#18206F'><b>Question: ${question.description}</b></font><br/><br/>")
            builder.append("<font color='#009688'>Answer: ${question.answer}</font><br/><br/>")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            findViewById<TextView>(R.id.txtAnswer).text = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT);
        } else {
            findViewById<TextView>(R.id.txtAnswer).text = Html.fromHtml(builder.toString());
        }
    }

    private fun calculateScore() {
        var score = 0
        for (entry in quiz.questions.entries) {
            val question = entry.value
            if (question.answer == question.userAnswer) {
                score += 10
            }
        }
        findViewById<TextView>(R.id.txtScore).text = "Your Score : $score"
    }
}