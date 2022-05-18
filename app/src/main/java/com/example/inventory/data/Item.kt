package com.example.inventory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

/**
 * entity / model class
 * defines a table
 *
 * for data classes the compiler automatically generates utilities for comparing, printing and copying such as toString(), copy(), and equals()
 * a data class should have at least one parameter
 * all parameters need to be marked as val or var
 * data classes cannot be abstract, open, sealed or inner
 */

@Entity(tableName="item")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name="name") val itemName: String,
    @ColumnInfo(name="price") val itemPrice: Double,
    @ColumnInfo(name="quantity") val quantityInStock:Int
)

/** get formatted price using a extension function */
fun Item.getFormattedPrice(): String = NumberFormat.getCurrencyInstance().format(itemPrice)

