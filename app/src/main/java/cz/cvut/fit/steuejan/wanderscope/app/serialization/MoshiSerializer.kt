package cz.cvut.fit.steuejan.wanderscope.app.serialization

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.model.ItineraryType
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response.*
import okio.BufferedSource

class MoshiSerializer : Serializer<Moshi> {

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(DateTimeAdapter())
            .add(itineraryPolymorphicAdapter())
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

    private fun itineraryPolymorphicAdapter(): PolymorphicJsonAdapterFactory<ItineraryResponse> {
        return PolymorphicJsonAdapterFactory.of(
            ItineraryResponse::class.java,
            ITINERARY_LABEL
        )
            .withSubtype(AccommodationItineraryResponse::class.java, ItineraryType.ACCOMMODATION.name)
            .withSubtype(ActivityItineraryResponse::class.java, ItineraryType.ACTIVITY.name)
            .withSubtype(DateItineraryResponse::class.java, ItineraryType.DATE.name)
            .withSubtype(PlaceItineraryResponse::class.java, ItineraryType.PLACE.name)
            .withSubtype(TransportItineraryResponse::class.java, ItineraryType.TRANSPORT.name)
    }

    companion object {
        const val ITINERARY_LABEL = "type"
    }
}