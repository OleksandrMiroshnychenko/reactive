package com.miroshnychenko.domain

import java.time.LocalDate

data class MovieInfo(
    val movieInfoId: Long,
    val name: String? = null,
    val year: Int? = null,
    val cast: List<String>? = null,
    val releaseDate: LocalDate? = null
)