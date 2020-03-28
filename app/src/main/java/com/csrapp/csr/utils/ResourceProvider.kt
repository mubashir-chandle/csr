package com.csrapp.csr.utils

import android.content.Context

object ResourceProvider {
    private var applicationContext: Context? = null

    fun setUpApplicationContext(context: Context) {
        applicationContext = context.applicationContext
    }

    fun getString(resId: Int, vararg formatArgs: Any) =
        applicationContext!!.getString(resId, *formatArgs)

    fun getColor(resId: Int) = applicationContext!!.resources.getColor(resId)

    fun getDrawable(resId: Int) = applicationContext!!.getDrawable(resId)!!
}