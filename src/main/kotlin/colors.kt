import androidx.compose.ui.graphics.Color

val backgroundColor = Color(43, 43, 43)
val defaultTextColor = Color(166,184,200)
val codeColors: HashMap<String, Color> = HashMap<String, Color>().apply {
    put("val$", Color(217,115,23))
    put("var$", Color(217,115,23))
    put("const$", Color(217,115,23))
    put("fun$", Color(198,106,28))
    put("class$", Color(198,106,28))
    put("interface$", Color(198,106,28))
    put("\\{", Color(159,116,174))
    put("\\}", Color(159,116,174))
    put(""""(.*)"""", Color(98,136,84))
}