package com.ntphat.thesisk40.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.CheckResult
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import com.bumptech.glide.Glide
import com.ntphat.thesisk40.R
import com.ntphat.thesisk40.adapter.FaceDetailAdapter
import com.ntphat.thesisk40.constant.App
import com.ntphat.thesisk40.constant.IntentString
import com.ntphat.thesisk40.data.Error
import com.ntphat.thesisk40.exception.InvalidExtraData
import com.ntphat.thesisk40.presenter.FaceCheckingPresenter
import com.ntphat.thesisk40.presenter.impl.FaceCheckingPresenterImpl
import com.ntphat.thesisk40.util.Camera
import com.ntphat.thesisk40.util.UrlParser
import com.ntphat.thesisk40.view.action.RecyclerTouchListener
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.lang.Exception


class ImageCheckingActivity : AppCompatActivity(), FaceCheckingPresenter.View {

    private val presenter = FaceCheckingPresenterImpl(this)

    private lateinit var takePictureLayout: LinearLayout
    private lateinit var progressLayout: LinearLayout
    private lateinit var checkResultLayout: ScrollView
    private lateinit var imageViewTakenPicture: ImageView
    private lateinit var imageViewCheckResult: ImageView
    private lateinit var btnTakePicture: Button
    private lateinit var btnCheck: Button
    private lateinit var btnBack: Button
    private lateinit var btnConfirm: Button
    private lateinit var rvFaceDetail: RecyclerView
    private var imageStoragePath: String = ""

    override fun showCheckResult(imageLink: String) {
        runOnUiThread {
            toggleCheckResult(true)
            val path = when (imageLink[0]) {
                '.' -> imageLink.drop(1)
                else -> imageLink
            }
            Glide.with(this)
                    .load(Uri.parse(UrlParser.getFullApiUrl(path)))
                    .into(imageViewCheckResult)
        }
    }

    // View
    companion object {
        @JvmStatic
        val REQUEST_CODE_CAPTURE_IMAGE = 1011

        @JvmStatic
        val REQUEST_CODE_PERMISSION = 1012
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_checking)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        takePictureLayout = findViewById(R.id.content_take_picture)
        progressLayout = findViewById(R.id.content_progress)
        checkResultLayout = findViewById(R.id.content_check_result)

        imageViewTakenPicture = findViewById(R.id.image_view_taken_picture)
        imageViewCheckResult = findViewById(R.id.image_view_checking_result)
        btnTakePicture = findViewById(R.id.btn_take_picture)
        btnCheck = findViewById(R.id.btn_check)
        btnBack = findViewById(R.id.btn_back_to_take_picture)
        btnConfirm = findViewById(R.id.btn_accept_check)

        initRecyclerView()
        initAction()

        try {
            presenter.initData(intent.getStringExtra(IntentString.TEACHING_CLASS) ?: "")
        } catch (e: InvalidExtraData) {
            presenter.handleError(e)
        }
    }

    private fun initRecyclerView() {
        rvFaceDetail = findViewById(R.id.rv_face_detail)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvFaceDetail.layoutManager = layoutManager
    }

    private fun initAction() {
        btnTakePicture.setOnClickListener {
            checkPermission()
        }

        btnCheck.setOnClickListener {
            presenter.check(imageStoragePath)
        }

        btnBack.setOnClickListener {
            presenter.backToTakePicture()
        }

        btnConfirm.setOnClickListener {
            presenter.backToCheckList()
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !Camera.permissionGranted(this)) {
            this.requestPermissions(arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), REQUEST_CODE_PERMISSION)
            return
        }
        takePicture()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != REQUEST_CODE_PERMISSION) {
            return
        }
        if (grantResults.size > 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            takePicture()
            return
        }
        alert("Bạn không cấp quyền chụp ảnh").show()
    }

    private fun takePicture() {
        Camera.deleteMedia(imageStoragePath)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = Camera.outputMediaFile
        if (file == null) {
            alert ("Lỗi không tạo được thư mục").show()
            return
        }

        imageStoragePath = file.absolutePath
        val uri = Camera.getOutputMediaFileUri(this, file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        this.startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_CODE_CAPTURE_IMAGE || resultCode != Activity.RESULT_OK) {
            return
        }

        try {
            Camera.refreshGallery(this, imageStoragePath)
            val bitmap = Camera.optimizeBitmap(App.BITMAP_SAMPLE_SIZE, imageStoragePath)
            imageViewTakenPicture.setImageBitmap(bitmap)
            btnCheck.isEnabled = true
        } catch (e: Exception) {
            btnCheck.isEnabled = false
            alert("Lỗi! Hãy thử chụp lại lần nữa").show()
            Log.e(javaClass.name, e.localizedMessage, e)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                alert("Xác nhận quay lại") {
                    yesButton { finish() }
                    noButton {  }
                }
                true
            }
            else -> {
                true
            }
        }
    }

    override fun showError(e: Error) {
        runOnUiThread {
            toggleTakePicture(true)
            alert(e.toString()) {
                yesButton {
                    if (!presenter.isBusinessError) {
                        exit()
                    }
                }
            }.show()
        }
    }

    override fun exit() {
        finish()
    }

    override fun toggleTakePicture(visible: Boolean) {
        takePictureLayout.visibility = if (visible) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            checkResultLayout.visibility = View.GONE
            progressLayout.visibility = View.GONE
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun toggleProgress(visible: Boolean) {
        progressLayout.visibility = if (visible) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            checkResultLayout.visibility = View.GONE
            takePictureLayout.visibility = View.GONE
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun toggleCheckResult(visible: Boolean) {
        checkResultLayout.visibility = if (visible) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            progressLayout.visibility = View.GONE
            takePictureLayout.visibility = View.GONE
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun confirmBack(hasFaceData: Boolean) {
        if (hasFaceData) {
            alert("Xác nhận quay lại chụp ảnh, dữ liệu hiện tại sẽ mất?") {
                yesButton {
                    presenter.destroyData()
                    toggleTakePicture(true)
                }
                noButton {  }
            }.show()
            return
        }
        toggleTakePicture(true)
    }

    override fun setAdapter(adapter: FaceDetailAdapter) {
        rvFaceDetail.adapter = adapter
    }

    override fun refreshList() {
        runOnUiThread {
            presenter.refreshList()
        }
    }

    override fun backToCheckList(faceCheckResultJson: String) {
        val i = Intent()
        i.putExtra(IntentString.FACE_CHECK_RESULT, faceCheckResultJson)
        setResult(Activity.RESULT_OK, i)
        finish()
    }
}
