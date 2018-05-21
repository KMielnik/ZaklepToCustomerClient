package to.zaklep.zakleptocustomerclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_city_selection.*

class CitySelectionActivity : AppCompatActivity() {

    val apiClient = APIClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_selection)
        GoToBrowseActivity() // As long as we work only in Stettin
    }

    override fun onResume() {
        super.onResume()
        if (apiClient.isLoggedIn() || apiClient.isUnregisteredUser())
            GoToBrowseActivity()
    }

    fun onCitySelectButtonClicked(view: View) {
        val intent = Intent(this, BrowseActivity::class.java)
        startActivity(intent)
    }

    private fun GoToBrowseActivity() {
        val intent = Intent(this, BrowseActivity::class.java)
        startActivity(intent)
    }
}
