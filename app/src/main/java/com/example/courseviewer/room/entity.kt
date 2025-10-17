package com.example.courseviewer.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course")
data class CourseEntity(
    val number: Int,
    val department: String,
    val location: String,
    @PrimaryKey(autoGenerate = true) val id:Int=0
)