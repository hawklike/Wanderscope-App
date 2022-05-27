package cz.cvut.fit.steuejan.wanderscope.account.repository

import cz.cvut.fit.steuejan.wanderscope.account.api.AccountApi
import cz.cvut.fit.steuejan.wanderscope.account.api.response.AccountResponse
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.app.util.toUnitIfSuccess
import kotlinx.coroutines.flow.Flow

class AccountRepository(private val accountApi: AccountApi) {

    suspend fun getAccount(): Flow<Result<AccountResponse>> {
        return performCall { accountApi.getAccount() }
    }

    suspend fun logoutAll(): Flow<Result<Unit>> {
        return performCall { accountApi.logoutAll() }.toUnitIfSuccess()
    }
}