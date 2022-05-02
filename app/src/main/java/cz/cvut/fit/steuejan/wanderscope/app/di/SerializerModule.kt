package cz.cvut.fit.steuejan.wanderscope.app.di

import com.squareup.moshi.Moshi
import cz.cvut.fit.steuejan.wanderscope.app.serialization.MoshiSerializer
import cz.cvut.fit.steuejan.wanderscope.app.serialization.Serializer
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val serializerModule = module {
    singleOf(::MoshiSerializer) {
        bind<Serializer<Moshi>>()
    }
}