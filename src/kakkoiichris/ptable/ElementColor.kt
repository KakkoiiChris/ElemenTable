package kakkoiichris.ptable

import java.awt.Color

sealed interface ElementColor {
    val fore: Color
    val back: Color

    enum class Category(override val fore: Color, override val back: Color) : ElementColor {
        AlkaliMetal(fgDark, Color(100, 185, 186)),
        AlkalineEarthMetal(fgDark, Color(204, 125, 49)),
        Lanthanide(fgLight, Color(103, 78, 167)),
        TransitionMetal(fgDark, Color(139, 143, 21)),
        PostTransitionMetal(fgDark, Color(239, 211, 216)),
        Actinide(fgDark, Color(218, 144, 169)),
        PolyatomicNonmetal(fgDark, Color(228, 187, 1)),
        DiatomicNonmetal(fgLight, Color(117, 17, 1)),
        NobleGas(fgDark, Color(228, 230, 104)),
        Metalloid(fgLight, Color(1, 110, 139)),
        Unknown(fgDark, Color(182, 188, 170)),
        Placeholder(fgLight, Color(0, 0, 0, 0));

        companion object {
            operator fun get(name: String): Category {
                val entryName = name
                    .split("(\\s+|-)".toRegex())
                    .joinToString(separator = "") { it.capitalized() }

                return entries.firstOrNull { it.name == entryName } ?: Unknown
            }
        }
    }

    enum class State(override val fore: Color, override val back: Color) : ElementColor {
        Solid(Color.BLACK, Color(255, 63, 63)),
        Liquid(Color.BLACK, Color(63, 255, 63)),
        Gas(Color.BLACK, Color(63, 63, 255)),
        Unknown(Color.BLACK, Color(127, 127, 127))
    }

    enum class Block(override val fore: Color, override val back: Color) : ElementColor {
        S(Color.BLACK, Color(255, 0, 127)),
        P(Color.BLACK, Color(255, 127, 0)),
        D(Color.BLACK, Color(127, 255, 0)),
        F(Color.BLACK, Color(0, 127, 255))
    }
}