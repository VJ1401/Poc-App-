package `in`.redbus.trendingapp.network

import `in`.redbus.trendingapp.model.SampleModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("search/repositories?sort=stars&order=desc&q=language:Any&q=created:>2022-02-18")
    suspend fun getList(): Response<SampleModel>
}

