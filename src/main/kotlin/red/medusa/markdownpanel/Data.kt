package red.medusa.markdownpanel

import java.io.File

class Data(file: File) {

    companion object {
        var mkFile: File? = null
    }

    private val lineData: LineData = LineData(file.readLines())

    init {
        mkFile = file.parentFile
    }

    fun prepared(): Data {
        while (lineData.hasMore()) {
            lineData.preNext()
            lineData.parseStrategy.lookFor(lineData)
        }
        return this
    }

    fun parse(): Data {
        lineData.lines.values.forEach {
            it.tagParse.parse(it)
        }
        return this
    }

    fun getLines() = lineData.lines.values
}