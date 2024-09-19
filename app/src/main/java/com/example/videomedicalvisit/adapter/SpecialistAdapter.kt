package com.example.videomedicalvisit.adapter

import android.annotation.SuppressLint
import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import com.example.videomedicalvisit.databinding.SpecialistItemBinding

import com.example.videomedicalvisit.model.Specialist


class SpecialistAdapter(var context: Context) : RecyclerView.Adapter<SpecialistAdapter.ViewHolder>() {
    private var specialistList = mutableListOf<Specialist>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(data: MutableList<Specialist>) {
        specialistList  = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: SpecialistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var nameTV = binding.TV



    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SpecialistItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {

        with(holder) {
            with(specialistList[position]) {
                holder.nameTV.text = this.specialist



            }
        }
    }
    override fun getItemCount(): Int {
        return  specialistList.size
    }


}