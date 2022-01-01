package uz.qwerty.travelcarsdrivers.data.remote.api.baseresponse

import androidx.annotation.Keep

@Keep
class BaseResponse<T>(
    val success: Boolean,
    val data: T,
    val error: ResponseError
)