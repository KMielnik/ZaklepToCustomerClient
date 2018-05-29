package to.zaklep.zakleptocustomerclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.android.UI
import to.zaklep.zakleptocustomerclient.Models.ErrorMessage
import to.zaklep.zakleptocustomerclient.Models.ModelState

class SignUpActivity : AppCompatActivity() {

    val apiClient = APIClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    override fun onResume() {
        super.onResume()
        if (apiClient.isLoggedIn() || apiClient.isUnregisteredUser()) {
            GoToCitySelection()
        }
    }

    fun onSignupButtonClicked(view: View) = launch(UI) {
            var result = apiClient.SignUpAsync(
                    first_name.text.toString(),
                    last_name.text.toString(),
                    phone_number.text.toString(),
                    login.text.toString(),
                    email.text.toString(),
                    password.text.toString()).await()
        GoToBrowseActivity()
    }

    private fun GoToBrowseActivity() {
        val intent = Intent(this, BrowseActivity::class.java)
        startActivity(intent)
    }

    private fun GoToCitySelection() {
        val intent = Intent(this, CitySelectionActivity::class.java)
        startActivity(intent)
    }
}
