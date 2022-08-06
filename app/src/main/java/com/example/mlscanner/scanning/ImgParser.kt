package com.example.mlscanner.scanning

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.mlscanner.AddItem
import com.example.mlscanner.DisplayCarton
import com.example.mlscanner.FillForm
import com.example.mlscanner.ScanItem
import com.example.mlscanner.databasehelper.DataBaseHandler
import com.example.mlscanner.scanutils.ScanUtilities
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

class ImgParser {

    fun scanBarcode(imgPath: Uri, ctx: Context, bCode: EditText) {
        var possibleCode = ""
        var poss: Barcode
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build() //TODO: Optimize for specific barcode format
        val imgToScan: InputImage
        try {
            imgToScan = InputImage.fromFilePath(ctx, imgPath)
            val scanner = BarcodeScanning.getClient(options)
            val result = scanner.process(imgToScan)
                .addOnSuccessListener { barcodes ->
                    //success
                    if(barcodes.isEmpty()) {
                        println("No barcode was detected!")
                        Toast.makeText(ctx, "No barcode found! Try again please.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(ctx, AddItem::class.java)
                        ctx.startActivity(intent) // take user back to scanner to try scan again

                    } else {
                        println("Code: ${barcodes[0].rawValue}")
                        poss = barcodes[0]
                        possibleCode = poss.displayValue!! // extract barcode value
                        bCode.setText(possibleCode) // set the barcode value into the EditText field
                    }

                }
                .addOnFailureListener {
                    //task failed
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /*Used in conjunction with db.checkBarcode to check if a barcode match is found inside the db*/
    fun checkBarcodeDb(imgPath: Uri, ctx: Context) {
        var possibleCode = ""

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build()
        val imgToScan: InputImage
        try {
            imgToScan = InputImage.fromFilePath(ctx, imgPath)
            val scanner = BarcodeScanning.getClient(options)
            val result = scanner.process(imgToScan)
                .addOnSuccessListener { barcodes ->
                    //success
                    //check if barcode was detected
                    if (barcodes.isEmpty()) {
                        //println("No barcode was detected")
                        Toast.makeText(ctx, "No barcode found! Try again please.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(ctx, ScanItem::class.java)
                        ctx.startActivity(intent) // take user back to scanner to try scan again
                    } else {
                        println("Code: ${barcodes[0].rawValue}")
                        //check if barcode exists in DB
                        val db = DataBaseHandler(ctx)
                        if(db.checkBarcodeExists(barcodes[0].displayValue!!)) {
                            //does exist so display carton
                            val intent = Intent(ctx, DisplayCarton::class.java)
                            intent.putExtra("barcode", barcodes[0].displayValue)
                            ctx.startActivity(intent)
                        } else {
                            //doesnt exist so tell user to scan pricetag to add location
                                // autochange activity to scanner in order to scan the bin tag
                            Toast.makeText(ctx, "No matches found, please scan bin tag to save location.", Toast.LENGTH_LONG).show()
                            val intent = Intent(ctx, AddItem::class.java)
                            ctx.startActivity(intent)
                        }
                    }
                }
                .addOnFailureListener {
                    //task failed
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun recognizeText(imgPath: Uri, ctx: Context, skuCode: EditText) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image: InputImage
        var sku = ""
        try {
            image = InputImage.fromFilePath(ctx, imgPath)
            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // Task completed successfully
                    //println("Text: ${visionText.text}")
                    //println("SKU: ${visionText.text[visionText.text.length]}")
                    //sku = visionText.text[visionText.text.length].toString()

                    //test
                    val resultText = visionText.text
                    for (block in visionText.textBlocks) {
                        val blockText = block.text
                        val blockCornerPoints = block.cornerPoints
                        val blockFrame = block.boundingBox
                        for (line in block.lines) {
                            val lineText = line.text
                            val lineCornerPoints = line.cornerPoints
                            val lineFrame = line.boundingBox
                            for (element in line.elements) {
                                val elementText = element.text
                                //println("POssible: ${elementText}")
                                if(elementText.contains('-') && (elementText.length == 8)) {
                                    println("FOUND SKU: ${elementText}")
                                    sku = elementText
                                    val skuSani = ScanUtilities() // remove the dash from the sku
                                    sku = skuSani.skuSanitize(sku)
                                    skuCode.setText(sku)
                                }
                                val elementCornerPoints = element.cornerPoints
                                println("Points: ${elementCornerPoints}")
                                //val elementFrame = element.boundingBox
                            }
                        }
                    }
                    //println("SKU: ${sku}")
                    //test

                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}