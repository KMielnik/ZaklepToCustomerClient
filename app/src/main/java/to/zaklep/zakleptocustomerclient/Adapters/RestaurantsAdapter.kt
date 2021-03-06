package to.zaklep.zakleptocustomerclient.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import to.zaklep.zakleptocustomerclient.Models.Restaurant
import to.zaklep.zakleptocustomerclient.R
import to.zaklep.zakleptocustomerclient.RestaurantPageActivity

class RestaurantsAdapter(var mContext: Context, var restaurantList: List<Restaurant>) : RecyclerView.Adapter<RestaurantsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_card, parent, false)
        var holder = MyViewHolder(itemView)
        holder.itemView.setOnClickListener {
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

        holder.restaurantFavourite.setOnClickListener { v -> (v as ImageButton).setColorFilter(Color.RED) }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var restaurantName: TextView = itemView.findViewById(R.id.restaurant_name)
        var restaurantCuisine: TextView = itemView.findViewById(R.id.restaurant_cousine)
        var restaurantPhoto: ImageView = itemView.findViewById(R.id.restaurant_photo)
        var restaurantFavourite: ImageButton = itemView.findViewById(R.id.restaurant_favourite)
    }
}