package com.youngerhousea.miraicompose.ui.feature.about


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRelease(
    @SerialName("assets_url")
    val assetsUrl: String = "",
    @SerialName("author")
    val author: Author = Author(),
    @SerialName("body")
    val body: String = "",
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("draft")
    val draft: Boolean = false,
    @SerialName("html_url")
    val htmlUrl: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("node_id")
    val nodeId: String = "",
    @SerialName("prerelease")
    val prerelease: Boolean = false,
    @SerialName("published_at")
    val publishedAt: String = "",
    @SerialName("tag_name")
    val tagName: String = "",
    @SerialName("tarball_url")
    val tarballUrl: String = "",
    @SerialName("target_commitish")
    val targetCommitish: String = "",
    @SerialName("upload_url")
    val uploadUrl: String = "",
    @SerialName("url")
    val url: String = "",
    @SerialName("zipball_url")
    val zipballUrl: String = ""
)

@Serializable
data class Author(
    @SerialName("avatar_url")
    val avatarUrl: String = "",
    @SerialName("events_url")
    val eventsUrl: String = "",
    @SerialName("followers_url")
    val followersUrl: String = "",
    @SerialName("following_url")
    val followingUrl: String = "",
    @SerialName("gists_url")
    val gistsUrl: String = "",
    @SerialName("gravatar_id")
    val gravatarId: String = "",
    @SerialName("html_url")
    val htmlUrl: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("login")
    val login: String = "",
    @SerialName("node_id")
    val nodeId: String = "",
    @SerialName("organizations_url")
    val organizationsUrl: String = "",
    @SerialName("received_events_url")
    val receivedEventsUrl: String = "",
    @SerialName("repos_url")
    val reposUrl: String = "",
    @SerialName("site_admin")
    val siteAdmin: Boolean = false,
    @SerialName("starred_url")
    val starredUrl: String = "",
    @SerialName("subscriptions_url")
    val subscriptionsUrl: String = "",
    @SerialName("type")
    val type: String = "",
    @SerialName("url")
    val url: String = ""
)