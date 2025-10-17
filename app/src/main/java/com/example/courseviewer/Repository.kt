package com.example.courseviewer

import com.example.courseviewer.room.CourseDao
import com.example.courseviewer.room.CourseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class Repository (val scope: CoroutineScope, private val dao: CourseDao) {

    val allCourses: Flow<List<CourseEntity?>> = dao.getAllCourses()

    fun addTask(number: Int, department: String, location: String) {
        scope.launch {
            val courseObj = CourseEntity(number, department, location)
            dao.insertCourse(courseObj)
        }
    }

    fun removeTask(number: Int, department: String, location: String) {
        scope.launch {
            val courseObj = dao.getCourse(number, department, location)
            courseObj?.let {
                dao.removeCourse(it)
            }
        }
    }
}