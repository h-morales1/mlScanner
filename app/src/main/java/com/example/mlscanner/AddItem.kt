package com.example.mlscanner

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mlscanner.databinding.ActivityAddItemBinding
import com.example.mlscanner.databinding.ActivityMainBinding
import com.example.mlscanner.scanning.ImgParser
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddItem : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    //private lateinit var bind: Acti

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var currentImgPath: Uri

    //scanner functions
    val imgParse = ImgParser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        outputDirectory = getOutputDirectory()

        if(allPermissionGranted()) {
            Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show()
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, Constants.REQUIRED_PERMISSIONS, Constants.REQUEST_CODE_PERMISSIONS)
        }

        binding.cameraCaptureButton.setOnClickListener {
            takePhoto()
        }

    }

    fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(Constants.FILE_NAME_FORMAT,
                Locale.getDefault()).format(System.currentTimeMillis()) + ".jpg")

        //
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOption, ContextCompat.getMainExecutor(this),
            object :ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile) //TODO: Use the uri and pass that to Additem, imgParser needs to accept EditText as parameters
                    //currentImgPath = savedUri // can delete
                    //imgParse.scanBarcode(savedUri, this@AddItem)
                    //imgParse.recognizeText(savedUri, this@AddItem)
                    val intent = Intent(this@AddItem, FillForm::class.java)
                    //val extras: Bundle = Bundle()
                    //extras.putString("barcode", imgParse.scanBarcode(savedUri, this@AddItem))
                    //extras.putInt("barcode", imgParse.scanBarcode(savedUri, this@AddItem).toInt())
                    //extras.putString("sku", imgParse.recognizeText(savedUri, this@AddItem)) //TODO: return SKU from recognizeText()
                    intent.putExtra("savedUri", savedUri.toString())
                    //intent.putExtra("barcode", imgParse.scanBarcode(savedUri, this@AddItem))
                    //println("LN: ${imgParse.scanBarcode(savedUri, this@AddItem)}")
                    //intent.putExtra("sku", imgParse.recognizeText(savedUri, this@AddItem))
                    //intent.putExtras(extras)
                    startActivity(intent)
                    //val msg = "Photo Saved"

                    //Toast.makeText(this@AddItem, "$msg $savedUri", Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(Constants.TAG, "onError: ${exception.message}", exception)
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider
            .getInstance(this)
        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also { mPreview->
                    mPreview.setSurfaceProvider(
                        binding.viewFinder.surfaceProvider
                    )
                }
            //13:22
            imageCapture = ImageCapture.Builder()
                .build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                //
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this,
                    cameraSelector, preview, imageCapture)
            } catch (e:Exception) {
                //
                Log.d(Constants.TAG,"startCamera failed: ", e)
            }
        }, ContextCompat.getMainExecutor(this))

    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
            if(allPermissionGranted()) {
                //camera start
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted!", Toast.LENGTH_SHORT).show()
                finish()
            }

        }
    }


    private fun allPermissionGranted() =
        //var permissionsGranted = false
        Constants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }

}