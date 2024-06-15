package com.devstreeit.vickypracticaldevstreeit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devstreeit.vickypracticaldevstreeit.database.model.PlaceEntity
import com.devstreeit.vickypracticaldevstreeit.database.repo.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    suspend fun insertLocation(placeEntity: PlaceEntity) {
        locationRepository.insertLocation(placeEntity)
    }

    fun getAllLocations(): LiveData<List<PlaceEntity>> {
        return locationRepository.getAllLocations()
    }

    suspend fun deleteLocation(placeEntity: PlaceEntity){
        locationRepository.deleteLocation(placeEntity)
    }

    fun updateLocation(placeEntity: PlaceEntity) {
        viewModelScope.launch {
            locationRepository.updateLocation(placeEntity)
        }
    }
}