package com.aadya.gist.navigation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aadya.gist.navigation.adapter.NavigationMenuAdapter.MyViewHolder
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.AdapterNavigationMenuBinding
import java.util.*

class NavigationMenuAdapter(
    context: Context,
    navigationMenus: ArrayList<String>?,
    navigationMenuListener: NavigationMenuListener
) : RecyclerView.Adapter<MyViewHolder>() {

    private val navigationMenus: ArrayList<String> = ArrayList<String>()
    private val context: Context
    private val navigationMenuListener: NavigationMenuListener


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {

        val itemView: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_navigation_menu, viewGroup, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        with(viewHolder) {
           binding.menuText.text = navigationMenus[i]
            binding.menuMainLayout.setOnClickListener {
                navigationMenuListener.onItemClick(
                    i
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return navigationMenus.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface NavigationMenuListener {
        //  fun onItemClick(id: Int)
        fun onItemClick(item: Int?)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = AdapterNavigationMenuBinding.bind(itemView)
    }

    init {
        this.navigationMenus.addAll(navigationMenus!!)
        this.context = context
        this.navigationMenuListener = navigationMenuListener
    }




}