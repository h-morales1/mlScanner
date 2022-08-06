package com.example.mlscanner.databasehelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

//import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import org.json.JSONObject.NULL

//import java.sql.Types.NULL

//original TABLENAME = cartons1 and TEXT is used for COL_LOC
val DATABASENAME = "cartonDB"
val TABLENAME = "cartons1"
val COL_BARCODE = "barcode"
val COL_SKU = "sku"
val COL_LOC = "location"
val COL_ID = "id"
class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASENAME, null,
    1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLENAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "                                   $COL_BARCODE TEXT, $COL_SKU TEXT, $COL_LOC BLOB)"
        //val createTable = "CREATE TABLE " + TABLENAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_BARCODE + " TEXT," + COL_SKU + " TEXT, "+ COL_LOC + "TEXT)"
        db?.execSQL(createTable)
        println("created table!")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //onCreate(db);
    }
    fun insertData(cartoon: Carton) {
        println("Inserting DATA!!!")
        println("TEST: ${cartoon.barcode}")
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_BARCODE, cartoon.barcode)
        contentValues.put(COL_SKU, cartoon.sku)
        contentValues.put(COL_LOC, cartoon.location)
        //val scuel = "INSERT INTO cartons1(barcode, sku, location) VALUES(23646644, 2343436, 14)"
        val firstString = "INSERT INTO cartons1(${COL_BARCODE}, ${COL_SKU}, ${COL_LOC})"
        val secondString = " VALUES(${cartoon.barcode}, ${cartoon.sku}, ${cartoon.location})"
        val finalString = firstString + secondString
        //val scuel = "INSERT INTO cartons1(barcode, sku, location) VALUES(${carton.barcode}, ${carton.sku}, ${carton.location})"
        //database.execSQL("INSERT INTO $TABLENAME ($COL_BARCODE, $COL_SKU, $COL_LOC) VALUES($carton.barcode, $carton.sku, $carton.location)")
        //database.execSQL(finalString)
        database.insert(TABLENAME, null, contentValues)
        /*if (result == (0).toLong()) {
            //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }*/
        database.close()
    }
    fun readData(): MutableList<Carton> {
        println("READING DATA!!")
        val list: MutableList<Carton> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val carton = Carton()
                carton.id = result.getColumnIndex(COL_ID)
                //carton.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                carton.sku = result.getColumnIndex(COL_SKU).toString()
                carton.barcode = result.getColumnIndex(COL_BARCODE).toString()
                carton.location = result.getColumnIndex(COL_LOC).toString()
                //user.age = result.getString(result.getColumnIndex(COL_AGE)).toInt()
                list.add(carton)
            }
            while (result.moveToNext())
        }
        //val gru = list[0].barcode
        //println("DBDATA: $gru")
        db.close()
        return list
        //println(list)
    }

    fun getSku(sku: String): MutableList<Carton> {
        sku.trim()
        val list: MutableList<Carton> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLENAME WHERE TRIM($COL_SKU) = ?"
        val result = db.rawQuery(query, arrayOf(sku))
        val newCarton = Carton()

        if (result.moveToFirst()) {
            do {
                //val carton = Carton()
                newCarton.id = result.getColumnIndex(COL_ID)
                newCarton.barcode = result.getColumnIndex(COL_BARCODE).toString()
                newCarton.sku = result.getColumnIndex(COL_SKU).toString()
                newCarton.location = result.getColumnIndex(COL_LOC).toString()
                list.add(newCarton)
            }
            while (result.moveToNext())
        }
        println("RESULT: ${list.size}")
        db.close()
        return list
    }

    fun getBarcode(bCode: String): MutableList<Carton> {
        println("bCode: ${bCode}")
        bCode.trim()
        val list: MutableList<Carton> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLENAME WHERE TRIM($COL_BARCODE) = ?"
        //val arr = arrayOf(bCode)
        val result = db.rawQuery(query, arrayOf(bCode))
        //println("RESULT: ${result}")
        val newCarton = Carton()

        if (result.moveToFirst()) {
            do {
                //val carton = Carton()
                newCarton.id = result.getColumnIndex(COL_ID)
                newCarton.barcode = result.getColumnIndex(COL_BARCODE).toString()
                newCarton.sku = result.getColumnIndex(COL_SKU).toString()
                newCarton.location = result.getColumnIndex(COL_LOC).toString()
                list.add(newCarton)
            }
                while (result.moveToNext())
        }
        //println("RESULT: ${newCarton.barcode}")
        //val egg = list[0]
        println("RESULT: ${list.size}")
        db.close()
        return list
    }

    fun getCarton(bCode: String): Carton {
        println("bCode: ${bCode}")
        bCode.trim()
        val list: MutableList<Carton> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLENAME WHERE TRIM($COL_BARCODE) = ?"
        //val arr = arrayOf(bCode)
        val cursor: Cursor?
        cursor = db.rawQuery(query, arrayOf(bCode))
        //println("RESULT: ${result}")
        val newCarton = Carton()

        if (cursor.moveToFirst()) {
            do {
                newCarton.barcode = cursor.getString(1)
                newCarton.sku = cursor.getString(2)
                newCarton.location = cursor.getString(3)
                //val carton = Carton()
                //carton.id = result.getColumnIndex(COL_ID)
                //carton.barcode = result.getColumnIndex(COL_BARCODE).toString()
                //println("BARCODER: ${carton.barcode}")
                //carton.sku = result.getColumnIndex(COL_SKU).toString()
                //carton.location = result.getColumnIndex(COL_LOC).toString()
                //list.add()
            }
            while (cursor.moveToNext())
        }
        //println("RESULT: ${newCarton.barcode}")
        //val egg = list[0]
        println("RESULT: ${list.size}")
        db.close()
        return newCarton
    }

    fun getCartonBySku(sku: String): Carton {
        println("bCode: ${sku}")
        sku.trim()
        val list: MutableList<Carton> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLENAME WHERE TRIM($COL_SKU) = ?"
        //val arr = arrayOf(bCode)
        val cursor: Cursor?
        cursor = db.rawQuery(query, arrayOf(sku))
        //println("RESULT: ${result}")
        val newCarton = Carton()

        if (cursor.moveToFirst()) {
            do {
                newCarton.barcode = cursor.getString(1)
                newCarton.sku = cursor.getString(2)
                newCarton.location = cursor.getString(3)
                //val carton = Carton()
                //carton.id = result.getColumnIndex(COL_ID)
                //carton.barcode = result.getColumnIndex(COL_BARCODE).toString()
                //println("BARCODER: ${carton.barcode}")
                //carton.sku = result.getColumnIndex(COL_SKU).toString()
                //carton.location = result.getColumnIndex(COL_LOC).toString()
                //list.add()
            }
            while (cursor.moveToNext())
        }
        //println("RESULT: ${newCarton.barcode}")
        //val egg = list[0]
        println("RESULT: ${list.size}")
        db.close()
        return newCarton
    }

    fun updateCarton(bCode: String, sku: String, cartonLoc: String) {
        bCode.trim()
        sku.trim()
        cartonLoc.trim()
        var content = ContentValues()
        content.put("sku", sku)
        content.put("location", cartonLoc)


        val db = this.readableDatabase
        db.update(TABLENAME,content, "barcode = ?", arrayOf(bCode))
        //val query = "UPDATE $TABLENAME SET $COL_SKU = ?, $COL_LOC = ? WHERE $COL_BARCODE = ?"
        //val cursor: Cursor?
        //db.rawQuery(query, arrayOf(sku, cartonLoc, bCode))
        db.close()

    }

    fun checkSkuExists(sku: String): Boolean {
        var existsSku = false
        val possibleSku = getSku(sku)
        if(possibleSku != emptyList<Carton>()) {
            existsSku = true
        }
        return existsSku
    }

    fun checkBarcodeExists(barcode: String): Boolean {
        var existBcode = false
        //val context = this
        //val db = this.readableDatabase
        val possibleCode = getBarcode(barcode)
        if(possibleCode != emptyList<Carton>()) {
            existBcode = true
        }
        return existBcode

    }

    fun deleteCarton(bCode: String) {
        val db = this.readableDatabase
        db.delete(TABLENAME, "barcode = ?", arrayOf(bCode))
        db.close()
        println("Deleted carton!!!!")
    }

}