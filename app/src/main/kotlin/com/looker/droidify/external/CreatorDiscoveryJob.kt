package com.looker.droidify.external

import kotlinx.serialization.Serializable

@Serializable
enum class CreatorDiscoveryStatus {
    QUEUED,
    RUNNING,
    RETRYING,
    SUCCEEDED,
    FAILED,
}

@Serializable
data class CreatorDiscoveryJob(
    val accountKey: String,
    val status: CreatorDiscoveryStatus = CreatorDiscoveryStatus.QUEUED,
    val processed: Int = 0,
    val total: Int = 0,
    val discovered: Int = 0,
    val attempt: Int = 0,
    val error: String = "",
    val processedRepositories: Set<String> = emptySet(),
    val updatedAt: Long = System.currentTimeMillis(),
) {
    val isActive: Boolean
        get() = status == CreatorDiscoveryStatus.QUEUED ||
            status == CreatorDiscoveryStatus.RUNNING ||
            status == CreatorDiscoveryStatus.RETRYING
}
