package com.andreikslpv.presentation_settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.andreikslpv.presentation.BaseFragment
import com.andreikslpv.presentation_settings.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initStartSetsType()
    }

    private fun initToolbar() {
        binding.toolbar.title = getString(R.string.title_settings)
        binding.toolbar.setNavigationIcon(com.andreikslpv.presentation.R.drawable.ic_arrow_back)
        binding.toolbar.navigationContentDescription =
            getString(com.andreikslpv.presentation.R.string.description_back_button)
        binding.toolbar.setNavigationOnClickListener { viewModel.goBack() }
    }

    private fun initStartSetsType() {
        viewModel.typesOfSet.observe(viewLifecycleOwner) {
            binding.startSetTypeText.apply {
                this.setSimpleItems(it.toTypedArray())
                this.setText(viewModel.getStartedTypeOfSet(), false)
                this.setOnItemClickListener { parent, _, position, _ ->
                    viewModel.setStartedTypeOfSet(parent.getItemAtPosition(position) as String)
                }
            }
        }
    }

}