package com.example.mytestingapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mytestingapp.AppDelegate
import com.example.mytestingapp.BuildConfig
import com.example.mytestingapp.R
import com.example.mytestingapp.data.model.PhotoDataModel
import com.example.mytestingapp.databinding.ActivityMainBinding
import com.example.mytestingapp.ui.adapter.AuthorPhotosLoadAdapter
import com.example.mytestingapp.ui.vm.MainViewModel
import com.example.mytestingapp.util.ConstantUtils.showToastLong
import com.example.mytestingapp.util.CustomProgressDialog
import com.example.mytestingapp.util.DownloadUtils
import com.example.mytestingapp.util.JLog
import com.example.mytestingapp.util.NetworkHelper
import com.example.mytestingapp.util.SharedPreferenceUtil
import com.example.mytestingapp.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object{
        const val  AUTHOR_PHOTO_ID_KEY = "key.author.photo"
        const val  API_PART_DATA = "&limit=15"
        const val  API_PAGE_PART_DATA = "list?page="
        const val  PAGE_NUMBER_KEY = "PAGENUMBER"
        const val  PERMISSION_REQUEST_ID = 222
        const val  DOWNLOAD_FILE_FOLDER = "/testdata/"
    }

    private val progressDialog by lazy { CustomProgressDialog(this) }
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: AuthorPhotosLoadAdapter
    private var pageCount = 1
    private var mPreviousDataStoreArray = ArrayList<PhotoDataModel>()
    private var myHandler: Handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        pageCount = SharedPreferenceUtil(AppDelegate.instance).getValueInt(PAGE_NUMBER_KEY,1)

        initSetup()
    }

    /** init setup */
    private fun initSetup(){
        binding.includeHeader.imgBack.visibility = View.GONE
        binding.includeHeader.tvTitleCommonHeader.text = getString(R.string.lbl_photo)

        adapter = AuthorPhotosLoadAdapter( this@MainActivity)
        val layoutManagerGrid = GridLayoutManager(this, 3)
        binding.recyclerViewList.layoutManager = layoutManagerGrid
        binding.recyclerViewList.adapter = adapter

        getDataFromDb()
        clickEventListenerCall()
    }

    private var myRunnable: Runnable = Runnable(){
        binding.swipeRefreshTest.isRefreshing = false
    }

    override fun onStop() {
        super.onStop()
        try {
            if(myHandler != null)
                if(myRunnable != null)
                    myHandler.removeCallbacks(myRunnable)

        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    /**  click event listener call function */
    private fun clickEventListenerCall(){
        binding.swipeRefreshTest.setOnRefreshListener {

                getDataFromServer(pageCount)

              myHandler.postDelayed(myRunnable,2000L)
        }

        binding.fabSyncButton.setOnClickListener {
            getDataFromServer(pageCount)
        }
    }

    /** get data from server - api call function */
    private fun getDataFromServer(pageNumber:Int){
      if(NetworkHelper(AppDelegate.instance).isNetworkConnected()) {
        lifecycleScope.launch {
            delay(500L)
            val url = BuildConfig.BASE_URL_DEV + API_PAGE_PART_DATA + pageNumber + API_PART_DATA
            viewModel.getDataFromServerApiCall(url)
                .collect{ response ->
                    when(response.status){
                        Status.SUCCESS ->{
                            progressDialog.stop()
                            // data display in UI part
                            JLog.logInfo("${JLog.LOG_TAG} >photo list >> ${response.data}")
                            dataSetInViewCall(response.data)
                            pageCount++
                            SharedPreferenceUtil(AppDelegate.instance).save(PAGE_NUMBER_KEY,pageCount)
                        }
                        Status.LOADING -> {
                            progressDialog.start(getString(R.string.lbl_please_wait), true)
                        }
                        Status.ERROR ->{
                            progressDialog.stop()
                            JLog.logInfo("${JLog.LOG_TAG} error >>> ")
                        }

                    }
                }
            }
        }else{
          AppDelegate.instance.showToastLong(getString(R.string.lbl_no_internet_connection))
       }
    }

    private fun dataSetInViewCall(data: List<PhotoDataModel>?) {
          if(data != null && data.isNotEmpty()){
              binding.tvNoRecordsFound.visibility= View.GONE
              binding.recyclerViewList.visibility= View.VISIBLE
              val mTempArray = ArrayList<PhotoDataModel>()
              dataInsertIntoDB(data)
              data as ArrayList
              if(mPreviousDataStoreArray != null && mPreviousDataStoreArray.size > 0) {
                  mTempArray.addAll(mPreviousDataStoreArray)
                  mPreviousDataStoreArray.clear()
              }
              mTempArray.addAll(data)
              adapter.updatedDataSet(mTempArray)
              mPreviousDataStoreArray = mTempArray
              downloadData(data)
          }else{
              binding.recyclerViewList.visibility= View.GONE
              binding.tvNoRecordsFound.visibility= View.VISIBLE
          }
    }

    /** insert data in db function */
    private fun dataInsertIntoDB(data: List<PhotoDataModel>) {
        viewModel.insertDataInDB(data)
    }

    /** download data function */
    private fun downloadData(data: ArrayList<PhotoDataModel>) {
        try {
            val newPath = getExternalFilesDir("")?.path+ DOWNLOAD_FILE_FOLDER
            if(!File(newPath).isDirectory)
                File(newPath).mkdir()
            for (m in data) {
                DownloadUtils().downloadImage(this,m.downloadUrl!!,"${m.id!!}.jpg",newPath)
                viewModel.updateDataInDB(m.id!!,newPath+"${m.id!!}.jpg")
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    /** get data from db function */
    private fun getDataFromDb(){
        try {
            lifecycleScope.launch {
                viewModel.getDataInDB().collect{ response->
                    dataSetInViewCall1(response)
                }
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    /** data fetch from db set in view function */
    private fun dataSetInViewCall1(data: List<PhotoDataModel>) {
        if(data != null && data.isNotEmpty()){
            binding.tvNoRecordsFound.visibility= View.GONE
            binding.recyclerViewList.visibility= View.VISIBLE
            data as ArrayList
            adapter.updatedDataSet(data)
            mPreviousDataStoreArray = data
        }else{
            binding.recyclerViewList.visibility= View.GONE
            binding.tvNoRecordsFound.visibility= View.VISIBLE
        }
    }


}