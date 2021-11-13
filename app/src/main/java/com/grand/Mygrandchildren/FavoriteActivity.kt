package com.grand.Mygrandchildren

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_setting.back

class FavoriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        back.setOnClickListener{
            finish()
        }
        video_intro_favorite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=kc2SthzglCM&t=1s"))
            startActivity(intent)
        }
        if (VisibleFavorite.isVisible == true) {
            video_intro1.visibility = View.VISIBLE
        } else {
            video_intro1.visibility = View.GONE
        }
        video_star1_favorite.setOnClickListener{
            video_intro1.visibility = View.GONE
            VisibleFavorite.isVisible = false
        }
    }
}