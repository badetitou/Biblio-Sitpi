package fr.badetitou.bibliositpi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.badetitou.bibliositpi.model.api.Resource
import fr.badetitou.bibliositpi.model.api.SearchAnswer
import fr.badetitou.bibliositpi.model.api.SearchQuery
import fr.badetitou.bibliositpi.model.api.SearchRequest
import fr.badetitou.bibliositpi.model.api.SearchResultMetadata
import fr.badetitou.bibliositpi.ui.theme.BiblioSitpiTheme
import retrofit2.Callback

class MainActivity : ComponentActivity() {

    private val listOfDocument = mutableStateListOf<SearchResultMetadata>()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BiblioSitpiTheme {
                Scaffold(topBar = {
                    DocumentSearchBar()
                }, content = { innerPadding ->
                    Box(modifier = Modifier.padding(all = 16.dp)) {
                        ListOfDocument(
                            innerPadding = innerPadding
                        )
                    }
                })
            }
        }
    }

    private fun search(query: String) {
        val call = SitpiClient.apiService.search(
            SearchRequest(
                SearchQuery(QueryString = query)
            )
        )
        call.enqueue(object : Callback<SearchAnswer> {

            override fun onResponse(
                call: retrofit2.Call<SearchAnswer>, response: retrofit2.Response<SearchAnswer>
            ) {
                listOfDocument.addAll(response.body()?.d?.Results ?: listOf())
                Log.v("list of document ", listOfDocument.toString())
            }

            override fun onFailure(call: retrofit2.Call<SearchAnswer>, t: Throwable) {
                Log.v("Search error ", t.message.toString())
            }

        })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DocumentSearchBar() {
        var text by rememberSaveable { mutableStateOf("") }
        var expanded by rememberSaveable { mutableStateOf(false) }

        SearchBar(modifier = Modifier.fillMaxWidth(),
            query = text,
            onQueryChange = {
                text = it
                listOfDocument.clear()
            },
            onSearch = {
                expanded = false
                search(it)
            },
            active = expanded,
            onActiveChange = { expanded = it },
            placeholder = { Text("Search") }) { }
    }

    @Composable
    fun ListOfDocument(innerPadding: PaddingValues = PaddingValues(0.dp)) {
        LazyColumn(
            contentPadding = innerPadding, modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),

            ) {
            items(
                count = listOfDocument.size
            ) { index ->
                DocumentItem(resource = listOfDocument[index].Resource)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DocumentItem(resource: Resource) {
        ElevatedCard(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
            Row (horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column {
                    AsyncImage(
                        model = "https://covers.openlibrary.org/b/isbn/${resource.Id.removePrefix("isbn:")}-M.jpg",
                        contentDescription = null,
                    )
                }
                Column (
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ){
                    Text(
                        text = "${resource.Ttl}",
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "${resource.Dt}",
                        textAlign = TextAlign.Left,
                        maxLines = 1
                    )
                    Text(
                        text = "${resource.Desc}",
                        textAlign = TextAlign.Left,
                        maxLines = 3
                    )
                }

            }

        }

    }
}


