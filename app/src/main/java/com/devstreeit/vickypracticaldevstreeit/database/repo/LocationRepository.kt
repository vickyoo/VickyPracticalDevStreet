package com.devstreeit.vickypracticaldevstreeit.database.repo

import androidx.lifecycle.LiveData
import com.devstreeit.vickypracticaldevstreeit.database.dao.LocationDao
import com.devstreeit.vickypracticaldevstreeit.database.model.PlaceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepository @Inject constructor(private val locationDao: LocationDao) {

    suspend fun insertLocation(placeEntity: PlaceEntity) {
        withContext(Dispatchers.IO) {
            locationDao.insert(placeEntity)
        }
    }

    fun getAllLocations(): LiveData<List<PlaceEntity>> {
        return locationDao.getAllLocation()
    }

    fun checkList(): List<PlaceEntity> {
        return locationDao.checkList()
    }



    suspend fun deleteLocation(placeEntity: PlaceEntity){
        withContext(Dispatchers.IO) {
            locationDao.delete(placeEntity)
        }
    }

    suspend fun updateLocation(placeEntity: PlaceEntity) {
        withContext(Dispatchers.IO) {
            locationDao.update(placeEntity)
        }
    }

}