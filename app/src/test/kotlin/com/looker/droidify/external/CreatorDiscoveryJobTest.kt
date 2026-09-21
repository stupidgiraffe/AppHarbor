package com.looker.droidify.external

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
}
