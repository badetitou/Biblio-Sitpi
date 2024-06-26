package fr.badetitou.bibliositpi

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.badetitou.bibliositpi.model.api.Resource
import fr.badetitou.bibliositpi.model.api.SearchQuery
import fr.badetitou.bibliositpi.model.api.SearchRequest
import fr.badetitou.bibliositpi.model.api.SearchResultMetadata
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Composable
fun HomeScreen(navController: NavHostController) {


    var text by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            DocumentSearchBar(onQueryChange = { text = it }, text = text)
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(all = 16.dp)) {
                ListOfDocument(
                    innerPadding = innerPadding, textQuery = text, navController = navController
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentSearchBar(onQueryChange: (String) -> Unit, text: String = "") {
    var expanded by rememberSaveable { mutableStateOf(false) }

    SearchBar(modifier = Modifier.fillMaxWidth(),
        query = text,
        onQueryChange = onQueryChange,
        onSearch = {
            expanded = false
        },
        active = expanded,
        onActiveChange = { expanded = it },
        placeholder = { Text(stringResource(R.string.search)) }) { }
}

class ResourcePagingSource(private var query: String) : PagingSource<Int, SearchResultMetadata>() {
    override fun getRefreshKey(state: PagingState<Int, SearchResultMetadata>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResultMetadata> {
        return try {
            val page = params.key ?: 1

            val response = SitpiClient.apiService.searchPaginated(
                SearchRequest(
                    SearchQuery(
                        QueryString = query, Page = page
                    )
                )
            )

            LoadResult.Page(
                data = response.d.Results,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.d.Results.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

class ResourceRepository @Inject constructor(
) {
    fun getResources(query: String) = Pager(config = PagingConfig(
        pageSize = 5,
    ), pagingSourceFactory = {
        ResourcePagingSource(query)
    }).flow
}

@HiltViewModel
class ResourceViewModel @Inject constructor(
    private val repository: ResourceRepository,
) : ViewModel() {

    fun getResultsMetadatas(query: String): Flow<PagingData<SearchResultMetadata>> =
        repository.getResources(query).cachedIn(viewModelScope)
}


@Composable
fun ListOfDocument(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    textQuery: String,
    navController: NavHostController
) {

    val viewModel = hiltViewModel<ResourceViewModel>()
    val documents = viewModel.getResultsMetadatas(textQuery).collectAsLazyPagingItems()

    LazyColumn(
        contentPadding = innerPadding, modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),

        ) {
        items(
            count = documents.itemCount
        ) { index ->
            DocumentItem(resource = documents[index]!!.Resource, navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentItem(resource: Resource, navController: NavHostController) {
    ElevatedCard(modifier = Modifier.fillMaxWidth(),
        onClick = {
            navController.navigate(resource)
        }) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column {
                AsyncImage(
                    model = "https://covers.openlibrary.org/b/isbn/${resource.Id.removePrefix("isbn:")}-M.jpg",
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
                    text = resource.Ttl,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall
                )
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

    }

}