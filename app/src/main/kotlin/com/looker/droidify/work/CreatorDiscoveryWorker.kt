package com.looker.droidify.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.looker.droidify.external.AccountDiscoveryTransientException
import com.looker.droidify.external.CreatorDiscoveryStatus
import com.looker.droidify.external.ExternalAppRepository
import com.looker.droidify.external.ExternalRefresher
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException

@HiltWorker
class CreatorDiscoveryWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: ExternalAppRepository,
    private val refresher: ExternalRefresher,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val accountKey = inputData.getString(CreatorDiscoveryScheduler.KEY_ACCOUNT)
            ?: return Result.failure(errorData("Missing account key"))
        val account = repository.getAccounts().firstOrNull { it.key == accountKey }
        if (account == null) {
            repository.removeDiscoveryJob(accountKey)
            return Result.success()
        }
        if (!account.enabled) {
            fail(accountKey, "Account is disabled")
            return Result.failure(errorData("Account is disabled"))
        }

        val existing = repository.getDiscoveryJob(accountKey)
        repository.updateDiscoveryJob(accountKey) {
            it.copy(
                status = CreatorDiscoveryStatus.RUNNING,
                attempt = runAttemptCount + 1,
                error = "",
            )
        }
        return try {
            val discovered = refresher.rescanAccountNow(
                account = account,
                alreadyProcessed = existing?.processedRepositories.orEmpty(),
            ) { processed, total, repoKey, discoveredNow ->
                repository.updateDiscoveryJob(accountKey) {
                    it.copy(
                        status = CreatorDiscoveryStatus.RUNNING,
                        processed = processed,
                        total = total,
                        discovered = it.discovered + discoveredNow,
                        processedRepositories = it.processedRepositories + repoKey,
                        error = "",
                    )
                }
                setProgress(workDataOf(KEY_PROCESSED to processed, KEY_TOTAL to total))
            }
            val finalJob = repository.getDiscoveryJob(accountKey)
            repository.updateDiscoveryJob(accountKey) {
                it.copy(
                    status = CreatorDiscoveryStatus.SUCCEEDED,
                    processed = it.total,
                    discovered = finalJob?.discovered ?: discovered,
                    error = "",
                    processedRepositories = emptySet(),
                )
            }
            Result.success(
                Data.Builder()
                    .putInt(KEY_DISCOVERED, finalJob?.discovered ?: discovered)
                    .build(),
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: AccountDiscoveryTransientException) {
            val message = e.message ?: "Temporary provider failure"
            if (runAttemptCount < MAX_RETRY_ATTEMPTS) {
                repository.updateDiscoveryJob(accountKey) {
                    it.copy(status = CreatorDiscoveryStatus.RETRYING, error = message)
                }
                Result.retry()
            } else {
                fail(accountKey, message)
                Result.failure(errorData(message))
            }
        } catch (e: Exception) {
            val message = e.message ?: e::class.java.simpleName
            fail(accountKey, message)
            Result.failure(errorData(message))
        }
    }

    private suspend fun fail(accountKey: String, message: String) {
        repository.updateDiscoveryJob(accountKey) {
            it.copy(status = CreatorDiscoveryStatus.FAILED, error = message)
        }
    }

    private fun errorData(message: String): Data =
        Data.Builder().putString(KEY_ERROR, message).build()

    companion object {
        const val KEY_PROCESSED = "processed"
        const val KEY_TOTAL = "total"
        const val KEY_DISCOVERED = "discovered"
        const val KEY_ERROR = "error"
        private const val MAX_RETRY_ATTEMPTS = 5
    }
}
