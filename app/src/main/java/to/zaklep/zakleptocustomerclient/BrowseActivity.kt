package to.zaklep.zakleptocustomerclient

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.activity_browse.*
import kotlinx.android.synthetic.main.app_bar_browse.*
import kotlinx.android.synthetic.main.content_browse.*
import kotlinx.android.synthetic.main.nav_header_browse.*
import kotlinx.coroutines.experimental.launch
import to.zaklep.zakleptocustomerclient.Adapters.RestaurantsAdapter
import to.zaklep.zakleptocustomerclient.Models.Restaurant
import kotlinx.coroutines.experimental.android.UI
import to.zaklep.zakleptocustomerclient.R.id.drawer_layout
import to.zaklep.zakleptocustomerclient.R.id.hidden_panel

class BrowseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val apiClient = APIClient()

    private val RestaurantList: MutableList<Restaurant> = mutableListOf<Restaurant>()
    private var filteredRestaurantList: MutableList<Restaurant> = mutableListOf<Restaurant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        // setNavHeader()

        FuelManager.instance.apply {
            basePath = "http://zakleptoapi.azurewebsites.net/api/"

        }

        var adapter: RestaurantsAdapter = RestaurantsAdapter(this, filteredRestaurantList)

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
        restaurants_list.addItemDecoration(GridSpacingItemDecoration(1, dpToPx(10), true))
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
                RestaurantList.filter { x -> x.cuisine == parent?.getItemAtPosition(position).toString() }.forEach() {
                    filteredRestaurantList.add(it)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                filteredRestaurantList.clear()
                RestaurantList.forEach() {
                    filteredRestaurantList.add(it)
                }
                adapter.notifyDataSetChanged()
            }
        }
        setNavHeader()
    }

    override fun onResume() {
        super.onResume()
        if (!(apiClient.isLoggedIn() || apiClient.isUnregisteredUser()))
            GoToMainActivity()
    }

    fun setNavHeader() = launch(UI) {
        if (apiClient.isLoggedIn()) {
            val customer = apiClient.GetProfile().await()
            customer_name_header.text = customer.firstName + " " + customer.lastName
        } else {
            customer_name_header.text = "Niezarejestrowany użytkowniku"
        }
    }

    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
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
        menuInflater.inflate(R.menu.browse, menu)
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

    private fun GoToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
