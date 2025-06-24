package com.pioneerbay.splitly.utils

import io.github.jan.supabase.createSupabaseClient

val supabase =
    createSupabaseClient(
        supabaseUrl = "https://elvvmswtzokrulfbgmda.supabase.co",
        supabaseKey = $$"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVsdnZtc3d0em9rcnVsZmJnbWRhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTA2MTc0ODIsImV4cCI6MjA2NjE5MzQ4Mn0.YDMalziQee4Gui2qsoJ4-TM__rCKA9P76WIhGSLYsf4",
    ) {
//        install(Auth)
//        install(Postgrest)
//         install other modules
    }
