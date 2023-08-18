package com.mg.exam

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build.VERSION_CODES.P
import android.provider.Settings.Global
import android.util.Log

class DataSource {
    private constructor() {
    }
    companion object {
        @Volatile
        private lateinit var instance: DataSource

        fun getInstance(): DataSource {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = DataSource()
                }
                return instance
            }
        }
    }

    val sectorsName:MutableList<String> = mutableListOf("Historical","Political","Cultural")
    val sectorQuestionMaximumNumbers:MutableList<Int> = mutableListOf(3,3,3)
    var currentSectionNum:Int = 0
    var currentQuestionNum:Int = 0
    var currentQuestion:Question? = null
    val Historical_Questions:MutableList<Question> = mutableListOf()
    val Political_Questions:MutableList<Question> = mutableListOf()
    val Cultural_Questions:MutableList<Question> = mutableListOf()
    val Current_Historical_Questions:MutableList<Question> = mutableListOf()
    val Current_Political_Questions:MutableList<Question> = mutableListOf()
    val Current_Cultural_Questions:MutableList<Question> = mutableListOf()
    val Current_FullSet:MutableList<MutableList<Question>> = mutableListOf()
    var Current_FullSet_Lenght:Int = 1
    val Questions_Correct:MutableList<Boolean> = mutableListOf()
    val Questions_Correct_ALL:MutableList<Boolean> = mutableListOf()
    val Passing_Score:MutableList<Int> = mutableListOf(1,2,3)
    var Global_AttemptNumber:Int = -1
    val RecordList:ArrayList<MutableList<Int>> = arrayListOf()

