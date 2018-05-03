package to.zaklep.zakleptocustomerclient

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.graphics.drawable.AnimationUtilsCompat
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_browse.*
import kotlinx.android.synthetic.main.app_bar_browse.*
import kotlinx.android.synthetic.main.content_browse.*

class BrowseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var isHiddenPanelShown: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)
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
    }

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
                        var slideDown = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top)
                        hidden_panel.startAnimation(slideDown)
                        hidden_panel.visibility = View.VISIBLE
                        placeForFilter.startAnimation(slideDown)
                        placeForFilter.visibility = View.VISIBLE
                        isHiddenPanelShown = true
                    }
                } else {
                    if (isHiddenPanelShown) {
                        var slideUp = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top)
                        hidden_panel.startAnimation(slideUp)
                        hidden_panel.visibility = View.GONE
                        placeForFilter.startAnimation(slideUp)
                        placeForFilter.visibility = View.GONE
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

            }
            R.id.nav_reservations -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
