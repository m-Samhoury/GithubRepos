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
) {
    fun joinErrors() = if (githubErrors.isNotEmpty()) githubErrors.joinToString(", ") { it.toString() }
    else message
}

@JsonClass(generateAdapter = true)
data class GithubErrorObject(
    @field:Json(name = "resource") val resource: String,
    @field:Json(name = "field") val field: String,
    @field:Json(name = "code") val code: String
) {
    override fun toString(): String {
        return "Resource: $resource, field:$field, code:$code"
    }
}

@JsonClass(generateAdapter = true)
data class GithubRepo(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "language") val programmingLanguage: String? = null,
    @field:Json(name = "description") val description: String? = null,
    @field:Json(name = "watchers_count") val starsCount: String? = null,
    @field:Json(name = "forks_count") val forks: Int

)

@JsonClass(generateAdapter = true)
data class GithubRepoList(
    @field:Json(name = "incomplete_results") val resultIncomplete: Boolean = true,
    @field:Json(name = "total_count") val total: Int = 0,
    @field:Json(name = "items") val items: List<GithubRepo> = emptyList(),
    val nextPage: Int? = null
)