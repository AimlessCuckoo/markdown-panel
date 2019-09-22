package red.medusa.markdownpanel

import javafx.scene.image.Image
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import red.medusa.markdownpanel.app.Styles
import tornadofx.*
import java.io.File
import java.io.FileInputStream


open class Segment : Region() {

    open fun getSegment(
        str: String,
        prefix: String?,
        postfix: String?,
        lineNumber: Int?,
        inlineText: InlineText? = null
    ): Pane? = hbox {
        hgrow = Priority.ALWAYS
        prefix?.apply {
            label("$this ")
        }
        label {
            text = str
        }

    }

    open fun getSegment(
        str: String,
        css: CssRule,
        prefix: String?,
        postfix: String?,
        lineNumber: Int?,
        inlineText: InlineText? = null
    ): Pane? = hbox {
        hgrow = Priority.ALWAYS
        addClass(css)
        prefix?.apply {
            label("$this ")
        }
        label {
            text = str
        }
    }

}

class OneTitleSegment : Segment() {
    override fun getSegment(
        str: String,
        prefix: String?,
        postfix: String?,
        lineNumber: Int?,
        inlineText: InlineText?
    ): Pane? {
        return super.getSegment(str, Styles.OneTitleSegment, prefix, postfix, lineNumber, inlineText)
    }
}

class TowTitleSegment : Segment() {
    override fun getSegment(
        str: String,
        prefix: String?,
        postfix: String?,
        lineNumber: Int?,
        inlineText: InlineText?
    ): Pane? {
        return super.getSegment(str, Styles.TowTitleSegment, prefix, postfix, lineNumber, inlineText)
    }
}

class ThreeTitleSegment : Segment() {
    override fun getSegment(
        str: String,
        prefix: String?,
        postfix: String?,
        lineNumber: Int?,
        inlineText: InlineText?
    ): Pane? {
        return super.getSegment(str, Styles.ThreeTitleSegment, prefix, postfix, lineNumber, inlineText)
    }
}

class FourTitleSegment : Segment() {
    override fun getSegment(
        str: String,
        prefix: String?,
        postfix: String?,
        lineNumber: Int?,
        inlineText: InlineText?
    ): Pane? {
        return super.getSegment(str, Styles.FourTitleSegment, prefix, postfix, lineNumber, inlineText)
    }
}

class FiveTitleSegment : Segment() {
    override fun getSegment(
        str: String,
        prefix: String?,
        postfix: String?,
        lineNumber: Int?,
        inlineText: InlineText?
    ): Pane? {
        return super.getSegment(str, Styles.FiveTitleSegment, prefix, postfix, lineNumber, inlineText)
    }
}

class SixTitleSegment : Segment() {
    override fun getSegment(
        str: String,
        prefix: String?,
        postfix: String?,
        lineNumber: Int?,
        inlineText: InlineText?
    ): Pane? {
        return super.getSegment(str, Styles.SixTitleSegment, prefix, postfix, lineNumber, inlineText)
    }
}

class BlankSegment : Segment() {
    override fun getSegment(
        str: String,
        prefix: String?,
        postfix: String?,
        lineNumber: Int?,
        inlineText: InlineText?
    ): Pane? {
        return null
    }
}

class LinefeedSegment : Segment() {
    override fun getSegment(
        str: String,
        prefix: String?,
        postfix: String?,
        lineNumber: Int?,
        inlineText: InlineText?
    ): Pane? {
        return super.getSegment(str, prefix, postfix, lineNumber, inlineText)
    }
}

class SeparatorSegment : Segment() {
    override fun getSegment(
        str: String,
        prefix: String?,
        postfix: String?,
        lineNumber: Int?,
        inlineText: InlineText?
    ): Pane? = vbox {
        addClass(Styles.SeparatorSegment)
        separator {
            useMaxSize = true
        }
    }
}

class OrderedSegment : Segment() {
    override fun getSegment(str: String, prefix: String?, postfix: String?, lineNumber: Int?, inlineText: InlineText?) =
        hbox {
            addClass(Styles.OrderedSegment)
            prefix?.apply {
                label(prefix) {
                    addClass(Styles.bold)
                }
            }
            label(str)
        }
}

class UnOrderedSegment : Segment() {
    override fun getSegment(str: String, prefix: String?, postfix: String?, lineNumber: Int?, inlineText: InlineText?) =
        hbox {
            addClass(Styles.OrderedSegment)
            label("▪") {
                addClass(Styles.bold)
            }
            label(str)
        }
}

class ParagraphSegment : Segment() {
    override fun getSegment(
        str: String,
        prefix: String?,
        postfix: String?,
        lineNumber: Int?,
        inlineText: InlineText?
    ): Pane? {
        return super.getSegment(str, prefix, postfix, lineNumber, inlineText).apply {
            addClass(Styles.ParagraphSegment)
        }
    }
}

class CodeArea1Segment : Segment() {
    override fun getSegment(str: String, prefix: String?, postfix: String?, lineNumber: Int?, inlineText: InlineText?) =
        vbox {
            addClass(Styles.CoderArea)
            text(str) {
                addClass(Styles.CoderAreaColor)
            }
        }
}

class CodeArea2Segment : Segment() {
    override fun getSegment(str: String, prefix: String?, postfix: String?, lineNumber: Int?, inlineText: InlineText?) =
        vbox {
            addClass(Styles.CoderArea)
            text(str) {
                addClass(Styles.CoderAreaColor)
            }
        }
}


class LinkSegment : Segment() {
    override fun getSegment(str: String, prefix: String?, postfix: String?, lineNumber: Int?, inlineText: InlineText?) =
        hbox {
            inlineText?.apply {
                val url = inlineText.linkName ?: inlineText.linkUrl ?: ""
                hyperlink(url).action {
                    tooltip(inlineText.linkName ?: "链接")
                }
            }
        }
}

class ImageSegment : Segment() {
    override fun getSegment(str: String, prefix: String?, postfix: String?, lineNumber: Int?, inlineText: InlineText?) =
        hbox {
            inlineText?.apply {
                Data.mkFile?.resolve(File(inlineText.imageUrl))?.canonicalFile?.apply {
                    if (this.exists() && this.isFile) {
                        imageview(Image(FileInputStream(this))) {
                            tooltip(inlineText.imageName ?: "图片")
                        }
                    }
                }
            }
        }
}




