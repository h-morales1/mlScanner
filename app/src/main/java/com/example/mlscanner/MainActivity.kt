package com.example.mlscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.content.Intent
import android.view.View
import android.widget.Button



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //references to buttons on mainActivity
        val scanBtn = findViewById<View>(R.id.scanButton) as Button
        val skuLookup = findViewById<View>(R.id.skuSearch) as Button
        val addItmBtn = findViewById<View>(R.id.addItemButton) as Button
        val massAddBtn = findViewById<View>(R.id.massAdd) as Button

        scanBtn.setOnClickListener {
            val intent = Intent(this, ScanItem::class.java)
            // scan an items barcode, see if location is in db
            startActivity(intent)
        }
        skuLookup.setOnClickListener {
            val intent = Intent(this, SkuSearch::class.java)
            // search if SKU exists in db, returns barcode and location if found
            startActivity(intent)
        }
        addItmBtn.setOnClickListener {
            val intent = Intent(this, AddItem::class.java)
            // scan bin tag in order to add the item to the db, manually type in location
            startActivity(intent)
        }

        massAddBtn.setOnClickListener {
            // send to new activity to auto type in isle number
            // 1. EditText with isle number, continue Button
        }



    }




}
