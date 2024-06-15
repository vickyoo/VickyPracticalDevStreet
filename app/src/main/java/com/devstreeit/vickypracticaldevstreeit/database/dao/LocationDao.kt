package com.devstreeit.vickypracticaldevstreeit.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devstreeit.vickypracticaldevstreeit.database.model.PlaceEntity
import com.devstreeit.vickypracticaldevstreeit.utility.dbLocation

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: PlaceEntity)

    @Delete
    fun delete(model: PlaceEntity) : Int

    @Update
    fun update(placeEntity: PlaceEntity) : Int

    @Query("SELECT * FROM $dbLocation")
    fun getAllLocation(): LiveData<List<PlaceEntity>>

    @Query("Select * from $dbLocation")
    fun checkList(): List<PlaceEntity>
}