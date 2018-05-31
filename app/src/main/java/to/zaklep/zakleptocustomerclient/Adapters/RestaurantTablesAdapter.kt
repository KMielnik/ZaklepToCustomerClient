package to.zaklep.zakleptocustomerclient.Adapters


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.restaurant_tables_card.view.*
import to.zaklep.zakleptocustomerclient.MakeReservationActivity
import to.zaklep.zakleptocustomerclient.Models.Restaurant
import to.zaklep.zakleptocustomerclient.R
import to.zaklep.zakleptocustomerclient.RestaurantPageActivity

class RestaurantsTablesAdapter(var mContext: Context, var restaurantList: List<Restaurant>, var datetime: List<String>) : RecyclerView.Adapter<RestaurantsTablesAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_tables_card, parent, false)
        var holder = MyViewHolder(itemView)
        holder.itemView.description_button.setOnClickListener {
            val intent = Intent(itemView.context, RestaurantPageActivity::class.java)
            intent.putExtra("ID", restaurantList[holder.adapterPosition].id)
            itemView.context.startActivity(intent)
        }
        return holder
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var restaurant: Restaurant = restaurantList.get(position)
        holder.restaurantName.text = restaurant.name
        holder.restaurantCuisine.text = restaurant.cuisine

        Glide.with(mContext).load(restaurant.representativePhotoUrl).into(holder.restaurantPhoto)

        var hourbeforecheck = (datetime[1].substringBefore(':').toInt() - 1)
        if (hourbeforecheck < 0)
            hourbeforecheck = 23

        if (restaurant.name == "Zamkowa") {
            holder.restaurantPageButton.setOnClickListener {
                val intent = Intent(mContext, RestaurantPageActivity::class.java)
                intent.putExtra("ID", restaurantList[position].id)
                mContext.startActivity(intent)
            }
            when (hourbeforecheck % 3) {
                0 -> {
                    holder.reservationHourBeforeButton.isEnabled = false
                    holder.reservationActualButton.isEnabled = true
                    holder.reservationHourAfterButton.isEnabled = true
                }
                2 -> {
                    holder.reservationHourBeforeButton.isEnabled = true
                    holder.reservationActualButton.isEnabled = false
                    holder.reservationHourAfterButton.isEnabled = true
                }
                1 -> {
                    holder.reservationHourBeforeButton.isEnabled = true
                    holder.reservationActualButton.isEnabled = true
                    holder.reservationHourAfterButton.isEnabled = false
                }
            }
        } else {
            holder.mCardView.setCardBackgroundColor(Color.rgb(200, 200, 200))
            holder.restaurantPhoto.setColorFilter(Color.rgb(178, 178, 178), PorterDuff.Mode.DARKEN)
            holder.reservationActualButton.isEnabled = false
            holder.reservationHourAfterButton.isEnabled = false
            holder.reservationHourBeforeButton.isEnabled = false
            holder.restaurantPageButton.isEnabled = false
        }


        var hourBefore = hourbeforecheck.toString() + ":" + datetime[1].substringAfter(':')
        var actualHour = datetime[1]
        var hourAfter = (datetime[1].substringBefore(':').toInt() + 1).toString() + ":" + datetime[1].substringAfter(':')

        holder.reservationHourBeforeButton.text = hourBefore
        holder.reservationHourBeforeButton.setOnClickListener {
            val intent = Intent(mContext, MakeReservationActivity::class.java)
            intent.putExtra("ID", restaurant.id)
            intent.putExtra("Time", hourBefore)
            intent.putExtra("Date", datetime[0])
            intent.putExtra("Seats", datetime[2])
            mContext.startActivity(intent)
        }

        holder.reservationActualButton.text = actualHour
        holder.reservationActualButton.setOnClickListener {
            val intent = Intent(mContext, MakeReservationActivity::class.java)
            intent.putExtra("ID", restaurant.id)
            intent.putExtra("Time", actualHour)
            intent.putExtra("Date", datetime[0])
            intent.putExtra("Seats", datetime[2])
            mContext.startActivity(intent)
        }

        holder.reservationHourAfterButton.text = hourAfter
        holder.reservationHourAfterButton.setOnClickListener {
            val intent = Intent(mContext, MakeReservationActivity::class.java)
            intent.putExtra("ID", restaurant.id)
            intent.putExtra("Time", hourAfter)
            intent.putExtra("Date", datetime[0])
            intent.putExtra("Seats", datetime[2])
            mContext.startActivity(intent)
        }


    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var restaurantName: TextView = itemView.findViewById(R.id.restaurant_name)
        var restaurantCuisine: TextView = itemView.findViewById(R.id.restaurant_cousine)
        var restaurantPhoto: ImageView = itemView.findViewById(R.id.restaurant_photo)
        var restaurantPageButton: Button = itemView.description_button
        var reservationHourBeforeButton: Button = itemView.hour_before_button
        var reservationActualButton: Button = itemView.hour_actual_button
        var reservationHourAfterButton: Button = itemView.hour_after_button
        var mCardView: CardView = itemView.restaurant_tables_cardview
    }
}