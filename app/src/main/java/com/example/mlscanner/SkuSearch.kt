package com.example.mlscanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mlscanner.databasehelper.DataBaseHandler

class SkuSearch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sku_search)

        val db = DataBaseHandler(this)

        val searchBtn = findViewById<View>(R.id.searchButton) as Button
        val searchSkuText = findViewById<View>(R.id.searchSku) as EditText

        searchBtn.setOnClickListener {
            //search sku in db
            if(searchSkuText.text.isEmpty()) {
                // its empty so warn user
                Toast.makeText(this, "Sku field is empty", Toast.LENGTH_LONG).show()
            } else {
                //search
                if(db.checkSkuExists(searchSkuText.text.toString())) {
                    //it does exist, send user to DisplayForm activity
                    val cart = db.getCartonBySku(searchSkuText.text.toString())
                    println("BARCODE_: ${cart.barcode}")
                    val intent = Intent(this, DisplayCarton::class.java)
                    intent.putExtra("barcode", cart.barcode)
                    startActivity(intent)
                } else {
                    // sku doesnt exist, send back to main
                    Toast.makeText(this, "Sku not found, please add this item", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}