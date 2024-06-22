package fr.badetitou.bibliositpi

import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import fr.badetitou.bibliositpi.model.api.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceDetailScreen(navController: NavHostController, resource: Resource) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(resource.Ttl)
        })
    }, content = { innerPadding ->
        Box(modifier = Modifier.padding(all = 16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(innerPadding)
            ) {
                Column {
                    AsyncImage(
                        model = "https://covers.openlibrary.org/b/isbn/${resource.Id.removePrefix("isbn:")}-M.jpg",
                        contentDescription = null,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(all = 8.dp).fillMaxWidth()
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

        }
    })
}