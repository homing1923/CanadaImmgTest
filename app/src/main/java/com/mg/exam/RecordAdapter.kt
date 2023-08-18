package com.mg.exam

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.mg.exam.databinding.RecordAdapterBinding
import com.mg.exam.databinding.RecordAdapterBinding.inflate

class RecordAdapter(context: Context, RecordArray:ArrayList<MutableList<Int>>): ArrayAdapter<MutableList<Int>>(context, 0 , RecordArray) {
    lateinit var dataSource:DataSource
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val currentitem = getItem(position)

        val binding: RecordAdapterBinding = inflate(LayoutInflater.from(context))

        val itemv = binding.root

        dataSource = DataSource.getInstance()

        if(currentitem != null){
            binding.recordAttemptNum.text = (position+1).toString()
            var failAttempt = false
            for((i, each) in currentitem.withIndex()){
                when(i){
                    0 -> {
                        binding.recordHScore.text = each.toString()
                        if(each < dataSource.Passing_Score[i]){
                            failAttempt = true
                            binding.recordHStatus.setImageResource(R.drawable.ic_fail)
                        }else{
                            binding.recordHStatus.setImageResource(R.drawable.ic_done)
                        }
                    }
                    1 -> {
                        binding.recordPScore.text = each.toString()
                        if(each < dataSource.Passing_Score[i]){
                            failAttempt = true
                            binding.recordPStatus.setImageResource(R.drawable.ic_fail)
                        }else{
                            binding.recordPStatus.setImageResource(R.drawable.ic_done)
                        }
                    }
                    2 -> {
                        binding.recordCScore.text = each.toString()
                        if(each < dataSource.Passing_Score[i]){
                            failAttempt = true
                            binding.recordCStatus.setImageResource(R.drawable.ic_fail)
                        }else{
                            binding.recordCStatus.setImageResource(R.drawable.ic_done)
                        }
                    }
                }
            }
            if(failAttempt){
               binding.attemptContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.TerraCotta_2))
            }
        }

        return  itemv
    }
}