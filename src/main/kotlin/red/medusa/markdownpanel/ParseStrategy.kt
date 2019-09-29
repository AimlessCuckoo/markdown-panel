package red.medusa.markdownpanel

import java.util.logging.Logger

enum class ParseStrategy {
    DOUBLE_LINEFEED {
        override fun lookFor(lineData: LineData) {
            if (lineData.lastWithCurrentLineIsBlank()) {
                lineData.log(TagParse.TagType.LINEFEED.name)
                lineData.makeUp(TagParse.TagType.LINEFEED)
                lineData.clean()
            } else
                LINEFEED.lookFor(lineData)
        }
    },
    LINEFEED {
        override fun lookFor(lineData: LineData) {
            return if (lineIsBlank(lineData.line.text)) {
                lineData.log(TagParse.TagType.BLANK.name)
                lineData.clean()
                lineData.makeUp(TagParse.TagType.BLANK)
            } else NEST_UNSORTED_LIST.lookFor(lineData)
        }
    },
    NEST_UNSORTED_LIST {
        override fun lookFor(lineData: LineData) {
            return if (nestUnsortedListRegex.containsMatchIn(lineData.line.text) && lineData.isNestList()) {
                lineData.log(TagParse.TagType.NEST_UNSORTED_LIST.name)
                lineData.clean()
                lineData.makeUp(TagParse.TagType.NEST_UNSORTED_LIST)
            } else NEST_ORDERED_LIST.lookFor(lineData)
        }
    },
    NEST_ORDERED_LIST {
        override fun lookFor(lineData: LineData) {
            return if (nestOrderedListRegex.containsMatchIn(lineData.line.text) && lineData.isNestList()) {
                lineData.log(TagParse.TagType.NEST_ORDERED_LIST.name)
                lineData.clean()
                lineData.makeUp(TagParse.TagType.NEST_ORDERED_LIST)
            } else OVER_FOUR_BLANK_SPACE.lookFor(lineData)
        }
    },
    OVER_FOUR_BLANK_SPACE {     // 四个空格表示代码块
        override fun lookFor(lineData: LineData) {
            if (overFourBlankSpaceRegex.containsMatchIn(lineData.line.text)) {
                if (lineData.parseStrategy != this) {
                    lineData.markStart(this)
                    lineData.log("${TagParse.TagType.CODE_AREA_1} START - ${lineData.line.number} - [ ${lineData.line.text} ]")
                }
                lineData.makeUp(TagParse.TagType.CODE_AREA_1)
                lineData.log("${TagParse.TagType.CODE_AREA_1} HANDING - ${lineData.line.number} - [ ${lineData.line.text} ]")
            } else {
                //不做任何结束,直接找下一个匹配项
                ANTI_POINTS.lookFor(lineData)
            }
        }
    },
    ANTI_POINTS {
        override fun lookFor(lineData: LineData) {
            if (lineData.isRequireMultipleHandle && lineData.parseStrategy == this) {
                if ((antiPointsPrefixRegex.containsMatchIn(lineData.line.text))) {
                    lineData.log("${TagParse.TagType.CODE_AREA_2} END - ${lineData.line.number} - [ ${lineData.line.text} ]")
                    lineData.line.postfix = "```"
                    lineData.markEnd()
                }
                lineData.makeUp(TagParse.TagType.CODE_AREA_2)
                lineData.log("${TagParse.TagType.CODE_AREA_2} HANDING - ${lineData.line.number} - [ ${lineData.line.text} ]")
                if (!lineData.line.isLine) //   全部都没找到
                    lineData.markEnd()
            } else {
                if (antiPointsPostfixRegex.containsMatchIn(lineData.line.text)) {
                    if (lineData.isRequireMultipleHandle && lineData.parseStrategy == OVER_FOUR_BLANK_SPACE)
                        lineData.clean()
                    lineData.log("${TagParse.TagType.CODE_AREA_2} START - ${lineData.line.number} - [ ${lineData.line.text} ]")
                    lineData.line.prefix = lineData.line.text
                    lineData.markStart(this)
                    lineData.makeUp(TagParse.TagType.CODE_AREA_2)
                } else
                    POUND_KEY.lookFor(lineData)
            }
        }
    },
    POUND_KEY {
        override fun lookFor(lineData: LineData) {
            if (poundKeyRegex.containsMatchIn(lineData.line.text)) {
                lineData.clean()
                lineData.makeUp(TagParse.TagType.TITLE)
                lineData.log(TagParse.TagType.TITLE.name)
            } else
                ORDERED_LIST.lookFor(lineData)
        }
    },
    ORDERED_LIST {
        override fun lookFor(lineData: LineData) {
            if (orderedListRegex.containsMatchIn(lineData.line.text)) {
                lineData.clean()
                lineData.makeUp(TagParse.TagType.ORDERED_LIST)
                lineData.log(TagParse.TagType.ORDERED_LIST.name)
            } else THREE_PMS.lookFor(lineData)
        }
    },
    THREE_PMS {
        override fun lookFor(lineData: LineData) {
            if (ThreePMSRegex.containsMatchIn(lineData.line.text.replace("\\s".toRegex(), ""))) {
                lineData.makeUp(TagParse.TagType.SEPARATOR)
                lineData.clean()
                lineData.log(TagParse.TagType.SEPARATOR.name)
            } else
                UNORDERED_LIST.lookFor(lineData)
        }
    },
    UNORDERED_LIST {
        override fun lookFor(lineData: LineData) {
            if (unorderedListRegex.containsMatchIn(lineData.line.text)) {
                lineData.makeUp(TagParse.TagType.UNORDERED_LIST)
                lineData.clean()
                lineData.log("+ -> ${TagParse.TagType.UNORDERED_LIST}")
            } else BRACKET.lookFor(lineData)
        }
    },
    BRACKET {
        override fun lookFor(lineData: LineData) {
            if (bracketRegex.containsMatchIn(lineData.line.text)) {
                lineData.clean()
                lineData.makeUp(TagParse.InlineTagType.LINK)
                lineData.log(TagParse.InlineTagType.LINK.name)
            } else EXCLAMATION_BRACKET.lookFor(lineData)
        }
    },
    EXCLAMATION_BRACKET {
        override fun lookFor(lineData: LineData) {
            if (exclamationBracketRegex.containsMatchIn(lineData.line.text)) {
                lineData.clean()
                lineData.makeUp(TagParse.InlineTagType.IMAGE)
                lineData.log(TagParse.InlineTagType.IMAGE.name)
            } else ANGLE_BRACKETS.lookFor(lineData)
        }
    },
    ANGLE_BRACKETS {
        override fun lookFor(lineData: LineData) {
            if (angleBracketsRegex.containsMatchIn(lineData.line.text)) {
                lineData.clean()
                lineData.makeUp(TagParse.InlineTagType.LINK)
                lineData.log(TagParse.InlineTagType.LINK.name)
            } else LABEL_WORLD.lookFor(lineData)
        }
    },
    LABEL_WORLD {
        override fun lookFor(lineData: LineData) {
            if (labelWorldRegex.containsMatchIn(lineData.line.text)) {
                lineData.makeUp(TagParse.TagType.LABEL_WORLD)
                lineData.clean()
                lineData.log("+ -> ${TagParse.TagType.LABEL_WORLD}")
            }else PARAGRAPH.lookFor(lineData)
        }
    },
    PARAGRAPH {
        override fun lookFor(lineData: LineData) {
            lineData.makeUp(TagParse.TagType.PARAGRAPH)
            lineData.clean()
            lineData.log(TagParse.TagType.PARAGRAPH.name)
        }
    },
    ;

