package regex

import org.junit.Assert
import org.junit.Test

class TagRegexTest {

    private var text = """Markdown使用星号/双星、底线/双底线表示*斜体*和**粗体**(星号),_斜体_和__粗体__(底线)。粗斜体使用***三星号***或___三底线___表示,链接使用中括号表示链接名,括号表示地址[链接名称](链接地址),或直接使用尖括号<www.google.com>,图片格式![alt 属性文本](img.jpg "可选标题")或![](img.jpg)"""

    @Test
    fun findImageTest() {
        text = """![](https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569490875869&di=63df696bd4945648d969093b25807614&imgtype=0&src=http%3A%2F%2Fpic27.nipic.com%2F20130319%2F2404952_103817736357_2.jpg) """
//
        var subRegex = """([a-zA-Z0-9\u4e00-\u9fa5._!@#$%^&*()=+`\/:-]+)"""

        val imageRegex = """!\[(alt(.*))?]\(((?:.(?!"))+?)(\s*\s"(.*?)"\s*)?\)""".toRegex()

        /**
         * Test Success
         */
        var r = imageRegex.find(text)
        ptImageGroup(r)

        /**
         * Test Success
         */
        r = imageRegex.find("""![alt 属性文本](图片地址 "可选标题")""")
        ptImageGroup(r)

        r = imageRegex.find("""![alt ](图片地址 )""")
        ptImageGroup(r)

        r = imageRegex.find("""![](图片地址)""")
        ptImageGroup(r)
        /**
         * Test Fail
         */
        r = imageRegex.find("""![alt](图片地址"标题")""")
        ptImageGroup(r)

        Assert.assertTrue(r?.destructured == null)

    }

    private fun ptImageGroup(r: MatchResult?) {
        println()
        println("匹配alt与属性文本 ====>" + r?.destructured?.component1())
        println("属性文本 ====>" + r?.destructured?.component2())
        println("图片地址 ====>" + r?.destructured?.component3())
        println("引号与可选标题 ====>" + r?.destructured?.component4())
        println("可选标题 ====>" + r?.destructured?.component5())
        println()
    }

    @Test
    fun findLinkTest() {
        val linkRegex = """\[(?!alt)(.*)]\((.*)\)""".toRegex()
        var r = linkRegex.find("链接使用中括号与圆括号定义[链接名称](www.google.com),图片![altdddddddd](img.jpg \"可选标题\")可这样定义")
        if (r != null) {
            println("名称======>" + r.destructured.component1())
            println("地址======>" + r.destructured.component2())
        }
    }

    @Test
    fun findNestList() {
        var nestRegex = Regex("""^(\s{4}|\t)+[-+*]\s""")
        var r = nestRegex.containsMatchIn("        * 嵌套第一项")
        println(r)
        nestRegex = Regex("""^(\s{4}|\t)+(\d+\.)+\s""")
        r = nestRegex.containsMatchIn("        1.1. 嵌套第一项")
        println(r)
    }

    @Test
    fun getNestList() {
        var nestRegex = Regex("""^((?:\s{4}|\t)+)([-+*])\s(.*)$""")
        var r = nestRegex.find("    * 嵌套第一项")
        if (r != null) {
            println("------>" + r.destructured.component1())
            println("------>" + r.destructured.component2())
            println("------>" + r.destructured.component3())
        }
        println()
        nestRegex = Regex("""^((?:\s{4}|\t)+)((\d+\.)+)\s(.*)$""")
        r = nestRegex.find("        1.1. 嵌套第一项")
        if (r != null) {
            println("------>" + r.destructured.component1())
            println("------>" + r.destructured.component2())
            println("------>" + r.destructured.component3())
            println("------>" + r.destructured.component4())
        }
    }
}