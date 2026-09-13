package com.example.S8107253assingment2

interface AuthRepository {
    suspend fun login(username: String, password: String): String
}

class NetworkAuthRepository(
    private val apiService: ApiService
) : AuthRepository {
    override suspend fun login(username: String, password: String): String {
        val response = apiService.login(LoginRequest(username, password))
        return response.keypass
    }
}