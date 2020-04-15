package io.github.jzdayz.cat.core

import java.util.concurrent.ConcurrentHashMap

data class Service(
        var name:String = "UNKNOWN",
        var instances: ConcurrentHashMap<String,Instance>
)