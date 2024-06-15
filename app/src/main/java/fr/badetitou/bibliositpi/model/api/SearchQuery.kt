package fr.badetitou.bibliositpi.model.api

@Suppress("PropertyName")
data class SearchQuery (val QueryString: String, val ScenarioCode: String = "DEFAULT", val Page: Int = 0)