package com.algarrapablo.mobile.tpalgarra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

private var NuevaEmpresa: Button? = null
private var VerEmpresa: Button? = null

class home_activity : AppCompatActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        NuevaEmpresa = findViewById(R.id.btnNewEmpresa)
        VerEmpresa = findViewById(R.id.btnVerEmpresa)
        NuevaEmpresa!!.setOnClickListener(this)
        VerEmpresa!!.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnNewEmpresa->{
                val intent: Intent = Intent(this, empresas_agregar_activity::class.java)
                startActivity(intent)

            }
            R.id.btnVerEmpresa->{
                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}