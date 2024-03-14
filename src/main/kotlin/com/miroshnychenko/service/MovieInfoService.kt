package com.miroshnychenko.service

import com.miroshnychenko.domain.MovieInfo
import com.miroshnychenko.util.CommonUtil.delay
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

class MovieInfoService {
    fun retrieveMoviesFlux(): Flux<MovieInfo> {
        val movieInfoList = listOf(
            MovieInfo(
                100L,
                "Batman Begins",
                2005,
                listOf("Christian Bale", "Michael Cane"),
                LocalDate.parse("2005-06-15")
            ),
            MovieInfo(
                101L,
                "The Dark Knight",
                2008,
                listOf("Christian Bale", "HeathLedger"),
                LocalDate.parse("2008-07-18")
            ),
            MovieInfo(
                102L,
                "Dark Knight Rises",
                2008,
                listOf("Christian Bale", "Tom Hardy"),
                LocalDate.parse("2012-07-20")
            )
        )

        return Flux.fromIterable(movieInfoList)
    }

    fun retrieveMovieInfoMonoUsingId(movieId: Long): Mono<MovieInfo> {
        val movie = MovieInfo(
            movieId,
            "Batman Begins",
            2005,
            listOf("Christian Bale", "Michael Cane"),
            LocalDate.parse("2005-06-15")
        )

        return Mono.just(movie)
    }

    fun movieList(): List<MovieInfo> {
        delay(1000)

        return java.util.List.of<MovieInfo>(
            MovieInfo(
                100L,
                "Batman Begins",
                2005,
                listOf("Christian Bale", "Michael Cane"),
                LocalDate.parse("2005-06-15")
            ),
            MovieInfo(
                101L,
                "The Dark Knight",
                2008,
                listOf("Christian Bale", "HeathLedger"),
                LocalDate.parse("2008-07-18")
            ),
            MovieInfo(
                102L,
                "Dark Knight Rises",
                2008,
                listOf("Christian Bale", "Tom Hardy"),
                LocalDate.parse("2012-07-20")
            )
        )
    }

    fun retrieveMovieUsingId(movieId: Long): MovieInfo {
        delay(1000)
        return MovieInfo(
            movieId,
            "Batman Begins",
            2005,
            listOf("Christian Bale", "Michael Cane"),
            LocalDate.parse("2005-06-15")
        )
    }
}