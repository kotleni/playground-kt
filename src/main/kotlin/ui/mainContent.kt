package ui

import ColorsTransformation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import backgroundColor
import lang.CompileTask
import lang.RunTask
import java.io.File
import kotlin.concurrent.thread

var lastChar = ' '

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun mainContent() {
    var isConsoleShow by remember { mutableStateOf(false) }
    var consoleText by remember { mutableStateOf("") }
    var text by remember {
        mutableStateOf(
            TextFieldValue(
                text = "fun main() {\n\n}"
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        Row {
            Spacer(modifier = Modifier.fillMaxWidth().weight(1f))
            TextButton(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                onClick = {
                    thread {
                        consoleText = ""
                        isConsoleShow = true

                        val ktFile = File("/tmp/code.kt")
                        ktFile.writeText(text.text)

                        val compileTask = CompileTask(ktFile) { text ->
                            consoleText += text
                        }

                        val jarFile = compileTask.invoke()

                        val runTask = RunTask(jarFile) { text ->
                            consoleText += text
                        }

                        runTask.invoke()
                    }
                },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
            ) {
                Text("Run")
            }
        }

        TextField(
            modifier = Modifier.fillMaxSize().weight(1f).onPreviewKeyEvent {
                if(it.key == Key.Enter) {
                    //println(lastChar)
                    if(lastChar == '{' && it.type == KeyEventType.KeyDown) {
                        text = TextFieldValue(
                            text = text.text.plus("\n\t\n}"),
                            selection = TextRange(text.text.length + 1)
                        )
                        return@onPreviewKeyEvent true
                    }
                }

                return@onPreviewKeyEvent false
            },
            value = text,
            onValueChange = {
                if(it.text.replace(text.text, "").isNotEmpty()) {
                    lastChar = it.text.replace(text.text, "").first()
                }
                text = it
            },
            visualTransformation = ColorsTransformation()
        )

        if(isConsoleShow) {
            Column {
                TextButton(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    onClick = {
                        isConsoleShow = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                ) {
                    Text("Close")
                }
                Text(
                    text = consoleText,
                    style = TextStyle(color = Color.White),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}