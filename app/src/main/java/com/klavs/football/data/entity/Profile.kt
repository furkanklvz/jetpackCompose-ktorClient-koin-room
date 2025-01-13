package com.klavs.football.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity("profiles")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") var id : Int = 0,
    @ColumnInfo("name") var name: String,
    @ColumnInfo("teams") var teams: List<String>
)
