package com.aadya.whiskyapp.dashboard.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.clublocation.ui.LocationFragment
import com.aadya.whiskyapp.databinding.ActivityDashBoardBinding
import com.aadya.whiskyapp.databinding.AppContentBinding
import com.aadya.whiskyapp.events.ui.EventsLaunchFragment
import com.aadya.whiskyapp.menu.MenuFragment
import com.aadya.whiskyapp.profile.ui.SecretCodeFragment
import com.aadya.whiskyapp.reserve.ui.ReserveFragment
import com.aadya.whiskyapp.specialoffers.ui.SpecialOfferViewPagerFragment
import com.aadya.whiskyapp.utils.BottomNavigationInterface
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.stripe.android.model.CardParams
import com.stripe.android.view.CardMultilineWidget
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*


class DashBoardActivity : AppCompatActivity() ,DrawerInterface,
    BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationInterface {
    private lateinit var mBinding: ActivityDashBoardBinding
    private lateinit var mIncludedLayoutBinding: AppContentBinding
    private lateinit var mSessionManager : SessionManager
    private lateinit var mCardMultilineWidget : CardMultilineWidget
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // showBottomSheetDialog()
        intializeMembers()
        setupDrawerLayout()
        setBottomNavigation()
        handleClickListner()
       // throw  RuntimeException("This is a crash")
        launchFragment(SecretCodeFragment.newInstance(), "SecretCodeFragment")

        mSessionManager= SessionManager.getInstance(this)!!

    }

//    private fun showBottomSheetDialog() {
//        val bottomSheetDialog = BottomSheetDialog(this)
//        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)
//        val payButton = bottomSheetDialog.findViewById<Button>(R.id.payButton)
//
//        payButton!!.setOnClickListener {
//            mCardMultilineWidget= CardMultilineWidget(this,null,0,false)
//            val cardParams: CardParams? = mCardMultilineWidget.cardParams
//            mSessionManager.setCardDetail("","","")
//            //bottomSheetDialog.dismiss()
//        }
//        bottomSheetDialog.show()
//    }

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
        } else
            super.onBackPressed()
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


}