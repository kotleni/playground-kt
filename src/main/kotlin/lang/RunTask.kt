package lang

import java.io.File

class RunTask(val file: File, val outputEvent: (line: String) -> Unit): Task {
    override fun invoke(): File {
        outputEvent("Starting...\n")

        val proc = Runtime.getRuntime().exec("java -jar ${file.path}")
        while(proc.isAlive) {
            val err = proc.errorStream.bufferedReader().readText()
            val out = proc.inputStream.bufferedReader().readText()
            val sum = err + out

            if(sum.isNotEmpty()) {
                outputEvent(sum)
            }
        }

        outputEvent("Finished.\n")
        return file
    }
}