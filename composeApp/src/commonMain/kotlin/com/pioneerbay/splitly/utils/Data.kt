package com.pioneerbay.splitly.utils

import com.pioneerbay.splitly.data.ProfileMoneyBalance
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

val supabase =
    createSupabaseClient(
        "https://elvvmswtzokrulfbgmda.supabase.co",
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVsdnZtc3d0em9rcnVsZmJnbWRhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTA2MTc0ODIsImV4cCI6MjA2NjE5MzQ4Mn0.YDMalziQee4Gui2qsoJ4-TM__rCKA9P76WIhGSLYsf4",
    ) {
        install(Postgrest)
        install(Auth)
    }

suspend fun fetchFriends(
    onSuccess: (List<Profile>) -> Unit = {},
    onError: (String) -> Unit = {},
) {
    try {
        val friendsList =
            supabase
                .from("profiles")
                .select()
                .decodeList<Profile>()
                .map { Profile(it.id, it.user_id, it.username ?: "Username", it.bio ?: "", it.created_at) }
        onSuccess(friendsList)
    } catch (e: Exception) {
        onError(e.message ?: "Unknown error occurred")
    }
}

suspend fun fetchMoneyBalances(
    onSuccess: (List<ProfileMoneyBalance>) -> Unit = {},
    onError: (String) -> Unit = {},
) {
    try {
        val moneyBalances =
            supabase
                .from("money_balance_3")
                .select()
                .decodeList<ProfileMoneyBalance>()
        onSuccess(moneyBalances)
    } catch (e: Exception) {
        onError(e.message ?: "Unknown error occurred")
    }
}

@Serializable
class Friend(
    val id: String,
    val user_1: String,
    val user_2: String,
    val created_at: String,
) {
    override fun toString(): String = "Friend(id=$id, user_1='$user_1', user_2='$user_2', created_at='$created_at')"
}

@Serializable
class Profile(
    val id: String,
    val user_id: String,
    val username: String? = "Username",
    val bio: String? = "",
    val created_at: String,
) {
    override fun toString(): String = "Profile(id=$id, username='$username', created_at='$created_at')"
}
