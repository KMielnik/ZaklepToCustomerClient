package to.zaklep.zakleptocustomerclient.Models

import com.google.gson.annotations.SerializedName

data class Table(
        @SerializedName("id") val id: String,
        @SerializedName("numberOfSeats") val numberOfSeats: Int,
        @SerializedName("coordinates") val coordinates: Coordinates
)