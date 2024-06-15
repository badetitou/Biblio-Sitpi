package fr.badetitou.bibliositpi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val username = remember { mutableStateOf(TextFieldValue())}
        val password = remember { mutableStateOf(TextFieldValue())}

        Text(text = "Login", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(20.dp))
        TextField(value = username.value, onValueChange = {username.value = it}, label = { Text(text = "Username")})
        Spacer(modifier = Modifier.height(10.dp))
        TextField(value = password.value, onValueChange = {password.value = it}, label = { Text(text = "Password")})
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { /*TODO*/ },
            shape = MaterialTheme.shapes.medium) {
            Text(text = "Login")
        }

    }
}