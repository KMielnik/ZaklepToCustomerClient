package to.zaklep.zakleptocustomerclient

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.transition.TransitionSet
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.TypedValue
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.takisoft.datetimepicker.DatePickerDialog
import com.takisoft.datetimepicker.TimePickerDialog
import kotlinx.android.synthetic.main.activity_find_table.*
import kotlinx.android.synthetic.main.app_bar_find_table.*
import kotlinx.android.synthetic.main.content_find_table.*
import kotlinx.android.synthetic.main.nav_header_find_table.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.toast
import to.zaklep.zakleptocustomerclient.Adapters.RestaurantsTablesAdapter
import to.zaklep.zakleptocustomerclient.Models.Restaurant
import java.text.SimpleDateFormat
import java.util.*

class FindTableActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val apiClient = APIClient()
    private val RestaurantList: MutableList<Restaurant> = mutableListOf<Restaurant>()
    private var filteredRestaurantList: MutableList<Restaurant> = mutableListOf<Restaurant>()
    var filteredDatetime = mutableListOf<String>("2018/05/18", "15:00", "2")

    var adapter: RestaurantsTablesAdapter = RestaurantsTablesAdapter(this, filteredRestaurantList, filteredDatetime)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_table)
        setSupportActionBar(toolbar)

        seats_filter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                filteredDatetime[2] = seats_filter.text.toString()
            }

        })

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        FuelManager.instance.apply {
            basePath = "http://zakleptoapi.azurewebsites.net/api/"

        }

        //Downloading restaurants from api
        "restaurants".httpGet()
                .responseObject<List<Restaurant>> { request, response, result ->
                    result.get().forEach {
                        RestaurantList.add(it)
                        filteredRestaurantList.add(it)
                    }
                    adapter.notifyDataSetChanged()
                }
        //


        var mLayoutManager = LinearLayoutManager(this)
        restaurants_list.layoutManager = mLayoutManager
        restaurants_list.addItemDecoration(BrowseActivity.GridSpacingItemDecoration(1, dpToPx(10), true))
        restaurants_list.itemAnimator = DefaultItemAnimator()
        restaurants_list.adapter = adapter

        nav_view.setNavigationItemSelectedListener(this)

        cousine_filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    onNothingSelected(parent)
                    return
                }
                filteredRestaurantList.clear()
                RestaurantList.filter { x -> x.cuisine == parent?.getItemAtPosition(position).toString() }.forEach {
                    filteredRestaurantList.add(it)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                filteredRestaurantList.clear()
                RestaurantList.forEach {
                    filteredRestaurantList.add(it)
                }
                adapter.notifyDataSetChanged()
            }
        }

        setNavHeader()
    }

    override fun onResume() {
        val calendar = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("yyyy/MM/dd ")
        date_filter.text = dateFormatter.format(calendar.time)
        filteredDatetime[0] = dateFormatter.format(calendar.time)

        val timeFormatter = SimpleDateFormat("HH:mm")
        time_filter.text = timeFormatter.format(calendar.time)

        filteredDatetime[1] = timeFormatter.format(calendar.time).substringBefore(':') + ":00"
        adapter.notifyDataSetChanged()

        super.onResume()
    }

    fun onDatePickerClicked(view: View) {
        val datePickerDialog = DatePickerDialog(this)
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->

            date_filter.text = SimpleDateFormat("yyyy/MM/dd ").format(Date(year - 1900, month, dayOfMonth))
            filteredDatetime[0] = SimpleDateFormat("yyyy/MM/dd ").format(Date(year - 1900, month, dayOfMonth))
            adapter.notifyItemRangeChanged(0,adapter.itemCount)
        }
        datePickerDialog.show()
    }

    fun onTimePickerClicked(view: View) {
        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            time_filter.text = hourOfDay.toString().padStart(2, '0') + ":" + minute.toString().padStart(2, '0')
            filteredDatetime[1] = hourOfDay.toString().padStart(2, '0') + ":00"
            adapter.notifyItemRangeChanged(0,adapter.itemCount)
        }, 15, 0, true)
        timePickerDialog.show()

    }

    fun setNavHeader() = launch(UI) {
        if (apiClient.isLoggedIn()) {
            val customer = apiClient.GetProfile().await()
            customer_name_header.text = customer.firstName + " " + customer.lastName
        } else {
            customer_name_header.text = "Niezarejestrowany użytkowniku"
        }
    }

    var isHiddenPanelShown: Boolean = true
    var downY: Float = 0.0f
    var startClickTime: Long = 0
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        if (ev?.action == MotionEvent.ACTION_DOWN) {
            downY = ev.y
            startClickTime = System.currentTimeMillis()
        } else if (ev?.action == MotionEvent.ACTION_UP) {
            if (System.currentTimeMillis() - startClickTime > ViewConfiguration.getTapTimeout() / 2)
                if (ev.y > downY - 10) {
                    if (!isHiddenPanelShown) {
                        ViewAnimations.expand(hidden_panel)
                        isHiddenPanelShown = true
                    }
                } else {
                    if (isHiddenPanelShown) {
                        ViewAnimations.collapse(hidden_panel)
                        isHiddenPanelShown = false
                    }
                }
        }

        return super.dispatchTouchEvent(ev)
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
        menuInflater.inflate(R.menu.find_table, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_logout -> {
                apiClient.LogOff()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_account -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_find_free_table -> {
                val intent = Intent(this, FindTableActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_browse_restaurants -> {
                val intent = Intent(this, BrowseActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_reservations -> {
                val intent = Intent(this, ReservationsActivity::class.java)
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun dpToPx(dp: Int): Int {
        val r = getResources()
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.getDisplayMetrics()))
    }
}
