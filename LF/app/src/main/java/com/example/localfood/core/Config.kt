package com.example.localfood.core

object Config {
    // true -> usa FakeRepositories
    // false -> usa Repositories reais (Retrofit + PHP)
    const val USE_FAKE = true

    // Quando fores ligar ao backend, muda aqui:
    const val BASE_URL = "http://10.0.2.2/localfood_api/"  // exemplo para XAMPP
}
