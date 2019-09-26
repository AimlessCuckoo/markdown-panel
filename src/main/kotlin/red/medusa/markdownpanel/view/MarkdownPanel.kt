package red.medusa.markdownpanel.view

import javafx.scene.Parent
import javafx.scene.layout.VBox
import red.medusa.markdownpanel.Data
import red.medusa.markdownpanel.app.MyApp.Companion.HEIGHT
import red.medusa.markdownpanel.app.MyApp.Companion.WIDTH
import red.medusa.markdownpanel.model.MKFileModel
import red.medusa.markdownpanel.service.MarkdownPanelParser
import tornadofx.*

class MarkdownPanelFragment : Fragment() {
    override val root: Parent
    private val fileModel: MKFileModel by inject()
    private val mkContent = Data(fileModel.item)
    private val parser = MarkdownPanelParser()

    init {
        this.root = initScrollPanel()
        initMarkdownPanel { this.root.content = it }
        importStylesheet(Styles::class)
        reloadStylesheetsOnFocus()
    }


    private fun initMarkdownPanel(runnable: (VBox) -> Unit) {
        runAsync(TaskStatus()) {
            /**
             * 构建Markdown 节点
             */
            mkContent
                .prepared()
                .parse()

            /**
             * 构建TornadoFX View
             */
            parser.produceView(mkContent.getLines())

        } ui {
            runnable(it)
        }
    }

    private fun initScrollPanel() = scrollpane {
        prefWidth = WIDTH
        prefHeight = HEIGHT

        isFitToHeight = true
        isFitToWidth = true

        subscribeHyperlinkEvent()

        addClass(Styles.ParagraphSegment)
    }

    private fun Component.subscribeHyperlinkEvent() {
        this.subscribe<OpenWindowForHyperlink> { event ->
            hostServices.showDocument(event.url)
        }
    }
}