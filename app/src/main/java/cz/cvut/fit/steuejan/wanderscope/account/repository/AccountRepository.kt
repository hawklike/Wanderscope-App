package cz.cvut.fit.steuejan.wanderscope.account.repository

import cz.cvut.fit.steuejan.wanderscope.account.api.AccountApi
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import kotlinx.coroutines.flow.Flow

class AccountRepository(private val accountApi: AccountApi) {

    fun test(): Flow<Result<String>> {
        return performCall { accountApi.getExpiration() }
    }
}