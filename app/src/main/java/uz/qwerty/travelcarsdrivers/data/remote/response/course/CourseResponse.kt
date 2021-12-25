package uz.qwerty.travelcarsdrivers.data.remote.response.course

import com.google.gson.annotations.SerializedName

class CourseResponse(
    @field:SerializedName("courseList")
    val courseList: ArrayList<CourseResponseItem>
)