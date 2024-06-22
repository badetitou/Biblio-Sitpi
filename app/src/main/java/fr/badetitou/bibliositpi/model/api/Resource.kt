package fr.badetitou.bibliositpi.model.api

import kotlinx.serialization.Serializable

@Serializable
data class Resource(
    var Ttl: String = "",
    var Desc: String = "",
    var Dt: String = "",
    var Crtr: String = "",
    var Id: String = "",
    var RscBase: String = "",
    var RscId: String = "",
)

