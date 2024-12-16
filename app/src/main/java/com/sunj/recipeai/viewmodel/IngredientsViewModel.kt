package com.sunj.recipeai.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IngredientsViewModel : ViewModel() {

    private val _ingredients = MutableLiveData<List<String>>()
    val ingredients: LiveData<List<String>> get() = _ingredients

    fun fetchIngredients() {
       // TODO : Use retrofit to fetch ingredients from the API.  Uncomment once we get base URL.
       val retrofit = Retrofit.Builder()
             .baseUrl("https://api.example.com/")
             .addConverterFactory(GsonConverterFactory.create())
             .build();

//        val ingredientsApi = retrofit.create(IngredientsApi::class.java)
//        val call = ingredientsApi.getIngredients()

    }

}