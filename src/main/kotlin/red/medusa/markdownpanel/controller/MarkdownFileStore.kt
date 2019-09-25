package red.medusa.markdownpanel.controller

import red.medusa.markdownpanel.Data
import tornadofx.Controller
import tornadofx.SortedFilteredList
import java.io.File

class MarkdownFileStore : Controller() {

    val mkFiles = SortedFilteredList<File>()

    init {
        val filePath = Data::class.java.classLoader.getResource("data")?.file
        filePath?.let {
            File(it).walk()
                .forEach { file ->
                    if (file.exists() && file.extension == "md") {
                        mkFiles.add(file)
                    }
                }
        }
    }
}