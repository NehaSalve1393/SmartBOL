package com.example.smartboldriver.api.services

import com.example.smartboldriver.models.SaveSignPdfRequest
import com.example.smartboldriver.models.UploadDataRequest
import com.example.smartboldriver.models.UploadSignRequest
import com.example.smartboldriver.models.auth.*
import com.example.smartboldriver.models.checkin.CheckInRequest
import com.example.smartboldriver.models.checkin.CheckOutRequest
import com.example.smartboldriver.models.pickup.GetPickupListRequest
import com.example.smartboldriver.models.pickup.PickupResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MasterAPIService {

    @Headers("Content-Type:application/x-www-form-urlencoded"
    )
    @POST("txm3.aspx")
    fun login(@Body loginRequest: LoginRequest): Observable<ArrayList<LoginResponse>>

    @Headers("Content-Type:application/x-www-form-urlencoded"
    )
    @POST("txm3.aspx")
    fun registartion(@Body loginRequest: RegistrationRequest): Observable<ArrayList<LoginResponse>>

    @Headers("Content-Type:application/x-www-form-urlencoded"
    )
    @POST("txm3.aspx")
    fun deviceActivation(@Body loginRequest: DeviceActivationRequest): Observable<ArrayList<LoginResponse>>

    @Headers("Content-Type:application/x-www-form-urlencoded"
    )
    @POST("txm3.aspx")
    fun checkin(@Body loginRequest: CheckInRequest): Observable<ArrayList<PickupResponse>>

    @Headers("Content-Type:application/x-www-form-urlencoded"
    )
    @POST("txm3.aspx")
    fun getPickupList(@Body loginRequest: GetPickupListRequest): Observable<ArrayList<PickupResponse>>

    @Headers("Content-Type:application/x-www-form-urlencoded"
    )
    @POST("txm3.aspx")
    fun getPickupListChkout(@Body loginRequest: GetPickupListRequest): Observable<ArrayList<PickupResponse>>


    @Headers("Content-Type:application/x-www-form-urlencoded"
    )
    @POST("txm3.aspx")
    fun reauth(@Body loginRequest: ReAuthRequest): Observable<ArrayList<LoginResponse>>
    @Headers("Content-Type:application/x-www-form-urlencoded"
    )
    @POST("txm3.aspx")
    fun upload(@Body loginRequest: UploadDataRequest): Observable<ArrayList<PickupResponse>>


    @Headers("Content-Type:application/x-www-form-urlencoded"
    )
    @POST("txm3.aspx")
    fun uploadDelSign(@Body loginRequest: UploadSignRequest): Observable<ArrayList<PickupResponse>>


    @Headers("Content-Type:application/x-www-form-urlencoded"
    )
    @POST("txm3.aspx")
    fun saveSignPDF(@Body loginRequest: SaveSignPdfRequest): Observable<ArrayList<PickupResponse>>

    @Headers("Content-Type:application/x-www-form-urlencoded"
    )
    @POST("txm3.aspx")
    fun checkout(@Body loginRequest: CheckOutRequest):  Observable<ArrayList<PickupResponse>>

}