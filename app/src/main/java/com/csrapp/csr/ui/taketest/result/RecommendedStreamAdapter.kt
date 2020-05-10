package com.csrapp.csr.ui.taketest.result

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.csrapp.csr.R
import com.csrapp.csr.utils.ResourceProvider
import kotlinx.android.synthetic.main.list_item_recommended_stream.view.*

class RecommendedStreamAdapter(
    private val items: List<RecommendationResultItem>,
    private val context: Context,
    private val navController: NavController
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ResultViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_recommended_stream,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ResultViewHolder -> {
                val resultItem = items[position]
                holder.bind(resultItem)

                holder.itemView.setOnClickListener {
                    AlertDialog.Builder(context)
                        .setTitle(resultItem.title)
                        .setMessage(resultItem.description)
                        .setPositiveButton("View Jobs") { _, _ ->
                            val bundle = bundleOf("stream" to resultItem.id)
                            navController.navigate(
                                R.id.action_resultFragment_to_jobSelectionFragment,
                                bundle
                            )
                        }
                        .setNegativeButton("Close", null)
                        .create()
                        .show()
                }
            }
        }
    }

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(receivedItem: RecommendationResultItem) {
            itemView.textViewRecommendedStreamTitle.text = receivedItem.title

            if (receivedItem.recommendationIntensity < 1f) {
                itemView.recommendedStreamWarningLine.setImageDrawable(
                    ResourceProvider.getDrawable(
                        R.drawable.line_less_recommended_stream
                    )
                )
            } else {
                itemView.recommendedStreamWarningLine.setImageDrawable(
                    ResourceProvider.getDrawable(
                        R.drawable.line_recommended_stream
                    )
                )
            }
        }

    }
}