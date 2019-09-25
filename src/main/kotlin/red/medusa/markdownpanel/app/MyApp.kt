package red.medusa.markdownpanel.app

import red.medusa.markdownpanel.Profile
import red.medusa.markdownpanel.view.MarkdownFileListView
import tornadofx.App
import tornadofx.launch

class MyApp : App(MarkdownFileListView::class)

fun main() {
    Profile.TEST = false
    launch<MyApp>()
}