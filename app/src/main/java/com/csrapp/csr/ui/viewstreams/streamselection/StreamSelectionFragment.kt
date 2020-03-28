package com.csrapp.csr.ui.viewstreams.streamselection

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
import com.csrapp.csr.utils.InjectorUtils
import com.csrapp.csr.utils.ResourceProvider
import kotlinx.android.synthetic.main.fragment_stream_selection.*


class StreamSelectionFragment : Fragment() {
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stream_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        initUI()
    }

    private fun initUI() {
        val factory = InjectorUtils.provideStreamSelectionViewModelFactory(requireContext())
        val viewModel = ViewModelProvider(this, factory).get(StreamSelectionViewModel::class.java)

        initRecyclerView(viewModel)
    }

    private fun initRecyclerView(viewModel: StreamSelectionViewModel) {
        val streamAdapter = StreamRecyclerAdapter()
        val streams = viewModel.getAllStreams()
        streamAdapter.populateWithData(streams)
        streamAdapter.setUpNavController(navController)

        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ResourceProvider.getDrawable(R.drawable.separator_stream))

        streamRecyclerView.apply {
            adapter = streamAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(itemDecorator)
        }
    }
}
