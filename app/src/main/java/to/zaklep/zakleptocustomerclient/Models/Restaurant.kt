package to.zaklep.zakleptocustomerclient.Models

import com.google.gson.annotations.SerializedName
data class Restaurant(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String,
        @SerializedName("cuisine") val cuisine: String,
        @SerializedName("localization") val localization: String,
        @SerializedName("representativePhotoUrl") val representativePhotoUrl: String,
        @SerializedName("tables") val tables: List<Any>
)