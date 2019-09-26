package red.medusa.markdownpanel.app

import red.medusa.markdownpanel.view.MarkdownFileListView
import tornadofx.App
import tornadofx.launch

class MyApp : App(MarkdownFileListView::class){
    companion object {
        val WIDTH = 950.0
        val HEIGHT = 800.0
    }
}

fun main() {
//    Profile.TEST = false
    launch<MyApp>()
}