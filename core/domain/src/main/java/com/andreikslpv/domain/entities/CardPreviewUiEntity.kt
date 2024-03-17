package com.andreikslpv.domain.entities

import java.io.Serializable

/** Represents data from [CardPreviewEntity] + state in coolection. */

data class CardPreviewUiEntity(
    override val artist: String?,
    override val cardBackId: String,
    override val cmc: Double,
    override val collectorNumber: String,
    override val colorIdentity: List<String>,
    override val colors: List<String>?,
    override val edhrecRank: Int?,
    override val finishes: List<String>,
    override val foil: Boolean?,
    override val highresImage: Boolean,
    override val id: String,
    override val imageStatus: String,
    override val imageDetailUri: String,
    override val imagePreviewUri: String,
    override val lang: String,
    override val manaCost: String?,
    override val name: String,
    override val nonfoil: Boolean?,
    override val pennyRank: Int?,
    override val power: String?,
    override val priceInEur: String?,
    override val priceInTix: String?,
    override val priceInUsd: String?,
    override val printedName: String?,
    override val cardPurchaseUrisEntity: CardPurchaseUrisEntity?,
    override val rarity: String,
    override val cardRelatedUrisEntity: CardRelatedUrisEntity,
    override val releasedAt: String,
    override val setCode: String,
    override val toughness: String?,
    override val uri: String,
    val availableCards: MutableList<AvailableCardEntity> = mutableListOf(),
    val isInCollection: Boolean = false,
) : CardPreviewEntity, Serializable {

    constructor(card: CardPreviewEntity?, isInCollection: Boolean) : this(
        artist = card?.artist ?: "",
        cardBackId = card?.cardBackId ?: "",
        cmc = card?.cmc ?: 0.0,
        collectorNumber = card?.collectorNumber ?: "",
        colorIdentity = card?.colorIdentity ?: listOf(),
        colors = card?.colors ?: listOf(),
        edhrecRank = card?.edhrecRank ?: 0,
        finishes = card?.finishes ?: listOf(),
        foil = card?.foil ?: false,
        highresImage = card?.highresImage ?: false,
        id = card?.id ?: "",
        imageStatus = card?.imageStatus ?: "",
        imageDetailUri = card?.imageDetailUri ?: "",
        imagePreviewUri = card?.imagePreviewUri ?: "",
        lang = card?.lang ?: "",
        manaCost = card?.manaCost ?: "",
        name = card?.name ?: "",
        nonfoil = card?.nonfoil ?: true,
        pennyRank = card?.pennyRank ?: 0,
        power = card?.power ?: "",
        priceInEur = card?.priceInEur ?: "",
        priceInTix = card?.priceInTix ?: "",
        priceInUsd = card?.priceInUsd ?: "",
        printedName = card?.printedName ?: "",
        cardPurchaseUrisEntity = card?.cardPurchaseUrisEntity,
        rarity = card?.rarity ?: "",
        cardRelatedUrisEntity = card?.cardRelatedUrisEntity ?: CardRelatedUrisEntity(),
        releasedAt = card?.releasedAt ?: "",
        setCode = card?.setCode ?: "",
        toughness = card?.toughness ?: "",
        uri = card?.uri ?: "",
        isInCollection = isInCollection,
    )
}
