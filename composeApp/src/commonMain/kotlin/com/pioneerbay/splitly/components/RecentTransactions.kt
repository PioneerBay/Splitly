package com.pioneerbay.splitly.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.data.TransactionWithProfiles
import com.pioneerbay.splitly.utils.Globals
import com.pioneerbay.splitly.utils.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

@Composable
fun RecentTransactions(onClick: (String) -> Unit = {}) {
    var transactions by remember { mutableStateOf<List<TransactionWithProfiles>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Listen to global transaction updates
    val transactionUpdateTrigger by Globals.transactionUpdateTrigger.collectAsState()

    // Function to fetch transactions
    val fetchTransactions =
        suspend {
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
                        .sortedByDescending { it.created_at }
                        .take(10) // Get the 10 most recent transactions

                transactions = fetchedTransactions
                isLoading = false
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to fetch transactions"
                isLoading = false
            }
        }

    // Initial load
    LaunchedEffect(Unit) {
        fetchTransactions()
    }

    // React to transaction updates
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
                Column {
                    transactions.forEach { transaction ->
                        Card(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surface),
//                            elevation = CardDefaults.cardElevation(5.dp),
                        ) {
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column {
                                    Text(
                                        text = "${transaction.amount} ${transaction.currency}",
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                    Text(
                                        text = "To: ${transaction.profile_to.username}",
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                    Text(
                                        text = "From: ${transaction.profile_from.username}",
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                                Text(
                                    text = transaction.created_at.substring(0, 10), // Show date only
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
