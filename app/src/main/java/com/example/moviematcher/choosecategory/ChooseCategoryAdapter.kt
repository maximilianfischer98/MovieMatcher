package com.example.moviematcher.choosecategory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviematcher.databinding.CardChooseCategoryBinding


class ChooseCategoryAdapter(private var categorys: ArrayList<ChooseCategoryModel>) :
    RecyclerView.Adapter<ChooseCategoryAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardChooseCategoryBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val category = categorys[holder.adapterPosition]
        holder.bind(category)
    }

    override fun getItemCount(): Int = categorys.size

    class MainHolder(private val binding : CardChooseCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: ChooseCategoryModel) {
            binding.CategoryTitle.text = category.title

        }
    }
}