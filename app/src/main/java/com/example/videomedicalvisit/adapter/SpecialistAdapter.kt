package com.example.videomedicalvisit.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.videomedicalvisit.R
import com.example.videomedicalvisit.databinding.SpecialistItemBinding
import com.example.videomedicalvisit.model.Specialist


class SpecialistAdapter(var context: Context,val Listener: OnCategoryClickListener) : RecyclerView.Adapter<SpecialistAdapter.ViewHolder>() {
    private var specialistList = mutableListOf<Specialist>()
    var row_index = -1


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

    override fun getItemCount(): Int {
        return  specialistList.size
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        with(holder) {
            Listener
            with(specialistList[position]) {
                holder.nameTV.text = this.specialist
                holder.nameTV.setOnClickListener {
                    row_index = position
                    Listener.onCategoryClick(specialistList[row_index],adapterPosition)
                   notifyDataSetChanged()
                }

                Log.d("TAG", "onBindViewHolder: ${row_index}")
                Log.d("TAG", "onBindViewHolder: ${position}")
                if (row_index == position) {
                    nameTV.setTextColor(ContextCompat.getColor(context, R.color.white))
                    nameTV.background.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.primary
                        ), PorterDuff.Mode.SRC_ATOP
                    )
                } else {
                    nameTV.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    nameTV.background.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.otp_color
                        ), PorterDuff.Mode.SRC_ATOP
                    )

                }


            }

        }


    }

    interface OnCategoryClickListener {
        fun onCategoryClick(specialist: Specialist,position: Int)
    }
}