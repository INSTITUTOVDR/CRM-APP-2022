package com.algarrapablo.mobile.tpalgarra

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONException

class new_empresas {

    companion object
    {
        private var URL_BASE = "http://institutosuperiorvilladelrosario.edu.ar/CRM/ws/Webservice1.asmx/"
        private var ENDPOINT = "NuevaEmpresa"

        //post login
        fun postNewEmpresas(
            context: Context,
            image: String,
            empresaedt: String

        ) {
            val progressDialog = ProgressDialog(context)
            progressDialog.setIcon(R.mipmap.ic_launcher)
            progressDialog.setMessage("Espere por favor...")
            progressDialog.show()
            val queue = Volley.newRequestQueue(context)
            val url = URL_BASE + ENDPOINT
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    try {
                        progressDialog.dismiss()

                        Toast.makeText(context,"datos guardados con exito",Toast.LENGTH_LONG).show()
                      open(context)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    Log.d("tag", "error: $error")
                    progressDialog.dismiss()
                    Toast.makeText(context, "Error--> $error", Toast.LENGTH_LONG).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): MutableMap<String, String>? {
                    val parametros: MutableMap<String, String> = HashMap()

                    val imagen = "\"Imagen\":"
                    val empresa = "\"RazonSocial\":"
                    //{"RazonSocial":"RazonSocial","Imagen":""}
                    parametros["Cadena"] = "{$empresa\"$empresaedt\",$imagen\"$image\"}"

//                    parametros["Image"] = image
//                    parametros["Description"] = description
//                    parametros["Type"] = type
//                    parametros["QuantityDays"] = quantity
//                    parametros["UserId"] = userId

                    println(Gson().toJson(parametros))


                    return parametros
                }
            }
            queue.add(stringRequest)
        }

        fun open(context: Context){
            val intent: Intent = Intent(context, MainActivity::class.java)
            startActivity(context,intent,null)
        }


    }
}

