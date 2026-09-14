package com.example.S8107253assingment2


interface DashboardRepository {
    suspend fun getDashboard(keypass: String): DashboardResponse
}

class NetworkDashboardRepository(
    private val apiService: ApiService
) : DashboardRepository {
    override suspend fun getDashboard(keypass: String): DashboardResponse {
        return apiService.getDashboard(keypass)
    }
}