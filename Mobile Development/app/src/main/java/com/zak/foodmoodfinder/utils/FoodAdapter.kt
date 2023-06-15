package com.zak.foodmoodfinder.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.zak.foodmoodfinder.R
import com.zak.foodmoodfinder.response.FoodsItem

class FoodAdapter(private val listFood: ArrayList<FoodsItem>) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFood : ImageView = itemView.findViewById(R.id.iv_food_image)
        val tvFoodName : TextView = itemView.findViewById(R.id.tv_food_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
         FoodViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false))


    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.tvFoodName.text = listFood[position].name
        holder.ivFood.load(listFood[position].photo) {
            placeholder(R.drawable.fmf_logo)
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listFood[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listFood.size

    interface OnItemClickCallback {
        fun onItemClicked(data: FoodsItem)
    }

}