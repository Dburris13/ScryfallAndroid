package com.example.scryfallmobile

class Card (
    // Core Information
    val name: String,
    val mana_cost: String,
    val type_line: String,
    val oracle_text: String,
    val flavor_text: String,
    val artist: String,
    val legalities: Legalities,


    // Images
    val image_uris: ImageUris,

    // Behind the scenes
    val id: String,
    val cmc: Int,
    val rarity: String
)

class HomeFeed(val cards : MutableList<Card>)

class ImageUris(
    val small : String,
    val normal : String,
    val large : String,
    val art_crop: String,
    val border_crop: String,
    val png: String
)

class Legalities(
    val standard: String,
    val future: String,
    val historic: String,
    val gladiator: String,
    val pioneer: String,
    val modern: String,
    val legacy: String,
    val pauper: String,
    val vintage: String,
    val penny: String,
    val commander: String,
    val brawl: String,
    val duel: String,
    val oldschool: String,
    val premodern: String
)