package to.zaklep.zakleptocustomerclient.Models

import com.google.gson.annotations.SerializedName

data class JwtToken(
        @SerializedName("token") val token: String,
        @SerializedName("expiry") val expiry: Long
)