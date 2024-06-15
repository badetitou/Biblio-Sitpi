package fr.badetitou.bibliositpi
import fr.badetitou.bibliositpi.model.api.SearchAnswer
import fr.badetitou.bibliositpi.model.api.SearchRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SitpiService {
    @POST("Portal/Recherche/Search.svc/Search")
    fun search(@Body searchRequest: SearchRequest): Call<SearchAnswer>

    @POST("Portal/Recherche/Search.svc/Search")
    suspend fun searchPaginated(@Body searchRequest: SearchRequest): SearchAnswer
}