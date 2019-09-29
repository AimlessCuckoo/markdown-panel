package red.medusa.markdownpanel

import java.io.File

class MKData(file: File) {

    companion object {
        var mkFile: File? = null
    }

    private val lineData: LineData = LineData(file.readLines())

    init {
        mkFile = file.parentFile
    }

    fun prepared(): MKData {
        pt("\n==================================phase one prepared=======================================\n")
        while (lineData.hasMore()) {
            lineData.preNext()
            lineData.parseStrategy.lookFor(lineData)
        }
        pt("\n==================================phase one already accomplish==============================\n")
        return this
    }

    fun parse(): MKData {
        pt("\n==================================phase tow prepared=======================================\n")
        lineData.lines.values.forEach {
            it.tagParse.parse(it)
        }
        pt("\n==================================phase tow already accomplish==============================\n")
        return this
    }

    fun getLines() = lineData.lines.values
}