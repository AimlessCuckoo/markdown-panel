package gallery

import org.junit.Test
import red.medusa.markdownpanel.ParagraphSegment
import java.util.*


class InlineTest {

    private val text3 =
        """Markdown使用星号/双星、底线/双底线表示*斜体*和**粗体**(星号),_斜体_和__粗体__(底线)。粗斜体使用***三星号***或___三底线___表示,链接使用中括号表示链接名,括号表示地址[链接名称](链接地址),或直接使用尖括号<www.google.com>,图片格式![alt 属性文本](img.jpg "可选标题")或![](img.jpg)
    """.trimMargin()

    private val TEST_TEXT = text3

    private val paragraph = ParagraphSegment()

    @Test
    fun inlineTest() {
        val startTime = System.currentTimeMillis()

        var contents = LinkedList<Any>()
        contents.add(TEST_TEXT)

        contents = paragraph.parseForImage(contents)
        contents.prettyPrint("Image")

        contents = paragraph.parseForLink(contents)
        contents.prettyPrint("Link1")



        contents = paragraph.parseForLink2(contents)
        contents.prettyPrint("Link2")

        contents = paragraph.parseForBoldAndItalics(contents)
        contents.prettyPrint("BoldAndItalics")

        contents = paragraph.parseForBold(contents)
        contents.prettyPrint("Bold")

        contents = paragraph.parseForItalics(contents)
        contents.prettyPrint("Italics")

        println(System.currentTimeMillis() - startTime)
    }

    @Test
    fun inlineTest2() {
        // 链接
        val linkRegex = """\[(?!alt)(.*)]\((.*)\)""".toRegex()

        var contends = linkRegex.find("""<链接地址>""")
        println(contends?.groupValues)
    }

    private fun List<Any>.prettyPrint(name: String) {
        println("------------------- $name start... ----------------------")
        this.forEach {
            println(it)
        }
        println("------------------- $name End... ----------------------")
        println()
    }

}





















