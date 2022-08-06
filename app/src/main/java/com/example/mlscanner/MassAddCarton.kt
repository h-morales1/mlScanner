package com.example.mlscanner

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MassAddCarton : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mass_add_carton)

        val isleNumber = findViewById<View>(R.id.savedIsle) as EditText
        val continueBtn = findViewById<View>(R.id.continueBtn) as Button
        val prefKeyName = "com.example.mlscanner.PREFERENCE_FILE_KEY"
        //val sharedPref = this.getSharedPreferences(Context.MODE_PRIVATE)
        val sharedPred = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        //val sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        continueBtn.setOnClickListener {
            //check if isleNumber is empty, toast if its empty, check sharedPref for matching result if not empty
            if(isleNumber.text.isEmpty()) {
                //if empty toast
                Toast.makeText(this, "Isle field can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                val dbIsle = sharedPred.getString("isleNumber", "0")
                if (dbIsle == "0") {
                    // if it equals 0 ask user to type in an islenumber
                    Toast.makeText(this, "Please type a value other than 0", Toast.LENGTH_LONG).show()
                } else if (dbIsle == isleNumber.text.toString()) {
                    // continue and scan bin tag
                } else if (dbIsle != isleNumber.text.toString()) {
                    // save isleNumber.text to sharedPred and continue
                }
            }
        }



    }
}