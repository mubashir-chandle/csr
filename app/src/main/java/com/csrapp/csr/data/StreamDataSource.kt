package com.csrapp.csr.data

import android.content.Context

class StreamDataSource {
    companion object {
        fun getStreamData(context: Context): List<StreamEntity> {
            val db = AppDatabase(context)
            return db.streamDao().getAllStreams()
        }
    }
}