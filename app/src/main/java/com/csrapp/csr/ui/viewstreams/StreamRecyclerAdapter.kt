package com.csrapp.csr.ui.viewstreams

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.csrapp.csr.R
import com.csrapp.csr.data.StreamEntity
import kotlinx.android.synthetic.main.list_item_stream.view.*

class StreamRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var streams: List<StreamEntity>
    private lateinit var navController: NavController

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StreamViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_stream,
                parent,
                false
            ),
            navController,
            parent.context
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

    fun populateWithData(streamData: List<StreamEntity>) {
        streams = streamData
    }

    fun setUpNavController(streamNavController: NavController) {
        navController = streamNavController
    }

    class StreamViewHolder(itemView: View, val navController: NavController, val context: Context) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private lateinit var stream: String
        private var streamTitle: TextView = itemView.streamTitle
        private var streamImage: ImageView = itemView.streamImage

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(streamEntity: StreamEntity) {
            streamTitle.text = streamEntity.title
            val identifier = context.resources.getIdentifier(
                "placeholder",
                "mipmap",
                context.packageName
            )

            streamImage.setImageResource(identifier)
            stream = streamEntity.id
        }

        override fun onClick(v: View?) {
            val bundle = bundleOf("stream" to stream)
            navController.navigate(
                R.id.action_streamSelectionFragment_to_jobSelectionFragment,
                bundle
            )
        }
    }
}