package cz.cvut.fit.steuejan.wanderscope.app.event

class AnySingleLiveEvent : SingleLiveEvent<Any>() {

    fun publish() {
        value = Any()
    }

    fun publishOnBackground() {
        postValue(Any())
    }
}