package com.ntphat.thesisk40.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.util.Log
import com.ntphat.thesisk40.BuildConfig
import com.ntphat.thesisk40.constant.App
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Camera {
    companion object {
        val outputMediaFile: File?
            get() {
                val mediaStorageDir = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        App.GALLERY_DIRECTORY_NAME
                )
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.e(App.GALLERY_DIRECTORY_NAME, "Cannot create dir!!!")
                        return null
                    }
                }
                val timeStamp = SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.getDefault()).format(Date())
                return File(mediaStorageDir.path + "/IMG_" + timeStamp + ".jpg")
            }

        fun refreshGallery(context: Context, filePath: String) {
            MediaScannerConnection.scanFile(context, arrayOf(filePath), null) { s, uri -> }
        }

        // kiểm tra quyền đọc ghi
        fun permissionGranted(context: Context): Boolean {
            return (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
        }

        // tránh lỗi tràn bộ nhớ khi render hình ảnh
        fun optimizeBitmap(size: Int, filePath: String): Bitmap {
            val options = BitmapFactory.Options()
            options.inSampleSize = size
            return BitmapFactory.decodeFile(filePath, options)
        }

        // kiểm tra máy có camera hay không, nếu đã set = true trong AndroidManifest.xml
        // thì không cần thiết
        fun isDeviceSupportCamera(context: Context): Boolean {
            return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
        }

        fun openSettings(context: Context) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        @SuppressLint("ObsoleteSdkInt")
        fun getOutputMediaFileUri(context: Context, file: File): Uri {
            // Api cũ không hỗ trợ provider path
            return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                Uri.fromFile(file)
            } else {
                FileProvider.getUriForFile(context, context.packageName + ".provider", file)
            }
        }

        fun deleteMedia(path: String) {
            val file = File(path)
            try {
                file.delete()
            } catch (e: IOException) {
                Log.e("CameraUtil", e.localizedMessage, e)
            }
        }

        fun deleteAppImageDir() {
            Storage.deleteRecursive(File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    App.GALLERY_DIRECTORY_NAME
            ))
        }
    }
}