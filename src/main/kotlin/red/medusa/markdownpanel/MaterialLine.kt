package red.medusa.markdownpanel

import red.medusa.markdownpanel.view.*

interface ViewLine {
    fun create(line: Line) {
        pt("${this.javaClass.simpleName} is loading - [${line.number}] : [ ${line.text} ]")
    }
}

class TitleLine : ViewLine {
    override fun create(line: Line) {
        val text = line.text.trimStart()
        when {
            text.startsWith("######") -> TagParse.TagType.SIX_TITLE.parse(line)
            text.startsWith("#####") -> TagParse.TagType.FIVE_TITLE.parse(line)
            text.startsWith("####") -> TagParse.TagType.FOUR_TITLE.parse(line)
            text.startsWith("###") -> TagParse.TagType.THREE_TITLE.parse(line)
            text.startsWith("##") -> TagParse.TagType.TOW_TITLE.parse(line)
            text.startsWith("#") -> TagParse.TagType.ONE_TITLE.parse(line)
        }
    }
}


class OneTitleLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)
        line.text = line.text.trimStart().removePrefix("#").trimStart()
        line.segmentView = OneTitleSegment::class
    }
}

class TowTitleLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)
        line.text = line.text.trimStart().removePrefix("##").trimStart()
        line.segmentView = TowTitleSegment::class
    }
}

class ThreeTitleLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)
        line.text = line.text.trimStart().removePrefix("###").trimStart()
        line.segmentView = ThreeTitleSegment::class
    }
}

class FourTitleLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)
        line.text = line.text.trimStart().removePrefix("####").trimStart()
        line.segmentView = FourTitleSegment::class
    }
}

class FiveTitleLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)
        line.text = line.text.trimStart().removePrefix("#####").trimStart()
        line.segmentView = FiveTitleSegment::class
    }
}

class SixTitleLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)
        line.text = line.text.trimStart().removePrefix("######").trimStart()
        line.segmentView = SixTitleSegment::class

    }
}

class BlankLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)
        line.segmentView = BlankSegment::class
    }
}

class LinefeedLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)
        line.segmentView = LinefeedSegment::class
    }
}

class SeparatorLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)
        line.segmentView = SeparatorSegment::class
    }
}

class OrderedListLine : ViewLine {
    private val regex = """^(\d+\.)+ (.*)""".toRegex()
    override fun create(line: Line) {
        super.create(line)
        regex.find(line.text)?.groupValues?.apply {
            line.prefix = this.component2()
            line.text = this.component3().trimStart()
            line.segmentView = OrderedSegment::class
        }

    }
}
class UnOrderedListLine : ViewLine {
    private val regex = """^[-+*] (.*)""".toRegex()
    override fun create(line: Line) {
        regex.find(line.text)?.groupValues.apply {
            line.text = this?.component2()?.trimStart() ?: ""
            line.segmentView = UnOrderedSegment::class
        }
        super.create(line)
    }
}

class NestUnOrderedListLine : ViewLine {
    private val regex = Regex("""^((?:\s{4}|\t)+)([-+*])\s(.*)$""")
    override fun create(line: Line) {
        super.create(line)
        regex.find(line.text)?.destructured.apply {
            line.prefix = this?.component1()
            line.text = this?.component3()?.trimStart() ?: ""
            line.segmentView = NestUnOrderedSegment::class
        }
        println()
    }
}
class NestOrderedListLine : ViewLine {
    private val regex = """^((?:\s{4}|\t)+)((\d+\.)+)\s(.*)$""".toRegex()
    override fun create(line: Line) {
        super.create(line)
        regex.find(line.text)?.destructured?.apply {
            line.prefix = this.component1()
            line.postfix = this.component2()
            line.text = this.component4().trimStart()
            line.segmentView = NestOrderedSegment::class
        }

    }
}

class CodeArea1Line : ViewLine {
    override fun create(line: Line) {
        super.create(line)
        line.text = line.text.replace("^[ ]{4}|\t".toRegex(), "")
        line.segmentView = CodeArea1Segment::class
    }
}

class CodeArea2Line : ViewLine {
    override fun create(line: Line) {
        super.create(line)
        line.segmentView = CodeArea2Segment::class
    }
}

class ParagraphLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)
        line.segmentView = ParagraphSegment::class
    }
}


class ImageLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)
        val regex = Regex("""^\s*!\[(alt(.*))?]\(((?:.(?!"))+?)(\s*\s"(.*?)"\s*)?\)\s*$""")
        regex.find(line.text)?.destructured?.apply {
            if (line.inlineText == null)
                line.inlineText = InlineText()
            line.inlineText?.imageName = component2()
            line.inlineText?.imageUrl = component3()
        }
        line.segmentView = ImageSegment::class
    }
}


class LinkLine : ViewLine {
    private val preRegex = """\s*^\[""".toRegex()
    private val regex1 = """^\[(.*)]\((.*)\)$""".toRegex()
    private val regex2 = """^<(.+)>$""".toRegex()
    override fun create(line: Line) {
        if (preRegex.containsMatchIn(line.text)) {
            create1(line)
        } else {
            create2(line)
        }

    }

    private fun create1(line: Line) {
        super.create(line)
        regex1.find(line.text)?.destructured?.apply {
            if (line.inlineText == null)
                line.inlineText = InlineText()
            line.inlineText?.linkName = component1()
            line.inlineText?.linkUrl = component2()
        }
        line.segmentView = LinkSegment::class
    }

    private fun create2(line: Line) {
        super.create(line)
        regex2.find(line.text)?.destructured?.apply {
            if (line.inlineText == null)
                line.inlineText = InlineText()
            line.inlineText?.linkUrl = component1()
        }
        line.segmentView = LinkSegment::class
    }
}

class ItalicsLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)

    }
}

class BoldLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)

    }
}

class BoldItalicsLine : ViewLine {
    override fun create(line: Line) {
        super.create(line)
    }
}












