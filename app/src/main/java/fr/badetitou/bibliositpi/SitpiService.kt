package fr.badetitou.bibliositpi

import fr.badetitou.bibliositpi.model.api.SearchAnswer
import fr.badetitou.bibliositpi.model.api.SearchRequest
import fr.badetitou.bibliositpi.model.api.holdings.HoldingRecordAnswer
import fr.badetitou.bibliositpi.model.api.holdings.SearchHoldingRecord
import fr.badetitou.bibliositpi.model.api.logon.LogonAnswer
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SitpiService {

    @POST("Portal/Recherche/Search.svc/Search")
    suspend fun searchPaginated(@Body searchRequest: SearchRequest): SearchAnswer

    @POST("Portal/Services/ILSClient.svc/GetHoldings")
    suspend fun getHoldings(@Body searchRequest: SearchHoldingRecord): HoldingRecordAnswer

    @FormUrlEncoded
    @POST("Portal/Recherche/logon.svc/logon")
    suspend fun logon(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("rememberMe") remember: Boolean
    ): LogonAnswer

}