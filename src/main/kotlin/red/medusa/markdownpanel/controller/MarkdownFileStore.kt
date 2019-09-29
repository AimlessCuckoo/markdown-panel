package red.medusa.markdownpanel.controller

import red.medusa.markdownpanel.MKData
import tornadofx.Controller
import tornadofx.SortedFilteredList
import java.io.File

class MarkdownFileStore : Controller() {

    val mkFiles = SortedFilteredList<File>()

    init {
        val filePath = MKData::class.java.classLoader.getResource("data")?.file
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