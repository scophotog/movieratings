package org.sco.movieratings.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoroutineScopeManager @Inject internal constructor() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    val coroutineScope get(): CoroutineScope = scope

    internal fun resetScope() {
        job.cancelChildren()
    }
}