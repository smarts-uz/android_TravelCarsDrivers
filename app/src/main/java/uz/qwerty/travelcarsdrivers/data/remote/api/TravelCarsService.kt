package uz.qwerty.travelcarsdrivers.data.remote.api

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*
import uz.qwerty.travelcarsdrivers.data.remote.response.main.*

interface TravelCarsService {
    @Headers("Accept: application/json", "Content-Type: application/json", "Content-Language: ru")
    @POST("/login")
    fun login(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("fcmToken") fcmToken: String?
    ): Observable<Response<UserResponse>>

    @Headers("Accept: application/json", "Content-Type: application/json", "Content-Language: ru")
    @GET("/profile")
    fun profile(
        @Query("api_token") apiKey: String?
    ): Observable<Response<UserResponse>>

    @Headers("Accept: application/json", "Content-Type: application/json", "Content-Language: ru")
    @GET("/logout")
    fun logout(
        @Query("api_token") apiKey: String?
    ): Observable<Response<UserResponse>>

    @Headers("Accept: application/json", "Content-Type: application/json", "Content-Language: ru")
    @GET("/trips/banners")
    fun tripsBanners(
        @Query("api_token") apiKey: String?
    ): Observable<Response<BookingBannerResponse>>

    @Headers("Accept: application/json", "Content-Type: application/json", "Content-Language: ru")
    @GET("/trips/last-active")
    fun lastActive(
        @Query("api_token") apiKey: String?
    ): Observable<Response<BookingBannerResponse>>

    @Headers("Accept: application/json", "Content-Type: application/json", "Content-Language: ru")
    @GET("/trips/counts")
    fun counts(
        @Query("api_token") apiKey: String?
    ): Observable<Response<CountsResponse>>


    @Headers("Accept: application/json", "Content-Type: application/json", "Content-Language: ru")
    @GET("/trips/history")
    fun history(
        @Query("api_token") apiKey: String?,
        @Query("type") type: String?,
        @Query("page") page: Long?
    ): Observable<Response<BookingHystoryResponse>>

    @Headers("Accept: application/json", "Content-Type: application/json", "Content-Language: ru")
    @GET("/trips/show/{id}")
    fun show(
        @Path("id") id: Int,
        @Query("api_token") apiKey: String?
    ): Observable<Response<BookingShowResponse>>

    @Headers("Accept: application/json", "Content-Type: application/json", "Content-Language: ru")
    @GET("/profile/edit")
    fun profileUpdate(
        @Query("api_token") apiKey: String?,
        @Query("current_password") currentPassword: String?,
        @Query("new_password") newPassword: String?,
        @Query("confirm_new_password") confirmNewPassword: String?
    ): Observable<Response<ProfileUpdateResponse>>

    @Headers("Accept: application/json", "Content-Type: application/json", "Content-Language: ru")
    @GET("/routes/list")
    fun routes(
        @Query("api_token") apiKey: String?,
        @Query("date") date: String?
    ): Observable<Response<BookingRouteResponse>>

    @Headers("Accept: application/json", "Content-Type: application/json", "Content-Language: ru")
    @GET("/routes/status/{id}")
    fun routeStatus(
        @Path("id") id: Int,
        @Query("api_token") apiKey: String?
    ): Observable<Response<BookingShowResponse>>


}