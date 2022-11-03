package com.algarrapablo.mobile.tpalgarra

import android.app.ProgressDialog
import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONException

private var dataSetType: MutableList<Types.Type> = ArrayList()
private var adapter: ArrayAdapter<Types.Type>? = null
private var tipo: Spinner? = null
private var adapter2: CustomerAdapter? = null
private var dataset: MutableList<Types.Type> = ArrayList()
private lateinit var items: RecyclerView

class MainActivity : AppCompatActivity(),CustomerAdapter.OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tipo = findViewById(R.id.tipo)
        items=findViewById(R.id.list)


        //getCompany()
        getListCompany()
    }

    fun getCompany() {


        val queue = Volley.newRequestQueue(this)
        val url =
            "http://institutosuperiorvilladelrosario.edu.ar/CRM/ws/Webservice1.asmx/GetEmpresas"
        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    var cadenaFinal = Config.cadena(response)

                    Log.d("Respuesta", cadenaFinal)

                    val data = Gson()!!.fromJson<Types.Types>(
                        cadenaFinal,
                        Types.Types::class.java
                    )

                    dataSetType.addAll(data.data)

                    adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        dataSetType

                    )
                    tipo!!.adapter = adapter

                } catch (e: JSONException) {

                    e.printStackTrace()
                    Toast.makeText(this, "Error--> $e", Toast.LENGTH_LONG).show()
                }
            }) { error ->

            error.printStackTrace()
            // Toast.makeText(activity, "Error--> $error", Toast.LENGTH_LONG).show()
        }
        queue!!.add(request)
    }

    fun getListCompany() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setIcon(R.mipmap.ic_launcher)
        progressDialog.setMessage("Espere por favor...")
        progressDialog.show()

        val queue = Volley.newRequestQueue(this)
        val url =

            "http://institutosuperiorvilladelrosario.edu.ar/CRM/ws/Webservice1.asmx/GetEmpresas"
        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    var cadenaFinal = Config.cadena(response)

                    Log.d("Respuesta", cadenaFinal)

                    val data = Gson()!!.fromJson<Types.Types>(
                        cadenaFinal,
                        Types.Types::class.java
                    )
                    dataset.clear()
                    dataset.addAll(data.data)
                 adapter2 = CustomerAdapter(this, dataset,this   )

                    items.layoutManager=LinearLayoutManager(this, RecyclerView.VERTICAL,false)
                    items.adapter= adapter2
                    progressDialog.hide()
                } catch (e: JSONException) {

                    e.printStackTrace()
                    Toast.makeText(this, "Error--> $e", Toast.LENGTH_LONG).show()
                    progressDialog.hide()
                }
            }) { error ->

            error.printStackTrace()
            // Toast.makeText(activity, "Error--> $error", Toast.LENGTH_LONG).show()
        }
        queue!!.add(request)
    }

    override fun onItemClick(position: Int, url: Types.Type) {
       Toast.makeText(this,url.name,Toast.LENGTH_LONG).show()
        val intent: Intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("dato",url.name)
        intent.putExtra("image",url.Image)
        startActivity(intent)


    }
}

