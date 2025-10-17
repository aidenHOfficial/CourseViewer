package com.example.courseviewer.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse (course: CourseEntity)

    @Delete()
    suspend fun removeCourse (course: CourseEntity)

    @Query("select * from course order by id desc")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM course WHERE number = :number AND department = :department AND location = :location LIMIT 1")
    suspend fun getCourse(number: Int, department: String, location: String): CourseEntity?
}