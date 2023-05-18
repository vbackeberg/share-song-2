package com.valerian.sharesong

import java.util.Locale

fun countryCodeFromHtml(siteAsString: String) =
    COUNTRY_CODE_REGEX.find(siteAsString)?.groupValues?.get(1)?.lowercase()

private val COUNTRY_CODE_REGEX = "lang=\"[a-z]{2}-([A-Z]{2})\"".toRegex()

fun countryCodeFromDevice() = (Locale.getDefault().country ?: "US")