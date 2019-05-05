package com.moustafasamhoury.githubchallenge.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author moustafasamhoury
 * created on Saturday, 04 May, 2019
 */

@JsonClass(generateAdapter = true)
data class GithubErrorResponse(
    @field:Json(name = "message") val message: String? = null,
    @field:Json(name = "errors") val githubErrors: List<GithubErrorObject> = listOf()
)

@JsonClass(generateAdapter = true)
data class GithubErrorObject(
    @field:Json(name = "resource") val resource: String,
    @field:Json(name = "field") val field: String,
    @field:Json(name = "code") val code: String
)

@JsonClass(generateAdapter = true)
data class GithubRepo(
    @field:Json(name = "items") val name: String? = null,
    @field:Json(name = "language") val programmingLanguage: String? = null,
    @field:Json(name = "description") val description: String? = null,
    @field:Json(name = "watchers_count") val starsCount: String? = null
)

@JsonClass(generateAdapter = true)
data class GithubRepoList(
    @field:Json(name = "items") val items: List<GithubRepo> = listOf()
)