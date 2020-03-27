package com.csrapp.csr.utils

import android.content.Context

object ResourceProvider {
    private var applicationContext: Context? = null

    fun setUpApplicationContext(context: Context) {
        applicationContext = context.applicationContext
    }

    fun getString(resId: Int) = applicationContext!!.getString(resId)

    fun getColor(resId: Int) = applicationContext!!.resources.getColor(resId)
}