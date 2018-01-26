package com.basicsteps.multipos.config

import com.google.gson.annotations.SerializedName

class UnitConfig { @SerializedName("units") var units: Map<String, Map<String, Map<String, Any>>>? = null }
class CurrencyConfig { @SerializedName("currencies") var currencies: Map<String, Map<String, Any>>? = null}