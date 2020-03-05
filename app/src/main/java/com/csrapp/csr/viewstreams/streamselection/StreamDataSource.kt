package com.csrapp.csr.viewstreams.streamselection

import com.csrapp.csr.R

class StreamDataSource {
    companion object {

        fun getStreamData(): ArrayList<Stream> {
            val streams = ArrayList<Stream>()

            streams.add(Stream("Agriculture", R.mipmap.placeholder))
            streams.add(Stream("Arts and Humanities", R.mipmap.placeholder))
            streams.add(Stream("Commerce", R.mipmap.placeholder))
            streams.add(Stream("Fine Arts", R.mipmap.placeholder))
            streams.add(Stream("Health and Life Sciences", R.mipmap.placeholder))
            streams.add(Stream("Technical", R.mipmap.placeholder))
            streams.add(Stream("Uniformed Services", R.mipmap.placeholder))

            return streams
        }
    }
}