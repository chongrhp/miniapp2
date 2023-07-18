package com.data.sarisaristore

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.data.sarisaristore.databinding.DialogProductLayoutBinding
import com.data.sarisaristore.databinding.ProductItemLayoutBinding

class ProductAdapter(private val products:List<Product>):RecyclerView.Adapter<ProductItemViewHolder>() {
    var onDeleteClick : ((Product) -> Unit)?=null
    var onUpdateClick : ((Product) -> Unit)?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductItemLayoutBinding.inflate(inflater, parent, false)
        return ProductItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        holder.bind(products[position])
        holder.binding.apply {
            btnDelete.setOnClickListener {
                onDeleteClick?.invoke(products[position])
            }

            btnUpdate.setOnClickListener {
                onUpdateClick?.invoke(products[position])
            }
        }

        holder.itemView.setOnClickListener {
            showDialog(holder, position)
        }
    }

    private fun showDialog(holder:ProductItemViewHolder, position:Int){
        val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
        val dialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.dialog_product_layout,null)
        val dialogBinding = DialogProductLayoutBinding.bind(dialogView)
        val btnUpdate = dialogBinding.btnAddUpdate
        val btnClose = dialogBinding.btnCancel

        dialogBinding.edQuantity.setText(products[position].prod_quantity.toString())
        dialogBinding.edProdTitle.setText(products[position].prod_name)
        dialogBinding.edProdDescription.setText(products[position].prod_description)
        dialogBinding.edUnit.setText(products[position].prod_unit)
        dialogBinding.edUnitCost.setText(products[position].prod_cost.toString())

        btnUpdate.isVisible = false
        btnClose.setText("Close")
        alertDialogBuilder.setView(dialogView)
        val showDial = alertDialogBuilder.create()

        btnClose.setOnClickListener {
            showDial.dismiss()
        }

        showDial.show()
    }
}