package com.dicoding.godive.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

typealias DivingPlace = List<DivingPlaceItem>

@Parcelize
data class DivingPlaceItem(
	@field:SerializedName("provinsi")
	val provinsi: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("kota")
	val kota: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("negara")
	val negara: String? = null,

	@field:SerializedName("lokasi")
	val lokasi: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("jam_operasi")
	val jamOperasi: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
) : Parcelable
