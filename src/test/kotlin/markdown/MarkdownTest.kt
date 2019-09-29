package markdown

import org.junit.Test
import red.medusa.markdownpanel.MKData
import red.medusa.markdownpanel.Profile
import java.io.File

class MarkdownTest {


    @Test
    fun strategyTest() {
        Profile.TEST =  true
        val filePath = MKData::class.java.classLoader.getResource("data/markdown-file/mk/A.md")?.file
        val data = MKData(File(filePath))

        data
            .prepared()         // 准备数据
            .parse()            // 构建数据

        data.getLines().forEach {
            println(it)
        }
    }

}