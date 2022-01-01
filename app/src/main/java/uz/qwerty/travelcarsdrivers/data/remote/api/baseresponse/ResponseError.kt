package uz.qwerty.travelcarsdrivers.data.remote.api.baseresponse

import com.google.gson.annotations.SerializedName

class ResponseError(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)