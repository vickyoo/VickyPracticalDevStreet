package com.devstreeit.vickypracticaldevstreeit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devstreeit.vickypracticaldevstreeit.database.dao.LocationDao
import com.devstreeit.vickypracticaldevstreeit.database.model.PlaceEntity

@Database(entities = [PlaceEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile private var instance: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDataBase::class.java, "location_data")
                .fallbackToDestructiveMigration()
                .build()
    }
}