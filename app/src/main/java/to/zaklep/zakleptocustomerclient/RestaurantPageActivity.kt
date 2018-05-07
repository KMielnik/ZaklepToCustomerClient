package to.zaklep.zakleptocustomerclient

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.provider.Contacts
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.failure
import kotlinx.android.synthetic.main.activity_restaurant_page.*
import kotlinx.android.synthetic.main.app_bar_restaurant_page.*
import kotlinx.android.synthetic.main.content_restaurant_page.*
import kotlinx.android.synthetic.main.restaurant_card.*
import to.zaklep.zakleptocustomerclient.Models.Restaurant
import kotlin.concurrent.thread

class RestaurantPageActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_page)
        setSupportActionBar(toolbar)



        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        var restaurant: Restaurant
        //Downloading restaurant
        "restaurants/${intent.getStringExtra("ID")}".httpGet()
                .responseObject<Restaurant>() { request, response, result ->
                    setPage(result.get())
                }


        expand_button.setOnClickListener {
            expand_button.visibility = View.GONE
            hide_button.visibility = View.VISIBLE
            restaurant_open_hours.setLines(7)
        }

        hide_button.setOnClickListener {
            hide_button.visibility = View.GONE
            expand_button.visibility = View.VISIBLE
            restaurant_open_hours.setLines(1)
        }
    }

    fun setPage(restaurant: Restaurant) {
        toolbar_layout.title = restaurant.name
        restaurant_description.text = restaurant.description
        restaurant_cousine_page.text = restaurant.cuisine
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.restaurant_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_account -> {

            }
            R.id.nav_find_free_table -> {

            }
            R.id.nav_browse_restaurants -> {
                val intent = Intent(this, BrowseActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_reservations -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
