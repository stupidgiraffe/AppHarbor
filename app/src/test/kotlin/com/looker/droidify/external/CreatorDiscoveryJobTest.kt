package com.looker.droidify.external

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CreatorDiscoveryJobTest {
    @Test
    fun activeStatesAreUnfinished() {
        listOf(
            CreatorDiscoveryStatus.QUEUED,
            CreatorDiscoveryStatus.RUNNING,
            CreatorDiscoveryStatus.RETRYING,
        ).forEach { status ->
            assertTrue(CreatorDiscoveryJob("account", status = status).isActive)
        }
        listOf(
            CreatorDiscoveryStatus.SUCCEEDED,
            CreatorDiscoveryStatus.FAILED,
        ).forEach { status ->
            assertFalse(CreatorDiscoveryJob("account", status = status).isActive)
        }
    }

    @Test
    fun persistedCheckpointRoundTripsWithoutLosingProgress() {
        val job = CreatorDiscoveryJob(
            accountKey = "GITHUB/example",
            status = CreatorDiscoveryStatus.RETRYING,
            processed = 7,
            total = 20,
            discovered = 4,
            attempt = 2,
            error = "temporary provider failure",
            processedRepositories = setOf("example/one", "example/two"),
            updatedAt = 123456789L,
        )

        val encoded = Json.encodeToString(job)
        val restored = Json.decodeFromString<CreatorDiscoveryJob>(encoded)

        assertEquals(job, restored)
    }
}
