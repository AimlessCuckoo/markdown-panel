package red.medusa.markdownpanel

import java.io.File
import java.util.logging.Logger

class Data {
    companion object {
        var mkFile: File? = null
        val lineData by lazy {
            loadLines()
        }

        private fun loadLines(): LineData {
            val logger = Logger.getLogger(Data::class.simpleName)
            logger.i("loading lines start ...")
            val filePath = Data::class.java.classLoader.getResource("data")?.file
            filePath?.let {
                File(it).walk()
//                    .filter { file ->
//                        file.isFile && file.extension in listOf("md")
//                    }
                    .forEach { file ->
                        if (file.extension == "md") {
                            val lines = file.readLines()
                            mkFile = file.parentFile
                            return LineData(lines)
                        }
                    }
            }
            return LineData(listOf())
        }
    }
}