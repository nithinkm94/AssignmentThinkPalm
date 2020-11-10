package com.example.thinkpalmassignment

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thinkpalmassignment.data.Items
import kotlinx.android.synthetic.main.item_list.view.*


class ItemListAdapter(private val applicationContext: Context) :
    PagedListAdapter<Items, ItemListAdapter.UserViewHolder>(USER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(applicationContext)
            .inflate(R.layout.item_list, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)
//        val searchText = "bir"
//        var sb : SpannableStringBuilder = SpannableStringBuilder()
//        var index: Int = item?.name?.indexOf(searchText) ?: 0
//        while(index>0) {
//            sb =  SpannableStringBuilder(item?.name)
//            val fcs =
//                ForegroundColorSpan(applicationContext.resources.getColor(R.color.colorRed)) //specify color here
//
//            sb.setSpan(fcs, index, searchText.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//            index = item?.name?.indexOf(searchText, index + 1) ?: 0
//
//        }


        holder.itemView.tv_name.text = item?.name
        val img = item?.image?.split(".")?.get(0)
        Glide.with(holder.itemView.context)
            .load(applicationContext.resources.getIdentifier(img, "drawable", applicationContext.packageName))
            .centerCrop()
            .placeholder(R.drawable.placeholder_for_missing_posters)
            .into(holder.itemView.imageView)
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
        }
    }

    companion object {
        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<Items>() {
            override fun areItemsTheSame(oldItem: Items, newItem: Items): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Items, newItem: Items): Boolean =
                newItem == oldItem
        }
    }
}