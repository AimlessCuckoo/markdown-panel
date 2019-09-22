package red.medusa.markdownpanel

import java.util.logging.Logger

enum class ParseStrategy {
    DOUBLE_LINEFEED {
        override fun lookFor() {
            if (lastWithCurrentLineIsBlank()) {
                log(TagParse.TagType.LINEFEED.name)
                Data.lineData.makeUp(TagParse.TagType.LINEFEED)
                Data.lineData.clean()
            } else
                LINEFEED.lookFor()
        }
    },
    LINEFEED {
        override fun lookFor() {
            return if (lineIsBlank(Data.lineData.line.text)) {
                log(TagParse.TagType.BLANK.name)
                Data.lineData.clean()
                Data.lineData.makeUp(TagParse.TagType.BLANK)
            } else OVER_FOUR_BLANK_SPACE.lookFor()
        }
    },
    OVER_FOUR_BLANK_SPACE {     // 四个空格表示代码块
        override fun lookFor() {
            if (Regex("""^[ ]{4,}""").containsMatchIn(Data.lineData.line.text)) {
                if (Data.lineData.parseStrategy != this) {
                    Data.lineData.markStart(this)
                    log("${TagParse.TagType.CODE_AREA_1} START - ${Data.lineData.line.number} - [ ${Data.lineData.line.text} ]")
                }
                Data.lineData.makeUp(TagParse.TagType.CODE_AREA_1)
                log("${TagParse.TagType.CODE_AREA_1} HANDING - ${Data.lineData.line.number} - [ ${Data.lineData.line.text} ]")
            } else {
                //不做任何结束,直接找下一个匹配项
                ANTI_POINTS.lookFor()
            }
        }
    },
    ANTI_POINTS {
        override fun lookFor() {
            if (Data.lineData.isRequireMultipleHandle && Data.lineData.parseStrategy == this) {
                if ((Regex("""```$""").containsMatchIn(Data.lineData.line.text))) {
                    log("${TagParse.TagType.CODE_AREA_2} END - ${Data.lineData.line.number} - [ ${Data.lineData.line.text} ]")
                    Data.lineData.line.postfix = "```"
                    Data.lineData.markEnd()
                }
                Data.lineData.makeUp(TagParse.TagType.CODE_AREA_2)
                log("${TagParse.TagType.CODE_AREA_2} HANDING - ${Data.lineData.line.number} - [ ${Data.lineData.line.text} ]")
                if (!Data.lineData.line.isLine) //   全部都没找到
                    Data.lineData.markEnd()
            } else {
                if (Regex("""^\s*```""").containsMatchIn(Data.lineData.line.text)) {
                    if (Data.lineData.isRequireMultipleHandle && Data.lineData.parseStrategy == OVER_FOUR_BLANK_SPACE)
                        Data.lineData.clean()
                    log("${TagParse.TagType.CODE_AREA_2} START - ${Data.lineData.line.number} - [ ${Data.lineData.line.text} ]")
                    Data.lineData.line.prefix = Data.lineData.line.text
                    Data.lineData.markStart(this)
                    Data.lineData.makeUp(TagParse.TagType.CODE_AREA_2)
                } else
                    POUND_KEY.lookFor()
            }
        }
    },
    POUND_KEY {
        override fun lookFor() {
            if (Regex("""^\s*#""").containsMatchIn(Data.lineData.line.text)) {
                Data.lineData.clean()
                Data.lineData.makeUp(TagParse.TagType.TITLE)
                log(TagParse.TagType.TITLE.name)
            } else
                NUMBER.lookFor()
        }
    },
    NUMBER {
        override fun lookFor() {
            if (Regex("""^\s*\d""").containsMatchIn(Data.lineData.line.text)) {
                Data.lineData.clean()
                Data.lineData.makeUp(TagParse.TagType.ORDERED_LIST)
                log(TagParse.TagType.ORDERED_LIST.name)
            } else THREE_PMS.lookFor()
        }
    },
    THREE_PMS {
        override fun lookFor() {
            if (Regex("""^\s*(\*\*\*)|(\+\+\+)|(---)""").containsMatchIn(
                    Data.lineData.line.text.replace(
                        "\\s".toRegex(),
                        ""
                    )
                )
            ) {
                Data.lineData.makeUp(TagParse.TagType.SEPARATOR)
                Data.lineData.clean()
                log(TagParse.TagType.SEPARATOR.name)
            } else
                PLUS.lookFor()
        }
    },
    PLUS {
        override fun lookFor() {
            if (Regex("""^\s*\+ """).containsMatchIn(Data.lineData.line.text)) {
                Data.lineData.makeUp(TagParse.TagType.UNORDERED_LIST)
                Data.lineData.clean()
                log("+ -> ${TagParse.TagType.UNORDERED_LIST}")
            } else MINUS.lookFor()
        }
    },
    MINUS {
        override fun lookFor() {
            if (Regex("""^\s*- """).containsMatchIn(Data.lineData.line.text)) {
                Data.lineData.clean()
                Data.lineData.makeUp(TagParse.TagType.UNORDERED_LIST)
                log("- -> ${TagParse.TagType.UNORDERED_LIST}")
            } else STARS.lookFor()
        }
    },
    STARS {
        override fun lookFor() {
            if (Regex("""^\s*\* """).containsMatchIn(Data.lineData.line.text)) {
                Data.lineData.clean()
                Data.lineData.makeUp(TagParse.TagType.UNORDERED_LIST)
                log("* -> ${TagParse.TagType.UNORDERED_LIST}")
            } else BRACKET.lookFor()
        }
    },
    BRACKET {
        override fun lookFor() {
            if (Regex("""^\s*\[""").containsMatchIn(Data.lineData.line.text)) {
                Data.lineData.clean()
                Data.lineData.makeUp(TagParse.InlineTagType.LINK)
                log(TagParse.InlineTagType.LINK.name)
            } else EXCLAMATION_BRACKET.lookFor()
        }
    },
    EXCLAMATION_BRACKET {
        override fun lookFor() {
            if (Regex("""\s*^!\[alt""").containsMatchIn(Data.lineData.line.text)) {
                Data.lineData.clean()
                Data.lineData.makeUp(TagParse.InlineTagType.IMAGE)
                log(TagParse.InlineTagType.IMAGE.name)
            } else ANGLE_BRACKETS.lookFor()
        }
    },
    ANGLE_BRACKETS {
        override fun lookFor() {
            if (Regex("""\s*^<.+>""").containsMatchIn(Data.lineData.line.text)) {
                Data.lineData.clean()
                Data.lineData.makeUp(TagParse.InlineTagType.LINK)
                log(TagParse.InlineTagType.LINK.name)
            } else PARAGRAPH.lookFor()
        }
    },
    PARAGRAPH {
        override fun lookFor() {
            Data.lineData.makeUp(TagParse.TagType.PARAGRAPH)
            Data.lineData.clean()
            log(TagParse.TagType.PARAGRAPH.name)
        }
    },
    ;

    private val logger = Logger.getLogger(this.javaClass.name)!!

    fun lineIsBlank(text: String) = Regex("""^[ ]{0,3}(\n|\r)""").containsMatchIn(text) || text.isEmpty()
    fun lastWithCurrentLineIsBlank() = Data.lineData.getLastLine().isLine
            && lineIsBlank(Data.lineData.getLastLine().text)
            && lineIsBlank(Data.lineData.line.text)

    fun log(parse: String) =
        logger.i("find pre parse $parse -> [ ${Data.lineData.line.number} ] : [ ${Data.lineData.line.text} ]")

    abstract fun lookFor()
}



