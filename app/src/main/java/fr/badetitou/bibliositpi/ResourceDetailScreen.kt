package fr.badetitou.bibliositpi

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.paging.PagingData
import androidx.paging.cachedIn
import coil.compose.AsyncImage
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.badetitou.bibliositpi.model.api.Resource
import fr.badetitou.bibliositpi.model.api.SearchResultMetadata
import fr.badetitou.bibliositpi.model.api.holdings.Holding
import fr.badetitou.bibliositpi.model.api.holdings.HoldingRecordAnswer
import fr.badetitou.bibliositpi.model.api.holdings.SearchHoldingRecord
import fr.badetitou.bibliositpi.model.api.holdings.SearchHoldingRecordMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceDetailScreen(navController: NavHostController, resource: Resource) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(resource.Ttl)
        })
    }, content = { innerPadding ->
        Box(modifier = Modifier.padding(all = 16.dp)) {
            Column (modifier = Modifier.padding(innerPadding)){
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),

                ) {
                    Column {
                        AsyncImage(
                            model = "https://covers.openlibrary.org/b/isbn/${
                                resource.Id.removePrefix(
                                    "isbn:"
                                )
                            }-M.jpg",
                            contentDescription = null,
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "${resource.Crtr} | ${resource.Dt}",
                            textAlign = TextAlign.Left,
                            maxLines = 1,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = resource.Desc,
                            textAlign = TextAlign.Left,
                            maxLines = 3,
                            style = MaterialTheme.typography.bodySmall
                        )

                    }
                }
                OutlinedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                ) {
                    Column {
                        Text(
                            text = "Se procurer le document",
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center,
                        )
                        ListOfHoldings(resource, navController)
                    }
                }
            }
        }
    })
}

@HiltViewModel
class HoldingsViewModel @Inject constructor() : ViewModel() {
    private val _holdingsListResponse = mutableStateListOf<Holding>()
    var errorMessage: String by mutableStateOf("")
    val holdingsListResponse: List<Holding> = _holdingsListResponse

    fun getHoldings(resource: Resource) {
        viewModelScope.launch {
            try {
                val holdingsRecord = SitpiClient.apiService.getHoldings(
                    searchRequest = SearchHoldingRecord(
                        SearchHoldingRecordMetadata(
                            resource.RscId, resource.RscBase
                        )
                    )
                )
                _holdingsListResponse.clear()
                _holdingsListResponse.addAll(holdingsRecord.d.Holdings)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}


@Composable
fun ListOfHoldings(resource: Resource, navController: NavHostController) {
    val viewModel = hiltViewModel<HoldingsViewModel>()
    LaunchedEffect(Unit, block = {
        viewModel.getHoldings(resource)
    })

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(),
    ) {
        items(
            count = viewModel.holdingsListResponse.size,
        ) { index ->
            HoldingItem(viewModel.holdingsListResponse[index], navController)
        }
    }
}

@Composable
fun HoldingItem(holding: Holding, navController: NavHostController) {
    val s = buildString {
        if (holding.Statut == "En prêt") {
            append("Retour prévu le ")
            append(holding.WhenBack)
        } else {
            append("Disponible")
        }
        append(" à ")
        append(holding.Site)
    }
    Column {
        // Short call to action
        Text(s, style = MaterialTheme.typography.titleSmall)
        // Description
        Text("${holding.Cote} | ${holding.Section} | ${holding.Type} | ${holding.Statut}",
            style = MaterialTheme.typography.bodySmall)
        Row (horizontalArrangement = Arrangement.Absolute.Right){
            Button(onClick = { navController.navigate(Screen.LOGIN.name) },
                shape = MaterialTheme.shapes.small
            ) {
                Text("Emprunter")
            }
        }


    }
}
