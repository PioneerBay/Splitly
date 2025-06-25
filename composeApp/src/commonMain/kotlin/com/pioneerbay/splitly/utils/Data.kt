package com.pioneerbay.splitly.utils

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

val supabase =
    createSupabaseClient(
        supabaseUrl = "https://elvvmswtzokrulfbgmda.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVsdnZtc3d0em9rcnVsZmJnbWRhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTA2MTc0ODIsImV4cCI6MjA2NjE5MzQ4Mn0.YDMalziQee4Gui2qsoJ4-TM__rCKA9P76WIhGSLYsf4",
    ) {
//        install(Auth)
        install(Postgrest)
//         install other modules
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
// credit to nick ess
