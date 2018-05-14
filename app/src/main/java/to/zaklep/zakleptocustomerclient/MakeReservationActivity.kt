package to.zaklep.zakleptocustomerclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
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
import org.jetbrains.anko.timePicker


class MakeReservationActivity : AppCompatActivity() {

    val apiClient = APIClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_reservation)

        var restaurant: Restaurant
        //Downloading restaurant
        "restaurants/${intent.getStringExtra("ID")}".httpGet()
                .responseObject<Restaurant>() { request, response, result ->
                    restaurant_name.text = result.get().name
                }

    }

    fun onDatePickerClicked(view: View) {
        val datePickerDialog = DatePickerDialog(this)
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            reservation_date.setText(SimpleDateFormat("yyyy-MM-dd").format(Date(year - 1900, month, dayOfMonth)))
        }
        datePickerDialog.show()
    }

    fun onTimePickerClicked(view: View) {
        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            reservation_time.setText(hourOfDay.toString().padStart(2, '0') + ":" + minute.toString().padStart(2, '0'))
        }, 15, 0, true)
        timePickerDialog.show()

    }

    fun onBookClicked(view: View) = launch(UI) {
        var dateStart = reservation_date.text.toString() + "T" + reservation_time.text
        //apiClient.MakeReservation(intent.getStringExtra("ID"),dateStart).await()

        Toast.makeText(this@MakeReservationActivity, "Zarezerwowano stolik.", Toast.LENGTH_LONG).show()
    }
}
