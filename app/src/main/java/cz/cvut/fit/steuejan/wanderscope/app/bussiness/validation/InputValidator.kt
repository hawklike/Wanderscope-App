package cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.ValidateDates.*
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet
import org.apache.commons.validator.routines.EmailValidator


class InputValidator {

    suspend fun validatePassword(password: String) = withDefault {
        when {
            password.isBlank() -> R.string.validation_password_blank
            password.length < Constants.PASSWORD_MIN_LENGTH -> R.string.validation_password_short
            password.length > Constants.PASSWORD_MAX_LENGTH -> R.string.validation_password_long
            else -> OK
        }
    }

    suspend fun validateConfirmPassword(password: String?, confirmPassword: String?) = withDefault {
        when {
            password != confirmPassword -> R.string.validation_password_not_match
            else -> OK
        }
    }

    suspend fun validateEmail(email: String) = withDefault {
        when {
            email.isBlank() -> R.string.validation_email_blank
            email.length > Constants.EMAIL_MAX_LENGTH -> R.string.validation_email_long
            !EmailValidator.getInstance().isValid(email) -> R.string.validation_email_invalid
            else -> OK
        }
    }

    suspend fun validateUsername(username: String) = withDefault {
        when {
            username.isBlank() -> R.string.validation_username_blank
            username.length < Constants.USERNAME_MIN_LENGTH -> R.string.validation_username_short
            username.length > Constants.USERNAME_MAX_LENGTH -> R.string.validation_username_long
            !username.isNameAllowed() -> R.string.validation_username_invalid
            else -> OK
        }
    }

    suspend fun validateDisplayName(displayName: String) = withDefault {
        when {
            displayName.isBlank() -> OK
            displayName.length < Constants.DISPLAY_NAME_MIN_LENGTH -> R.string.validation_displayname_short
            displayName.length > Constants.DISPLAY_NAME_MAX_LENGTH -> R.string.validation_displayname_long
            !displayName.isNameAllowed() -> R.string.validation_displayname_invalid
            else -> OK
        }
    }

    suspend fun validateCarsAndSeats(carOrSeat: String) = withDefault {
        if (carOrSeat.contains("\\")) R.string.car_seat_invalid else OK
    }

    suspend fun validateExpenseMember(member: String) = withDefault {
        if (member.contains(";")) R.string.expense_room_member_invalid else OK
    }

    fun validateIfNotTooLong(text: String, maxLength: Int): Int {
        return if (text.length > maxLength) R.string.validation_too_long else OK
    }

    suspend fun validateIfNotEmpty(text: String) = withDefault {
        if (text.isBlank()) R.string.validation_blank else OK
    }

    fun validateDates(startDate: Long?, endDate: Long?, type: ValidateDates = NORMAL): Int {
        return multipleLet(startDate, endDate) { startMillis, endMillis ->
            if (endMillis < startMillis) {
                when (type) {
                    NORMAL -> R.string.enddate_before_startdate
                    CHECKIN -> R.string.checkout_before_checkin
                    DEPARTURE -> R.string.arrival_before_departure
                }
            } else OK
        } ?: OK
    }

    fun validateExtension(extension: String?): Int {
        return when {
            extension == null -> {
                R.string.unknown_extension
            }
            extension.lowercase() !in listOf("pdf", "png", "jpg", "jpeg", "gif", "gpx") -> {
                R.string.upload_document_extensions
            }
            else -> {
                OK
            }
        }
    }

    fun validateDocumentSize(size: Int?): Int {
        return if (size ?: 0 > Constants.DOCUMENT_MAX_SIZE) {
            R.string.upload_document_too_large
        } else {
            OK
        }
    }

    fun validateDocumentKey(key: String): Int {
        return when {
            key.isNotEmpty() && key.isBlank() -> R.string.validation_document_key_blank
            key.isNotEmpty() && key.length < Constants.DOCUMENT_KEY_MIN -> R.string.validation_document_key_short
            key.length > Constants.DOCUMENT_KEY_MAX -> R.string.validation_document_key_long
            else -> OK
        }
    }

    private fun String.isNameAllowed(): Boolean {
        val regex = Regex("^[^\r\n;~]+$")
        return this matches regex
    }

    companion object {
        const val OK = -1
    }

    enum class ValidateDates {
        NORMAL, CHECKIN, DEPARTURE
    }
}