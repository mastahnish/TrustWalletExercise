package com.example.trustwallet.feature.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trustwallet.common.theme.TrustWalletTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CommandsScreen() {
    val viewModel = koinViewModel<CommandsViewModel>()
    val state by viewModel.state.collectAsState()
    CommandsScreenContent(
        state,
        viewModel::onParameter1Change,
        viewModel::onParameter2Change,
        viewModel::onCommandMenuClicked,
        viewModel::onCommandSubmit
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandsScreenContent(
    viewState: CommandsViewState,
    onParameter1Change: (String) -> Unit,
    onParameter2Change: (String) -> Unit,
    onCommandMenuClicked: () -> Unit,
    onCommandSubmit: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Simple Transactional Key Value Store APP",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )
                },
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Button(onClick = { onCommandMenuClicked() }) {
                        Text(
                            text = viewState.chosenCommand.name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                        )
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                    if (viewState.isParameter1Visible)
                        InputTextField(
                            value = viewState.parameter1,
                            onValueChange = onParameter1Change,
                            label = "Param 1",
                            modifier = Modifier.weight(1f)
                        )
                    if (viewState.isParameter2Visible)
                        InputTextField(
                            value = viewState.parameter2,
                            onValueChange = onParameter2Change,
                            label = "Param 2",
                            modifier = Modifier.weight(1f)
                        )
                    FloatingActionButton(
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
                        onClick = { },
                        modifier = Modifier
                            .width(48.dp)
                            .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Default.Send, contentDescription = "")
                    }
                }

            }
        },
        modifier = Modifier.imePadding()
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .padding(contentPadding),
        ) {
            items(viewState.commands) { command ->
                Command(
                    text = command.first,
                    isUser = command.second,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun Command(
    text: String,
    isUser: Boolean = false,
    error: String? = null
) {
    val isError = error != null
    Row(
        horizontalArrangement = if (isUser) Arrangement.Start else Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Card(
                shape = RoundedCornerShape(4.dp),
                colors = if (isUser) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                else
                    CardDefaults.cardColors(),
                modifier = Modifier.alpha(
                    if (isError) 0.5f else 1f
                )
            ) {
                Text(
                    text = text, modifier = Modifier.padding(
                        horizontal = 8.dp, vertical = 8.dp
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            if (isError)
                Row {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Error",
                        style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.error)
                    )
                }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(8.dp)
    ) {
        BasicTextField2(
            value = value,
            onValueChange = onValueChange,
            lineLimits = TextFieldLineLimits.SingleLine,
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth(),
        )
        if (value.isBlank()) {
            Text(
                label,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .alpha(0.3f)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    TrustWalletTheme {
        CommandsScreenContent(
            viewState = CommandsViewState().copy(
                commands = listOf(
                    "SET test value" to true,
                    "value" to false,
                    "BEGIN" to true,
                    "GET test" to true,
                )
            ),
            onParameter1Change = {},
            onParameter2Change = {},
            onCommandMenuClicked = {},
            onCommandSubmit = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CommmandItemPreview() {
    TrustWalletTheme {
        Command(
            text = "SET foo 123",
            isUser = true,
            error = "Error occurred!"
        )
    }
}