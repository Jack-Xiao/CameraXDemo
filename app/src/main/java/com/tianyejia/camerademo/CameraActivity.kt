package com.tianyejia.camerademo

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Rational
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.lifecycle.LifecycleOwner
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.camera_ui_container.*

/**
 *   created by xiaojiang on 2019-07-26
 */
const val IMMERSIVE_FLAG_TIMEOUT = 500L

class CameraActivity : AppCompatActivity() {
    private var displayId = -1 //镜头id
    private var preview: Preview? = null

    private var lensFacing = CameraX.LensFacing.BACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        view_finder.post {
            displayId = view_finder.display.displayId

            updateCameraUi()
            bindCameraUseCases()
        }
    }

    override fun onResume() {
        super.onResume()
        camera_container.postDelayed({
            camera_container.systemUiVisibility = FLAGS_FULLSCREEN
        }, IMMERSIVE_FLAG_TIMEOUT)
    }


    private fun bindCameraUseCases() {
        val metrics = DisplayMetrics().also { view_finder.display.getRealMetrics(it) }
        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)
        // Set up the view finder use case to display camera preview
        val viewFinderConfig = PreviewConfig.Builder().apply {
            setLensFacing(lensFacing)
            // We request aspect ratio but no resolution to let CameraX optimize our use cases
            setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
            setTargetRotation(view_finder.display.rotation)
        }.build()
        preview = AutoFitPreviewBuilder.build(viewFinderConfig, view_finder)

        CameraX.bindToLifecycle(this@CameraActivity, preview)
    }


    private fun updateCameraUi() {

        //切换前置镜头与后置镜头
        camera_switch_button.setOnClickListener {
            lensFacing = if (CameraX.LensFacing.FRONT == lensFacing) {
                CameraX.LensFacing.BACK
            } else {
                CameraX.LensFacing.FRONT
            }
            CameraX.unbindAll()
            bindCameraUseCases()
        }


    }
}