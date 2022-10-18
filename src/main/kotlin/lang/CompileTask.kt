package lang

import java.io.File

class CompileTask(val file: File, val outputEvent: (line: String) -> Unit): Task {
    private val resultFile = File("${file.path}.jar")
    override fun invoke(): File {
        outputEvent("Compiling...\n")

        val proc = Runtime.getRuntime().exec("kotlinc ${file.path} -include-runtime -d ${resultFile.path}")

        while(proc.isAlive) {
            val err = proc.errorStream.bufferedReader().readText()
            val out = proc.inputStream.bufferedReader().readText()
            val sum = err + out

            if(sum.isNotEmpty()) {
                outputEvent(sum)
            }
        }

        return resultFile
    }
}