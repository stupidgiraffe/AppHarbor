package com.looker.droidify.work

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.looker.droidify.external.CreatorDiscoveryJob
import com.looker.droidify.external.ExternalAccount
import com.looker.droidify.external.ExternalAppRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreatorDiscoveryScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val repository: ExternalAppRepository,
) {
    val jobs: Flow<List<CreatorDiscoveryJob>> = repository.discoveryJobs

    suspend fun enqueue(account: ExternalAccount): Boolean {
        if (!account.enabled || !repository.tryQueueDiscovery(account.key)) return false
        enqueueUnique(account.key)
        return true
    }

    suspend fun resumeAndSchedule() {
        val accounts = repository.getAccounts().associateBy { it.key }
        repository.getDiscoveryJobs()
            .filter { it.isActive && accounts[it.accountKey]?.enabled == true }
            .forEach { enqueueUnique(it.accountKey) }
        scheduleEligibleAccounts()
    }

    suspend fun scheduleEligibleAccounts() {
        val now = System.currentTimeMillis()
        repository.getAccounts()
            .filter {
                it.enabled &&
                    (it.lastScan == 0L || now - it.lastScan >= ACCOUNT_RESCAN_INTERVAL_MS)
            }
            .forEach { enqueue(it) }
    }

    fun cancel(accountKey: String) {
        WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName(accountKey))
    }

    private fun enqueueUnique(accountKey: String) {
        val request = OneTimeWorkRequestBuilder<CreatorDiscoveryWorker>()
            .setInputData(Data.Builder().putString(KEY_ACCOUNT, accountKey).build())
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .addTag(TAG)
            .addTag(accountTag(accountKey))
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            uniqueWorkName(accountKey),
            ExistingWorkPolicy.KEEP,
            request,
        )
    }

    companion object {
        internal const val KEY_ACCOUNT = "account_key"
        internal const val TAG = "creator_discovery"
        private const val ACCOUNT_RESCAN_INTERVAL_MS = 24L * 60 * 60 * 1000

        internal fun uniqueWorkName(accountKey: String): String = "$TAG:$accountKey"
        internal fun accountTag(accountKey: String): String = "$TAG:account:$accountKey"
    }
}
