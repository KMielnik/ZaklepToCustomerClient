package to.zaklep.zakleptocustomerclient.Models

import com.google.gson.annotations.SerializedName

data class ModelState(
        @SerializedName("key") val email: List<String>,
        @SerializedName("Login") val login: List<String>,
        @SerializedName("Password") val password: List<String>
)