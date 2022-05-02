package cz.cvut.fit.steuejan.wanderscope.app.session

import android.content.Context
import androidx.core.content.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber


class SessionManagerImpl(private val context: Context) : SessionManager {

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

    override fun isUserLoggedIn(): Boolean {
        return getAccessToken() != null
    }

    override suspend fun requestLogout() {
        requestLogout(true)
        requestLogout(false)
    }

    private suspend fun requestLogout(logout: Boolean) {
        context.logoutDataStore.edit {
            it[LOGOUT_USER] = logout
        }
    }

    override fun shouldLogoutUser(): Flow<Boolean> {
        return context.logoutDataStore.data.map {
            it[LOGOUT_USER] ?: false
        }.catch {
            Timber.w(it)
        }
    }

    private val accessTokenPref = context.getSharedPreferences(ACCESS_TOKEN_SHARED_PREF, Context.MODE_PRIVATE)

    private val refreshTokenPref = EncryptedSharedPreferences.create(
        context,
        REFRESH_TOKEN_SHARED_PREF,
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val Context.logoutDataStore by preferencesDataStore(LOGOUT_DATASTORE_PREF)

    companion object {
        private const val ACCESS_TOKEN_SHARED_PREF = "cz.cvut.fit.steuejan.wanderscope.access_token_pref"
        private const val REFRESH_TOKEN_SHARED_PREF = "cz.cvut.fit.steuejan.wanderscope.refresh_token_pref"
        private const val LOGOUT_DATASTORE_PREF = "cz.cvut.fit.steuejan.wanderscope.logout_datastore"

        private const val ACCCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"

        private val LOGOUT_USER = booleanPreferencesKey("logout_user")
    }
}