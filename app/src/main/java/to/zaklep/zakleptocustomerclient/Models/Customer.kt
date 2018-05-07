package to.zaklep.zakleptocustomerclient.Models

import com.google.gson.annotations.SerializedName

data class Customer(
        @SerializedName("login") val login: String,
        @SerializedName("firstName") val firstName: String,
        @SerializedName("lastName") val lastName: String,
        @SerializedName("email") val email: String,
        @SerializedName("phone") val phone: String,
        @SerializedName("createdAt") val createdAt: String
)