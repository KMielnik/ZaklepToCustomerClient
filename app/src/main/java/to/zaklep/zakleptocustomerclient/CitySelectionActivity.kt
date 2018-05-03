package to.zaklep.zakleptocustomerclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_city_selection.*

class CitySelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_selection)
    }

    fun onCitySelectButtonClicked(view: View) {
        val intent = Intent(this, BrowseActivity::class.java)
        startActivity(intent)
    }
}
