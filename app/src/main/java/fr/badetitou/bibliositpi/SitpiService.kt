package fr.badetitou.bibliositpi
import fr.badetitou.bibliositpi.model.api.SearchAnswer
import fr.badetitou.bibliositpi.model.api.SearchRequest
import fr.badetitou.bibliositpi.model.api.holdings.HoldingRecordAnswer
import fr.badetitou.bibliositpi.model.api.holdings.SearchHoldingRecord
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SitpiService {

    @POST("Portal/Recherche/Search.svc/Search")
    suspend fun searchPaginated(@Body searchRequest: SearchRequest): SearchAnswer

    @POST("Portal/Services/ILSClient.svc/GetHoldings")
    suspend fun getHoldings(@Body searchRequest: SearchHoldingRecord): HoldingRecordAnswer

}