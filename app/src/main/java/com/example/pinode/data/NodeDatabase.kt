package com.example.pinode.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Node::class], version = 1, exportSchema = false)
abstract class NodeDatabase : RoomDatabase() {

    abstract fun nodeDao(): NodeDao

    companion object {
        @Volatile
        private var Instance: NodeDatabase? = null

        fun getDatabase(context: Context): NodeDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, NodeDatabase::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}