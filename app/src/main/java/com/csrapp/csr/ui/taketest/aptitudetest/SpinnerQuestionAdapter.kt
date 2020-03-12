package com.csrapp.csr.ui.taketest.aptitudetest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.csrapp.csr.R
import com.csrapp.csr.data.AptitudeQuestionEntity

class SpinnerQuestionAdapter(val context: Context, questions: List<AptitudeQuestionEntity>) :
    BaseAdapter() {
    private var questionHolders: Array<QuestionHolder?> = arrayOfNulls(questions.size)
    private val layoutInflater = LayoutInflater.from(context)

    init {
        for (i in questions.indices) {
            questionHolders[i] = QuestionHolder(
                questions[i],
                QuestionHolder.QuestionResponseType.UNVISITED
            )
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: QuestionViewHolder
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.spinner_item_aptitude_question, parent, false)
            viewHolder = QuestionViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as QuestionViewHolder
        }

        viewHolder.questionNumber!!.text = "Question ${position + 1}"
        val color = when (questionHolders[position]!!.responseType) {
            QuestionHolder.QuestionResponseType.ANSWERED -> R.color.spinnerItemQuestionAnswered
            QuestionHolder.QuestionResponseType.MARKED -> R.color.spinnerItemQuestionMarked
            QuestionHolder.QuestionResponseType.SKIPPED -> R.color.spinnerItemQuestionSkipped
            QuestionHolder.QuestionResponseType.UNVISITED -> R.color.spinnerItemQuestionUnvisited
        }

        val colorResource = context.resources.getColor(color)
        viewHolder.questionNumber!!.setTextColor(colorResource)
        return view
    }

    override fun getItem(position: Int): QuestionHolder? {
        return questionHolders[position]
    }

    override fun getItemId(position: Int): Long {
        // TODO: Check if this is the right solution.
        return 0
    }

    override fun getCount(): Int {
        return questionHolders.size
    }

    private class QuestionViewHolder(row: View?) {
        var questionNumber = row?.findViewById<TextView>(R.id.questionNumber)
    }
}