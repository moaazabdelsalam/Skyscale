package com.io.skyscale.api

import com.squareup.moshi.Json

data class ResultImage(
    @field:Json(name = "result_image") val imageUrl: String
)