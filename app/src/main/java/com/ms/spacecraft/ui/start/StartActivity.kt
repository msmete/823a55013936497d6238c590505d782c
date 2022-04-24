package com.ms.spacecraft.ui.start

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ms.spacecraft.R
import com.ms.spacecraft.databinding.ActivityStartBinding
import com.ms.spacecraft.model.BasicCallBack
import com.ms.spacecraft.ui.game.GameActivity
import com.ms.spacecraft.utils.Alert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    private val startViewModel: StartViewModel by viewModels()
    private lateinit var binding: ActivityStartBinding
    private var stationListCallSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (startViewModel.controlSavedDataExist()) {
            openGameActivity()
        } else {
            init()
        }

    }

    private fun init() {
        binding.speedBar.setOnSeekBarChangeListener(seekBarListener)
        binding.capacityBar.setOnSeekBarChangeListener(seekBarListener)
        binding.durabilityBar.setOnSeekBarChangeListener(seekBarListener)
        binding.continueButton.setOnClickListener(continueClickListener)
        initObservers()
        getStationList()
    }

    private fun initObservers() {
        startViewModel.speed.observe(this) {
            binding.speedBar.progress = it
        }
        startViewModel.capacity.observe(this) {
            binding.capacityBar.progress = it
        }
        startViewModel.durability.observe(this) {
            binding.durabilityBar.progress = it
        }
        startViewModel.totalPointText.observe(this) {
            binding.totalPointText.text = it
        }
    }

    private var seekBarListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, newValue: Int, fromUser: Boolean) {
            if (fromUser) {
                when (p0) {
                    binding.speedBar -> {
                        startViewModel.controlAndIncreaseSpeedBar(newValue)
                    }
                    binding.capacityBar -> {
                        startViewModel.controlAndIncreaseCapacityBar(newValue)
                    }
                    binding.durabilityBar -> {
                        startViewModel.controlAndIncreaseDurabilityBar(newValue)
                    }
                }
            }

        }

        override fun onStartTrackingTouch(p0: SeekBar?) {}
        override fun onStopTrackingTouch(p0: SeekBar?) {}
    }

    private var continueClickListener = View.OnClickListener {

        if (validateInput() && startViewModel.saveSpaceCraft(binding.nameArea.text.toString())) {
            openGameActivity()
        }
    }

    private fun validateInput(): Boolean {
        if (binding.nameArea.text.isNullOrBlank()) {
            Alert.showMessage(getString(R.string.name_area_empty_error), this)
            return false
        }

        if (!startViewModel.controlTotalPointForSave()) {
            Alert.showMessage(getString(R.string.total_point_error), this)
            return false
        }

        if (!stationListCallSuccess) {
            Alert.showMessage(getString(R.string.service_error), this@StartActivity)
            return false
        }
        return true
    }

    private fun getStationList() {
        startViewModel.getStationList(object : BasicCallBack {
            override fun onResult(isSuccess: Boolean) {
                stationListCallSuccess = isSuccess
            }
        })
    }

    private fun openGameActivity() {
        val intent = Intent(this, GameActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}