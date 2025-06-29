package com.pioneerbay.splitly.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.data.TransactionWithProfiles
import com.pioneerbay.splitly.utils.Globals
import com.pioneerbay.splitly.utils.Globals.currentUser
import com.pioneerbay.splitly.utils.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

@Composable
fun RecentTransactions() {
    var transactions by remember { mutableStateOf<List<TransactionWithProfiles>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Listen to global transaction updates
    val transactionUpdateTrigger by Globals.transactionUpdateTrigger.collectAsState()

    // Function to fetch transactions
    suspend fun fetchTransactions() {
        try {
            val fetchedTransactions =
                supabase
                    .from("transaction")
                    .select(
                        Columns.raw(
                            "*," +
                                "profile_from:profiles!from(*)," +
                                "profile_to:profiles!to(*)",
                        ),
                    ).decodeList<TransactionWithProfiles>()
                    .filter { it.from == currentUser.id || it.to == currentUser.id }
                    .sortedByDescending { it.created_at }
                    .take(10)

            transactions = fetchedTransactions
            isLoading = false
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = e.message ?: "Failed to fetch transactions"
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        fetchTransactions()
    }

    LaunchedEffect(transactionUpdateTrigger) {
        fetchTransactions()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Recent Transactions",
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(Modifier.height(8.dp))

        when {
            isLoading -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                )
            }
            transactions.isEmpty() -> {
                Text(
                    text = "No transactions found",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            else -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(transactions) { transaction ->
                        Card(
                            modifier =
                                Modifier
                                    .width(150.dp),
                            colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surface),
//                            elevation = CardDefaults.cardElevation(5.dp),
                            shape = MaterialTheme.shapes.medium,
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    text = "${transaction.amount} ${transaction.currency}",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                Text(
                                    text = "${transaction.profile_to.username} -> ${transaction.profile_from.username}",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                Text(
                                    text = transaction.created_at.substring(0, 10),
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
