package com.paparazziteam.cleanarquitecturepokemon.shared.utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T : Any> toJson(value : T) = Json{
    encodeDefaults = true
    ignoreUnknownKeys = true
    isLenient = true
}.encodeToString(value)



inline fun <reified T> fromJson(json: String) : T {
    return Json{
        ignoreUnknownKeys = true
        isLenient = true
    }.decodeFromString(json)
}