package red.medusa.markdownpanel.app

import red.medusa.markdownpanel.view.MarkdownFileListView
import tornadofx.App
import tornadofx.launch

class MyApp : App(MarkdownFileListView::class) {
    companion object {
        const val WIDTH = 950.0
        const val HEIGHT = 800.0
    }

}

fun main() {
//    打开调试信息
//    Profile.TEST = false
    launch<MyApp>()
}