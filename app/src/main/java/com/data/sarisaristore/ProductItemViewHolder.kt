package com.data.sarisaristore

import android.text.Html
import androidx.recyclerview.widget.RecyclerView
import com.data.sarisaristore.databinding.ProductItemLayoutBinding

class ProductItemViewHolder(val binding:ProductItemLayoutBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(product:Product){
        val varCost = String.format("%.2f",product.prod_cost)
        val varQuantity = "<b>QTY</b><br/><br/><b><font color='#1028AA'>"+
        "<big><big><big>${product.prod_quantity}<big></big></font></b>"
        val varUnitAndCost = "<b>UNIT:</b> ${product.prod_unit}\t\t"+
                "<b>PRICE:</b> <font color='#21DD29'>$varCost</font>"
        binding.tvItemName.text = product.prod_name
        binding.tvItemDescription.text = product.prod_description
        binding.tvQuantity.setText(Html.fromHtml(varQuantity))
        binding.tvUnitAndCost.setText(Html.fromHtml(varUnitAndCost))
    }
}