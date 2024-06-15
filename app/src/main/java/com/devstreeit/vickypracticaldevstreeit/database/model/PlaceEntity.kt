package com.devstreeit.vickypracticaldevstreeit.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devstreeit.vickypracticaldevstreeit.utility.dbLocation
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = dbLocation)
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var name: String,
    var address: String,
    var latitude: Double,
    var longitude: Double,
    var primary : Boolean,
    var distance: Double = 0.0
) : Parcelable