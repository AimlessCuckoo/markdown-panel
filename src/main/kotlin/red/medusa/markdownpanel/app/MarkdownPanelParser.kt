package red.medusa.markdownpanel.app

import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import red.medusa.markdownpanel.*
import tornadofx.*
import kotlin.collections.set
import kotlin.reflect.KClass


class MarkdownPanelParser(private var lines: Collection<Line>) {
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

            this[ParagraphSegment::class] = ParagraphSegment()

            this[CodeArea1Segment::class] = CodeArea1Segment()
            this[CodeArea2Segment::class] = CodeArea2Segment()

            this[LinkSegment::class] = LinkSegment()
            this[ImageSegment::class] = ImageSegment()
        }
    }

    private var chunkedLineNumber = -1
    private var chunkedStart = false
    private var chunkedRunning = false
    private var chunkedTagParse: KClass<*> = ParagraphSegment::class
    private val panels = mutableListOf<Pane>()

    private val chunked: StringBuilder = StringBuilder()

    private var lastLinefeed: Line? = null

    /**
     * 仅解析文本
     */
    fun parse() {
        lines.forEach {
            it.tagParse.parse(it)
        }
    }

    /**
     * 生成 TornadoFX面板
     */
    fun produceView(): List<Pane> {
        panels.clear()
        lines.forEach {
            when (it.tagParse) {
                TagParse.TagType.CODE_AREA_1 -> {
                    startChunked(it)
                    appendChunked(it.text)
                }
                TagParse.TagType.CODE_AREA_2 -> {
                    startChunked(it)
                    if (it.prefix == null && it.postfix == null) {
                        appendChunked(it.text)
                    } else {
                        flushChunked()
                    }
                }
                TagParse.TagType.LINEFEED ->
                    // 上一次已经有换行了就不换了
                    if (lastLinefeed?.tagParse != TagParse.TagType.LINEFEED) {
                        addPanel(it)
                    }
                else -> {
                    if (isChunkedRunning())
                        flushChunked()

                    if (it.tagParse == TagParse.TagType.PARAGRAPH)
                        addPanelForParagraph(it)
                    else
                        addPanel(it)
                }
            }
        }
        return panels
    }

    private fun appendChunked(text: String) {
        chunked.append(text)
        chunked.append(System.lineSeparator())
    }

    private fun isChunkedRunning() = chunkedStart && chunkedRunning

    private fun startChunked(line: Line) {
        if (!chunkedRunning) {
            chunked.clear()
            chunkedStart = true
            chunkedRunning = true
            chunkedTagParse = line.segmentView
            chunkedLineNumber = line.number
            lastLinefeed = line
        }
    }

    private fun endChunked() {
        if (chunkedRunning) {
            chunkedStart = false
            chunkedRunning = false
            chunkedTagParse = ParagraphSegment::class
            chunkedLineNumber = -1
            chunked.clear()
        }
    }

    private fun flushChunked() {
        addPanel(chunkedTagParse, chunked.toString(), null, null, chunkedLineNumber)
        endChunked()
    }

    private fun addPanel(
        segment: KClass<*>,
        text: String,
        prefix: String? = null,
        postfix: String? = null,
        lineNumber: Int? = null
    ) {
        segmentViews[segment]?.getSegment(text, prefix, postfix, lineNumber).apply { panels.add(this!!) }
    }

    private fun addPanel(line: Line) {

        segmentViews[line.segmentView]?.getSegment(line.text, line.prefix, line.postfix, line.number, line.inlineText)
            ?.apply { panels.add(this) }

        lastLinefeed = line
    }

    private fun addPanelWithLinefeed(line: Line) {
        segmentViews[line.segmentView]?.getSegment(
            "${line.text}${System.lineSeparator()}",
            line.prefix,
            line.postfix,
            line.number
        )
            ?.apply { panels.add(this) }

        lastLinefeed = line
    }

    private fun addPanelForParagraph(line: Line) {
        if (
//            lastLinefeed?.tagParse != TagParse.TagType.BLANK &&
            lastLinefeed?.tagParse != TagParse.TagType.LINEFEED &&
            lastLinefeed?.tagParse != TagParse.TagType.PARAGRAPH
        ) {
            segmentViews[line.segmentView]?.getSegment(
                "${System.lineSeparator()}${line.text}",
                line.prefix,
                line.postfix,
                line.number
            )?.apply { panels.add(this) }
        } else {
            addPanel(line)
        }
        lastLinefeed = line
    }
}

class MarkdownPanel : View() {
    private val WIDTH = 950.0
    private val HEIGTH = 800.0
    private val parser =
        MarkdownPanelParser(Data.lineData.lines.values)

    override val root =
        scrollpane {
            vbox {
                setPrefSize(WIDTH, HEIGTH)
                addClass(Styles.MarkdownPanel)
                hgrow = Priority.ALWAYS
                useMaxSize = true
                while (Data.lineData.hasMore()) {
                    Data.lineData.preNext()
                    Data.lineData.parseStrategy.lookFor()
                }
                parser.parse()
                parser.produceView().forEach {
                    it.attachTo(this)
                }
            }
        }

    init {
        importStylesheet(Styles::class)
        reloadStylesheetsOnFocus()
    }
}

class MarkdownPanelApp : App(MarkdownPanel::class)

fun main() {

    //  Debug
    //  Profile.TEST = false
    launch<MarkdownPanelApp>()
}










