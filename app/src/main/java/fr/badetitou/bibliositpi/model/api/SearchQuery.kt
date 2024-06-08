package fr.badetitou.bibliositpi.model.api

data class SearchQuery (val QueryString: String, val ScenarioCode: String = "DEFAULT")