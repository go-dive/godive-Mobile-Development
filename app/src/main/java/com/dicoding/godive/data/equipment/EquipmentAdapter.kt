package com.dicoding.godive.data.equipment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.godive.R

class EquipmentAdapter(
    private val context: Context,
    private val equipmentList: List<String>,
    private val predictedLabels: List<String>
) : RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_equipment, parent, false)
        return EquipmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipmentViewHolder, position: Int) {
        val equipment = equipmentList[position]
        holder.label.text = equipment
        holder.checkBox.isChecked = predictedLabels.contains(equipment)
    }

    override fun getItemCount(): Int = equipmentList.size

    inner class EquipmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.equipment_label)
        val checkBox: CheckBox = view.findViewById(R.id.equipment_checkbox)
    }
}
