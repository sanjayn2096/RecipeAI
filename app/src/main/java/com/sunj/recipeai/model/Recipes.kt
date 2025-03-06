package com.sunj.recipeai.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

data class Recipes (
    val recipeId: String,
    val recipeName: String,
    val image: String,
    val ingredients: String,
    val instructions: String,
    val cookingTime: String,
    val cuisine: String,
    val nutritionalValue: NutritionalValue,
    var isFavorite: Boolean = false
) : Parcelable {
    @SuppressLint("NewApi")
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable<NutritionalValue>(NutritionalValue::class.java.classLoader)!!,
        parcel.readBoolean()
    )

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Recipes> {
        override fun createFromParcel(parcel: Parcel): Recipes {
            return Recipes(parcel)
        }

        override fun newArray(size: Int): Array<Recipes?> {
            return arrayOfNulls(size)
        }
    }
}
