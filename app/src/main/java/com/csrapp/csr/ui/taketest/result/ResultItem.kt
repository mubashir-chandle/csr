package com.csrapp.csr.ui.taketest.result

class ResultItem(
    val type: ResultItemType,
    val id: String,
    val title: String,
    val description: String,
    val score: Int
) {
    enum class ResultItemType {
        APTITUDE, PERSONALITY
    }
}
