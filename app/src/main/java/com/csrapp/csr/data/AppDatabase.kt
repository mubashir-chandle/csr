package com.csrapp.csr.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StreamEntity::class, JobEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun streamDao(): StreamDao
    abstract fun jobDao(): JobDao

    companion object {
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context) = instance
            ?: buildDatabase(context)

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "csr_data.db"
            )
            .allowMainThreadQueries()
            .createFromAsset("database/csr_data.db")
            .build()
    }
}