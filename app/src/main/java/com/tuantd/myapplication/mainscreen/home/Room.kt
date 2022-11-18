package com.tuantd.myapplication.mainscreen.home

import java.io.Serializable

data class Room(
    var id_bai_dang: String,
    var id_nguoi_dung: String,
    var dia_chi : String,
    var list_image: List<String>?,
    var gia: String,
    var dien_tich: String,
    var mo_ta: String,
    var name: String,
    var sdt: String,
    var wifi : String,
    var nha_ve_sinh: String,
    var tu_do : String,
    var tu_lanh : String,
    var dieu_hoa: String,
    var may_giat: String,
    var giu_xe: String,
    var bep_nau : String,
    var trang_thai_bai_dang: String,
    var trang_thai_duyet: String,
    var thoi_gian: String,
    var tieu_de: String,
    var id_loai_bai_dang: String
    ) : Serializable {
}

