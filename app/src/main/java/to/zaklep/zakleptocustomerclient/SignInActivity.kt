package to.zaklep.zakleptocustomerclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }

    fun onLoginButtonClicked(view: View) {
        val intent = Intent(this, CitySelectionActivity::class.java)
        startActivity(intent)
    }
}
