package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 *  RoomDatabase object gets entities and DAOs and connects them
 *  the class provides the app with defined instances of DAOs
 *  the RoomDatabase is abstract because Room creates the implementation
 */

// Annotate the class with @Database - in the arguments, list the entities for the database and set the version number
// entities - list of entities, version - increases whenever the schema of the database table changes,
// exportSchema - not keep schema history backups
@Database(entities = [Item::class], version = 1, exportSchema = false)
// Create a public abstract class that extends RoomDatabase
abstract class ItemRoomDatabase: RoomDatabase() {

    // Define an abstract method or property that returns an ItemDao instance and the Room will generate the implementation
    abstract fun itemDao(): ItemDao

    // companion object allows access tot he methods for creating and getting the database using the class name as the qualifier
    companion object{

        // the value of a volatile variable will never be cached, and all writes and reads will be done to and from the main memory
        @Volatile
        // the INSTANCE variable will keep a reference to the database, when one has been created
        // this helps in maintaining a single instance of the database, which is an expensive resource to create and maintain
        private var INSTANCE: ItemRoomDatabase? = null

        fun getDatabase(context: Context): ItemRoomDatabase {

            // synchronized block means that only on thread of execution at a time can enter this block of code
            return INSTANCE ?: synchronized(this) {
                // passing in the application context, the database class, and a name for the database to the Room.databaseBuilder()
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                )
                        // adding required migration strategy to the builder
                        // a migration objects is an object that defines how you take all rows with the old schema and convert them to rows
                        // in the new schema so that no data is lost.
                        // this migration strategy destroys and rebuild database, which means that the data is lost
                    .fallbackToDestructiveMigration()
                        // create database instance
                    .build()

                INSTANCE = instance

                return instance
            }

        }

    }

}