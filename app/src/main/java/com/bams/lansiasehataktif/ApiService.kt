package com.bams.lansiasehataktif

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("bigdata/dpmdes/idm_jml_lansia__des_kel?exclude=bps_kode_desa_kelurahan,bps_kode_kabupaten_kota,bps_kode_kecamatan,bps_nama_kabupaten_kota,bps_nama_kecamatan,kemendagri_kode_desa_kelurahan,kemendagri_kode_kecamatan,kemendagri_nama_desa_kelurahan,kemendagri_nama_kecamatan")
    fun getLansiaData(): Call<List<LansiaData>>
}
