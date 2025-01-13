package com.klavs.football.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.klavs.football.data.entity.Profile

@Database(entities = [Profile::class], version = 1)
@TypeConverters(Converters::class)
abstract class ProfilesDatabase: RoomDatabase() {
    abstract fun profilesDao(): ProfilesDao

    companion object{
        private var INSTANCE: ProfilesDatabase? = null

        fun getDatabase(context: Context):ProfilesDatabase?{
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context, ProfilesDatabase::class.java,
                        name = "profiles"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

}