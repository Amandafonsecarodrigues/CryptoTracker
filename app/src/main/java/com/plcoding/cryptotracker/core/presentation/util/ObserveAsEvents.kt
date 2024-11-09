package com.plcoding.cryptotracker.core.presentation.util

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun <T> ObserveAsEvents(
    events: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    /*
    usada para observar um Flow de eventos e reagir a eles enquanto
     a tela está ativa. O objetivo é garantir que os eventos sejam
      coletados e processados apenas quando o ciclo de vida da tela
      está no estado STARTED ou mais ativo,
     */
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2) {
        /*
        LaunchedEffect é um efeito que inicia uma coroutine quando o
        lifecycleOwner.lifecycle, key1, ou key2 mudam. Ele ajuda a controlar
        o ciclo de vida da coroutine, cancelando-a
        quando a tela é recombinada ou qualquer um dos parâmetros mud
         */
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                events.collect(onEvent)
            }
        }
    }
}