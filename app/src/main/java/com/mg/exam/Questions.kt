package com.mg.exam

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.mg.exam.databinding.ActivityQuestionsBinding

class Questions : AppCompatActivity(), View.OnClickListener {
    lateinit var binding:ActivityQuestionsBinding
    lateinit var dataSource:DataSource
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        dataSource = DataSource.getInstance()
        sharedPrefs = this.getSharedPreferences("com_mg_exam_PREF", MODE_PRIVATE)
    }

    override fun onStart() {
        setOnclickListenrs()
        loadQuestions()
        super.onStart()
    }

    override fun onBackPressed() {
        val CDialog = AlertDialog.Builder(this)
            .setTitle("Quit?")
            .setMessage("Progress would not be saved")
            .setPositiveButton("Yes"){dialog, it ->
                finish()
            }
            .setNegativeButton("No", null)
            .create()
            .show()
    }

    private fun loadQuestions() {
        if(dataSource.currentSectionNum < dataSource.sectorQuestionMaximumNumbers.size){
            if(dataSource.currentQuestionNum < dataSource.sectorQuestionMaximumNumbers[dataSource.currentSectionNum]){
                binding.questionsNum.text = "${dataSource.sectorsName[dataSource.currentSectionNum]} Question ${dataSource.currentQuestionNum +1}"
                binding.questionQuestion.text = dataSource.Current_FullSet[dataSource.currentSectionNum][dataSource.currentQuestionNum].question
                binding.questionAns1.text =
                    if(dataSource.Current_FullSet[dataSource.currentSectionNum][dataSource.currentQuestionNum].answerArray[0] == dataSource.Current_FullSet[dataSource.currentSectionNum][dataSource.currentQuestionNum].correctAnswerString){
                        "*${dataSource.Current_FullSet[dataSource.currentSectionNum][dataSource.currentQuestionNum].answerArray[0]}"
                    }else{
                        dataSource.Current_FullSet[dataSource.currentSectionNum][dataSource.currentQuestionNum].answerArray[0]
                    }
                binding.questionAns2.text =
                    if(dataSource.Current_FullSet[dataSource.currentSectionNum][dataSource.currentQuestionNum].answerArray[1] == dataSource.Current_FullSet[dataSource.currentSectionNum][dataSource.currentQuestionNum].correctAnswerString){
                        "*${dataSource.Current_FullSet[dataSource.currentSectionNum][dataSource.currentQuestionNum].answerArray[1]}"
                    }else{
                        dataSource.Current_FullSet[dataSource.currentSectionNum][dataSource.currentQuestionNum].answerArray[1]
                    }
            }
        }
    }

    private fun setOnclickListenrs() {
        binding.questionAns1.setOnClickListener(this)
        binding.questionAns2.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0 != null){
            when (p0.id){
                binding.questionAns1.id -> answer(p0, binding.questionAns1.text.toString())
                binding.questionAns2.id -> answer(p0, binding.questionAns2.text.toString())
            }
        }
    }

    private fun answer(view: View, ans: String) {
        if(ans == "*${dataSource.Current_FullSet[dataSource.currentSectionNum][dataSource.currentQuestionNum].correctAnswerString}"){
            dataSource.Questions_Correct.add(true)
            dataSource.Questions_Correct_ALL.add(true)
        }else{
            dataSource.Questions_Correct.add(false)
            dataSource.Questions_Correct_ALL.add(false)
        }
        if(dataSource.currentQuestionNum == 2){
            dataSource.currentQuestionNum = 0
            if(dataSource.currentSectionNum == 2){
                FinishTest()
                dataSource.currentSectionNum = 0
            }else{
                dataSource.currentSectionNum ++
                FinishSection()
            }
        }else{
            dataSource.currentQuestionNum ++
            recreate()
        }
    }


    private fun FinishTest() {
        var localscore = 0
        val ResultDialog = AlertDialog.Builder(this)
        var text:String = "Canada Citizenship Test result\n"
        for((i, each) in dataSource.Questions_Correct.withIndex()){
            if(each){
                localscore++
            }
        }
        if(localscore != dataSource.Passing_Score[dataSource.currentSectionNum]){
            ResultDialog
                .setTitle("You Failed on ${dataSource.sectorsName[dataSource.currentSectionNum - 1]}\n")
        }else{
            ResultDialog
                .setTitle("You Pass The Whole Test\n")
        }
        for((i, each) in dataSource.Questions_Correct_ALL.withIndex()){
            text += "Question ${i + 1}: "
            text += if(each){
                "Correct\n"
            }else{
                "Incorrect\n"
            }
        }
        ResultDialog
            .setMessage(text)
            .setCancelable(false)
            .setPositiveButton("Return Home"){dialog, it ->
                finish()
            }
            .create()
            .show()
        savescore()
    }

    private fun savescore() {
        val thisScoreList = mutableListOf<Int>()
        if(dataSource.Questions_Correct_ALL.size < dataSource.Current_FullSet_Lenght){
            for(x in 0..dataSource.Current_FullSet_Lenght-dataSource.Questions_Correct_ALL.size){
                dataSource.Questions_Correct_ALL.add(false)
            }
        }
        for(i in dataSource.Current_FullSet.indices){
            val start = (dataSource.Current_FullSet.size * i)
            val end = (dataSource.Current_FullSet.size * i) + dataSource.Current_FullSet[i].size-1
            var localscore = 0
            for(x in start..end){
                if(dataSource.Questions_Correct_ALL[x]){
                    localscore++
                }
            }
            thisScoreList.add(localscore)
        }
        with(sharedPrefs.edit()){
            for((i, each) in thisScoreList.withIndex()){
                putInt("KEY_${dataSource.sectorsName[i]}_${dataSource.Global_AttemptNumber}", each)
            }
            putInt("KEY_TOTAL_ATTEMPTS", dataSource.Global_AttemptNumber)
            apply()
        }
        dataSource.Questions_Correct_ALL.removeAll(dataSource.Questions_Correct_ALL)
    }

    private fun FinishSection() {
        var localscore = 0
        var text:String = "${dataSource.sectorsName[dataSource.currentSectionNum - 1]}\n"
        val ResultDialog = AlertDialog.Builder(this)
        for((i, each) in dataSource.Questions_Correct.withIndex()){
            text += "Question ${i + 1}: "
            if(each){
                text += "Correct\n"
                localscore++
            }else{
                text += "Incorrect\n"
            }
        }

        if(localscore < dataSource.Passing_Score[dataSource.currentSectionNum - 1]){
            ResultDialog
                .setTitle("You Failed on ${dataSource.sectorsName[dataSource.currentSectionNum - 1]}")
                .setMessage(text)
                .setCancelable(false)
                .setNeutralButton("Return Home"){dialog, it ->
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            savescore()

        }else{
            ResultDialog
                .setTitle("You Passed ${dataSource.sectorsName[dataSource.currentSectionNum - 1]}")
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("Continue"){dialog, it ->
                    recreate()
                }
        }
        ResultDialog
            .create()
            .show()
        dataSource.Questions_Correct.removeAll(dataSource.Questions_Correct)
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

    override fun onDestroy() {
        super.onDestroy()
    }


}