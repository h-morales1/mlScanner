package com.example.mlscanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.mlscanner.databasehelper.DataBaseHandler
import org.w3c.dom.Text

class DisplayCarton : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_carton)

        //database handler
        val context = this
        val db = DataBaseHandler(context)


        val ss:String = intent.getStringExtra("barcode").toString() // get barcode from previous activity
        val newCarton = db.getCarton(ss)
        //val list: MutableList<Carton> = ArrayList()
        //list.add(db.getBarcode(ss))
        println("CARTON: ${newCarton}")

        val cLoc = findViewById<View>(R.id.cLoc) as TextView //get reference to Textview
        cLoc.text = newCarton.location
        val cSku = findViewById<View>(R.id.cartonSku) as TextView //get reference to Textview
        cSku.text = newCarton.sku
        val cBarcode = findViewById<View>(R.id.cartonBarcode) as TextView //get reference to Textview
        cBarcode.text = newCarton.barcode

        //webcrawler: try to get SKU automatically
        //val crawl = WebCrawler()
        //crawl.getHtmlFromSite(newCarton.sku)
        //crawl.getHtmlFromSite(newCarton.sku)

        val backButton = findViewById<View>(R.id.backButton) as Button //get reference to backbutton
        val upButton = findViewById<View>(R.id.updateButton) as Button // get reference to updateButton
        val delButton = findViewById<View>(R.id.deleteButton) as Button // get reference to deleteButton

        delButton.setOnClickListener {
            //delete carton
            db.deleteCarton(ss)
            Toast.makeText(this, "Deleted carton!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            //send user back to scanner
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        upButton.setOnClickListener {
            // send user to UpdateForm
            //val intent = Intent(this, UpdateForm::class.java)
            //intent.putExtra("barcode", newCarton.barcode)
            //startActivity(intent)
        }

    }
}