package com.ms.spacecraft.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ms.spacecraft.R
import com.ms.spacecraft.databinding.FavoriteItemViewBinding
import com.ms.spacecraft.model.AddFavoriteCallBack
import com.ms.spacecraft.model.SpaceCraft
import com.ms.spacecraft.model.Station
import com.ms.spacecraft.utils.Util

class FavoriteListAdapter(
    private val dataSet: List<Station>,
    private val earthStation: Station,
    private val spaceCraft: SpaceCraft,
    private val addFavoriteCallBack: AddFavoriteCallBack
) :
    RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: FavoriteItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(station: Station, position: Int) {
            binding.apply {
                binding.nameText.text = station.name
                binding.capacityText.text = "${station.stock}/${station.capacity}"

                val totalDistance = Util.calculateDistanceCraftToStation(spaceCraft, station)
                binding.distanceText.text = "$totalDistance EUS"

                val totalDistanceForEarth =
                    Util.calculateDistanceBetweenStations(earthStation, station)
                binding.distanceToEarthText.text = "$totalDistanceForEarth EUS"

                binding.favoriteButton.setOnClickListener {
                    addFavoriteCallBack.addFavorite(station, position)
                }
                if (station.isFavorite) {
                    binding.favoriteButton.setImageResource(R.drawable.star)
                } else {
                    binding.favoriteButton.setImageResource(R.drawable.star_empty)
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            FavoriteItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position], position)
    }

    override fun getItemCount() = dataSet.size
}