    companion object {
        val blankLineReg = Regex("""^[ ]{0,3}(\n|\r)""")
        val nestUnsortedListRegex = Regex("""^(\s{4}|\t)+[-+*]\s""")
        val nestOrderedListRegex = Regex("""^(\s{4}|\t)+(\d+\.)+\s""")
        val overFourBlankSpaceRegex = Regex("""^[ ]{4,}""")
        val antiPointsPrefixRegex = Regex("""```$""")
        val antiPointsPostfixRegex = Regex("""^\s*```""")
        val poundKeyRegex = Regex("""^\s*#""")
        val orderedListRegex = Regex("""^(\d+\.)+ (.*)""")
        val ThreePMSRegex = Regex("""(^\*{3,}$)|(^\+{3,}$)|(^-{3,}$)""")
        val unorderedListRegex = Regex("""^[-+*]\s""")
        val labelWorldRegex = Regex("""^\s*`[^`]+`\s*$""")
        val bracketRegex = Regex("""^\s*\[(?!alt)([^]]*)]\(([^)]+)\)\s*$""")
        val exclamationBracketRegex = Regex("""^\s*!\[(alt(.*))?]\(((?:.(?!"))+?)(\s*\s"(.*?)"\s*)?\)\s*$""")
        val angleBracketsRegex = Regex("""\s*^<.+>$""")
    }

    private val logger = Logger.getLogger(this.javaClass.name)!!

    fun lineIsBlank(text: String) = text.isEmpty() || blankLineReg.containsMatchIn(text)
    fun LineData.lastWithCurrentLineIsBlank() = this.getLastLine().isLine
            && lineIsBlank(this.getLastLine().text)
            && lineIsBlank(this.line.text)

    fun LineData.isNestList() =
        this.getLastLine().tagParse == TagParse.TagType.UNORDERED_LIST ||
                this.getLastLine().tagParse == TagParse.TagType.ORDERED_LIST ||
                this.getLastLine().tagParse == TagParse.TagType.NEST_UNSORTED_LIST ||
                this.getLastLine().tagParse == TagParse.TagType.NEST_ORDERED_LIST

    fun LineData.log(parse: String) = pt("find pre parse $parse -> [ ${this.line.number} ] : [ ${this.line.text} ]")

    abstract fun lookFor(lineData: LineData)
}



