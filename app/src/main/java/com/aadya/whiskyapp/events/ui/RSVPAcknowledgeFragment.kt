package com.aadya.whiskyapp.events.ui

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.FragmentRSVPAcknowledgeBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RSVPAcknowledgeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mBinding: FragmentRSVPAcknowledgeBinding
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