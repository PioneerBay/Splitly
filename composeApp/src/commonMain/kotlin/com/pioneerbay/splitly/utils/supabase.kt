package com.pioneerbay.splitly.utils

import io.github.jan.supabase.createSupabaseClient

val supabase =
    createSupabaseClient(
        supabaseUrl = "https://xyzcompany.supabase.co",
        supabaseKey = "public-anon-key",
    ) {
//        install(Auth)
//        install(Postgrest)
//         install other modules
    }
