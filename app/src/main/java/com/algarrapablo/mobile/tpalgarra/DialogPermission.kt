package com.algarrapablo.mobile.tpalgarra

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import pl.droidsonroids.gif.GifImageView

class DialogPermission(
    context: Context,
    callBack: View.OnClickListener?,
    private val title: String,
    private val description: String) : AlertDialog(context), View.OnClickListener {
    private var titleView: TextView? = null
    private var descriptionView: TextView? = null
    private var cancelButtonVisible = true
    private var gif: Int = 0
    private var callBack: View.OnClickListener? = null
    private var image: GifImageView? = null
    private var cancelDialog = true
    private var textCancel = R.string.cancel
    private var textOk = android.R.string.ok

    init {
        this.gif = R.mipmap.camera
        this.callBack = callBack
    }

    fun setGif(gif: Int) {
        this.gif = gif
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(cancelDialog)
        setContentView(R.layout.alert_permission)

        titleView = findViewById(R.id.titleDialog)
        descriptionView = findViewById(R.id.descriptionDialog)
        image = findViewById(R.id.gif)

        if (image != null)
            image!!.setImageResource(gif)

        if (titleView != null)
            titleView!!.text = title

        if (descriptionView != null)
            descriptionView!!.text = description


        (findViewById<TextView>(R.id.accept))!!.text = context.getString(textOk)
        (findViewById<TextView>(R.id.cancel))!!.text = context.getString(textCancel)

        if (findViewById<TextView>(R.id.cancel) != null) {
            if (!this.cancelButtonVisible) {
                (findViewById<TextView>(R.id.accept))!!.text = context.getString(android.R.string.ok)
                findViewById<TextView>(R.id.cancel)!!.visibility = View.GONE
            }
            findViewById<TextView>(R.id.cancel)!!.setOnClickListener(this)
        }

        if (findViewById<TextView>(R.id.accept) != null)
            findViewById<TextView>(R.id.accept)!!.setOnClickListener(this)

        if (window != null) {
            window!!.setBackgroundDrawable(ContextCompat.getDrawable(context, android.R.color.transparent))
            window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onClick(view: View) {
        dismiss()
        when (view.id) {
            R.id.cancel, R.id.accept -> {
                callBack?.onClick(view)
            }
        }
    }

    fun enableCancelButton(visible: Boolean) {
        this.cancelButtonVisible = visible
    }

    fun setTextCancel(text: Int) {
        textCancel = text
    }

    fun setTextOk(text: Int) {
        textOk = text
    }

    override fun setCancelable(cancel: Boolean) {
        super.setCancelable(cancel)
    }
}