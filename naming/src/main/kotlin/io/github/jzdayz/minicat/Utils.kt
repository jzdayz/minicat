package io.github.jzdayz.minicat

import io.github.jzdayz.minicat.core.Service
import java.util.concurrent.ConcurrentHashMap

object Utils {
    fun<K,V> concurrentHashMap(k: K,v: V): ConcurrentHashMap<K,V>{
        val concurrentHashMap = ConcurrentHashMap<K, V>()
        concurrentHashMap[k] = v
        return concurrentHashMap
    }
    fun expelMap(map: ConcurrentHashMap<String, Service>){
//        map.forEachValue()
    }
}