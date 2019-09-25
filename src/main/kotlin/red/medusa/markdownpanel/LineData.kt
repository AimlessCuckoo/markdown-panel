package red.medusa.markdownpanel

import java.util.logging.Logger

class LineData(datas: List<String>) {
    private val logger = Logger.getLogger(this.javaClass.name)
    lateinit var lines: Map<Int, Line>
    private val defaultLine = Line(-1, "", isHandle = false, isLine = false)
    var line: Line = defaultLine
    var isRequireMultipleHandle = false
    var parseStrategy: ParseStrategy = ParseStrategy.DOUBLE_LINEFEED
    private var processLine: Int = -1
    private var isFinished: Boolean = false


    init {
        setLines(datas)
    }

    fun preNext() {
        this.line = lines.getOrDefault(++processLine, defaultLine)
    }

    fun makeHandle() {
        line.isHandle = true
    }

    fun makeFinished() {
        isFinished = true
    }

    fun hasMore(): Boolean = processLine < lines.size

    // 获取上N次记录
    fun getLastLines(num: Int): List<Line> {
        val from = processLine - num + 1
        val list = mutableListOf<Line>()
        (from until processLine).forEach { lines[it]?.apply { list.add(this) } }
        return list
    }

    // 获取上两次记录
    fun getLastDoubleLines(): List<Line> {
        val list = mutableListOf<Line>()
        lines[processLine - 2]?.apply { list.add(this) }
        lines[processLine - 1]?.apply { list.add(this) }
        return list
    }

    // 获取上一次记录
    fun getLastLine() = lines.getOrDefault(processLine - 1, defaultLine)

    // 获取前面没有被处理的记录
    fun getNotHandleLastLines() = lines.values.toList().takeLastWhile { !it.isHandle }

    fun markStart(parseStrategy: ParseStrategy) {
        isRequireMultipleHandle = true
        if (parseStrategy != ParseStrategy.OVER_FOUR_BLANK_SPACE)
            this.parseStrategy = parseStrategy
    }

    fun markEnd() {
        isRequireMultipleHandle = false
        parseStrategy = ParseStrategy.DOUBLE_LINEFEED
    }

    fun clean() {
        if (isRequireMultipleHandle && parseStrategy == ParseStrategy.OVER_FOUR_BLANK_SPACE) {
            this.markEnd()
            logger.i("${TagParse.TagType.CODE_AREA_1} END - ${processLine + 1} - [ ${this.line.text} ]")
        }
    }

    private fun setLines(lines: List<String>) {
        val map = LinkedHashMap<Int, Line>()
        lines.forEachIndexed { i, line ->
            map[i] = Line(i + 1, line)
        }
        val finishedLine = Line(-1, "")
        finishedLine.isLine = false
        map[-1] = finishedLine
        this.lines = map
    }

    fun makeUp(tagParse: TagParse) {
        this.line.tagParse = tagParse
    }




}