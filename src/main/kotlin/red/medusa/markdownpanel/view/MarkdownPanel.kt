package red.medusa.markdownpanel.view

import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.layout.VBox
import red.medusa.markdownpanel.MKData
import red.medusa.markdownpanel.app.MyApp.Companion.HEIGHT
import red.medusa.markdownpanel.app.MyApp.Companion.WIDTH
import red.medusa.markdownpanel.controller.MarkdownViewStore
import red.medusa.markdownpanel.model.MKFileModel
import red.medusa.markdownpanel.service.MarkdownPanelParser
import tornadofx.*

class MarkdownPanelFragment : Fragment() {

    private val markdownViewStore: MarkdownViewStore by inject()

    override val root: Parent
    private val fileModel: MKFileModel by inject()
    private val mkContent = MKData(fileModel.item)
    private val parser = MarkdownPanelParser()

    init {

        this.root = initScrollPanel()
        initMarkdownPanel { root.content = it }

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
            parser.produceView(mkContent.getLines()).apply {
                spacing = 5.0
            }

        } ui {
            runnable(it)
        }
    }

    private fun initScrollPanel() = scrollpane {

        prefWidth = WIDTH
        prefHeight = HEIGHT

        isFitToHeight = true
        isFitToWidth = true

        padding = Insets(0.0,30.0,30.0,30.0)

        style{
            baseColor = c("353535")
            fontFamily = "Comic Sans MS"
        }

        subscribeHyperlinkEvent()

        addClass(Styles.MarkdownPanelFragment)
    }

    private fun Component.subscribeHyperlinkEvent() {
        this.subscribe<OpenWindowForHyperlink> { event ->
            hostServices.showDocument(event.url)
        }
    }

    override fun onUndock() {
        markdownViewStore.alreadyExistsFragment.remove(this)
    }
}