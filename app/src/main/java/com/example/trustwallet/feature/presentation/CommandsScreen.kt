package com.example.trustwallet.feature.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@Composable
fun CommandsScreen() {
    val viewModel = koinViewModel<CommandsViewModel>()
    val state by viewModel.state.
    CommandsScreenContent(
        listOf()
    )
}

@Composable
fun CommandsScreenContent(
    commands: List<Pair<String, Boolean>>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(commands) { command ->
                Command(
                    text = command.first,
                    isUser = command.second,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        CommandInputField()
    }
}

@Composable
fun Command(
    text: String,
    isUser: Boolean = false
) {
    Row(
        horizontalArrangement = if (isUser) Arrangement.Start else Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Text(
                text = text, modifier = Modifier.padding(
                    horizontal = 8.dp, vertical = 8.dp
                ),
                style = TextStyle(fontSize = 20.sp)
            )
        }
    }
}

@Composable
fun CommandInputField() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.Green
            )
        ) {
            Text(
                text = "SET", style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    )
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        TextField(value = "", onValueChange = {}, modifier = Modifier.weight(1f))
        TextField(value = "", onValueChange = {}, modifier = Modifier.weight(1f))
        Button(
            onClick = { },
            shape = CircleShape,
            modifier = Modifier
                .width(48.dp)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Default.Send, contentDescription = "")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    CommandsScreenContent(
        commands = listOf(
            "SET test value" to true,
            "value" to false,
            "BEGIN" to true,
            "GET test" to true,
        )
    )
}