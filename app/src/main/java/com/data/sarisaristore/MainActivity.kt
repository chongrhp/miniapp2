package com.data.sarisaristore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.data.sarisaristore.databinding.ActivityMainBinding
import com.data.sarisaristore.databinding.DialogAboutLayoutBinding
import com.data.sarisaristore.databinding.DialogProductLayoutBinding
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var prodList:MutableList<Product>
    private lateinit var adapter:ProductAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBarTop.setOnMenuItemClickListener {
           when(it.itemId){
               R.id.appBarAbout -> {showAbout()}
               R.id.appBarLogout -> {
                   val loginIntent = Intent(this, LoginActivity::class.java)
                   startActivity(loginIntent)
                   finish()
               }
           }
            true
        }


        //object instantiation
        databaseHelper = DatabaseHelper(this)
        //setup the recyclerView
        recyclerView = binding.recyclerView
        //add layout to recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        //declare data
        prodList = getProducts()
        //initialize adapter
        adapter = ProductAdapter(prodList)
        //set adapter of recycler view
        recyclerView.adapter = adapter
        adapter.onDeleteClick = {prod ->
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Delete product")
            alertDialogBuilder.setMessage("You want to delete this product?\n\nName ${prod.prod_name}\nQuantity: ${prod.prod_quantity}")

            alertDialogBuilder.setPositiveButton("Ok") { dialog, _ ->
                deleteProd(prod.prod_id, prod.prod_name)
                prodList.remove(prod)
                adapter.notifyDataSetChanged()
            }

            alertDialogBuilder.setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        adapter.onUpdateClick = {prod ->
            val alertDialogBuilder = AlertDialog.Builder(this)
            val dialogLayout = layoutInflater.inflate(R.layout.dialog_product_layout, null)
            val dialogBinding = DialogProductLayoutBinding.bind(dialogLayout)
            val btnUpdate = dialogBinding.btnAddUpdate
            val btnCancel = dialogBinding.btnCancel
            alertDialogBuilder.setView(dialogLayout)


            dialogBinding.edProdTitle.setText(prod.prod_name)
            dialogBinding.edProdDescription.setText(prod.prod_description)
            dialogBinding.edQuantity.setText(prod.prod_quantity.toString())
            dialogBinding.edUnit.setText(prod.prod_unit)
            dialogBinding.edUnitCost.setText(prod.prod_cost.toString())


            btnUpdate.text = "Update"
            val alertDialog: AlertDialog = alertDialogBuilder.create()

            btnUpdate.setOnClickListener {
                val prodName = dialogBinding.edProdTitle.text.toString()
                val prodDesc = dialogBinding.edProdDescription.text.toString()
                val prodQty = dialogBinding.edQuantity.text.toString().toInt()
                val prodUnit = dialogBinding.edUnit.text.toString()
                val prodUnitCost = dialogBinding.edUnitCost.text.toString().toDouble()
                val newProd = Product(prod.prod_id,prodName, prodDesc, prodQty, prodUnit, prodUnitCost)
                updateProd(newProd)

                val updateNotePosition = prodList.indexOfFirst { it.prod_id == prod.prod_id }
                if(updateNotePosition !== -1){
                    prodList[updateNotePosition] = newProd
                    adapter.notifyItemChanged(updateNotePosition)
                    alertDialog.dismiss()
                }
            }

            btnCancel.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }


        searchView = findViewById(R.id.app_bar_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                prodList = getSearchProd(newText)
                adapter = ProductAdapter(prodList)
                recyclerView.adapter = adapter
                return true
            }
        })


        binding.floatingActionButton.setOnClickListener {
            showAddUpdateDialog()
        }

    }

    private fun showAddUpdateDialog(){
        val alertDialogBuilder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_product_layout,null)
        val dialogBinding = DialogProductLayoutBinding.bind(dialogLayout)
        alertDialogBuilder.setView(dialogLayout)

        val btnAddUpdate = dialogBinding.btnAddUpdate
        val btnCancel = dialogBinding.btnCancel

        btnAddUpdate.setText("Add product")
        val alertDialog: AlertDialog = alertDialogBuilder.create()

        btnAddUpdate.setOnClickListener {
            val prodName = dialogBinding.edProdTitle.text.toString()
            val prodQty = dialogBinding.edQuantity.text.toString().toInt()
            val prodDesc = dialogBinding.edProdDescription.text.toString()
            val prodUnit = dialogBinding.edUnit.text.toString()
            val prodUnitCost = dialogBinding.edUnitCost.text.toString().toDouble()
            val newProd = Product(0, prodName, prodDesc, prodQty, prodUnit, prodUnitCost)
            val prodId = addDProd(newProd)
            newProd.prod_id = prodId
            prodList.add(newProd)

            recyclerView.adapter?.notifyDataSetChanged()
            alertDialog.dismiss()
        }

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun getProducts():MutableList<Product>{
        return databaseHelper.getAllProducts()
    }

    private fun addDProd(prod:Product):Int{
        databaseHelper.insertProduct(prod)
        Toast.makeText(applicationContext,"New product added...",Toast.LENGTH_SHORT).show()
        val prodidNo = databaseHelper.lastProductInserted()
        return prodidNo
    }

    private fun deleteProd(id:Int, prodName:String){
        databaseHelper.deleteProduct(id)
        Toast.makeText(applicationContext,"Product name $prodName deleted...",Toast.LENGTH_SHORT).show()
    }

    private fun updateProd(prod:Product){
        databaseHelper.updateProduct(prod)
        Toast.makeText(applicationContext,"Product name ${prod.prod_name} updated...",Toast.LENGTH_SHORT).show()
    }

    private fun showAbout(){
        val builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_about_layout, null)
        val dialogBinding = DialogAboutLayoutBinding.bind(dialogLayout)
        builder.setView(dialogLayout)
        val btnClose = dialogBinding.btnClose
        val alertDialog: AlertDialog = builder.create()

        btnClose.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun getSearchProd(prod: String):MutableList<Product>{
        return databaseHelper.findProduct("%$prod%")
    }

}