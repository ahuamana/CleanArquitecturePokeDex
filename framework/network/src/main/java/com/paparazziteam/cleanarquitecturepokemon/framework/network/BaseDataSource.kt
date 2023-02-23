package com.paparazziteam.cleanarquitecturepokemon.framework.network

import android.util.Log
import com.paparazziteam.cleanarquitecturepokemon.domain.GeneralErrorResponse
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.fromJson
import pe.com.tarjetaw.android.client.shared.network.Resource
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            if(response.code() == 400){
                val errorResponse = fromJson<GeneralErrorResponse>(response.errorBody()?.string()?:"")
                Log.e("BaseDataSource","Error 400 -- ${errorResponse.error.message}")
                return Resource.error(errorResponse.error.message?:"")
            }
            return error(" not e ${response.code()} ${response.body()}")
        } catch (e: Exception) {
            return error()
        }
    }

    private fun <T> error(message: String): Resource<T> {
        Log.e("BaseDataSource", message)
        return Resource.error("Network call has failed for a following reason: $message")
    }

    private fun <T> error(): Resource<T> {
        Log.e("BaseDataSource","remoteDataSource error")
        return Resource.error("")
    }

}