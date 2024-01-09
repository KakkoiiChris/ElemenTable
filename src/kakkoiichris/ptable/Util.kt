package kakkoiichris.ptable

fun String.capitalized() =
    lowercase().replaceFirstChar { it.titlecase() }