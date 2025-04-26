package com.numosophy.model

data class CountryResponse(
    val data: List<CountryData>
)

data class CountryData(
    val name: String,
    val states: List<StateData>
)

data class StateData(
    val name: String
)

