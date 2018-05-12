package to.zaklep.zakleptocustomerclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.progressDialog
import to.zaklep.zakleptocustomerclient.Models.ErrorMessage

class SignInActivity : AppCompatActivity() {

    val apiClient = APIClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }

    override fun onResume() {
        super.onResume()
        if (apiClient.isLoggedIn())
            GoToBrowseActivity()
    }

    fun onLoginButtonClicked(view: View) = launch(UI) {

        if (password.text.isBlank()) {
            password.error = resources.getString(R.string.error_invalid_password)
            return@launch
        }

        var result = apiClient.LoginAsync(login.text.toString(), password.text.toString()).await()

        if (result is ErrorMessage) {
            when (result.code) {
                "user_not_found" -> login.error = result.message
                "invalid_password" -> password.error = result.message
            }
        } else {
            GoToCitySelection()
        }
    }

    private fun GoToCitySelection() {
        val intent = Intent(this@SignInActivity, CitySelectionActivity::class.java)
        startActivity(intent)
    }

    private fun GoToBrowseActivity() {
        val intent = Intent(this, BrowseActivity::class.java)
        startActivity(intent)
    }
}
