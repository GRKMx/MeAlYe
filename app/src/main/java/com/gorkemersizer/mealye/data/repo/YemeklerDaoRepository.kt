package com.gorkemersizer.mealye.data.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import com.gorkemersizer.mealye.data.entity.*
import com.gorkemersizer.mealye.retrofit.YemeklerDao
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YemeklerDaoRepository(var ydao: YemeklerDao) {
    var yemeklerListesi: MutableLiveData<List<Yemekler>>
    var sepetListesi: MutableLiveData<List<SepetYemekler>>
    var siparisYemekler: MutableLiveData<List<SiparisYemekler>>

    init {
        yemeklerListesi = MutableLiveData()
        sepetListesi = MutableLiveData()
        siparisYemekler = MutableLiveData()
    }

    fun yemekleriGetir() : MutableLiveData<List<Yemekler>> {
        return yemeklerListesi
    }

    fun yemekEkle(yemek_ad: String, yemek_resim_adi: String, yemek_fiyat: Int, yemek_siparis_adet: Int, kullanici_adi: String) {
        ydao.sepeteYemekEkle(yemek_ad, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi).enqueue(object : Callback<CRUDCevap>{
            override fun onResponse(call: Call<CRUDCevap>, response: Response<CRUDCevap>) {
                val basari = response.body()!!.success
                val mesaj = response.body()!!.message
                Log.e("Yemek kayıt","$basari - $mesaj")
            }
            override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {}
        })
    }

    fun yemekSil(sepet_yemek_id: Int, kullanici_adi: String) {
        ydao.sepettenYemekSil(sepet_yemek_id, kullanici_adi).enqueue(object : Callback<CRUDCevap> {
            override fun onResponse(call: Call<CRUDCevap>, response: Response<CRUDCevap>) {
                val basari = response.body()!!.success
                val mesaj = response.body()!!.message
                Log.e("Yemek sil","$basari - $mesaj")
                tumYemekleriAl()
            }
            override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {}
        })
    }

    fun sepetCagir(): MutableLiveData<List<SepetYemekler>>{
        return sepetListesi
    }

    fun sepetiGetir(kullanici_adi: String) {
        ydao.sepettekiYemekleriGetir(kullanici_adi).enqueue(object : Callback<SepetYemeklerCevap> {
            override fun onResponse(call: Call<SepetYemeklerCevap>, response: Response<SepetYemeklerCevap>) {
                val basari = response.body()!!.success
                val liste = response.body()!!.sepet_yemekler
                sepetListesi.value = liste
            }
            override fun onFailure(call: Call<SepetYemeklerCevap>, t: Throwable) {}
        })
    }

    fun tumYemekleriAl() {
        ydao.tumYemekler().enqueue(object:Callback<YemeklerCevap>{
            override fun onResponse(call: Call<YemeklerCevap>, response: Response<YemeklerCevap>) {
                val liste = response.body()!!.yemekler
                yemeklerListesi.value = liste
            }
            override fun onFailure(call: Call<YemeklerCevap>, t: Throwable) {}
        })
    }

}