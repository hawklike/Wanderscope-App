package cz.cvut.fit.steuejan.wanderscope.app.serialization

import okio.BufferedSource

interface Serializer<T> {
    fun getSerializer(): T
    fun <E : Any> fromJson(json: String, clazz: Class<E>): E?
    fun <E : Any> fromJson(source: BufferedSource, clazz: Class<E>): E?
}

inline fun <reified E : Any> Serializer<*>.fromJson(json: String) = fromJson(json, E::class.java)
inline fun <reified E : Any> Serializer<*>.fromJson(source: BufferedSource) = fromJson(source, E::class.java)