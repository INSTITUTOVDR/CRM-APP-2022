package com.algarrapablo.mobile.tpalgarra

import android.media.Image
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Types {



    @SerializedName("Status")
    var statusCode: Int = 0

    @SerializedName("Data")
    var data: Receipt? = null



    inner class Type : Serializable {
        @SerializedName("RazonSocial")
        var name: String? = null

        @SerializedName("CategoryId")
        var categoryId: String? = null

        @SerializedName("Imagen")
        var Image: String? = null

        constructor(name: String?) {
            this.name = name
        }

        override fun toString(): String {
            return name!!
        }


    }

    inner class Types(){
        @SerializedName("Status")
        var statusCode: Int? = null

        @SerializedName("Data")
        var data: MutableList<Type> = ArrayList()
    }

}
