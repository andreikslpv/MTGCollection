package com.andreikslpv.cards.domain.entities

data class CardFeatureModel(
    val artist: String,
    val cmc: Double,
    val colorIdentity: List<String>,
    val colors: List<String>,
    val flavor: String,
    val foreignNames: List<ForeignNameFeatureModel>,
    val id: String,
    val imageUrl: String,
    val layout: String,
    val manaCost: String,
    val multiverseid: String,
    val name: String,
    val number: String,
    val originalText: String,
    val originalType: String,
    val power: String,
    val printings: List<String>,
    val rarity: String,
    val set: String,
    val setName: String,
    val subtypes: List<String>,
    val text: String,
    val toughness: String,
    val type: String,
    val types: List<String>,
    val watermark: String,
)