package com.ikresimir.weatherilicic.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ikresimir.weatherilicic.model.UserData

@Dao
abstract class LocalDao {


    @Query("SELECT * FROM user_data")
    abstract fun getLocationData(): UserData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLocationData(userData: UserData)

}