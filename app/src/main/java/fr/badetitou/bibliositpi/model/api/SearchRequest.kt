package fr.badetitou.bibliositpi.model.api


data class SearchRequest (val query: SearchQuery, val sst: Int = 4)