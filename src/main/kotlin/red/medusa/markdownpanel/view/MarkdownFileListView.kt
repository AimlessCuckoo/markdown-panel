package red.medusa.markdownpanel.view

import javafx.scene.layout.Priority
import red.medusa.markdownpanel.controller.MarkdownFileStore
import red.medusa.markdownpanel.model.MKFileModel
import tornadofx.*

class MarkdownFileListView : View() {

    private val fileModel: MKFileModel by inject()
    private val markdownFileStore: MarkdownFileStore by inject()

    override val root = vbox {
        listview(markdownFileStore.mkFiles) {
            prefWidth = 350.0
            prefHeight = 400.0
            style {
                fontSize = 22.px
                fontFamily = "Comic Sans MS"
            }
            vgrow = Priority.ALWAYS
            cellFormat {
                graphic = label(it.name)
            }
            bindSelected(fileModel)
            setOnMouseClicked {
                if (fileModel.item != null && fileModel.item.exists())
                   find<MarkdownPanelFragment>().openWindow(owner = null)
            }
        }
    }
}

