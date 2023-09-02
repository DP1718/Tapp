package com.example.mytestingapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytestingapp.AppDelegate
import com.example.mytestingapp.R
import com.example.mytestingapp.data.model.PhotoDataModel
import com.example.mytestingapp.data.repository.DBRepository
import com.example.mytestingapp.data.repository.MainRepository
import com.example.mytestingapp.util.JLog
import com.example.mytestingapp.util.NetworkHelper
import com.example.mytestingapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.EOFException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository,
                                        private val networkHelper: NetworkHelper,
  private val dbRepository: DBRepository) : ViewModel() {



    fun getDataFromServerApiCall(url: String)= flow{
        emit(Resource.loading())
        try {
            if (networkHelper.isNetworkConnected()){
                mainRepository.getDataFromApi(url).let {
                    JLog.logInfo("Activity_apicall ${it.code()}")
                    if(it.code() == 200){
                        emit(Resource.success(it.body()))
                        JLog.logInfo("Activity_apicall= ${it.code()}")
                    }else{
                        emit(Resource.error(it.toString(),it.code(),null))
                    }
                }
            }else
                emit(Resource.error(AppDelegate.instance.getString(R.string.lbl_no_internet_connection),0,null))
        }catch (ex: EOFException){
            JLog.logError("Activity_apicall ${ex.message}")
            ex.printStackTrace()
            emit(Resource.error(AppDelegate.instance.getString(R.string.lbl_no_internet_connection),0,null))
        }
    }


    fun insertDataInDB(mArray: List<PhotoDataModel>) {
        try {
            viewModelScope.launch {
                dbRepository.insertAll(mArray)
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        JLog.logError("_dataInsert ${e.message}")
                    }
                    .collect{
                        JLog.logError("_dataInsert success")
                    }
            }
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

    fun updateDataInDB(mId:String, filePath:String) {
        try {
            viewModelScope.launch {
                dbRepository.updateRecordCall(mId,filePath)
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        JLog.logError("_dataupdate ${e.message}")
                    }
                    .collect{
                        JLog.logError("_dataupdate success")
                    }
            }
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }



    fun getDataInDB() = flow{
        try {
                dbRepository.getRecordFromDB()
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        JLog.logError("photo_list_dataInsert= ${e.message}")
                    }
                    .collect{ mData->
                        emit(mData)
                        JLog.logError("photo_list_dataInsert= success")
                    }
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }


}


