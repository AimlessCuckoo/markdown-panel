package red.medusa.markdownpanel.service

import javafx.scene.layout.VBox
import red.medusa.markdownpanel.Line
import red.medusa.markdownpanel.TagParse
import red.medusa.markdownpanel.view.*
import tornadofx.add
import tornadofx.clear
import kotlin.collections.set
import kotlin.reflect.KClass


class MarkdownPanelParser {
    companion object {
        private val segmentViews = mutableMapOf<KClass<*>, Segment>().apply {
            this[OneTitleSegment::class] = OneTitleSegment()
            this[TowTitleSegment::class] = TowTitleSegment()
            this[ThreeTitleSegment::class] = ThreeTitleSegment()
            this[FourTitleSegment::class] = FourTitleSegment()
            this[FiveTitleSegment::class] = FiveTitleSegment()
            this[SixTitleSegment::class] = SixTitleSegment()

            this[BlankSegment::class] = BlankSegment()
            this[LinefeedSegment::class] = LinefeedSegment()

            this[SeparatorSegment::class] = SeparatorSegment()

            this[OrderedSegment::class] = OrderedSegment()
            this[UnOrderedSegment::class] = UnOrderedSegment()
            this[NestOrderedSegment::class] = NestOrderedSegment()
            this[NestUnOrderedSegment::class] = NestUnOrderedSegment()

            this[ParagraphSegment::class] = ParagraphSegment()

            this[CodeArea1Segment::class] = CodeArea1Segment()
            this[CodeArea2Segment::class] = CodeArea2Segment()

            this[LinkSegment::class] = LinkSegment()
            this[ImageSegment::class] = ImageSegment()
            this[LabelWordHighlightSegment::class] = LabelWordHighlightSegment()
        }
    }

    private var chunkedLineNumber = -1
    private var chunkedStart = false
    private var chunkedRunning = false
    private var chunkedTagSegment: KClass<*> = ParagraphSegment::class
    private val markdownPanel = VBox()

    private val chunked: StringBuilder = StringBuilder()

    private var lastLinefeed: Line? = null


    /**
     * 生成 TornadoFX面板
     */
    fun produceView(lines: Collection<Line>): VBox {
        markdownPanel.clear()
        lines.forEach {
            when (it.tagParse) {
                // 出于方便使用的目的,不遵循传统的两换行才算换行的规范了
//                TagParse.TagType.PARAGRAPH -> {
//                    it.maybeNeedToMakeChunked(isNeedTrim = true, isNeedLinefeed = false)
//                }
                TagParse.TagType.CODE_AREA_1 -> {
                    it.maybeNeedToMakeChunked()
                }
                TagParse.TagType.CODE_AREA_2 -> {
                    it.maybeNeedToMakeChunkedForNotAppend()
                    if (it.prefix == null && it.postfix == null) {
                        maybeAppendChunked(it.text)
                    } else if (it.postfix?.contains("```") == true) {
                        flushChunked()
                    }
                }
                TagParse.TagType.LINEFEED -> {
                    // 上一次已经有换行了就不换了
                    if (lastLinefeed?.tagParse != TagParse.TagType.LINEFEED) {
                        addPanel(it)
                    }
                }
                else -> {
                    if (isChunkedRunning())
                        flushChunked()
                    addPanel(it)
                }
            }

        }
        if (isChunkedRunning())
            flushChunked()
        return markdownPanel
    }

    /**
     * 主要针对于那些不能用后缀判断结束的标签
     */
    private fun Line.maybeNeedToMakeChunked(isNeedTrim: Boolean = false, isNeedLinefeed: Boolean = true) {
        maybeFlushChunked(this)
        maybeStartChunked(this)
        maybeAppendChunked(this.text, isNeedTrim = isNeedTrim, isNeedLinefeed = isNeedLinefeed)
    }

    /**
     * 主要提供给使用后缀判断的标签
     */
    private fun Line.maybeNeedToMakeChunkedForNotAppend() {
        maybeFlushChunked(this)
        maybeStartChunked(this)
    }

    private fun maybeStartChunked(line: Line) {
        if (!chunkedRunning) {
            chunked.clear()
            chunkedStart = true
            chunkedRunning = true
            chunkedTagSegment = line.segmentView
            chunkedLineNumber = line.number
            lastLinefeed = line
        }
    }

    private fun maybeFlushChunked(line: Line) {
        if ((line.segmentView != chunkedTagSegment && isChunkedRunning()) || !line.isLine)
            flushChunked()
    }

    private fun maybeAppendChunked(text: String, isNeedTrim: Boolean = false, isNeedLinefeed: Boolean = true) {
        if (isChunkedRunning()) {
            val textVar = if (isNeedTrim) text.trim() else text
            chunked.append(textVar)
            if (isNeedLinefeed)
                chunked.append(System.lineSeparator())
        }
    }

    private fun isChunkedRunning() = chunkedStart && chunkedRunning
    private fun flushChunked() {
        addPanel(chunkedTagSegment, chunked.toString(), null, null, chunkedLineNumber)
        endChunked()
    }

    private fun endChunked() {
        if (chunkedRunning) {
            chunkedStart = false
            chunkedRunning = false
            chunkedTagSegment = ParagraphSegment::class
            chunkedLineNumber = -1
            chunked.clear()
        }
    }

    private fun addPanel(
        segment: KClass<*>,
        text: String,
        prefix: String? = null,
        postfix: String? = null,
        lineNumber: Int? = null
    ) {
        segmentViews[segment]?.getSegment(text, prefix, postfix, lineNumber).apply { markdownPanel.add(this!!) }
    }

    private fun addPanel(line: Line) {

        segmentViews[line.segmentView]?.getSegment(line.text, line.prefix, line.postfix, line.number, line.inlineText)
            ?.apply { markdownPanel.add(this) }

        lastLinefeed = line
    }
}












