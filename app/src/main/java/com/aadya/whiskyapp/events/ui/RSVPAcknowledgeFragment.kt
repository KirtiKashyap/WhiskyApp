package com.aadya.whiskyapp.events.ui

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.FragmentRSVPAcknowledgeBinding
import com.aadya.whiskyapp.databinding.MainHeaderNewBinding
import com.aadya.whiskyapp.profile.ui.ProfileFragment
import com.aadya.whiskyapp.utils.DrawerInterface

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RSVPAcknowledgeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var mDrawerInterface: DrawerInterface? = null
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private lateinit var mBinding: FragmentRSVPAcknowledgeBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDrawerInterface = context as DrawerInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
            R.layout.fragment_r_s_v_p_acknowledge,
            container,
            false
        )
        mBinding.tvMsg1.text = Html.fromHtml(param1)
        mBinding.tvMsg2.text = Html.fromHtml(param2)

        setIncludedLayout()
    }
    private fun setIncludedLayout() {
        mIncludedLayoutBinding = mBinding.mainheader
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
        fun newInstance(param1: String, param2: String) =
            RSVPAcknowledgeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}