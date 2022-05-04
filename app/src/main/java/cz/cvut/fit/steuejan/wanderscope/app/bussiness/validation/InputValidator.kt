package cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import org.apache.commons.validator.routines.EmailValidator


class InputValidator {

    suspend fun validatePassword(password: String) = withDefault {
        when {
            password.isBlank() -> R.string.validation_password_blank
            password.length < 8 -> R.string.validation_password_short
            password.length > 50 -> R.string.validation_password_long
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
            email.length > 254 -> R.string.validation_email_long
            !EmailValidator.getInstance().isValid(email) -> R.string.validation_email_invalid
            else -> OK
        }
    }

    suspend fun validateUsername(username: String) = withDefault {
        when {
            username.isBlank() -> R.string.validation_username_blank
            username.length < 3 -> R.string.validation_username_short
            username.length > 30 -> R.string.validation_username_long
            !username.isNameAllowed() -> R.string.validation_username_invalid
            else -> OK
        }
    }

    suspend fun validateDisplayName(displayName: String) = withDefault {
        when {
            displayName.isBlank() -> R.string.validation_displayname_blank
            displayName.length < 3 -> R.string.validation_displayname_short
            displayName.length > 30 -> R.string.validation_displayname_long
            !displayName.isNameAllowed() -> R.string.validation_displayname_invalid
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
}