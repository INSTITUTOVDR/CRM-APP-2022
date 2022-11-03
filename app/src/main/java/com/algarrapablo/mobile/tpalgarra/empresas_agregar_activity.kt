package com.algarrapablo.mobile.tpalgarra

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.IOException

private var nombre: EditText? = null
private var aceptar: Button? = null

//camara
private var avatarBitmap: Bitmap? = null
private var forceReload = false
private var enablePermission = false
private var isCamera: Boolean = false
private var imageGenerateUri: Uri? = null
private lateinit var image: AppCompatImageView
private lateinit var clean: AppCompatImageView



class empresas_agregar_activity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresas_agregar)
        nombre=findViewById(R.id.edtNombre)
        aceptar=findViewById(R.id.btnAceptar)
        image = findViewById(R.id.image) as AppCompatImageView
        image.setOnClickListener(this)
        aceptar!!.setOnClickListener(this)


    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnAceptar->{
                var image = ""
                if (avatarBitmap == null) {

                } else {
                    image = toBase64()
                }


                new_empresas.postNewEmpresas(this, image, nombre!!.text.toString() )

            }
            R.id.image -> {
                if (!forceReload) {
                    val selectDirecory = DialogPermission(
                        this,
                        View.OnClickListener {
                            isCamera = it.id == R.id.accept
                            checkForGalleryPermissions()
                        },
                        getString(R.string.directory_title),
                        getString(R.string.directory_description)
                    )
                    selectDirecory.setGif(R.mipmap.camera)
                    selectDirecory.setTextCancel(R.string.gallery)
                    selectDirecory.setTextOk(R.string.camera)
                    selectDirecory.setCancelable(true)
                    selectDirecory.show()
                }
            }
        }
    }
    //camara
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkForGalleryPermissions() {
        val readChecked = ContextCompat.checkSelfPermission(
            (if (this == null) this!! else this)!!,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writeChecked = ContextCompat.checkSelfPermission(
            (if (this   == null) this!! else this)!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val camera =
            ContextCompat.checkSelfPermission(
                (if (this == null) this!! else this)!!,
                Manifest.permission.CAMERA
            )

        if (readChecked != PackageManager.PERMISSION_GRANTED ||
            writeChecked != PackageManager.PERMISSION_GRANTED ||
            camera != PackageManager.PERMISSION_GRANTED
        ) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
            ) {
                showPermissionsNeededDialog(false)
                return
            } else {
                showPermissionsNeededDialog(true)
            }
            return
        }
        enablePermission = true
        forceReload = false
        if (isCamera) {
            getImageCamera()
        } else {
            getImageDirectory()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionsNeededDialog(isRequest: Boolean) {

        val permission = DialogPermission(
           this!!,
            View.OnClickListener { view ->
                if (view.id == R.id.accept) {
                    if (isRequest) {
                        val i = Intent()
                        i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        i.addCategory(Intent.CATEGORY_DEFAULT)
                        i.data = Uri.parse("package:" + this!!.packageName)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        this!!.startActivity(i)
                        forceReload = true
                    } else {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            ), 2
                        )
                    }
                } else {
                    enablePermission = false
                    permissionDenied()
                }
            },
            getString(R.string.requested_permission_missing_title),
            getString(R.string.requested_permission_external_store)
        )
        permission.setGif(R.mipmap.camera)
        permission.show()
    }

    private fun permissionDenied() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.access_denied)
        builder.setMessage(R.string.access_read_denied)
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            Toast.makeText(
                this,
                "camara",
                Toast.LENGTH_LONG
            ).show()
        }
        builder.show()
    }


    @SuppressLint("UseRequireInsteadOfGet")
    private fun getImageDirectory() {
        val intent = Intent()
        intent.type = "image/jpeg"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "image"),
            2
        )
    }

    private fun getImageCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, createImage())
        startActivityForResult(intent, 3)
    }

    private fun createImage(): Uri {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, getString(R.string.app_name))
        values.put(
            MediaStore.Images.Media.DESCRIPTION,
            "Photo taken on " + System.currentTimeMillis()
        )
        imageGenerateUri = this!!.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )!!
        return imageGenerateUri!!
    }

    @Suppress("DEPRECATION")
    private fun onSelectFromGalleryResult(data: Intent?) {
        var bm: Bitmap? = null
        if (data != null) {
            try {
                bm =
                    MediaStore.Images.Media.getBitmap(this!!.contentResolver, data.data)
                val selectedImageName = data!!.data?.lastPathSegment
                Log.i("foto3", selectedImageName.toString())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        if (bm != null) {
            this

            Log.i("foto3", imageGenerateUri.toString())

            image.setImageBitmap(avatarBitmap)
            clean.visibility = View.VISIBLE
            aceptar!!.visibility = View.VISIBLE

        }
    }

    @Suppress("DEPRECATION")
    private fun onSelectFromGalleryResult() {
        var bm: Bitmap? = null
        if (imageGenerateUri != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(
                    this!!.contentResolver,
                    imageGenerateUri
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        if (bm != null) {
            avatarBitmap = bm
            image.setImageBitmap(avatarBitmap)
            //clean.visibility = View.VISIBLE
            aceptar!!.visibility = View.VISIBLE

        }
    }

    private fun toBase64(): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        avatarBitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (requestCode == 4) {
            if (grantResults.size == 3) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] == PackageManager.PERMISSION_GRANTED
                ) {
                    enablePermission = true
                    if (isCamera) {
                        getImageCamera()
                    } else {
                        getImageDirectory()
                    }
                } else {
                    permissionDenied()
                }
            } else {
                permissionDenied()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 4) {
            enablePermission = true
            if (isCamera) {
                getImageCamera()
            } else {
                getImageDirectory()
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                onSelectFromGalleryResult(data)


            }
        } else if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                onSelectFromGalleryResult()


            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}