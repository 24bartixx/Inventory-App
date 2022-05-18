package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO is to hide all the complexities involved in performing the database operations in the underlying persistence level from the rest
 * of the application
 *
 * We need following queries:
 * - insert new item
 * - update an existing item to update name, price, and quantity
 * - get a specific item based on its primary key
 * - get all items to display them
 * - delete an entry (row)
 */

@Dao
interface ItemDao {

    /** insert a new item */
    // onConflict argument in case of a conflict
    // to know more about available conflict strategies - documentation: https://developer.android.com/reference/androidx/room/OnConflictStrategy.html
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    // suspend means that the function can be called from a coroutine
    suspend fun insert(item: Item)

    /** update an existing item to update name, price, and quantity */
    @Update
    suspend fun update(item: Item)

    /** delete an entry */
    @Delete
    suspend fun delete(item: Item)

    /** get a specific item based on its primary key */
    @Query("SELECT * FROM item WHERE id = :id")
    // Using Flow or LiveData notifies whenever the data in the database changes
    // However, it is recommended to use Flow in the persistence layer
    // Room keeps this Flow updated, so we need to explicitly get the data once
    // Because of Flow return type, Room runs the query on the background thread
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * FROM item ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>

}