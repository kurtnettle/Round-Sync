package ca.pkay.rcloneexplorer.ui.compose

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.pkay.rcloneexplorer.ui.compose.viewmodels.WebGuiViewModel

@Composable
fun WebGuiScreen(webGuiViewModel: WebGuiViewModel) {
    val commandOutput by webGuiViewModel.commandOutput.collectAsState()
    val addr by webGuiViewModel.addr.collectAsState()
    val isRunning by webGuiViewModel.isRunning.collectAsState()

    val outputBoxScrollState = rememberScrollState()
    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                label = { Text("Server Address") },
                value = addr,
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                onValueChange = { webGuiViewModel.setAddr(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (!isRunning) {
                        webGuiViewModel.start()
                    } else {
                        webGuiViewModel.stop()
                    }
                }, modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(text = if (isRunning) "Stop" else "Start")
            }

            Icon(
                imageVector = Icons.Default.ClearAll,
                contentDescription = "clear all logs",
                modifier = Modifier
                    .size(24.dp)
                    .padding(2.dp)
                    .align(Alignment.End)
                    .clickable {
                        webGuiViewModel.clear()
                    },
                tint = MaterialTheme.colorScheme.onSurface
            )
            RcloneProcessOutputBox(
                modifier = Modifier.weight(1f), commandOutput, outputBoxScrollState
            )
        }
    }
}

@Composable
fun RcloneProcessOutputBox(
    modifier: Modifier = Modifier, commandOutput: String, scrollState: ScrollState
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(0.7f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(8.dp)
            .verticalScroll(scrollState)
    ) {
        SelectionContainer {
            Text(
                text = commandOutput,
                color = MaterialTheme.colorScheme.onSurface,
                style = LocalTextStyle.current.copy(
                    fontFamily = FontFamily.SansSerif, fontSize = 10.sp, lineHeight = 14.sp
                )
            )
        }
    }

    LaunchedEffect(commandOutput) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }
}
