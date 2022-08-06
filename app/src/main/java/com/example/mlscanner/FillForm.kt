package com.example.mlscanner

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mlscanner.databasehelper.Carton
import com.example.mlscanner.databasehelper.DataBaseHandler
import com.example.mlscanner.scanning.ImgParser
import com.example.mlscanner.scanutils.ScanUtilities

class FillForm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_form)

        //Uri passed in from AddItem
        val ss: Uri = Uri.parse(intent.getStringExtra("savedUri"))
        println("SS: ${ss}")

        val saveButton = findViewById<View>(R.id.saveButton) as Button // get reference to saveButton
        val skuED = findViewById<View>(R.id.skuNumber) as EditText // get reference to sku field
        val barcode = findViewById<View>(R.id.barcode) as EditText // get reference to barcode field
        val locationED = findViewById<View>(R.id.cartonLocation) as EditText // get reference to location field

        //ImgParser init + barcode scanning + SKU text recognition
        val imgParse = ImgParser()
        imgParse.scanBarcode(ss, this, barcode)
        imgParse.recognizeText(ss, this, skuED)
        val fdeleter = ScanUtilities()
        fdeleter.fdelete(ss)


        //save button code
        saveButton.setOnClickListener {
            //run code on click
            if((checkFieldsEmpty(skuED, barcode, locationED)) || (!checkSku(skuED.text.toString()))) {
                //do nothing
            } else {
                //insert data into database
                    var newCarton: Carton = Carton(barcode.text.toString(), skuED.text.toString(), locationED.text.toString())
                    insertCartonToDb(newCarton)
                //send user back to scanner
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }

        }
    }

    private fun checkSku(sku: String): Boolean {
        var properSku: Boolean = false

        if(sku.length == 7) {
            properSku = true
        } else {
            //toast explaining proper sku length
            Toast.makeText(this, "SKU must be 7 digits long!", Toast.LENGTH_SHORT).show()
        }
        return properSku
    }

    fun checkFieldsEmpty(skuED: EditText, barcode: EditText, locationED: EditText): Boolean {
        var fieldEmpty: Boolean
        if((skuED.text.toString() == "") || (barcode.text.toString() == "") || (locationED.text.toString() == "")) {
            //if they are empty do something
            fieldEmpty = true
            Toast.makeText(this, "You must fill in all fields!", Toast.LENGTH_SHORT).show()

        } else {
            //if not empty do something
            fieldEmpty = false

        }
        return fieldEmpty
    }
    fun insertCartonToDb(carton: Carton) {
        val context = this
        val db = DataBaseHandler(context)
        db.insertData(carton)
        db.close()
    }
}