package to.zaklep.zakleptocustomerclient


import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.notificationManager
import kotlinx.coroutines.experimental.android.UI
import to.zaklep.zakleptocustomerclient.Models.Reservation
import java.util.Timer
import java.util.TimerTask

class NotificationService : Service() {

    val apiClient = APIClient()

    internal var timer: Timer? = null
    internal var timerTask: TimerTask = object : TimerTask() {
        override fun run() {

            //use a handler to run a toast that shows the current timestamp
            handler.post {
                //TODO CALL NOTIFICATION FUNC
            }
        }
    }
    internal var TAG = "Timers"
    internal var Your_X_SECS = 5

    //we are going to use a handler to be able to run in our TimerTask
    internal val handler = Handler()


    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")
        super.onStartCommand(intent, flags, startId)

        startTimer()

        return Service.START_STICKY
    }


    override fun onCreate() {
        FuelManager.instance.apply {
            basePath = "http://zakleptoapi.azurewebsites.net/api/"

        }


    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        stoptimertask()
        super.onDestroy()


    }


    fun startTimer() = launch(UI) {
        //set a new Timer
        timer = Timer()

        //initialize the TimerTask's job
        initializeTimerTask()

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer!!.schedule(timerTask, 5000, (Your_X_SECS * 1000).toLong()) //
        //timer.schedule(timerTask, 5000,1000); //
    }

    fun stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }


    fun initializeTimerTask() {

        timerTask = object : TimerTask() {
            override fun run() {
                handler.post {


                    if(apiClient.isLoggedIn()) {
                        val reservations = "reservations/active/${APIClient.Login}".httpGet()
                                .responseObject<MutableList<Reservation>>() { request, response, result ->
                                    var prefs = this@NotificationService.getSharedPreferences("to.zaklep.zakleptocustomerclient", Context.MODE_PRIVATE)

                                    val oldReservationID = "oldReservation"
                                    var oldJson = prefs.getString(oldReservationID, "kkk")
                                    var newJson = Gson().toJson(result.get())
                                    Log.d("OLD", oldJson)
                                    Log.d("New", newJson)
                                    if (newJson != oldJson && oldJson != "kkk") {
                                        Log.d(TAG, "ITS DIFFRENT")
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            var importance = NotificationManager.IMPORTANCE_LOW;
                                            var notificationChannel = NotificationChannel("IDdd", "NAMEEE", importance);
                                            notificationChannel.enableLights(true);
                                            notificationChannel.setLightColor(Color.RED);
                                            notificationChannel.enableVibration(true);
                                            notificationChannel.importance = NotificationManager.IMPORTANCE_HIGH
                                            notificationChannel.setVibrationPattern(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                                            notificationManager.createNotificationChannel(notificationChannel);
                                        }

                                        var intent = Intent(this@NotificationService, ReservationsActivity::class.java)
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        var pendingIntent = PendingIntent.getActivity(this@NotificationService, 0, intent, 0)

                                        var mBuilder = NotificationCompat.Builder(this@NotificationService, "IDdd")
                                                .setContentTitle("Zmieniono status twojej rezerwacji")
                                                .setContentText("Sprawdź w aplikacji")
                                                .setSmallIcon(R.drawable.zakleptologo)
                                                .setSubText("Rezerwacja stolika")
                                                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                                .setPriority(Notification.PRIORITY_MAX)
                                                .setContentIntent(pendingIntent)
                                                .setAutoCancel(true)
                                        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                        notificationManager.notify(0, mBuilder.build())
                                    }

                                    prefs.edit().putString(oldReservationID, newJson).commit()
                                }
                    }
                }
            }
        }
    }
}