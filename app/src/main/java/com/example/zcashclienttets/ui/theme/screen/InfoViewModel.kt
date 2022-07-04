package com.example.zcashclienttets.ui.theme.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cash.z.ecc.android.bip39.Mnemonics
import cash.z.ecc.android.bip39.toSeed
import cash.z.ecc.android.sdk.internal.service.LightWalletGrpcService
import cash.z.ecc.android.sdk.tool.DerivationTool
import cash.z.ecc.android.sdk.type.ZcashNetwork
import com.example.zcashclienttets.testSeedPhrase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(private val lightwalletService: LightWalletGrpcService) : ViewModel() {

    val blockHeight = mutableStateOf("")
    var transparentAddress = mutableStateOf("")
    var shieldedAddress = mutableStateOf("")
    val networkInfo = mutableStateOf("")

    private val numberFormat = NumberFormat.getNumberInstance(Locale.US)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                networkInfo.value = lightwalletService.getServerInfo().chainName
                blockHeight.value = numberFormat.format(lightwalletService.getLatestBlockHeight())
                val seed = Mnemonics.MnemonicCode(testSeedPhrase).toSeed()
                transparentAddress.value = DerivationTool.deriveTransparentAddress(seed, ZcashNetwork.Testnet)
                shieldedAddress.value = DerivationTool.deriveShieldedAddress(seed, ZcashNetwork.Testnet)
            }
        }
    }

    fun copyToClipboard(context: Context, clipboardManager: ClipboardManager, text: String) {
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        clipboardManager.setText(AnnotatedString(text))
    }
}

