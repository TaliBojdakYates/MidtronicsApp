package com.example.midtronics

data class Country(
    val name: Map<String, Any>, // This represents the nested "name" object
    val population: Long,
    val capital: List<String>,
    val area: Double,
    val region: String,
    val subregion: String,

)
