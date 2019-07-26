package com.tianyejia.camerademo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rxPermissions = RxPermissions(this)

        btn_camera.setOnClickListener {
            rxPermissions.request(android.Manifest.permission.CAMERA)
                .subscribe {
                    startActivity(Intent(this, CameraActivity::class.java))
                }

        }
    }
}
