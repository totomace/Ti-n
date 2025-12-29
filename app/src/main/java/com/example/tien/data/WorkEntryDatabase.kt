package com.example.tien.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tien.data.dao.WorkEntryDao
import com.example.tien.data.entity.WorkEntryEntity

@Database(entities = [WorkEntryEntity::class], version = 1, exportSchema = false)
abstract class WorkEntryDatabase : RoomDatabase() {
    abstract fun workEntryDao(): WorkEntryDao

    companion object {
        @Volatile private var INSTANCE: WorkEntryDatabase? = null

        fun getInstance(context: Context): WorkEntryDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    WorkEntryDatabase::class.java,
                    "work_entry_db"
                ).build().also { INSTANCE = it }
            }
    }
}
