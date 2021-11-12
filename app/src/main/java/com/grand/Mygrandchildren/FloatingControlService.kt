package com.grand.Mygrandchildren

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.grand.Mygrandchildren.FloatingActivity.Companion.ACTION_STOP_FOREGROUND
import kotlin.math.abs
import kotlin.math.roundToInt


class FloatingControlService :Service() {
    private var LAYOUT_FLAG: Int = 0
    private var windowManager: WindowManager? = null
    private var floatingControlView: ViewGroup? = null
    var iconHeight = 0
    var iconWidth = 0
    private var screenHeight = 0
    private var screenWidth = 0
//    private var hideHandler: Handler? = null
//    private var hideRunnable: Runnable? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    @SuppressLint("InflateParams")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(windowManager == null) {
            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        }
        if(floatingControlView == null ){
            val li = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            floatingControlView = li.inflate(R.layout.layout_floating_control_view, null) as ViewGroup?

        }
        if (intent?.action != null && intent.action.equals(
                ACTION_STOP_FOREGROUND, ignoreCase = true)) {
            removeFloatingControl()
            stopForeground(true)
            stopSelf()
        }else {
            addFloatingMenu()
            generateForegroundNotification()
        }
        return START_STICKY

        //Normal Service To test sample service comment the above    generateForegroundNotification() && return START_STICKY
        // Uncomment below return statement And run the app.
