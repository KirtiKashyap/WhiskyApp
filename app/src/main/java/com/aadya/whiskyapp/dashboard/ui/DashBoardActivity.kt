package com.aadya.whiskyapp.dashboard.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.clublocation.ui.LocationFragment
import com.aadya.whiskyapp.dashboard.model.LogoutRequest
import com.aadya.whiskyapp.databinding.ActivityDashBoardBinding
import com.aadya.whiskyapp.databinding.AppContentBinding
import com.aadya.whiskyapp.events.ui.EventsLaunchFragment
import com.aadya.whiskyapp.landing.ui.LandingActivity
import com.aadya.whiskyapp.menu.MenuFragment
import com.aadya.whiskyapp.profile.ui.ProfileDetailFragment
import com.aadya.whiskyapp.profile.ui.ProfileEditFragment
import com.aadya.whiskyapp.profile.ui.SecretCodeFragment
import com.aadya.whiskyapp.purchasehistory.ui.PurchaseHistoryFragment
import com.aadya.whiskyapp.reserve.ui.ReserveFragment
import com.aadya.whiskyapp.retrofit.APICallService
import com.aadya.whiskyapp.retrofit.APIClient
import com.aadya.whiskyapp.specialoffers.ui.SpecialOfferViewPagerFragment
import com.aadya.whiskyapp.utils.BottomNavigationInterface
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stripe.android.view.CardMultilineWidget
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashBoardActivity : AppCompatActivity() ,DrawerInterface,
    BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationInterface {
    private lateinit var mBinding: ActivityDashBoardBinding
    private lateinit var mIncludedLayoutBinding: AppContentBinding
    private lateinit var mSessionManager : SessionManager
    private lateinit var mCardMultilineWidget : CardMultilineWidget
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intializeMembers()
        setupDrawerLayout()
        setBottomNavigation()
        handleClickListner()
       // throw  RuntimeException("This is a crash")
        launchFragment(SecretCodeFragment.newInstance(), "SecretCodeFragment")

        mSessionManager= SessionManager.getInstance(this)!!

    }

    private fun handleClickListner() {
        mBinding.closeNavImageView.setOnClickListener{
            mBinding.drawerLayout.closeDrawers()
        }

        mBinding.rlMenu.setOnClickListener {
            mIncludedLayoutBinding.bottomNavigationMenu.menu.getItem(0).isChecked = true
            mBinding.drawerLayout.closeDrawers()
            callFragment(0)
        }

        mBinding.rlId.setOnClickListener {
            mIncludedLayoutBinding.bottomNavigationMenu.menu.getItem(1).isChecked = true
            mBinding.drawerLayout.closeDrawers()
            callFragment(1)
        }
        mBinding.rlEvents.setOnClickListener {
            mIncludedLayoutBinding.bottomNavigationMenu.menu.getItem(2).isChecked = true
            mBinding.drawerLayout.closeDrawers()
            callFragment(2)
        }

        mBinding.rlVip.setOnClickListener {
            mIncludedLayoutBinding.bottomNavigationMenu.menu.getItem(3).isChecked = true
            mBinding.drawerLayout.closeDrawers()
            callFragment(3)
        }

        mBinding.rlWhereIsDisPlace.setOnClickListener {
            mBinding.drawerLayout.closeDrawers()
            callFragment(4)
        }
        mBinding.rlPurchaseHistory.setOnClickListener {
            mBinding.drawerLayout.closeDrawers()
            callFragment(5)
        }
        mBinding.rlLogout.setOnClickListener {
            mBinding.drawerLayout.closeDrawers()
            lifecycleScope.launch{
                LoginHelper.logout(mSessionManager).join()
                val intent = Intent(this@DashBoardActivity, LandingActivity::class.java)
                        startActivity(intent)
                        finish()
            }
        }
    }



    private fun setBottomNavigation() {
        mIncludedLayoutBinding.bottomNavigationMenu.setOnNavigationItemSelectedListener(this)
        mIncludedLayoutBinding.bottomNavigationMenu.menu.getItem(1).isChecked = true
    }

    private fun setupDrawerLayout() {
        val toggle = ActionBarDrawerToggle(
            this,
            mBinding.drawerLayout,
            mIncludedLayoutBinding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toggle.drawerArrowDrawable.color = Color.WHITE
        mBinding.drawerLayout.addDrawerListener(toggle)
        mBinding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(view: View, v: Float) {}
            override fun onDrawerOpened(view: View) {

            }

            override fun onDrawerClosed(view: View) {
            }

            override fun onDrawerStateChanged(i: Int) {}
        })
        toggle.syncState()


    }


    private fun intializeMembers() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board)
        mIncludedLayoutBinding = mBinding.appContentLayout1
        val versionName: String = packageManager
            .getPackageInfo(packageName, 0).versionName
        mBinding.versionTextView.text=versionName

    }


    private fun callFragment(navigationItem: Int?) {
        when(navigationItem){
            0 -> {
                launchFragment(
                    MenuFragment.newInstance(),
                    "MenuFragment"
                )
            }

            1 -> {
                launchFragment(
                    SecretCodeFragment.newInstance(),
                    "SecretCodeFragment"
                )
            }

            2 -> {
                launchFragment(
                    EventsLaunchFragment.newInstance(),
                    "EventsLaunchFragment"
                )
            }


            3 -> {
                launchFragment(
                    SpecialOfferViewPagerFragment.newInstance(),
                    "SpecialOfferViewPagerFragment"
                )
            }
            4 -> {
                launchFragment(
                    LocationFragment.newInstance(),
                    "LocationFragment"
                )
            }
            5 -> {
                launchFragment(
                    PurchaseHistoryFragment.newInstance(),
                    "PurchaseHistoryFragment"
                )
            }
        }

    }

    override fun setOnDrwawerClickResult() {
        mBinding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        CommonUtils.LogMessage("MenuItem launchFragment")
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.app_container, fragment, tag)
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onBackPressed() {
        val mFragmentChange = supportFragmentManager.findFragmentById(R.id.app_container)

        if (mFragmentChange is SecretCodeFragment || mFragmentChange is EventsLaunchFragment || mFragmentChange is SpecialOfferViewPagerFragment || mFragmentChange is ReserveFragment || mFragmentChange is MenuFragment ) {
            finish()
        } else if(supportFragmentManager.findFragmentByTag("ProfileDetailFragment") is ProfileDetailFragment){
            supportFragmentManager.popBackStack()
        }else if(supportFragmentManager.findFragmentByTag("ProfileEditFragment") is ProfileEditFragment){
            supportFragmentManager.popBackStack()
            val ft = supportFragmentManager?.beginTransaction()
            ft?.setCustomAnimations(
                R.anim.slide_in_bottom,
                R.anim.slide_out_top,
                R.anim.slide_in_top,
                R.anim.slide_out_bottom
            )
            ft?.replace(
                R.id.app_container,
                ProfileDetailFragment.newInstance(),
                "ProfileDetailFragment"
            )
            ft?.addToBackStack(null)
            ft?.commit()
        }else{
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("TAG", "MenuItem: $item")

        when (item.itemId) {
            R.id.navigation_Events -> {
                launchFragment(
                    EventsLaunchFragment.newInstance(),
                    "EventsLaunchFragment"
                )
                return true
            }

            R.id.navigation_vip -> {
                launchFragment(
                    SpecialOfferViewPagerFragment.newInstance(),
                    "SpecialOfferViewPagerFragment"
                )
                return true
            }

            R.id.navigation_ID -> {
                launchFragment(
                    SecretCodeFragment.newInstance(),
                    "SecretCodeFragment"
                )
                return true
            }

            R.id.navigation_reserve -> {
                launchFragment(
                    ReserveFragment.newInstance(),
                    "ReserveFragment"
                )
                return true
            }

            R.id.navigation_menu -> {
                launchFragment(
                    MenuFragment.newInstance(),
                    "MenuFragment"
                )
                return true
            }
        }
        return false
    }

    override fun setOnBottomNavigationResult() {
        mIncludedLayoutBinding.bottomNavigationMenu.menu.setGroupCheckable(0, true, true)
        mIncludedLayoutBinding.bottomNavigationMenu.selectedItemId = R.id.navigation_menu
    }
    object LoginHelper {
        private val scope = CoroutineScope(SupervisorJob() + CoroutineName("LoginHelper"))

        fun logout(mSessionManager: SessionManager): Job = scope.launch {
            Log.d("","logout")
            val toTypedArray = mSessionManager.getAuthorization()?.split(" ")!!.toTypedArray()
            val logoutRequest = LogoutRequest()
            logoutRequest.AccessToken=toTypedArray[1]
            deleteSession(mSessionManager.getAuthorization(),logoutRequest)
        }

        private suspend fun deleteSession(authorization: String?, logoutRequest: LogoutRequest,) {

            val service = APIClient.getRetrofitInstance().create(APICallService::class.java)
            val call: Call<Any> = service.getLogout(authorization,logoutRequest)
            call?.enqueue(object : Callback<Any?> {
                override fun onResponse(
                    call: Call<Any?>,
                    response: Response<Any?>
                ) {
                    if (response.isSuccessful) {

                    } else {
                        Log.d("", response.errorBody().toString())
                    }

                }

                override fun onFailure(call: Call<Any?>, t: Throwable) {
                    t.message?.let { Log.d("", it) }
                }
            })

        }
    }

}