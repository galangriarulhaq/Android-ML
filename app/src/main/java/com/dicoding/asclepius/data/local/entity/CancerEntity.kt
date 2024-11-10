package com.dicoding.asclepius.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cancers")
class CancerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @field:ColumnInfo(name = "mediaCover")
    var mediaCover: String,
    @field:ColumnInfo(name = "title")
    var title: String,
    @field:ColumnInfo(name = "date")
    var date: String,
    @field:ColumnInfo(name = "inference")
    var inference: String,
    )