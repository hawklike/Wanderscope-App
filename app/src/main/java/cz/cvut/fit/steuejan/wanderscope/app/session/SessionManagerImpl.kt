package cz.cvut.fit.steuejan.wanderscope.app.session

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SessionManagerImpl(context: Context) : SessionManager {

    override fun saveAccessToken(token: String?) {
        accessTokenPref.edit {
            putString(ACCCESS_TOKEN_KEY, token)
        }
    }

    override fun getAccessToken(): String? {
        return accessTokenPref.getString(ACCCESS_TOKEN_KEY, null)
    }

    override fun saveRefreshToken(token: String?) {
        refreshTokenPref.edit {
            putString(REFRESH_TOKEN_KEY, token)
        }
    }

    override fun getRefreshToken(): String? {
        return refreshTokenPref.getString(REFRESH_TOKEN_KEY, null)
    }

    private val accessTokenPref = context.getSharedPreferences(ACCESS_TOKEN_SHARED_PREF, Context.MODE_PRIVATE)

    private val refreshTokenPref = EncryptedSharedPreferences.create(
        context,
        REFRESH_TOKEN_SHARED_PREF,
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val ACCESS_TOKEN_SHARED_PREF = "cz.cvut.fit.steuejan.wanderscope.access_token_pref"
        private const val REFRESH_TOKEN_SHARED_PREF = "cz.cvut.fit.steuejan.wanderscope.refresh_token_pref"
        private const val ACCCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }
}