package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.model

enum class Currency {
    DOLLAR, EURO, CZECH, POUND, YEN, ALBANIA, BOSNIA, SWISS, DENMARK, CROATIA, HUNGARY, ICELAND,
    MOLDOVA, NORWAY, POLAND, ROMANIA, RUSSIA, SERBIA, SWEDEN, TURKEY, UKRAINE;

    fun getCode(): String {
        return when (this) {
            DOLLAR -> "USD"
            EURO -> "EUR"
            CZECH -> "CZK"
            POUND -> "GBP"
            YEN -> "JPY"
            SWISS -> "CHF"
            CROATIA -> "HRK"
            ALBANIA -> "ALL"
            BOSNIA -> "BAM"
            DENMARK -> "DKK"
            HUNGARY -> "HUF"
            ICELAND -> "ISK"
            MOLDOVA -> "MDL"
            NORWAY -> "NOK"
            ROMANIA -> "RON"
            RUSSIA -> "RUB"
            SERBIA -> "RSD"
            SWEDEN -> "SEK"
            UKRAINE -> "UAH"
            TURKEY -> "TRY"
            POLAND -> "PLN"
        }
    }

}