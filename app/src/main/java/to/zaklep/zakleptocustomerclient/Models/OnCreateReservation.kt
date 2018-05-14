package to.zaklep.zakleptocustomerclient.Models

import com.google.gson.annotations.SerializedName

data class OnCreateReservation(
        @SerializedName("restaurant") val restaurant: Restaurant,
        @SerializedName("dateStart") val dateStart: String,
        @SerializedName("dateEnd") val dateEnd: String,
        @SerializedName("table") val table: Table,
        @SerializedName("customer") val customer: Customer,
        @SerializedName("isConfirmed") val isConfirmed: Boolean,
        @SerializedName("isActive") val isActive: Boolean
)