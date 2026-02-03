package com.grupo5.tickets4u

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import okhttp3.*
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QrScannerActivity : AppCompatActivity() {
    private lateinit var previewView: PreviewView
    private lateinit var cameraExecutor: ExecutorService

    companion object {
        private const val TAG = "QrScanner"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)  // ← CORREGIDO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)

        previewView = findViewById(R.id.previewView)
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QrAnalyzer { qrCode ->
                        validateQrCode(qrCode)
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProviderFuture.get().apply {
                    unbindAll()
                    bindToLifecycle(this@QrScannerActivity, cameraSelector, preview, imageAnalysis)
                }
            } catch (exc: Exception) {
                Log.e(TAG, "Error binding camera", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun validateQrCode(qrResult: String) {
        Log.d(TAG, "Escaneando QR: $qrResult")  // ← DEBUG

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/api/tickets/validate?qr=$qrResult")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {  // ← TIPOS EXPLÍCITOS
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@QrScannerActivity, "❌ Backend offline: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        val resultado = response.body?.string() ?: "Sin respuesta"
                        Toast.makeText(this@QrScannerActivity, "✅ $resultado", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@QrScannerActivity, "❌ Error ${response.code}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
