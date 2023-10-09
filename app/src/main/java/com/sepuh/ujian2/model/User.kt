package com.sepuh.ujian2.model

import java.io.Serializable

data class User(
    val id: Int,
    val nama: String,
    val alamat: String,
    val jumlah_outstanding: Int
): Serializable
