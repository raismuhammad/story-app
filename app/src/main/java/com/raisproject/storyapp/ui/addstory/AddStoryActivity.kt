package com.raisproject.storyapp.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.raisproject.storyapp.R
import com.raisproject.storyapp.utils.createTempFile
import com.raisproject.storyapp.data.Result
import com.raisproject.storyapp.data.preferences.UserPreferences
import com.raisproject.storyapp.databinding.ActivityAddStoryBinding
import com.raisproject.storyapp.ui.ViewModelFactory
import com.raisproject.storyapp.ui.userstory.UserStoryActivity
import com.raisproject.storyapp.utils.reduceFileImage
import com.raisproject.storyapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var getFile: File? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var userPreferences: UserPreferences

    lateinit var factory: ViewModelFactory
    val viewModel: AddStoryViewModel by viewModels() { factory }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = null
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        userPreferences = UserPreferences.getInstance(this)
        factory = ViewModelFactory.getInstance(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val token = userPreferences.getUser().token

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener {
            if (token != null) {
                uploadImage(
                    binding.etDesc.text.toString(),
                    token
                )
            }
        }
    }



    private fun uploadImage(desc: String, token: String) {
        if (getFile != null) {
            val lastLoc = fusedLocationProviderClient.lastLocation
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            }

            var lat: Double
            var lon: Double

            lastLoc.addOnSuccessListener { location: Location? ->
                if (location != null) {

                    val auth = "bearer ${token}"
                    val file = reduceFileImage(getFile as File)

                    lat = location.latitude
                    lon = location.longitude

                    val description = desc.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )
                    viewModel.uploadStory(imageMultiPart, description, lat, lon, auth).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(this, "Add Story ${result.data.message}", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, UserStoryActivity::class.java))
                                }
                                is Result.Loading -> { binding.progressBar.visibility = View.VISIBLE }
                                is Result.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    Log.d("TAG", "uploadImage: ${result.message}")
                                    Toast.makeText(this, "Add Story ${result.message}" , Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
                } else {
                    Toast.makeText(this@AddStoryActivity, R.string.insert_image, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.raisproject.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                getFile = file
                binding.ivImageStory.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                binding.ivImageStory.setImageURI(uri)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}