package to.zaklep.zakleptocustomerclient.Models

import com.google.gson.annotations.SerializedName

data class ErrorMessage(
        @SerializedName("code") val code: String,
        @SerializedName("message") val message: String
)