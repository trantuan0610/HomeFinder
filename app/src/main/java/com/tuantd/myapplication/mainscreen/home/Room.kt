package com.tuantd.myapplication.mainscreen.home

import java.io.Serializable

data class Room(
    var roomId: String,
    var email : String,
    var roomAddress: String,
    var roomImage: String,
    var price: String,
    var roomArea: String,
    var roomDescription: String,
    var name: String,
    var phone: String,
    var wifi: String,
    var wc: String,
    var free: String,
    var fridge: String,
    var airConditional: String,
    var washingMachine: String,
    var parking: String,
    var kitchen: String
    ) : Serializable {
}

