package com.csrapp.csr.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [StreamEntity::class, JobEntity::class, AptitudeQuestionEntity::class, BasePersonalityQuestionEntity::class, StreamQuestionEntity::class, AptitudeCategoryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class CSRDatabase : RoomDatabase() {
    abstract fun streamDao(): StreamDao
    abstract fun jobDao(): JobDao
    abstract fun aptitudeQuestionDao(): AptitudeQuestionDao
    abstract fun streamQuestionDao(): StreamQuestionDao
    abstract fun personalityQuestionDao(): BasePersonalityQuestionDao
    abstract fun aptitudeCategoryDao(): AptitudeCategoryDao

    companion object {
        private var instance: CSRDatabase? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context)
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            CSRDatabase::class.java,
            "csr_data.db"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset("database/csr_data.db")
            .build()
    }
}