package to.zaklep.zakleptocustomerclient


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Contacts
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.android.UI


class MainActivity : AppCompatActivity() {

    val apiClient = APIClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var googleLogo = resources.getDrawable(R.drawable.google_logo)
        googleLogo.setBounds(20, 0, 100, 80)
        googleLoginButton.setCompoundDrawables(googleLogo, null, null, null)

        var facebookLogo = resources.getDrawable(R.drawable.facebook_logo)
        facebookLogo.setBounds(20, 0, 100, 80)
        facebookLoginButton.setCompoundDrawables(facebookLogo, null, null, null)

        facebookLoginButton.isEnabled = false
        googleLoginButton.isEnabled = false
        signUpButton.isEnabled = false
        continueAsGuestButton.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        if (apiClient.isLoggedIn())
            GoToBrowseActivity()
    }

    fun onSignUpButtonClicked(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun onSignInButtonClicked(view: View) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    fun onGuestButtonClicked(view: View) {
        apiClient.loginAsUnregistered()
        val intent = Intent(this, CitySelectionActivity::class.java)
        startActivity(intent)
    }

    private fun GoToBrowseActivity() {
        val intent = Intent(this, BrowseActivity::class.java)
        startActivity(intent)
    }

    fun onGoogleSignInButtonClicked(view: View) = launch(UI) {
        apiClient.LoginAsync("customer40", "qwerty1").await()
        val intent = Intent(this@MainActivity, CitySelectionActivity::class.java)
        startActivity(intent)
    }
}