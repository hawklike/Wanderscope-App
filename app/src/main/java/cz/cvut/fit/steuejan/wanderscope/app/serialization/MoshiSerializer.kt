package cz.cvut.fit.steuejan.wanderscope.app.serialization

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.BufferedSource
import org.joda.time.DateTime

class MoshiSerializer : Serializer<Moshi> {

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(DateTime::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    override fun getSerializer() = moshi

    override fun <E : Any> fromJson(json: String, clazz: Class<E>): E? {
        return moshi.adapter(clazz).fromJson(json)
    }

    override fun <E : Any> fromJson(source: BufferedSource, clazz: Class<E>): E? {
        return moshi.adapter(clazz).fromJson(source)
    }
}