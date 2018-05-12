package to.zaklep.zakleptocustomerclient

import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.gson.responseObject
import com.google.gson.Gson
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import to.zaklep.zakleptocustomerclient.Models.*
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import javax.xml.transform.Result

class APIClient {
    companion object {
        init {
            FuelManager.instance.apply {
                basePath = "http://zakleptoapi.azurewebsites.net/api"
                baseHeaders = mapOf(Pair("Content-Type", "application/json"))
            }
        }

        private var Token: String = ""
        private var Login: String = ""
    }


    fun isLoggedIn(): Boolean {

        return Token.isNotBlank()
    }

    fun isUnregisteredUser(): Boolean {
        return Token == "Unregistered"
    }

    fun loginAsUnregistered() {
        Token = "Unregistered"
    }

    fun LoginAsync(login: String, password: String) = async(CommonPool) {
        var loginCredentials = LoginCredentials(login, password)
        var json = gson.toJson(loginCredentials)

        val result = Fuel.post("customers/login")
                .body(json)
                .responseString()

        val (data, error) = result.third
        if (error == null) {
            val jwtToken = Deserialize<JwtToken>(data)
            Token = jwtToken.token

            Login = Fuel.post("customers/getlogin")
                    .body(data!!)
                    .responseString()
                    .third
                    .get()

        } else {
            return@async GetError(result)
        }
    }

    fun LogOff() {
        Token = ""
        Login = ""
    }

    fun SignUpAsync(firstName: String, lastName: String, phoneNumber: String,
                    login: String, email: String, password: String) = async(CommonPool) {
        var onCreateCustomer = OnCreateCustomer(login, password, firstName, lastName, email, phoneNumber.toInt())
        val json = gson.toJson(onCreateCustomer)

        val result = Fuel.post("customers/register")
                .body(json)
                .responseString()

        val (data, error) = result.third
        if (error != null) {
            //if (error.response.statusCode == 400)
            //  return@async GetError(result)
            //if (error.response.statusCode == 400)

        }
        return@async 0

    }

    fun GetProfile() = async(CommonPool) {
        val customer = Fuel.get("customers/$Login")
                .responseObject<Customer>()
                .third
                .get()
        return@async customer
    }

    fun GetActiveReservations() = async(CommonPool) {
        val reservations = Fuel.get("reservations/active/$Login")
                .responseObject<MutableList<Reservation>>()
                .third
                .get()
        return@async reservations
    }

    fun DeleteReservation(reservation: Reservation) = async(CommonPool) {
        Fuel.delete("reservations/${reservation.id}/remove")
                .body(reservation.id)
                .response()
        return@async
    }

    val gson = Gson()
    private inline fun <reified T : Any> Deserialize(json: String?): T = gson.fromJson(json, T::class.java)

    private fun GetError(result: Triple<Request, Response, com.github.kittinunf.result.Result<String, FuelError>>): ErrorMessage {
        return gson.fromJson(InputStreamReader(ByteArrayInputStream(result.second.data)), ErrorMessage::class.java)
    }


}