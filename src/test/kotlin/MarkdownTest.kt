package gallery

import org.junit.Test
import red.medusa.markdownpanel.Data

class MarkdownTest {


    @Test
    fun strategyTest() {
        Data.markdownFileName = "RREAME.md"
        while (Data.lineData.hasMore()) {
            Data.lineData.preNext()
            Data.lineData.parseStrategy.lookFor()
        }
        Data.lineData.lines.forEach { it.value.tagParse.parse(it.value) }
    }

}