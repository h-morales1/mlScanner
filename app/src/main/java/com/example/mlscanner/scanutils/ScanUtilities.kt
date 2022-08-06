package com.example.mlscanner.scanutils

import android.content.Context
import android.net.Uri
import java.io.File

class ScanUtilities {

    /*Delete photo after processing*/
    fun fdelete(filepath: Uri) {
        val fdel = File(filepath.path!!)
        if(fdel.exists()){
            if(fdel.delete()) {
                println("File deleted: ${filepath}")
            } else {
                println("File not deleted: ${filepath}")
            }
        }
    }

    /*Removes the dash between first 3 digits of sku and 4 remaining digits*/
    fun skuSanitize(sku: String): String {
        var saniSku = sku
        if (sku.contains("-")) {
            println("Sku before: $sku")
            saniSku = sku.replace("-","")
            println("Sku after: $saniSku")
        }
        return saniSku
    }
}