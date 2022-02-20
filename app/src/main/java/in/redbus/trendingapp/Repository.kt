package `in`.redbus.trendingapp

import `in`.redbus.trendingapp.network.Service

class Repository(private val service:Service) {

    suspend fun getAllList() = service.getList()
}