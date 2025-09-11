package com.example.courseviewer

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Course(
    val number: Int,
    val department: String,
    val location: String
)

class CourseViewModel : ViewModel() {
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    fun addCourse(course: Course) {
        _courses.value = _courses.value + course
    }

    fun removeCourse(course: Course) {
        _courses.value = _courses.value - course
    }
}

class CourseViewInputs : ViewModel() {
    var courseInput by mutableStateOf("")
        private set
    var courseNumberInput by mutableStateOf("")
        private set
    var courseLocation by mutableStateOf("")
        private set

    fun updateCourseInput(newValue: String) {
        courseInput = newValue
    }

    fun updateCourseNumberInput(newValue: String) {
        courseNumberInput = newValue
    }

    fun updateCourseLocation(newValue: String) {
        courseLocation = newValue
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val courseViewModel: CourseViewModel = viewModel()
            CourseList(courseViewModel)
        }
    }
}

fun isValidCourse(courseNumber: String, courseDepartment: String, courseLocation: String): Boolean {
    return courseDepartment != "" && courseNumber != "" && courseLocation != "" && courseNumber.toInt() != 0
}

fun handleAddCourse(courseNumber: String, courseDepartment: String, courseLocation: String, viewModel: CourseViewModel): Boolean {
    if (isValidCourse(courseNumber, courseDepartment, courseLocation)){
        viewModel.addCourse(Course(courseNumber.toInt(), courseDepartment, courseLocation))
        return true
    }
    return false
}

@Composable
fun CourseItem(course: Course, myViewModel: CourseViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(245, 141, 22))
            .border(
                width = 2.dp,
                color = Color(245, 107, 22),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { expanded = !expanded }
            .padding(10.dp)
            .fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    )
    {
        if (expanded) {
            Column {
                Text(
                    text = "Department: ${course.department}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Number: ${course.number}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Location: ${course.location}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = {
                        myViewModel.removeCourse(course)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(52, 140, 235), // background
                        contentColor = Color.Black           // text/icon color
                    )
                ) {
                    Text(
                        text = "Remove course",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        else {
            Text(
                text = "${course.department} ${course.number}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CourseList(courseViewModel: CourseViewModel) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    val courseViewInputs: CourseViewInputs = viewModel()

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitCourseList(courseViewModel, courseViewInputs)
    } else {
        LandscapeCourseList(courseViewModel, courseViewInputs)
    }
}

@Composable
fun LandscapeCourseList(courseViewModel: CourseViewModel, courseViewInputs: CourseViewInputs) {
    Row (
        Modifier
            .fillMaxWidth()
            .padding(50.dp),
    ) {
        val observableList by courseViewModel.courses.collectAsState()

        Column {
            OutlinedTextField(
                value = courseViewInputs.courseInput,
                onValueChange = { newText -> courseViewInputs.updateCourseInput(newText) },
                label = { Text("Enter Course Name", color = Color.Black) },
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color(52, 140, 235),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color(52, 140, 235),
                    unfocusedBorderColor = Color.Gray
                )
            )
            OutlinedTextField(
                value = courseViewInputs.courseNumberInput,
                onValueChange = { newValue -> courseViewInputs.updateCourseNumberInput(newValue) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text="Enter Course Number", color=Color.Black) },
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color(52, 140, 235),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color(52, 140, 235),
                    unfocusedBorderColor = Color.Gray
                )
            )
            OutlinedTextField(
                value = courseViewInputs.courseLocation,
                onValueChange = { newText -> courseViewInputs.updateCourseLocation(newText) },
                label = { Text("Enter Course Location", color = Color.Black) },
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color(52, 140, 235),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color(52, 140, 235),
                    unfocusedBorderColor = Color.Gray
                )
            )
            Button(
                onClick = {
                    val added = handleAddCourse(
                        courseNumber = courseViewInputs.courseNumberInput,
                        courseDepartment = courseViewInputs.courseInput,
                        courseLocation = courseViewInputs.courseLocation,
                        viewModel = courseViewModel
                    )
                    if (added) {
                        courseViewInputs.updateCourseInput("")
                        courseViewInputs.updateCourseNumberInput("")
                        courseViewInputs.updateCourseLocation("")
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(52, 140, 235), // background
                    contentColor = Color.Black           // text/icon color
                )
            ) {
                Text("Add Course")
            }
        }
        Column(
            Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Row {
                Text("Course List",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color=Color(245, 141, 22)
                )
            }
            Row{
                LazyColumn (
                    Modifier
                        .fillMaxWidth()
                        .padding(50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(observableList) { CourseItem(it, courseViewModel) }
                }
            }
        }
    }
}

@Composable
fun PortraitCourseList(courseViewModel: CourseViewModel, courseViewInputs: CourseViewInputs) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        val observableList by courseViewModel.courses.collectAsState()

        Row {
            Column {
                OutlinedTextField(
                    value = courseViewInputs.courseInput,
                    onValueChange = { newText -> courseViewInputs.updateCourseInput(newText) },
                    label = { Text("Enter Course Name", color = Color.Black) },
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color(52, 140, 235),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = Color(52, 140, 235),
                        unfocusedBorderColor = Color.Gray
                    )
                )
                OutlinedTextField(
                    value = courseViewInputs.courseNumberInput,
                    onValueChange = { newValue -> courseViewInputs.updateCourseNumberInput(newValue)},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text="Enter Course Number", color=Color.Black) },
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color(52, 140, 235),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = Color(52, 140, 235),
                        unfocusedBorderColor = Color.Gray
                    )
                )
                OutlinedTextField(
                    value = courseViewInputs.courseLocation,
                    onValueChange = { newText -> courseViewInputs.updateCourseLocation(newText) },
                    label = { Text("Enter Course Location", color = Color.Black) },
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color(52, 140, 235),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = Color(52, 140, 235),
                        unfocusedBorderColor = Color.Gray
                    )
                )
            }
        }

        Row {
            Button(
                onClick = {
                    val added = handleAddCourse(
                        courseNumber = courseViewInputs.courseNumberInput,
                        courseDepartment = courseViewInputs.courseInput,
                        courseLocation = courseViewInputs.courseLocation,
                        viewModel = courseViewModel
                    )
                    if (added) {
                        courseViewInputs.updateCourseInput("")
                        courseViewInputs.updateCourseNumberInput("")
                        courseViewInputs.updateCourseLocation("")
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(52, 140, 235), // background
                    contentColor = Color.Black           // text/icon color
                )
            ) {
                Text("Add Course")
            }
        }
        Spacer(Modifier.height(10.dp))
        Text("Course List",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            color=Color(245, 141, 22)
        )
        Row{
            LazyColumn (
                Modifier
                    .fillMaxWidth()
                    .padding(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                items(observableList) { CourseItem(it, courseViewModel) }
            }
        }
    }
}