package com.plcoding.cryptotracker.core.navigation

import android.widget.Toast
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.cryptotracker.core.presentation.util.ObserveAsEvents
import com.plcoding.cryptotracker.core.presentation.util.toString
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.CoinDetailScreen
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListAction
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListEvent
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListScreen
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class) //Habilita o uso de APIs
@Composable
fun AdapticeCoinListDetailPaine(
    modifier: Modifier = Modifier,
    viewModel: CoinListViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    // collectAsStateWithLifecycle() mantém o estado atualizado enquanto a tela está ativa.
    val context = LocalContext.current

    ObserveAsEvents(events = viewModel.events) { event ->
        //observa o fluxo de eventos do viewModel
        when(event) {
            is CoinListEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    //pensanod já em navegação entre telas
    //com remember, que mantém o estado do navegador durante recomposições.
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane{
                CoinListScreen(
                    state = state,
                    onAction = {action ->
                        viewModel.onAction(action)
                        //Quando uma moeda é clicada (CoinListAction.OnCoinClick),
                        // o navegador muda para o painel de detalhes
                        // (ListDetailPaneScaffoldRole.Detail)
                        when(action){
                            is CoinListAction.OnCoinClick -> {
                                navigator.navigateTo(
                                    pane = ListDetailPaneScaffoldRole.Detail
                                )
                            }
                        }
                    }
                )
            }
        },
        detailPane ={
            AnimatedPane{
                CoinDetailScreen(state = state)
            }
        },
        modifier =  Modifier
    )
}