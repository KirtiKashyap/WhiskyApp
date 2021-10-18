package com.aadya.whiskyapp.profile.ui

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.TransitionInflater
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.FragmentProfileNewBinding
import com.aadya.whiskyapp.databinding.MainHeaderNewBinding
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager
import com.aadya.whiskyapp.utils.onPageSwipeUpListner

class ProfileFragment : Fragment() {

    private lateinit var mBinding: FragmentProfileNewBinding
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private var mDrawerInterface: DrawerInterface? = null
    private lateinit var mCommonUtils: CommonUtils
    private lateinit var mSessionManager: SessionManager
    var onPageSwipeUpListener: onPageSwipeUpListner? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDrawerInterface = context as DrawerInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)
        handleClickListner()
        onPageSwipeUpListener?.getSwipeUpViewState()?.observe(viewLifecycleOwner, Observer {

            val ft = activity?.supportFragmentManager?.beginTransaction()
            ft?.replace(
                R.id.app_container,
                ProfileDetailFragment.newInstance(),
                "ProfileDetailFragment"
            )
            ft?.addToBackStack(null)
            ft?.commit()

        })

        return mBinding.root
    }


    private fun handleClickListner() {
        onPageSwipeUpListener = onPageSwipeUpListner(requireContext(), mBinding.constraintProfile)


    }

    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {


        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile_new,
            container,
            false
        )

        mCommonUtils = CommonUtils
        setIncludedLayout()
        mSessionManager = SessionManager.getInstance(requireContext())!!

        setAgentName()



    }

    private fun setAgentName() {

        var agent_name1 = "STEVEN"
       createDynamicallyTextView(agent_name1)
        val rowTextView = TextView(requireContext())
        // set some properties of rowTextView or something
        rowTextView.text = "O"
        mBinding.tvAgentname.addView(rowTextView)
        rowTextView.background = resources.getDrawable(R.drawable.profiletextview_bg, null)
        rowTextView.setTextColor(resources.getColor(R.color.blanktextviewcolor))
        val lparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT) // Width , height
        rowTextView.gravity = Gravity.CENTER
        rowTextView.layoutParams = lparams
        rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22F)
        rowTextView.setPadding(10, 10, 10, 10)
        rowTextView.setTypeface(rowTextView.typeface, Typeface.BOLD_ITALIC)

        var agent_name2 = "BROWN"
        createDynamicallyTextView(agent_name2)

    }

    private fun createDynamicallyTextView(agentName: String) {
        val myTextViews = arrayOfNulls<TextView>(agentName.length)


        val charsList: List<Char> = agentName.toList()

        for (i in charsList.indices) {
            val rowTextView = TextView(requireContext())
            // set some properties of rowTextView or something
            rowTextView.text = charsList[i].toString()
            // add the textview to the linearlayout
            mBinding.tvAgentname.addView(rowTextView)
            rowTextView.background = resources.getDrawable(R.drawable.agentname_bg, null)
            rowTextView.setTextColor(resources.getColor(R.color.black))
            val lparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT) // Width , height
            rowTextView.gravity = Gravity.CENTER;
            rowTextView.layoutParams = lparams
            rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22F)
            rowTextView.setPadding(10, 10, 10, 10)
            rowTextView.setTypeface(rowTextView.typeface, Typeface.BOLD_ITALIC)
            // save a reference to the textview for later
            myTextViews[i] = rowTextView
        }
    }

    private fun setIncludedLayout() {
        mIncludedLayoutBinding = mBinding.mainHeader
           mIncludedLayoutBinding.imgDrawer.setOnClickListener {
               mDrawerInterface?.setOnDrwawerClickResult()
           }

        mIncludedLayoutBinding.imgLogo.visibility = View.GONE


    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {

            }


    }


}