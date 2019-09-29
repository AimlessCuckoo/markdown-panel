package red.medusa.markdownpanel

interface TagParse {
    fun parse(line: Line)
    enum class InlineTagType(private val product: MaterialLine) : TagParse {
        //图片
        IMAGE(ImageLine()),
        //链接
        LINK(LinkLine()),
        // 粗斜体
        ITALICS_STAR(ItalicsLine()),
        ITALICS_UNDERLINE(ItalicsLine()),
        BOLD_STAR(BoldLine()),
        BOLD_UNDERLINE(BoldLine()),
        BOLD_ITALICS_STAR(BoldItalicsLine()),
        BOLD_ITALICS_UNDERLINE(BoldItalicsLine()),
        ;

        override fun parse(line: Line) {
            product.create(line)
        }

    }

    enum class TagType(private val product: MaterialLine) : TagParse {
        BLANK(BlankLine()),
        LINEFEED(LinefeedLine()),
        // 标题
        ONE_TITLE(OneTitleLine()),
        TOW_TITLE(TowTitleLine()),
        THREE_TITLE(ThreeTitleLine()),
        FOUR_TITLE(FourTitleLine()),
        FIVE_TITLE(FiveTitleLine()),
        SIX_TITLE(SixTitleLine()),
        TITLE(TitleLine()),

        // 分隔符
        SEPARATOR(SeparatorLine()),

        // 列表
        ORDERED_LIST(OrderedListLine()),
        UNORDERED_LIST(UnOrderedListLine()),

        NEST_UNSORTED_LIST(NestUnOrderedListLine()),
        NEST_ORDERED_LIST(NestOrderedListLine()),

        //代码块
        CODE_AREA_1(CodeArea1Line()),
        CODE_AREA_2(CodeArea2Line()),

        LABEL_WORLD(LabelWordHighlightLine()),

        // 段落
        PARAGRAPH(ParagraphLine());


        override fun parse(line: Line) {
            product.create(line)
        }
    }
}






















