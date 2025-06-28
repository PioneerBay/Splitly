package com.pioneerbay.splitly.data

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val amount: Double,
    val currency: String,
    val to: String,
)
