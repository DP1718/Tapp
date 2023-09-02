package com.example.mytestingapp.ui.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mytestingapp.AppDelegate
import com.example.mytestingapp.R
import com.example.mytestingapp.ui.activities.MainActivity.Companion.PERMISSION_REQUEST_ID
import com.example.mytestingapp.util.ConstantUtils.showToastLong
import com.example.mytestingapp.util.JLog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val mPermissionArray = arrayListOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(1000L)
            permissionSetup()
        }

    }

    /** permission setup */
    private fun permissionSetup(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                nextActivityCall()
            }else {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data =
                    Uri.parse(String.format("package:%s", applicationContext.packageName))
                startActivityForResult(intent, PERMISSION_REQUEST_ID)
            }
        }else{
            getRunTimePermissionCall(mPermissionArray)
        }
    }


    /** redirect to next screen */
    private fun nextActivityCall(){
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    /** Dexter user for permission check */
    private lateinit var dexter : Dexter

    /**
     * Runtime permission check
     */
   private fun getRunTimePermissionCall(mArray: ArrayList<String>) {
        dexter = Dexter.withContext(this)
            .withPermissions(mArray)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    report.let {

                        if (report.areAllPermissionsGranted()) {
//                            "Permissions Granted"
                            nextActivityCall()
                        } else {
                            goToSettingsDialog()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest?>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener{
                AppDelegate.instance.showToastLong(it.name)
            } as Dexter
        dexter.check()
    }

    /**
     * Runtime permission result.
     */
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        run {
            dexter.check()
            JLog.logInfo("${JLog.LOG_TAG} >photo_list_=result= >> $result")
        }
    }

    private fun goToSettingsDialog(){
        AlertDialog.Builder(this@SplashActivity, R.style.CustomDialogThemePermission).apply {
            setMessage(getString(R.string.msg_allow_required_permissions))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.button_settings)) { _, _ ->
                    val reqIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .apply {
                            val uri = Uri.fromParts("package", packageName, null)
                            data = uri
                        }
                    resultLauncher.launch(reqIntent)
                }
            val alert = this.create()
            alert.show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PERMISSION_REQUEST_ID){
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    nextActivityCall()
                    JLog.logInfo("${JLog.LOG_TAG} >photo_list_= >> allow permission success")
                } else {
                    AppDelegate.instance.showToastLong(getString(R.string.msg_allow_required_permissions))
                }
            }
        }
    }
}