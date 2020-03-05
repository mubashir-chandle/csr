package com.csrapp.csr.viewstreams.jobmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Job(val title: String, val streamId: String, val description: String) : Parcelable {
    val jobId = UUID.randomUUID().toString()
}