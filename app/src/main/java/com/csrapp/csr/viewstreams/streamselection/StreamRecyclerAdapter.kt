package com.csrapp.csr.viewstreams.streamselection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.csrapp.csr.R
import kotlinx.android.synthetic.main.list_item_stream.view.*

class StreamRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var streams: ArrayList<Stream>
    private lateinit var navController: NavController

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StreamViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_stream,
                parent,
                false
            ), navController
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

    fun setUpNavController(streamNavController: NavController) {
        navController = streamNavController
    }

    class StreamViewHolder(itemView: View, val navController: NavController) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var streamTitle = itemView.streamTitle
        var streamImage = itemView.streamImage

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(stream: Stream) {
            streamTitle.text = stream.title
            streamImage.setImageResource(stream.image)
        }

        override fun onClick(v: View?) {
            navController.navigate(R.id.action_streamSelectionFragment_to_jobSelectionFragment)
        }
    }
}