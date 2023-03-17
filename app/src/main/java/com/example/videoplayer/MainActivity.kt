package com.example.videoplayer

import android.Manifest
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.net.Uri.parse
import android.os.Build.VERSION
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.internal.ContextUtils.getActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requesForPermission();
    }

    private fun requesForPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startvideo();
                // You can use the API that requires the permission.
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_VIDEO) -> {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            showInContextUI()
        }
            else -> {

                if (VERSION.SDK_INT >= 33) {

                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf<String>(Manifest.permission.READ_MEDIA_VIDEO),
                        200
                    )

                } else{
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                        200
                    )
                }
            }
        }
    }

    private fun startvideo() {


        val videoView = findViewById<VideoView>(R.id.videoView)
        //Creating MediaController
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        //specify the location of media file
//        val uri: Uri = parse(Environment.getExternalStorageDirectory().getPath() + "/Movies/video.mp4")
        val uri: Uri = parse("https://file-examples.com/storage/fef1706276640fa2f99a5a4/2017/04/file_example_MP4_480_1_5MG.mp4")
//        val uri: Uri = parse("android.resource://"+packageName+"/"+R.raw.video)




        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(uri)
        videoView.requestFocus()
//        videoView.start()


        val progDailog :ProgressDialog = ProgressDialog.show(this, "Processing video", "Retrieving data ...", true)

        videoView.setOnPreparedListener(OnPreparedListener { mp ->

            mp.setOnBufferingUpdateListener { mp, percent ->

                progDailog.setMessage("Please wait Loading "+percent+"%")
                if (percent == 100) {
                    //video have completed buffering
                    progDailog.dismiss()
                    videoView.start()
                }
            }
        })



        //https://file-examples.com/storage/fef1706276640fa2f99a5a4/2017/04/file_example_MP4_480_1_5MG.mp4
        //https://file-examples.com/storage/fef1706276640fa2f99a5a4/2017/04/file_example_MP4_1920_18MG.mp4

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when{
            grantResults.size > 0  -> {
                if (requestCode ==200){
                    startvideo()
                }
            }
        }
    }


    private fun showInContextUI() {
        TODO("Not yet implemented")
        //show rationale here
    }
}