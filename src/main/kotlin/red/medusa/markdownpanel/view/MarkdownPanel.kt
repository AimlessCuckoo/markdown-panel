package red.medusa.markdownpanel.view

import red.medusa.markdownpanel.Data
import red.medusa.markdownpanel.model.MKFileModel
import tornadofx.*

class MarkdownPanelFragment : Fragment() {

    companion object {
        val WIDTH = 950.0
        val HEIGTH = 800.0
    }

    private val fileModel: MKFileModel by inject()

    private val mkContent = Data(fileModel.item)

    private val parser = MarkdownPanelParser()

    override val root = scrollpane {
        prefWidth = WIDTH
        prefHeight = HEIGTH

        isFitToHeight = true
        isFitToWidth = true
        content = vbox {
            /**
             * 构建Markdown 节点
             */
            mkContent
                .prepared()
                .parse()

            /**
             * 构建TornadoFX View
             */
            parser.produceView(mkContent.getLines()).forEach {
               this.add(it)
            }
            // 点击超链接可打开游览器
            subscribeHyperlinkEvent()
        }
        vvalue = 0.0
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