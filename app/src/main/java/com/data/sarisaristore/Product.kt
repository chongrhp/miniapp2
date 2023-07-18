package com.data.sarisaristore

data class Product(
    var prod_id:Int,
    val prod_name:String,
    val prod_description:String,
    val prod_quantity:Int,
    val prod_unit:String,
    val prod_cost:Double)