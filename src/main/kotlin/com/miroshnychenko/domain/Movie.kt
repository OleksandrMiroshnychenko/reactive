package com.miroshnychenko.domain

data class Movie(val movieInfo: MovieInfo, val reviewList: List<Review>, var revenue: Revenue? = null)