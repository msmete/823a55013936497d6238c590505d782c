package com.ms.spacecraft.ui.game.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.ms.spacecraft.R
import com.ms.spacecraft.databinding.FragmentDashboardBinding
import com.ms.spacecraft.model.AddFavoriteCallBack
import com.ms.spacecraft.model.Station
import com.ms.spacecraft.model.StationTravelCallBack
import com.ms.spacecraft.ui.adapter.StationListAdapter
import com.ms.spacecraft.ui.game.fragments.SharedGameViewModel
import com.ms.spacecraft.ui.start.StartViewModel
import com.ms.spacecraft.utils.Alert
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val sharedGameViewModel: SharedGameViewModel by viewModels()
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var lastStationIndexForAutoFocus = 0
    private var mStationAdapter: StationListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper: SnapHelper = PagerSnapHelper()
        binding.stationList.layoutManager = layoutManager
        snapHelper.attachToRecyclerView(binding.stationList)
        initObservers()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mStationAdapter?.let {
                    it.getFilter()?.filter(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                mStationAdapter?.let {
                    it.getFilter()?.filter(query)
                }
                return true
            }

        })
    }

    private fun initObservers() {
        sharedGameViewModel.endMessage.observe(viewLifecycleOwner) {
            when (it) {
                SharedGameViewModel.EndMessageType.UGS -> {
                    Alert.showMessage(getString(R.string.end_message_UGS), requireContext())
                }
                SharedGameViewModel.EndMessageType.DAMAGE -> {
                    Alert.showMessage(getString(R.string.end_message_DAMAGE), requireContext())
                }
                SharedGameViewModel.EndMessageType.EUS -> {
                    Alert.showMessage(getString(R.string.end_message_EUS), requireContext())
                }
            }
        }

        sharedGameViewModel.damageCounter.observe(viewLifecycleOwner) {
            binding.damageCounterText.text = it
        }

        sharedGameViewModel.spaceCraftLiveData().observe(viewLifecycleOwner) { spaceCraft ->
            binding.UGSText.text = "UGS : ${spaceCraft.UGS}"
            binding.EUSText.text = "EUS : ${spaceCraft.EUS}"
            binding.DSText.text = "DS : ${spaceCraft.DS}"
            binding.spaceCraftNameText.text = spaceCraft.name
            binding.damageText.text = "${spaceCraft.damageCapacity}"
            binding.currentStationText.text = spaceCraft.currentStation
            sharedGameViewModel.localSpaceCraft = spaceCraft

            sharedGameViewModel.stationListLiveData().observe(viewLifecycleOwner) { stationList ->
                val listWithoutEarth = ArrayList<Station>()
                stationList.forEach {
                    if (it.name != StartViewModel.startStation) {
                        listWithoutEarth.add(it)
                    }
                }
                sharedGameViewModel.localStationList = listWithoutEarth
                mStationAdapter = StationListAdapter(
                    listWithoutEarth,
                    spaceCraft,
                    travelOnClick,
                    addFavoriteOnClick
                )
                binding.stationList.adapter = mStationAdapter
                binding.stationList.scrollToPosition(lastStationIndexForAutoFocus)
                mStationAdapter!!.getFilter()?.filter(binding.searchView.query)

                sharedGameViewModel.startTimerForDamage()
            }
        }
    }

    private val travelOnClick = object : StationTravelCallBack {
        override fun onTravel(
            station: Station,
            position: Int
        ) {
            lastStationIndexForAutoFocus = position
            sharedGameViewModel.travel(station)
        }
    }

    private val addFavoriteOnClick = object : AddFavoriteCallBack {
        override fun addFavorite(station: Station, position: Int) {
            lastStationIndexForAutoFocus = position
            sharedGameViewModel.addFavorite(station)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}