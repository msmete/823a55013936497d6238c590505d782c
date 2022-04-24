package com.ms.spacecraft.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.ms.spacecraft.R
import com.ms.spacecraft.databinding.StationItemViewBinding
import com.ms.spacecraft.model.AddFavoriteCallBack
import com.ms.spacecraft.model.SpaceCraft
import com.ms.spacecraft.model.Station
import com.ms.spacecraft.model.StationTravelCallBack
import com.ms.spacecraft.utils.Util
import java.util.*

class StationListAdapter(
    private val dataSet: List<Station>,
    private val spaceCraft: SpaceCraft,
    private val travelCallBack: StationTravelCallBack,
    private val addFavoriteCallBack: AddFavoriteCallBack
) :
    RecyclerView.Adapter<StationListAdapter.ViewHolder>() {

    private var filteredList: ArrayList<Station> = ArrayList(dataSet)

    inner class ViewHolder(var binding: StationItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(station: Station, position: Int) {
            binding.apply {
                binding.nameText.text = station.name
                binding.capacityText.text = "${station.stock}/${station.capacity}"
                val totalDistance = Util.calculateDistanceCraftToStation(spaceCraft, station)
                binding.eusText.text = "$totalDistance EUS"
                binding.travelButton.isEnabled = !station.isCompleted
                if (totalDistance > spaceCraft.EUS) {
                    binding.travelButton.isEnabled = false
                }
                binding.travelButton.setOnClickListener {
                    travelCallBack.onTravel(station, position)
                }
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
            StationItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredList[position], position)
    }

    override fun getItemCount() = filteredList.size

    fun getFilter(): Filter {
        return stationFilter
    }

    private val stationFilter = object : Filter() {
        override fun performFiltering(searchText: CharSequence?): FilterResults {
            val filteredList: ArrayList<Station> = ArrayList()
            if (searchText == null || searchText.isEmpty()) {
                searchText.let { filteredList.addAll(dataSet) }
            } else {
                val query = searchText.toString().trim().lowercase()
                dataSet.forEach {
                    if (it.name.lowercase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                filteredList.clear()
                filteredList.addAll(results.values as ArrayList<Station>)
                notifyDataSetChanged()
            }
        }
    }
}
