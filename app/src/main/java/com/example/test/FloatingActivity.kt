package com.example.test

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_floating.*


class FloatingActivity : AppCompatActivity() {
    private lateinit var getResult: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floating)
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        back1.setOnClickListener{
            val nextIntent = Intent(this, MainActivity::class.java)
            startActivity(nextIntent)
        }
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == REQUEST_CODE_DRAW_PREMISSION)
                checkAndStartService()
        }
        initListeners()
    }
    private fun initListeners() {
       btn_call.setOnClickListener {
            if(checkHasDrawOverlayPermissions()) {
                startService(Intent(this, FloatingControlService::class.java))
                val intent = Intent(Intent.ACTION_DIAL)
                if(intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }
            }else{
                navigateDrawPermissionSetting()
            }
       }
       btn_msg.setOnClickListener{
           if(checkHasDrawOverlayPermissions()) {
               startService(Intent(this, FloatingControlMsg::class.java))
               val intent = packageManager.getLaunchIntentForPackage("com.samsung.android.messaging")
               startActivity(intent)
           }else{
               navigateDrawPermissionSetting()
           }
       }
        btn_camera.setOnClickListener{
            if(checkHasDrawOverlayPermissions()) {
                startService(Intent(this, FloatingControlCamera::class.java))
                val intent = packageManager.getLaunchIntentForPackage("com.sec.android.app.camera")
                startActivity(intent)
            }else{
                navigateDrawPermissionSetting()
            }
        }
        btn_kakaotalk.setOnClickListener{
            if(checkHasDrawOverlayPermissions()) {
                startService(Intent(this, FloatingControlKakaotalk::class.java))
                val kakaoPackage = "com.kakao.talk"
                val intentKakao = packageManager.getLaunchIntentForPackage(kakaoPackage)
                try {
                    startActivity(intentKakao)
                } catch (e: Exception) {
                    val intentPlayStore = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + kakaoPackage)) // 설치 링크를 인텐트에 담아
                    startActivity(intentPlayStore) // 플레이스토어로 이동
                }

            }else{
                navigateDrawPermissionSetting()
            }
        }
    }

    private fun navigateDrawPermissionSetting() {
        val intent1 = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName"))
        getResult.launch(intent1)
//        startActivityForResult(intent1, REQUEST_CODE_DRAW_PREMISSION)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == REQUEST_CODE_DRAW_PREMISSION){
//            checkAndStartService()
//        }
//    }

    private fun checkAndStartService() {
        if(checkHasDrawOverlayPermissions()) {
            startService(Intent(this, FloatingControlService::class.java))
        }
    }

    private fun checkHasDrawOverlayPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        }else{
            true
        }
    }
    companion object{
        const val  ACTION_STOP_FOREGROUND = "${BuildConfig.APPLICATION_ID}.stopfloating.service"
        const val REQUEST_CODE_DRAW_PREMISSION = 2

    }
}