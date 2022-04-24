package com.ms.spacecraft.ui.game.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ms.spacecraft.databinding.FragmentFavoriteBinding
import com.ms.spacecraft.model.AddFavoriteCallBack
import com.ms.spacecraft.model.Station
import com.ms.spacecraft.ui.adapter.FavoriteListAdapter
import com.ms.spacecraft.ui.game.fragments.SharedGameViewModel
import com.ms.spacecraft.ui.start.StartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private val sharedGameViewModel: SharedGameViewModel by viewModels()
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private var lastStationIndexForAutoFocus = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.favoriteList.layoutManager = layoutManager

        sharedGameViewModel.spaceCraftLiveData().observe(viewLifecycleOwner) { spaceCraft ->
            sharedGameViewModel.stationListLiveData().observe(viewLifecycleOwner) { stationList ->
                val listWithoutEarth = ArrayList<Station>()
                var earthStation: Station? = null
                stationList.forEach {
                    if (it.name != StartViewModel.startStation && it.isFavorite) {
                        listWithoutEarth.add(it)
                    } else {
                        earthStation = it
                    }
                }
                earthStation?.let {
                    binding.favoriteList.adapter =
                        FavoriteListAdapter(listWithoutEarth, it, spaceCraft, addFavoriteOnClick)
                    binding.favoriteList.scrollToPosition(lastStationIndexForAutoFocus)
                }
            }
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