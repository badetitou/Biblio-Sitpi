package fr.badetitou.bibliositpi.model.api.holdings

@Suppress("PropertyName")
data class Holding(
    val Site: String,
    val Type: String,
    val WhenBack: String,
    val Statut: String,
    val Cote: String,
    val Section: String
)
