package com.miroshnychenko.service

import com.miroshnychenko.domain.Revenue
import com.miroshnychenko.util.CommonUtil.delay

class RevenueService {
    fun getRevenue(movieId: Long?): Revenue {
        delay(1000) // simulating a network call ( DB or Rest call)
        return Revenue(movieId, 1000000.0, 5000000.0)
    }
}
