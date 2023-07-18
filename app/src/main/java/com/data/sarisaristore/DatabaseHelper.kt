package com.data.sarisaristore

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DatabaseHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        val DATABASE_NAME = "inventory.db"
        val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE users (
            user_id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT,
            email_add TEXT,
            user_name TEXT,
            user_pass TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE products(
            prod_id INTEGER PRIMARY KEY AUTOINCREMENT,
            prod_name TEXT,
            prod_description TEXT,
            prod_quantity INTEGER,
            prod_unit TEXT,
            prod_cost DOUBLE
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS products")
        onCreate(db)
    }

    //users database helper
    fun insertUser(user:User){
        val db = writableDatabase
        val sql = "INSERT INTO users (name, email_add, user_name, user_pass) VALUES (?,?,?,?)"
        val args = arrayOf(user.name, user.email_add, user.user_name, user.user_pass)
        db.execSQL(sql, args)
    }

    
    fun loginUser(username:String, password:String):Boolean{
        val db = readableDatabase
        val args = arrayOf(username, password)
        val cursor = db.rawQuery("SELECT * FROM users WHERE user_name = ? AND user_pass = ?", args)
        var found = false

        if (cursor.count > 0) found = true
        return found

        cursor.close()
        db.close()
    }

    fun ifExists(username:String):Boolean{
        val db = readableDatabase
        val args = arrayOf(username)
        val cursor = db.rawQuery("SELECT * FROM users WHERE user_name LIKE ?", args)
        var found = false

        if (cursor.count > 0) found = true
        return found

        cursor.close()
        db.close()
    }

    fun updateUser(user: User){
        val db = writableDatabase
        val updateQuery = "UPDATE users SET name = '${user.name}', email_add = '${user.email_add}', " +
                "user_name = '${user.user_name}', user_pass = '${user.user_pass}' WHERE user_id = ${user.user_id}"
        db.execSQL(updateQuery)
    }

    fun deleteUser(id: Int){
        val db = writableDatabase
        val deleteQuery  = "DELETE FROM users WHERE user_id = $id"
        db.execSQL(deleteQuery)
    }

    fun getAllUsers():MutableList<User>{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users", null)
        val userList = mutableListOf<User>()

        while(cursor.moveToNext()){
            val userId = cursor.getInt(0)
            val name = cursor.getString(1)
            val email = cursor.getString(2)
            val userName = cursor.getString(3)
            val userPass = cursor.getString(4)
            val newUser = User(userId, name, email, userName, userPass)
            userList.add(newUser)
        }

        cursor.close()
        return userList
    }

    // products database helper
    fun insertProduct(prod: Product){
        val db = writableDatabase
        val sql = "INSERT INTO products (prod_name, prod_description, prod_quantity, prod_unit, prod_cost) VALUES (?,?,?,?,?)"
        val args = arrayOf(prod.prod_name, prod.prod_description, prod.prod_quantity, prod.prod_unit, prod.prod_cost)
        db.execSQL(sql, args)
    }

    fun deleteProduct(id: Int){
        val db = writableDatabase
        val deleteQuery  = "DELETE FROM products WHERE prod_id = $id"
        db.execSQL(deleteQuery)
    }


    fun updateProduct(prod: Product){
        val db = writableDatabase
        val updateQuery = "UPDATE products SET prod_name = '${prod.prod_name}', prod_description = '${prod.prod_description}', " +
                "prod_quantity = ${prod.prod_quantity}, prod_unit = '${prod.prod_unit}', prod_cost = ${prod.prod_cost} WHERE prod_id = ${prod.prod_id}"
        db.execSQL(updateQuery)
    }



    fun findProduct(name:String):MutableList<Product>{
        val db = readableDatabase
        val args = arrayOf(name)
        val cursor = db.rawQuery("SELECT * FROM products WHERE prod_name LIKE ?",args)
        val prodList = mutableListOf<Product>()

        if (cursor.count > 0){
            while(cursor.moveToNext()){
                val prodId = cursor.getInt(0)
                val prodName = cursor.getString(1)
                val prodDescription = cursor.getString(2)
                val prodQuantity = cursor.getInt(3)
                val prodUnit = cursor.getString(4)
                val prodCost = cursor.getDouble(5)
                var newProd = Product(prodId, prodName, prodDescription, prodQuantity, prodUnit, prodCost)
                prodList.add(newProd)
            }
        }

        cursor.close()
        return prodList
    }

    fun lastProductInserted():Int{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM products ORDER BY prod_id DESC LIMIT 1",null)
        val prodId = 0
        if (cursor.count > 0){
            cursor.moveToFirst()
            cursor.getInt(0)
        }
        cursor.close()
        return prodId
    }

    fun getAllProducts():MutableList<Product>{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM products",null)
        val prodList = mutableListOf<Product>()

        while(cursor.moveToNext()){
            val prodId = cursor.getInt(0)
            val prodName = cursor.getString(1)
            val prodDescription = cursor.getString(2)
            val prodQuantity = cursor.getInt(3)
            val prodUnit = cursor.getString(4)
            val prodCost = cursor.getDouble(5)
            var newProd = Product(prodId, prodName, prodDescription, prodQuantity, prodUnit, prodCost)
            prodList.add(newProd)
        }

        cursor.close()
        return prodList
    }


}