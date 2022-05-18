package com.example.inventory

import androidx.lifecycle.*
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    // creating a list of Item objects
    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    // inserting an item to the database thought DAO
    private fun insertItem(item: Item) {
        // the ViewModelScope is an extension property to the ViewModel class that automatically cancels its child coroutines when the ViewModel
        // is destroyed
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    // function that takes in three strings and returns an Item instance
    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item( itemName = itemName, itemPrice = itemPrice.toDouble(), quantityInStock = itemCount.toInt() )
    }

    // public class that creates instance of an entity in a database
    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName,itemPrice,itemCount)
        insertItem(newItem)
    }

    // verify user input before adding or updating he entity in the database (it might be empty)
    fun inEntryValid(itemName: String, itemPrice:String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    // retrieve data of an item based on the id
    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

    // function to modify / update an entity
    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    // function called by a fragment to sell item
    fun sellItem(item: Item) {
        if(item.quantityInStock > 0) {
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }

    // function to decide whether the sell button in the UI is enabled
    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }

    // function to delete the specific item in the database
    fun deleteItem(item:Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    // function to get updated item entry
    private fun getUpdatedItemEntry ( itemId: Int, itemName: String, itemPrice: String, itemCount: String ): Item {
        return Item(id = itemId, itemName = itemName, itemPrice = itemPrice.toDouble(), quantityInStock = itemCount.toInt())
    }

    // functions to update item
    fun updateItem(itemId: Int, itemName: String, itemPrice: String, itemCount: String) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }

}

// a class to instantiate view model - factory class
class InventoryViewModelFactory(private val itemDao: ItemDao): ViewModelProvider.Factory {

    // function takes any class type as an argument and returns a ViewModel object
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        /** boilerplate code */

        // check if the model Class is the same as the InventoryViewModel class
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            // return instance of it
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}