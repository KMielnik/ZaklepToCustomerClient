package to.zaklep.zakleptocustomerclient.Models

import com.google.gson.annotations.SerializedName

data class OnCreateCustomer(
        @SerializedName("Login") val login: String,
        @SerializedName("Password") val password: String,
        @SerializedName("FirstName") val firstName: String,
        @SerializedName("LastName") val lastName: String,
        @SerializedName("Email") val email: String,
        @SerializedName("Phone") val phone: Int
)