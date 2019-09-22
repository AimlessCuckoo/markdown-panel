package red.medusa.markdownpanel.app

import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val MarkdownPanel by cssclass()
        val lineNumber by cssclass()
        val bold by cssclass()
        val OneTitleSegment by cssclass()
        val TowTitleSegment by cssclass()
        val ThreeTitleSegment by cssclass()
        val FourTitleSegment by cssclass()
        val FiveTitleSegment by cssclass()
        val SixTitleSegment by cssclass()
        val OrderedSegment by cssclass()

        val ParagraphSegment by cssclass()

        val SeparatorSegment by cssclass()

        val CoderArea by cssclass()
        val CoderAreaColor by cssclass()

        val boldMixin = mixin {
            fontWeight = FontWeight.BOLD
            padding = box(0.2.em, 0.em)
        }
    }

    init {
        root{
            baseColor = c("353535")
            fontSize = 18.px
        }

        lineNumber {
            alignment = Pos.CENTER_RIGHT
        }
        bold {
            fontWeight = FontWeight.BOLD
        }
        OneTitleSegment {
            +boldMixin
            fontSize = 2.5.em
        }
        TowTitleSegment {
            +boldMixin
            fontSize = 2.0.em
        }
        ThreeTitleSegment {
            +boldMixin
            fontSize = 1.8.em
        }

        FourTitleSegment {
            fontSize = 1.5.em
            +boldMixin
        }

        FiveTitleSegment {
            +boldMixin
            fontSize = 1.3.em
        }
        SixTitleSegment {
            +boldMixin
        }
        SeparatorSegment {
            padding = box(1.em)
            separator {
                backgroundColor += c("a5aaae", 0.5)
            }
        }
        OrderedSegment {
            padding = box(0.em, 0.em, 0.em, 2.em)
            label {
                padding = box(0.em, 0.7.em, 0.em, 0.em)
            }
        }
        CoderArea {
            borderWidth += box(0.px)
            borderColor += box(Color.TRANSPARENT)
            backgroundColor += c("23241f")
        }
        CoderAreaColor {
            fill =  c("47dfc5")
        }

    }
}




















