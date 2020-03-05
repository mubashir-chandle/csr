package com.csrapp.csr.streams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csrapp.csr.R
import kotlinx.android.synthetic.main.list_item_stream.view.*

class StreamRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var streams: ArrayList<Stream>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StreamViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_stream,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StreamViewHolder -> {
                holder.bind(streams[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return streams.size
    }

    fun populateWithData(streamData: ArrayList<Stream>) {
        streams = streamData
    }

    class StreamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var streamTitle = itemView.streamTitle
        var streamImage = itemView.streamImage

        fun bind(stream: Stream) {
            streamTitle.text = stream.title
            streamImage.setImageResource(stream.image)
        }
    }
}