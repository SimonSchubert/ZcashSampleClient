package com.example.zcashclienttets.ui.theme.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransactionsScreen(viewModel: TransactionsViewModel) {
    LazyColumn {
        items(items = viewModel.transactions.value, key = { it.index }) { transaction ->
            ListItem(
                text = { Text(viewModel.buildTransactionTitle(transaction)) },
                secondaryText = { Text(transaction.txid.toStringUtf8()) })
        }
    }
}