//        return START_NOT_STICKY
    }

    private fun removeFloatingControl() {
        if(floatingControlView?.parent !=null) {
            windowManager?.removeView(floatingControlView)
        }
    }

    private fun addFloatingMenu() {
        if (floatingControlView?.parent == null) {
            //Set layout params to display the controls over any screen.
            @Suppress("DEPRECATION")
            LAYOUT_FLAG = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_PHONE else WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
//            params.height = dpToPx(50)
//            params.width = dpToPx(50)
//            iconWidth = params.width
//            iconHeight = params.height
            @Suppress("DEPRECATION")
            screenHeight = windowManager?.defaultDisplay?.height ?: 0
            @Suppress("DEPRECATION")
            screenWidth = windowManager?.defaultDisplay?.width ?: 0
            //Initial position of the floating controls
            params.gravity = Gravity.TOP or Gravity.START
            params.x = 0
            params.y = 100

            //Add the view to window manager
            windowManager?.addView(floatingControlView, params)
            try {
                addOnTouchListener(params)
            } catch (e: Exception) {
                // TODO: handle exception
            }

        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun addOnTouchListener(params: WindowManager.LayoutParams) {
        val collapsedView = floatingControlView?.findViewById<RelativeLayout>(R.id.collapse_view)
        val expandedView = floatingControlView?.findViewById<LinearLayout>(R.id.expanded_container)
        val test1 = floatingControlView?.findViewById<TextView>(R.id.test1)
        val test2 = floatingControlView?.findViewById<TextView>(R.id.test2)
        val test3 = floatingControlView?.findViewById<TextView>(R.id.test3)
        //Add touch listerner to floating controls view to move/close/expand the controls
        floatingControlView?.findViewById<View>(R.id.fab2)?.setOnTouchListener(object : View.OnTouchListener {
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            private var initialX = 0
            private var initialY = 0

            override fun onTouch(view: View?, motionevent: MotionEvent): Boolean {
                val flag3: Boolean = true
                var flag = false
                when (motionevent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        params.alpha = 1.0f
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = motionevent.rawX
                        initialTouchY = motionevent.rawY
                        Log.d(
                            "OnTouchListener",
                            java.lang.StringBuilder("POS: x = ")
                                .append(initialX).append(" y = ")
                                .append(initialY).append(" tx = ")
                                .append(initialTouchX)
                                .append(" ty = ")
                                .append(initialTouchY).toString()
                        )
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        val Xdiff = (motionevent.rawX - initialTouchX) as Float
                        val Ydiff = (motionevent.rawY - initialTouchY) as Float
                        flag = flag3
                        if (Xdiff < 15 && Ydiff < 15) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                collapsedView?.setVisibility(View.GONE);
                                expandedView?.setVisibility(View.VISIBLE);
                            }
                        }
                        return if (abs(initialTouchX - motionevent.rawX) >= 25f) {
                            flag
                        } else {
                            flag = flag3
                            if (abs(
                                    initialTouchY
                                            - motionevent.rawY
                                ) >= 25f
                            ) {
                                flag
                            } else {
                                true
                            }
                        }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        initialX = params.x
                        initialY = params.y
//                        if ((motionevent.rawX < (initialX - iconWidth / 2).toFloat()) || (motionevent.rawY < (initialY - iconHeight / 2).toFloat()) || (motionevent.rawX
//                                .toDouble() > initialX.toDouble() + iconWidth.toDouble() * 1.2)
//                        ) {
//
//                        }
                        params.x = (motionevent.rawX - (iconWidth / 2).toFloat()).toInt()
                        params.y = (motionevent.rawY - iconHeight.toFloat()).toInt()

                        try {
                            windowManager?.updateViewLayout(floatingControlView, params)
                        } // Misplaced declaration of an exception
                        // variable
                        catch (e: java.lang.Exception) {
                            e.printStackTrace()
//                                ExceptionHandling(e)
                            return true
                        }
                        return true
                    }
                    else -> {
                    }
                }
                return flag
            }
        })
        floatingControlView?.findViewById<View>(R.id.close_btn)?.setOnClickListener{
            val intentStop = Intent(this, FloatingControlService::class.java)
            intentStop.action = ACTION_STOP_FOREGROUND
            startService(intentStop)
        }
        floatingControlView?.findViewById<View>(R.id.next_btn)?.setOnClickListener{
            if (test1?.visibility === View.VISIBLE){
                test1?.setVisibility(View.GONE)
                test2?.setVisibility(View.VISIBLE)
            } else if (test3?.visibility === View.VISIBLE){
                test3?.setVisibility(View.GONE)
                test1?.setVisibility(View.VISIBLE)
            } else if (test2?.visibility === View.VISIBLE){
                test2?.setVisibility(View.GONE)
                test3?.setVisibility(View.VISIBLE)
            }

        }
        floatingControlView?.findViewById<View>(R.id.prev_btn)?.setOnClickListener{
            if (test1?.visibility === View.VISIBLE){
                test1?.setVisibility(View.GONE)
                test3?.setVisibility(View.VISIBLE)
            } else if (test2?.visibility === View.VISIBLE){
                test2?.setVisibility(View.GONE)
                test1?.setVisibility(View.VISIBLE)
            } else if (test3?.visibility === View.VISIBLE){
                test3?.setVisibility(View.GONE)
                test2?.setVisibility(View.VISIBLE)
            }
        }
        floatingControlView?.findViewById<View>(R.id.close_button)?.setOnClickListener{
            collapsedView?.setVisibility(View.VISIBLE)
            expandedView?.setVisibility(View.GONE)
        }
    }
    private fun isViewCollapsed(): Boolean {
        return floatingControlView == null || floatingControlView?.findViewById<RelativeLayout>(R.id.collapse_view)
            ?.getVisibility() === View.VISIBLE
    }


    //Notififcation for ON-going
    private var iconNotification: Bitmap? = null
    private var notification: Notification? = null
    var mNotificationManager: NotificationManager? = null
    private val mNotificationId = 123

    private fun generateForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intentMainLanding = Intent(this, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intentMainLanding, 0)
            iconNotification = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            if (mNotificationManager == null) {
                mNotificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                assert(mNotificationManager != null)
                mNotificationManager?.createNotificationChannelGroup(
                    NotificationChannelGroup("chats_group", "Chats")
                )
                val notificationChannel =
                    NotificationChannel("service_channel", "Service Notifications",
                        NotificationManager.IMPORTANCE_MIN)
                notificationChannel.enableLights(false)
                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
                mNotificationManager?.createNotificationChannel(notificationChannel)
            }
            val builder = NotificationCompat.Builder(this, "service_channel")

            builder.setContentTitle(StringBuilder(resources.getString(R.string.app_name)).append(" 가 실행되고 있어요").toString())
                .setTicker(StringBuilder(resources.getString(R.string.app_name)).append("가 실행되고 있어요").toString())
                .setContentText("눌러서 내 손 주 다시 열기") //                    , swipe down for more options.
                .setSmallIcon(R.drawable.child_floating_logo)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(0)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
            if (iconNotification != null) {
                builder.setLargeIcon(Bitmap.createScaledBitmap(iconNotification!!, 128, 128, false))
            }
            builder.color = ContextCompat.getColor(applicationContext, R.color.purple_200)
            notification = builder.build()
            startForeground(mNotificationId, notification)
        }

    }

    //Method to convert dp to px
    private fun dpToPx(dp: Int): Int {
        val displayMetrics = this.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

}