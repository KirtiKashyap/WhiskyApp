package com.aadya.whiskyapp.clublocation.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.FragmentLocationBinding
import com.aadya.whiskyapp.databinding.MainHeaderNewBinding
import com.aadya.whiskyapp.profile.ui.ProfileFragment
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


//https://console.cloud.google.com/freetrial/signup/billing/IN?redirectPath=%2Fbilling&project=438854502250
//Draw path between 2 point is not free need billing account
//https://c1ctech.com/android-googlemap-example-to-draw-route-between-two-locations/


class LocationFragment : Fragment() ,OnMapReadyCallback {
    private lateinit var mBinding: FragmentLocationBinding
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private lateinit var mSessionManager: SessionManager
    private lateinit var mCommonUtils : CommonUtils
    private var mDrawerInterface: DrawerInterface? = null
    private var mMap: GoogleMap? = null
   /* Map declaration */
   private var googleMap:GoogleMap?=null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDrawerInterface = context as DrawerInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)
        return mBinding.root
    }

    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_location,
            container,
            false
        )
        mCommonUtils = CommonUtils
        mSessionManager = SessionManager.getInstance(context)!!
        setIncludedLayout()
        val mapFragment : SupportMapFragment = childFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        //Adding markers to map

        val latLng=LatLng(28.6139, 77.2090)
        val markerOptions:MarkerOptions=MarkerOptions().position(latLng).title("New Delhi")

        // moving camera and zoom map

        val zoomLevel = 12.0f //This goes up to 21


        googleMap.let {
            it!!.addMarker(markerOptions)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
        }

    }





    private fun setIncludedLayout() {
        mIncludedLayoutBinding = mBinding.mainHeader
        mIncludedLayoutBinding.imgDrawer.setOnClickListener {
            mDrawerInterface?.setOnDrwawerClickResult()
        }

        mIncludedLayoutBinding.imgLogo.setOnClickListener {

            launchFragment(ProfileFragment.newInstance(), "ProfileFragment")
        }

    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        val ft = activity?.supportFragmentManager?.beginTransaction()
        ft?.replace(R.id.app_container, fragment, tag)
        ft?.addToBackStack(null)
        ft?.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LocationFragment().apply {
            }
    }




}