package to.zaklep.zakleptocustomerclient.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.google.gson.internal.bind.util.ISO8601Utils
import kotlinx.android.synthetic.main.reservation_card.view.*
import to.zaklep.zakleptocustomerclient.Models.Reservation
import to.zaklep.zakleptocustomerclient.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ReservationsAdapter(var mContext: Context, var reservationsList: List<Reservation>) : RecyclerView.Adapter<ReservationsAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var reservation: Reservation = reservationsList.get(position)
        holder.restaurantName.text = reservation.restaurant.name
        Glide.with(mContext).load(reservation.restaurant.representativePhotoUrl).into(holder.restaurantPhoto)

        holder.reservationConfirmed.isChecked = reservation.isConfirmed
        holder.reservationDate.text = reservation.dateStart.substringBefore('T')
        holder.reservationHour.text = reservation.dateStart.substringAfter('T').substringBeforeLast(':') + "-" + reservation.dateEnd.substringAfter('T').substringBeforeLast(':')
        holder.cancelReservation.setOnClickListener {
            Toast.makeText(mContext, "DELETED", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationsAdapter.MyViewHolder {
        var itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.reservation_card, parent, false)
        var holder = MyViewHolder(itemView)

        return holder
    }

    override fun getItemCount(): Int {
        return reservationsList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var restaurantName: TextView = itemView.findViewById(R.id.restaurant_name)
        var restaurantPhoto: ImageView = itemView.findViewById(R.id.restaurant_photo)
        var reservationConfirmed: CheckBox = itemView.findViewById(R.id.reservation_confirmed)
        var cancelReservation: Button = itemView.findViewById(R.id.cancel_reservation)
        var reservationDate: TextView = itemView.findViewById(R.id.reservation_date)
        var reservationHour: TextView = itemView.findViewById(R.id.reservation_hour)
    }
}