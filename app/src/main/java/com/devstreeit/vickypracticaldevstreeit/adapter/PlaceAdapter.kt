package com.devstreeit.vickypracticaldevstreeit.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.devstreeit.vickypracticaldevstreeit.database.model.PlaceEntity
import com.devstreeit.vickypracticaldevstreeit.databinding.ItemPlaceListBinding

class LocationAdapter(
    var list: ArrayList<PlaceEntity>,
    private var originalOrderList: List<PlaceEntity>,
    val listenerEdit: onClickEditListener,
    val listenerDelete: onClickDeleteListener
) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPlaceListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: PlaceEntity) {
            binding.txtPlaceName.text = model.name
            binding.txtPlaceAddress.text = model.address
            binding.txtDistance.text = "Distance: %.2f KM".format(model.distance)
            if (model.distance == 0.0) {
                binding.txtDistance.isGone = true
            }
            else {
                binding.txtDistance.isVisible = true
            }

            binding.imgDelete.setOnClickListener {
                listenerDelete.onClickDelete(list[layoutPosition])
            }

            binding.imgEdit.setOnClickListener {
                listenerEdit.onClickEdit(list[layoutPosition])
            }
            if (model.primary) {
                binding.txtPrimary.isVisible = true
            } else {
                binding.txtPrimary.isGone = true
            }
        }
    }

    fun updateList(newList: ArrayList<PlaceEntity>) {
        list = newList
        notifyDataSetChanged()
    }

    fun updateOriginalList(newList: ArrayList<PlaceEntity>) {
        originalOrderList = newList
        notifyDataSetChanged()
    }

    fun sortByAscending() {
        list.sortBy { it.distance }
        notifyDataSetChanged()
    }

    fun sortByDescending() {
        list.sortByDescending { it.distance }
        notifyDataSetChanged()
    }

    fun clearSort() {
        list.clear()
        list.addAll(originalOrderList)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPlaceListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

interface onClickEditListener {
    fun onClickEdit(model: PlaceEntity)
}

interface onClickDeleteListener {
    fun onClickDelete(model: PlaceEntity)
}
