package to.zaklep.zakleptocustomerclient.Models

import com.google.gson.annotations.SerializedName

data class Coordinates(
        @SerializedName("item1") val item1: Int,
        @SerializedName("item2") val item2: Int
)