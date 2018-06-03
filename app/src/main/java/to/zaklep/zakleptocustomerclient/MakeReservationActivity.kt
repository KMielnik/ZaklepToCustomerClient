package to.zaklep.zakleptocustomerclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.takisoft.datetimepicker.DatePickerDialog
import com.takisoft.datetimepicker.TimePickerDialog
import kotlinx.android.synthetic.main.activity_make_reservation.*
import kotlinx.coroutines.experimental.launch
import to.zaklep.zakleptocustomerclient.Models.OnCreateReservation
import to.zaklep.zakleptocustomerclient.Models.Restaurant
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.experimental.android.UI


class MakeReservationActivity : AppCompatActivity() {

    val apiClient = APIClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this,NotificationService::class.java))
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_make_reservation)

        var restaurant: Restaurant
        //Downloading restaurant
        "restaurants/${intent.getStringExtra("ID")}".httpGet()
                .responseObject<Restaurant>() { request, response, result ->
                    restaurant_name.text = result.get().name
                }

        val calendar = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("yyyy / MM / dd ")
        reservation_date.text = dateFormatter.format(calendar.time)

        val timeFormatter = SimpleDateFormat("HH:mm")
        reservation_time.text = timeFormatter.format(calendar.time)


        if (intent.getStringExtra("Time") != null)
            reservation_time.text = intent.getStringExtra("Time")
        if (intent.getStringExtra("Date") != null)
            reservation_date.text = intent.getStringExtra("Date")
        if (intent.getStringExtra("Seats") != null)
            number_of_seats.setText(intent.getStringExtra("Seats"))
    }

    fun onDatePickerClicked(view: View) {
        val datePickerDialog = DatePickerDialog(this)
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            reservation_date.text = SimpleDateFormat("yyyy/MM/dd ").format(Date(year - 1900, month, dayOfMonth))
        }
        datePickerDialog.show()
    }

    fun onTimePickerClicked(view: View) {
        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            reservation_time.text = hourOfDay.toString().padStart(2, '0') + ":" + minute.toString().padStart(2, '0')
        }, 15, 0, true)
        timePickerDialog.show()

    }

    fun onBookClicked(view: View) = launch(UI) {
        var dateStart = reservation_date.text.toString() + "T" + reservation_time.text
        apiClient.MakeReservation(intent.getStringExtra("ID"), dateStart).await()
        Snackbar.make(reservation_layout, "Pomyślnie dokonano rezerwacji", Snackbar.LENGTH_LONG).show()
    }
}
