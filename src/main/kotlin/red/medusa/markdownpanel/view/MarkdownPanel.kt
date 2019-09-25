package red.medusa.markdownpanel.view

import red.medusa.markdownpanel.Data
import red.medusa.markdownpanel.app.MyApp.Companion.HEIGHT
import red.medusa.markdownpanel.app.MyApp.Companion.WIDTH
import red.medusa.markdownpanel.model.MKFileModel
import red.medusa.markdownpanel.service.MarkdownPanelParser
import tornadofx.*

class MarkdownPanelFragment : Fragment() {

    private val fileModel: MKFileModel by inject()

    private val mkContent = Data(fileModel.item)

    private val parser = MarkdownPanelParser()

    override val root = scrollpane {
        /**
         * 构建Markdown 节点
         */
        mkContent
            .prepared()
            .parse()

        /**
         * 构建TornadoFX View
         */
        val markdownPanel = parser.produceView(mkContent.getLines())

        prefWidth = WIDTH
        prefHeight = HEIGHT

        isFitToHeight = true
        isFitToWidth = true
        content = markdownPanel

        //点击超链接可打开游览器
        subscribeHyperlinkEvent()

        vvalue = 0.0

        addClass(Styles.ParagraphSegment)
    }

    private fun Component.subscribeHyperlinkEvent() {
        this.subscribe<OpenWindowForHyperlink> { event ->
            hostServices.showDocument(event.url)
        }
    }

    init {
        importStylesheet(Styles::class)
        reloadStylesheetsOnFocus()
    }
}