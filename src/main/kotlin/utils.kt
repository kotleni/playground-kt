import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle

// parse code words in string
fun String.parseWords(): List<String> {
    val words = ArrayList<String>()
    var buffer = ""
    var isStringMode = false

    forEach {
        if(isStringMode) {
            if(it == '"') {
                words.add(buffer + '"')
                isStringMode = false
                buffer = ""
            } else {
                buffer += it
            }
        } else {
            when(it) {
                ' ', '\n', '{', '}' -> {
                    if(buffer.isNotEmpty()) {
                        words.add(buffer)
                        buffer = ""
                    }

                    words.add(it.toString())
                }

                '"' -> {
                    isStringMode = true
                    buffer += it
                }

                else -> {
                    buffer += it
                }
            }
        }
    }

    if(buffer.isNotEmpty()) {
        words.add(buffer)
        buffer = ""
    }

    //println(words)
    return words
}

fun buildAnnotatedStringWithColors(text: String): AnnotatedString {
    val words: List<String> = text.parseWords()

    val builder = AnnotatedString.Builder()
    for (word in words) {
        var color = defaultTextColor
        codeColors.keys.forEach {
            if(word.contains(it.toRegex())) {
                color = codeColors[it]!!
            }
        }

        builder.withStyle(style = SpanStyle(color = color)) {
            append("$word")
        }
    }
    return builder.toAnnotatedString()
}