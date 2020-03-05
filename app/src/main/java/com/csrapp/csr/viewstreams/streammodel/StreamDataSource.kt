package com.csrapp.csr.viewstreams.streammodel

import com.csrapp.csr.R

class StreamDataSource {
    companion object {

        fun getStreamData(): ArrayList<Stream> {
            val streams = ArrayList<Stream>()

            streams.add(Stream("Agriculture", R.mipmap.placeholder, "agriculture"))
            streams.add(Stream("Arts and Humanities", R.mipmap.placeholder, "arts"))
            streams.add(Stream("Commerce", R.mipmap.placeholder, "commerce"))
            streams.add(Stream("Fine Arts", R.mipmap.placeholder, "fine_arts"))
            streams.add(Stream("Health and Life Sciences", R.mipmap.placeholder, "health"))
            streams.add(Stream("Technical", R.mipmap.placeholder, "technical"))
            streams.add(Stream("Uniformed Services", R.mipmap.placeholder, "uniformed_services"))

            return streams
        }
    }
}