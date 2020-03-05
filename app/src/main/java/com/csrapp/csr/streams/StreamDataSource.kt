package com.csrapp.csr.streams

import com.csrapp.csr.R

class StreamDataSource {
    companion object {

        fun getStreamData(): ArrayList<Stream> {
            val streams = ArrayList<Stream>()

            streams.add(Stream("Agriculture", R.drawable.ic_android))
            streams.add(Stream("Arts and Humanities", R.drawable.ic_android))
            streams.add(Stream("Commerce", R.drawable.ic_android))
            streams.add(Stream("Fine Arts", R.drawable.ic_android))
            streams.add(Stream("Health and Life Sciences", R.drawable.ic_android))
            streams.add(Stream("Technical", R.drawable.ic_android))
            streams.add(Stream("Uniformed Services", R.drawable.ic_android))

            return streams
        }
    }
}