package com.tuantd.myapplication.mainscreen.home

import java.io.Serializable

data class Room(
    var id_bai_dang: String,
    var id_nguoi_dung: String?,
    var dia_chi : String?,
    var list_image: List<String>?,
    var gia: String,
    var dien_tich: String,
    var mo_ta: String,
    var name: String,
    var sdt: String,
    var wifi : Boolean,
    var nha_ve_sinh: Boolean,
    var tu_do : Boolean,
    var tu_lanh : Boolean,
    var dieu_hoa: Boolean,
    var may_giat: Boolean,
    var giu_xe: Boolean,
    var bep_nau : Boolean,
    var trang_thai_bai_dang: Boolean,
    var trang_thai_duyet: Boolean,
    var thoi_gian: String,
    var tieu_de: String,
    var id_loai_bai_dang: String
    ) : Serializable {
}

