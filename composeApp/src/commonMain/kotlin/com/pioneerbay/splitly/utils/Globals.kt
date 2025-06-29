package com.pioneerbay.splitly.utils

import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object Globals {
    private val _transactionUpdateTrigger = MutableStateFlow(0L)
    val transactionUpdateTrigger: StateFlow<Long> = _transactionUpdateTrigger.asStateFlow()
    lateinit var currentUser: UserInfo

    fun notifyTransactionUpdate() {
        _transactionUpdateTrigger.value = _transactionUpdateTrigger.value + 1
    }
}
