package com.io.skyscale.api

import com.squareup.moshi.Json
import retrofit2.http.Query

data class UpscalePostRequest (
    @field:Json(name = "image") val imageBytes: String,
    @field:Json(name ="scale") val scale: String,
    @field:Json(name ="feature") val feature: String,
    @field:Json(name ="token") val  token: String
)

data class OtherFeaturesPostRequest(
    @field:Json(name = "image") val imageBytes: String,
    @field:Json(name ="feature") val feature: String,
    @field:Json(name ="token") val  token: String
)