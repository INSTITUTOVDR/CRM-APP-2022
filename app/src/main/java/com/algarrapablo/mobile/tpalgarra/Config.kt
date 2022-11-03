package com.algarrapablo.mobile.tpalgarra

class Config() {


    companion object {

        fun cadena(response: String) : String {
            val cadena = response.substring(76)
            var cadenaFinal = cadena
            cadenaFinal = cadenaFinal.substring(0, cadenaFinal.length - 9)
            return  cadenaFinal
        }

    }
}

