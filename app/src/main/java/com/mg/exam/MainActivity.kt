package com.mg.exam

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.mg.exam.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var dataSource: DataSource
    lateinit var sharedPrefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        dataSource = DataSource.getInstance()
        sharedPrefs = this.getSharedPreferences("com_mg_exam_PREF", MODE_PRIVATE)
    }

    override fun onStart() {
        binding.btnStartTest.setOnClickListener{
            val intent = Intent(this, Questions::class.java)
            startActivity(intent)
        }
        dataSource.Questions_Correct.removeAll(dataSource.Questions_Correct)
        getAttemptNum()
        getNewSetOfQuestions()
        super.onStart()
    }

    override fun onResume() {
        dataSource.resetNums()
        dataSource.RecordList.removeAll(dataSource.RecordList)
        dataSource.Questions_Correct_ALL.removeAll(dataSource.Questions_Correct_ALL)
        super.onResume()
    }

    private fun getAttemptNum() {
        dataSource.Global_AttemptNumber = if(!sharedPrefs.contains("KEY_TOTAL_ATTEMPTS")){
            1
        }else{
            sharedPrefs.getInt("KEY_TOTAL_ATTEMPTS", 1) + 1
        }
    }

    private fun getNewSetOfQuestions() {
        dataSource.clrList()
        for((i, each) in dataSource.sectorsName.withIndex()){
            dataSource.getOneSetOfQuestion(each, dataSource.sectorQuestionMaximumNumbers[i])
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_viewRecord -> {
                val intent = Intent(this, PreviousScore::class.java)
                startActivity(intent)
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

}