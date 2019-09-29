package red.medusa.markdownpanel.view

import javafx.scene.layout.Priority
import red.medusa.markdownpanel.controller.MarkdownFileStore
import red.medusa.markdownpanel.controller.MarkdownViewStore
import red.medusa.markdownpanel.model.MKFileModel
import tornadofx.*


class MarkdownFileListView : View() {

    private val fileModel: MKFileModel by inject()
    private val markdownFileStore: MarkdownFileStore by inject()
    private val markdownViewStore: MarkdownViewStore by inject()

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

            selectionModel.selectedItemProperty()
                .addListener { _ , _, _->
                    if (fileModel.item != null && fileModel.item.exists()) {
                        val fragment = find<MarkdownPanelFragment>()
                        if (!markdownViewStore.alreadyExistsFragment.contains(fragment)) {
                            markdownViewStore.alreadyExistsFragment.add(fragment)
                            fragment.openWindow(owner = null)
                        }
                    }
                }
        }
    }
}

