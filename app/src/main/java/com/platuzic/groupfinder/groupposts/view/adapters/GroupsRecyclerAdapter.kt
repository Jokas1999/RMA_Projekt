package com.platuzic.groupfinder.groupposts.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.platuzic.groupfinder.database.models.Group
import com.platuzic.groupfinder.databinding.ItemGroupBinding
import com.platuzic.groupfinder.utils.OnItemClick

class GroupsRecyclerAdapter(
    private val groups: List<Group>,
    private val clickListener: OnItemClick
    ) : RecyclerView.Adapter<GroupsRecyclerAdapter.GroupsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolder =
        GroupsViewHolder(
            ItemGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: GroupsViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount(): Int = groups.size

    inner class GroupsViewHolder(private val binding: ItemGroupBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(group: Group){
            binding.group = group
            binding.viewHolder = this@GroupsViewHolder
        }

        fun cardClicked(group: Group){
            clickListener.onClick(group)
        }
    }
}