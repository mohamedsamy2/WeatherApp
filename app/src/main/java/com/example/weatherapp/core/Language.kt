package com.example.weatherapp.core

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import java.util.*

public fun changeLang(context: Context, lang_code: String): ContextWrapper? {
    var context = context
    val sysLocale: Locale
    val rs = context.resources
    val config = rs.configuration
    sysLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        config.locales[0]
    } else {
        config.locale
    }
    if (lang_code != "" && !sysLocale.language.equals(lang_code)) {
        val locale = Locale(lang_code)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context = context.createConfigurationContext(config)
        } else {
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
    }
    return ContextWrapper(context)
}