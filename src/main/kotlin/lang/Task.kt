package lang

import java.io.File

interface Task {
    fun invoke(): File
}