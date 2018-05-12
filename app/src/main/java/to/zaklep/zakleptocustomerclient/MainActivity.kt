package to.zaklep.zakleptocustomerclient


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    val apiClient = APIClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
}