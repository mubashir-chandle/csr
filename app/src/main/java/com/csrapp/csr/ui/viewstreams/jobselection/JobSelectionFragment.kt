package com.csrapp.csr.ui.viewstreams.jobselection

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.csrapp.csr.R
import com.csrapp.csr.data.StreamEntity
import com.csrapp.csr.utils.InjectorUtils
import kotlinx.android.synthetic.main.fragment_job_selection.*

class JobSelectionFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController
    private lateinit var stream: StreamEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        initUI()
    }

    private fun initUI() {
        val streamId = arguments!!.getString("stream")!!

        val factory = InjectorUtils.provideJobSelectionViewModelFactory(requireContext())
        val viewModel = ViewModelProvider(this, factory).get(JobSelectionViewModel::class.java)

        stream = viewModel.getStreamById(streamId)
        streamTitle.text = stream.title

        initRecyclerView(stream.id, viewModel)
        fabDescription.setOnClickListener(this)
    }

    private fun initRecyclerView(stream: String, viewModel: JobSelectionViewModel) {
        val jobAdapter = JobRecyclerAdapter()
        val jobs = viewModel.getJobsByStream(stream)
        jobAdapter.populateJobs(jobs)
        jobAdapter.setUpNavController(navController)

        jobRecyclerView.apply {
            adapter = jobAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabDescription -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(stream.title)
                    .setMessage(stream.description)
                    .setPositiveButton(R.string.close, null)
                    .create()
                    .show()
            }
        }
    }
}
