package io.github.jzdayz.cat.core

data class Instance(
        var ip: String = "UNKNOWN",
        var port: Int = 0,
        var name: String = "UNKNOWN",
        var lastHeartBeat: Long = 0,
        var serviceName: String = "UNKNOWN"
)