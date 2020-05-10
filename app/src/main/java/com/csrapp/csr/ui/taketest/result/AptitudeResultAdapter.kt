package com.csrapp.csr.ui.taketest.result

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csrapp.csr.R
import com.csrapp.csr.utils.ResourceProvider
import kotlinx.android.synthetic.main.list_item_result.view.*

class AptitudeResultAdapter(
    private val items: List<AptitudeResultItem>,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ResultViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_result,
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
                val aptitudeResultItem = items[position]
                holder.bind(aptitudeResultItem)

                holder.itemView.setOnClickListener {
                    AlertDialog.Builder(context)
                        .setTitle(aptitudeResultItem.title)
                        .setMessage(aptitudeResultItem.description)
                        .setPositiveButton("Close", null)
                        .create()
                        .show()
                }
            }
        }
    }

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(receivedItem: AptitudeResultItem) {
            itemView.textViewTitle.text = receivedItem.title
            itemView.progressBar.progress = if (receivedItem.score > 0) receivedItem.score else 0

            itemView.textViewScore.text =
                ResourceProvider.getString(R.string.percent, receivedItem.score)

            if (receivedItem.score <= 0)
                itemView.textViewScore.setTextColor(ResourceProvider.getColor(R.color.colorTextError))
        }

    }
}