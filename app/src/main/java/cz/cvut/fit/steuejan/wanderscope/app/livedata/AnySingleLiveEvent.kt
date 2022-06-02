package cz.cvut.fit.steuejan.wanderscope.app.livedata

class AnySingleLiveEvent : SingleLiveEvent<Any>() {

    fun publish() {
        value = Any()
    }

    fun publishOnBackground() {
        postValue(Any())
    }
}