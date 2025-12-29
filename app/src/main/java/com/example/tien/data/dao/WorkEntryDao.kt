package com.example.tien.data.dao

import androidx.room.*
import com.example.tien.data.entity.WorkEntryEntity

@Dao
interface WorkEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: WorkEntryEntity): Long

    @Update
    suspend fun update(entry: WorkEntryEntity)

    @Delete
    suspend fun delete(entry: WorkEntryEntity)

    @Query("SELECT * FROM work_entries ORDER BY date DESC, startTime DESC")
    suspend fun getAll(): List<WorkEntryEntity>

    @Query("SELECT * FROM work_entries WHERE id = :id")
    suspend fun getById(id: Long): WorkEntryEntity?
}
