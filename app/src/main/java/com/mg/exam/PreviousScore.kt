package com.mg.exam

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import com.mg.exam.databinding.ActivityPreviousScoreBinding

class PreviousScore : AppCompatActivity() {
    lateinit var binding: ActivityPreviousScoreBinding
    lateinit var dataSource: DataSource
    lateinit var sharedPrefs: SharedPreferences
    lateinit var recordAdapter: RecordAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPreviousScoreBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        dataSource = DataSource.getInstance()
        sharedPrefs = this.getSharedPreferences("com_mg_exam_PREF", MODE_PRIVATE)
    }

    override fun onStart() {
        clrBtn()
        readRecord()
        setAdapter()
        super.onStart()
    }

    private fun clrBtn() {
        binding.recordClrBtn.setOnClickListener(){
            if(dataSource.Questions_Correct_ALL.isEmpty()){
                with(sharedPrefs.edit()){
                    clear()
                    apply()
                }
                dataSource.Global_AttemptNumber = 0
                dataSource.RecordList.removeAll(dataSource.RecordList)
                recordAdapter.notifyDataSetChanged()
                recreate()
            }else{
                Toast.makeText(this, "Please first finish the test", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun setAdapter() {
        recordAdapter = RecordAdapter(this, dataSource.RecordList)
        binding.recordListview.adapter = recordAdapter
    }

    private fun readRecord(){
        if(dataSource.Global_AttemptNumber > 1){
            dataSource.RecordList.removeAll(dataSource.RecordList)
            for(x in 1..dataSource.Global_AttemptNumber-1){
                val historyRecord = sharedPrefs.getInt("KEY_Historical_$x", -1)
                val politicsRecord = sharedPrefs.getInt("KEY_Political_$x", -1)
                val cultureRecord = sharedPrefs.getInt("KEY_Cultural_$x", -1)
                dataSource.RecordList.add(mutableListOf(historyRecord,politicsRecord,cultureRecord))
            }
        }else{

        }
    }
}