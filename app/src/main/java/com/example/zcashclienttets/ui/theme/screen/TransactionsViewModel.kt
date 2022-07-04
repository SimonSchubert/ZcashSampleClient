package com.example.zcashclienttets.ui.theme.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cash.z.ecc.android.bip39.Mnemonics
import cash.z.ecc.android.bip39.toSeed
import cash.z.ecc.android.sdk.internal.service.LightWalletGrpcService
import cash.z.ecc.android.sdk.tool.DerivationTool
import cash.z.ecc.android.sdk.type.ZcashNetwork
import cash.z.wallet.sdk.rpc.Service
import com.example.zcashclienttets.testSeedPhrase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(val lightwalletService: LightWalletGrpcService) : ViewModel() {

    var transactions = mutableStateOf<List<Service.GetAddressUtxosReply>>(emptyList())

    private val numberFormat = NumberFormat.getNumberInstance(Locale.US)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val seed = Mnemonics.MnemonicCode(testSeedPhrase).toSeed()
                val transparentAddress = DerivationTool.deriveTransparentAddress(seed, ZcashNetwork.Testnet)

                val rawTransactions = lightwalletService.fetchUtxos(transparentAddress, 1)

                transactions.value = rawTransactions
            }
        }
    }

    fun buildTransactionTitle(transaction: Service.GetAddressUtxosReply): String {
        return "ZAT: ${numberFormat.format(transaction.valueZat)} (Height: ${numberFormat.format(transaction.height)})"
    }

//    suspend fun setup(context: Context, coroutineScope: CoroutineScope) {
//        val network = ZcashNetwork.Testnet
//        val seedPhrase =
//            "wish puppy smile loan doll curve hole maze file ginger hair nose key relax knife witness cannon grab despair throw review deal slush frame"
//        val seed = Mnemonics.MnemonicCode(seedPhrase).toSeed()
//        val initializer =
//            Initializer.new(context) {
//                runBlocking {
//                    it.importWallet(seed, network = network)
//                }
//                it.setNetwork(network)
//            }
//        val transparent = DerivationTool.deriveTransparentAddress(seed, network)
//        address.value = DerivationTool.deriveShieldedAddress(seed, network)
//        Log.e("TAG", "address: $transparent")
//        val synchronizer = Synchronizer.newBlocking(initializer)
//
//        // transactions = synchronizer.clearedTransactions
//        synchronizer.clearedTransactions.collectWith(viewModelScope) {
//            Log.e("TAG", "trans: ${it.size}")
//        }
//        synchronizer.status.collectWith(viewModelScope) {
//            Log.e("TAG", it.name)
//        }
//        synchronizer.progress.collectWith(viewModelScope) {
//            Log.e("TAG", "progress: $it")
//        }
//        synchronizer.processorInfo.collectWith(viewModelScope) {
//            Log.e("TAG", "${it.isScanning} ${it.isDownloading}")
//        }
//
//        synchronizer.prepare()
//        val initialCount = (synchronizer as SdkSynchronizer).getTransactionCount()
//        Log.e("TAG", "count: ${initialCount}")
//
//        synchronizer.start(viewModelScope)
//
//        // (synchronizer as SdkSynchronizer).refreshTransactions()
//        synchronizer.refreshUtxos()
//
//        Log.e(
//            "TAG",
//            "${
//                lightwalletService.getTAddressTransactions(
//                    transparent,
//                    IntRange(1, lightwalletService.getLatestBlockHeight())
//                ).size
//            }"
//        )
//
//        Log.e("TAG", "ZAT: ${synchronizer.getTransparentBalance(transparent).totalZatoshi}")
//    }

}

