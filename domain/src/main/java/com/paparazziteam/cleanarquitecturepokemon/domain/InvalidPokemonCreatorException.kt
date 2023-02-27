package com.paparazziteam.cleanarquitecturepokemon.domain

class InvalidPokemonCreatorException:Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}