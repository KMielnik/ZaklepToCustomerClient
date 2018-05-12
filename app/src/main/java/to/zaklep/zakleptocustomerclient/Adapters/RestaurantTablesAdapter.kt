package to.zaklep.zakleptocustomerclient.Adapters


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.restaurant_tables_card.view.*
import to.zaklep.zakleptocustomerclient.Models.Restaurant
import to.zaklep.zakleptocustomerclient.R
import to.zaklep.zakleptocustomerclient.RestaurantPageActivity

class RestaurantsTablesAdapter(var mContext: Context, var restaurantList: List<Restaurant>) : RecyclerView.Adapter<RestaurantsTablesAdapter.MyViewHolder>() {
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

        holder.restaurantPageButton.setOnClickListener {
            val intent = Intent(mContext, RestaurantPageActivity::class.java)
            intent.putExtra("ID", restaurantList[position].id)
            mContext.startActivity(intent)
        }

        holder.reservationHourAfterButton.setTextColor(Color.LTGRAY)
        holder.reservationHourBeforeButton.text = "17:00"
        holder.reservationActualButton.text = "18:00"
        holder.reservationHourAfterButton.text = "19:00"
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var restaurantName: TextView = itemView.findViewById(R.id.restaurant_name)
        var restaurantCuisine: TextView = itemView.findViewById(R.id.restaurant_cousine)
        var restaurantPhoto: ImageView = itemView.findViewById(R.id.restaurant_photo)
        var restaurantPageButton: Button = itemView.description_button
        var reservationHourBeforeButton: Button = itemView.hour_before_button
        var reservationActualButton: Button = itemView.hour_actual_button
        var reservationHourAfterButton: Button = itemView.hour_after_button
    }
}