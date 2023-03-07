package com.example.roomdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.roomdb.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var appDb : AppDatabase
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appDb = AppDatabase.getDatabase(this)
        binding.btnWriteData.setOnClickListener {
            writeData()
        }

        binding.btnReadData.setOnClickListener {
            readData()
        }

        binding.btnDeleteAll.setOnClickListener {
            GlobalScope.launch {
                appDb.studentDao().deleteAll()
            }
        }
    }

    private fun writeData(){

        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val rollNo = binding.etDate.text.toString()
        val email = binding.etEmail.text.toString()

        if(firstName.isNotEmpty() &&  lastName.isNotEmpty() && rollNo.isNotEmpty()&&email.isNotEmpty()) {
            val student = Student(null, firstName = firstName, lastName=lastName, date = rollNo, email = email)
            GlobalScope.launch(Dispatchers.IO) {
                appDb.studentDao().insert(student)
            }
            binding.etFirstName.text.clear()
            binding.etEmail.text.clear()
            binding.etLastName.text.clear()
            binding.etDate.text.clear()
            Toast.makeText(this@MainActivity,"Successfully written",Toast.LENGTH_SHORT).show()
        }else Toast.makeText(this@MainActivity,"PLease Enter Data",Toast.LENGTH_SHORT).show()

    }

    private fun readData(){
        val rollNo = binding.etDateID.text.toString()

        if (rollNo.isNotEmpty()){
             var student : Student

            GlobalScope.launch {
                student = appDb.studentDao().findByRoll(rollNo)
                Log.d("Robin Data",student.toString())
                displayData(student)
            }
        }else Toast.makeText(this@MainActivity,"Please enter the data", Toast.LENGTH_SHORT).show()
    }

    private suspend fun displayData(student: Student){

        withContext(Dispatchers.Main){
            binding.tvFirstName.text = student.firstName
            binding.tvLastName.text = student.lastName
            binding.date.text = student.date
            binding.tvEmail.text = student.email
        }
    }
}