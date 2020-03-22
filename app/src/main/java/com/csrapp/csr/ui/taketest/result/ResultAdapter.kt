package com.csrapp.csr.ui.taketest.result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csrapp.csr.R
import kotlinx.android.synthetic.main.list_item_result.view.*

class ResultAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var items: List<ResultItem>

    fun setUpItems(resultItems: List<ResultItem>) {
        items = resultItems
    }

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
                holder.bind(items[position])
            }
        }
    }

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(receivedItem: ResultItem) {
            itemView.textViewTitle.text = receivedItem.title
            itemView.progressBar.progress = receivedItem.score

            itemView.textViewScore.text = "${receivedItem.score}%"
        }

    }
}