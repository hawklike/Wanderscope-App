package cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class ValidationMediator(
    private vararg val validations: LiveData<Int>
) : MediatorLiveData<Boolean>() {

    init {
        validations.forEach { liveData ->
            addSource(liveData) {
                value = it == InputValidator.OK && othersValid(liveData)
            }
        }
    }

    fun add(vararg newValidations: LiveData<Int>): ValidationMediator {
        val allValidations = validations.toList() + newValidations
        return ValidationMediator(*allValidations.toTypedArray())
    }

    private fun othersValid(currentLiveData: LiveData<Int>): Boolean {
        return validations
            .filterNot { it == currentLiveData }
            .all { it.value == InputValidator.OK }
    }
}