package app.boboc.chatserver.data

enum class CountryCode(val countryNumber: String) {
    US("1"),
    KR("82"),
    JP("81"),
    CN("86");

    companion object {
        fun findByCountryNumber(countryNumber: String): CountryCode? {
            return CountryCode.entries.find { it.countryNumber == countryNumber }
        }

        fun findByCountryCodeName(countryCodeName: String): CountryCode? {
            return countryCodeName.uppercase().run {
                CountryCode.entries.find {
                    it.name == this
                }
            }
        }
    }
}