//    var Political_Questions_Correct:Int = 0
//    var Curtural_Questions_Correct:Int = 0

    fun initlist(){
        Historical_Questions.removeAll(Historical_Questions)
        Political_Questions.removeAll(Political_Questions)
        Cultural_Questions.removeAll(Cultural_Questions)
        Historical_Questions.add(
            Question(
                "What is one of Canada's founding principles?",
                mutableListOf("The rule of Queen","The rule of law"),
                "The rule of law"))
        Historical_Questions.add(
            Question(
                "Habeas Corpus the right to challenge unlawful detention by the state, comes from _________",
                mutableListOf("English Common Law","French Common Law"),
                "English Common Law"))
        Historical_Questions.add(
            Question(
                "No person or group is above the law, true or false?",
                mutableListOf("True","False"),
                "True"))
        Historical_Questions.add(
            Question(
                "Military Service is compulsory in Canada, true or false?",
                mutableListOf("True","False"),
                "False"))
        Historical_Questions.add(
            Question(
                "Poets and songwriters have hailed Canada as the _________",
                mutableListOf("Great Dominion","Great Unity"),
                "Great Dominion"))
        Historical_Questions.add(
            Question(
                "Who proclaimed the amended constitution of Canada in 1982?",
                mutableListOf("Queen Elizabeth II","George VI"),
                "Queen Elizabeth II"))
        Historical_Questions.add(
            Question(
                "When was the Constitution of Canada amended to entrench the Canadian Charter of Rights and Freedoms",
                mutableListOf("1982","1972"),
                "1982"))

        Political_Questions.add(
            Question(
                "Mobility Rights Identify the TRUE statement",
                mutableListOf("French and English have equal status in Parliament and throughout the government", "Canadians can live and work anywhere they choose in Canada, enter and leave the country freely, and apply for a passport "),
                "Canadians can live and work anywhere they choose in Canada, enter and leave the country freely, and apply for a passport "
            ))

        Political_Questions.add(
            Question("Individuals and governments are regulated by",
                mutableListOf("Queen", "Laws"),
                "Laws"
            ))

        Political_Questions.add(
            Question("Canada citizen's rights and responsibilities are secured by",
                mutableListOf("Canadian Laws", "English Laws"),
                "Canadian Laws"
            ))

        Political_Questions.add(
            Question("The right to vote comes with a responsibility to vote in elections. TRUE or FALSE?",
                mutableListOf("True","False"),
                "True"
            ))

        Political_Questions.add(
            Question("Which 2 languages have equal status in Parliament and throughout the government?",
                mutableListOf("True","False"),
                "True"
            ))

        Political_Questions.add(
            Question("What are three responsibilities of citizenship?",
                mutableListOf("Obeying the law, taking responsibility for oneself and one's family, serving on a jury","Being loyal to Canada, recycling newspapers, serving in the navy, army or air force"),
                "Obeying the law, taking responsibility for oneself and one's family, serving on a jury"
            ))
        Political_Questions.add(
            Question("How many Legislative Assembly chooses a premier and ministers by consensus?",
                mutableListOf("19","21"),
                "19"))

        Cultural_Questions.add(
            Question("What is a fundamental characteristic of the Canadian Heritage and Identity?",
                mutableListOf("Multiculturalism", "Wealth"),
                "Multiculturalism"
            ))

        Cultural_Questions.add(
            Question("2 languages have equal status in Parliament and throughout the government?",
                mutableListOf("French and English", "French and Spanish"),
                "French and English"
            ))

        Cultural_Questions.add(
            Question("Canadians are the only constitutional monarchy in North America? TRUE or FALSE?",
                mutableListOf("True","False"),
                "True"
            ))

        Cultural_Questions.add(
            Question("Which is the second-longest river system in North America?",
                mutableListOf("The Mackenzie River","Missouri River"),
                "The Mackenzie River"
            ))

        Cultural_Questions.add(
            Question("What is Nunavut official language and the first language in schools?",
                mutableListOf("Inuktitut","English"),
                "Inuktitut"
            ))

        Cultural_Questions.add(
            Question("Inuit art is sold throughout Canada and around the world.",
                mutableListOf("True","False"),
                "True"
            ))
        Cultural_Questions.add(
            Question("What is the highest mountain in Canada?",
                mutableListOf("Mount Logan","Mount Saint Elias"),
                "Mount Logan"
            ))

    }

    fun clrList(){
        if(Current_Historical_Questions.size > 0){
            Current_Historical_Questions.removeAll(Current_Historical_Questions)
        }
        if(Current_Political_Questions.size > 0){
            Current_Political_Questions.removeAll(Current_Political_Questions)
        }
        if(Current_Cultural_Questions.size > 0){
            Current_Cultural_Questions.removeAll(Current_Cultural_Questions)
        }
        if(Current_FullSet.size > 0){
            Current_FullSet.removeAll(Current_FullSet)
        }
        Current_FullSet_Lenght = -1
    }

    fun resetNums(){
        currentSectionNum = 0
        currentQuestionNum= 0
    }

    fun getOneSetOfQuestion(tag:String, num:Int):MutableList<Question>{
        initlist()
        val list:MutableList<Question> = mutableListOf()
        when(tag){
            "Historical" -> {
                val localList = Historical_Questions
                for(x in 1..num){
                    val end = Historical_Questions.size - 1
                    val pickIndex = (0..end).random()
                    val picked:Question = localList[pickIndex]
                    list.add(picked)
                    localList.removeAt(pickIndex)
                    Current_FullSet_Lenght ++
                }
            }
            "Political" -> {
                val localList = Political_Questions
                for(x in 1..num){
                    val end = Political_Questions.size - 1
                    val pickIndex = (0..end).random()
                    val picked:Question = localList[pickIndex]
                    list.add(picked)
                    localList.removeAt(pickIndex)
                    Current_FullSet_Lenght ++
                }
            }
            "Cultural" -> {
                val localList = Cultural_Questions
                for(x in 1..num){
                    val end = Cultural_Questions.size - 1
                    val pickIndex = (0..end).random()
                    val picked:Question = localList[pickIndex]
                    list.add(picked)
                    localList.removeAt(pickIndex)
                    Current_FullSet_Lenght ++
                }
            }
            else -> {Log.d("D1", "no such name")}
        }
        Current_FullSet.add(list)
        return list
    }

}