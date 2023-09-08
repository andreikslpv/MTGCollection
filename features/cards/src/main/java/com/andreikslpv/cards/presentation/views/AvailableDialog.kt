package com.andreikslpv.cards.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.andreikslpv.cards.R
import com.andreikslpv.cards.databinding.MergeAvailableDialogBinding
import com.andreikslpv.cards.domain.entities.CardCondition
import com.andreikslpv.cards.domain.entities.CardLanguage
import com.andreikslpv.presentation.views.visible
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class AvailableDialog(context: Context, attributeSet: AttributeSet?) :
    ConstraintLayout(context, attributeSet) {
    private val binding = MergeAvailableDialogBinding.inflate(LayoutInflater.from(context), this)
    val actionButton = binding.dialogActionButton
    val deleteButton = binding.dialogDelete
    val languageText = binding.dialogLanguageText
    val conditionText = binding.dialogConditionText
    val countText = binding.dialogCountText
    val foilText = binding.dialogFoilText
    val dialogTitle = binding.dialogTitle

    init {
        binding.dialogCancelButton.setOnClickListener {
            this.visible(false)
        }

        (binding.dialogLanguageText as? MaterialAutoCompleteTextView)?.setSimpleItems(
            getCardLanguageArray()
        )

        (binding.dialogConditionText as? MaterialAutoCompleteTextView)?.setSimpleItems(
            getCardConditionArray()
        )

        (binding.dialogFoilText as? MaterialAutoCompleteTextView)?.setSimpleItems(
            arrayOf(context.getString(R.string.foil_no), context.getString(R.string.foil_yes))
        )
    }

    private fun getCardLanguageArray(): Array<String> {
        val result = mutableListOf<String>()
        CardLanguage.values().forEach {
            result.add(it.cardLang)
        }
        return result.toTypedArray()
    }

    private fun getCardConditionArray(): Array<String> {
        val result = mutableListOf<String>()
        CardCondition.values().forEach {
            result.add(it.fullName)
        }
        return result.toTypedArray()
    }

}