package com.ms.spacecraft.di


import android.content.Context
import androidx.room.Room
import com.ms.spacecraft.db.CommonDatabase
import com.ms.spacecraft.db.SpaceCraftDao
import com.ms.spacecraft.db.StationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CommonDatabaseModule {

    @Provides
    @Singleton
    fun provideSpaceCraftDatabase(@ApplicationContext appContext: Context): CommonDatabase {
        return Room.databaseBuilder(
            appContext,
            CommonDatabase::class.java,
            "SpaceCraftDB"
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }

    @Provides
    fun provideSpaceCraftDao(commonDatabase: CommonDatabase): SpaceCraftDao {
        return commonDatabase.spaceCraftDao()
    }

    @Provides
    fun provideStationDao(commonDatabase: CommonDatabase): StationDao {
        return commonDatabase.stationDao()
    }

}