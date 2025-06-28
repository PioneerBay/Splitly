package com.pioneerbay.splitly.data

import com.pioneerbay.splitly.utils.Profile
import kotlinx.serialization.Serializable

@Serializable
data class TransactionInsert(
    val amount: Double,
    val currency: String,
    val to: String,
)

@Serializable
data class TransactionSelect(
    val id: String,
    val amount: Double,
    val currency: String,
    val to: String,
    val from: String,
    val created_at: String,
)

@Serializable
data class TransactionWithProfiles(
    val id: String,
    val amount: Double,
    val currency: String,
    val to: String,
    val from: String,
    val created_at: String,
    val profile_from: Profile,
    val profile_to: Profile,
)
