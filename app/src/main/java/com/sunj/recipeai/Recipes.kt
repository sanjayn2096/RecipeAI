package com.sunj.recipeai

import android.os.Parcel
import android.os.Parcelable

data class Recipes (
    val recipeName: String,
    val image: String,
    val ingredients: String,
    val instructions: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

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
