@file:OptIn(ExperimentalMaterialApi::class)

package com.example.zcashclienttets.ui.theme.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import com.example.zcashclienttets.testSeedPhrase

@Composable
fun InfoScreen(viewModel: InfoViewModel) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    Column {
        ListItem(
            text = { Text("Network") },
            secondaryText = { Text(viewModel.networkInfo.value) })
        ListItem(
            text = { Text("Block height") },
            secondaryText = { Text(viewModel.blockHeight.value) })
        ListItem(
            text = { Text("Seed phrase") },
            secondaryText = { Text(testSeedPhrase) })
        ListItem(
            text = { Text("Transparent Address") },
            secondaryText = { Text(viewModel.transparentAddress.value) },
            modifier = Modifier.clickable {
                viewModel.copyToClipboard(context, clipboardManager, viewModel.transparentAddress.value)
            })
        ListItem(
            text = { Text("Shielded Address") },
            secondaryText = { Text(viewModel.shieldedAddress.value) },
            modifier = Modifier.clickable {
                viewModel.copyToClipboard(context, clipboardManager, viewModel.shieldedAddress.value)
            })
    }
}