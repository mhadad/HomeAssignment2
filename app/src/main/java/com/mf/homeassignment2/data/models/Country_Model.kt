package com.mf.homeassignment2.data.models


/*
{
    "capital": "Kabul",
    "code": "AF",
    "currency": {
      "code": "AFN",
      "name": "Afghan afghani",
      "symbol": "؋"
    },
    "flag": "https://restcountries.eu/data/afg.svg",
    "language": {
      "code": "ps",
      "name": "Pashto"
    },
    "name": "Afghanistan",
    "region": "AS"
  },
 */

data class Country_Model(val capital: String, val code: String, val flag: String, val name: String, val region: String, val currency: CurrencyModel, val language: LanguageModel)