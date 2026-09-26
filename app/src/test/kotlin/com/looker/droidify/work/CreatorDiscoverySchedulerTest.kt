package com.looker.droidify.work

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class CreatorDiscoverySchedulerTest {
    @Test
    fun uniqueWorkNameIsStableAndScopedPerAccount() {
        val first = CreatorDiscoveryScheduler.uniqueWorkName("GITHUB/example")
        val again = CreatorDiscoveryScheduler.uniqueWorkName("GITHUB/example")
        val second = CreatorDiscoveryScheduler.uniqueWorkName("GITHUB/other")

        assertEquals("creator_discovery:GITHUB/example", first)
        assertEquals(first, again)
        assertNotEquals(first, second)
    }
}
