package to.zaklep.zakleptocustomerclient.Models

import com.google.gson.annotations.SerializedName

data class Restaurant(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String,
        @SerializedName("cuisine") val cuisine: String,
        @SerializedName("localization") val localization: String,
        @SerializedName("representativePhotoUrl") val representativePhotoUrl: String,
        @SerializedName("tables") val tables: List<Table>
) {
    data class Table(
            @SerializedName("id") val id: String,
            @SerializedName("numberOfSeats") val numberOfSeats: Int,
            @SerializedName("coordinates") val coordinates: Coordinates
    ) {
        data class Coordinates(
                @SerializedName("item1") val item1: Int,
                @SerializedName("item2") val item2: Int
        )
    }
}