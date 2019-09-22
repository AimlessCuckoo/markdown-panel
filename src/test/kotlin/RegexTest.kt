package gallery

import org.junit.Test

class RegexTest {


    @Test
    fun findImage() {
        val regex = """!\[alt( .*)?]\(([a-zA-Z0-9\u4e00-\u9fa5._!@#$%^&*()=+`\/-]+)( ".*")?\)""".toRegex()
            .find("![alt 属性文本](./A.png \"可选标题\")")?.destructured
        println("1" + regex?.component1())
        println("2" + regex?.component2())
        println("3" + regex?.component3())
        println("4" + regex?.component4())
        println("5" + regex?.component5())

    }
}