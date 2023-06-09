package dhruvi.patel.cashstockapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dhruvi.patel.cashstockapp.utility.MyApplication
import dhruvi.patel.cashstockapp.utility.NetworkUtil
import dhruvi.patel.cashstockapp.api.StockService
import dhruvi.patel.cashstockapp.models.Stocks
import org.json.JSONObject
import org.json.JSONTokener

class StockRepository(private val stockService: StockService ) {

    private val stockLiveData = MutableLiveData<Response<Stocks>>()
    private val allStockLiveData = MutableLiveData<Response<Stocks>>()
    private val falseStockLiveData = MutableLiveData<Response<Stocks>>()

    val stocksData : LiveData<Response<Stocks>>
        get() = stockLiveData

    val allStockData : LiveData<Response<Stocks>>
        get() = allStockLiveData

    val falseStockData : LiveData<Response<Stocks>>
        get() = falseStockLiveData



    suspend fun getEmptyStockData(){
        stockLiveData.postValue(Response.Loading())
        Log.e("Dhruvi Repo" , " Initiate Loading")

        if(NetworkUtil.isNetworkAvailable(MyApplication.applicationContext())){
            val result = stockService.getEmptyStocks()
            if(result.body() != null){
                Log.e("Dhruvi Repo" , " Result is not null")
                //stockLiveData.postValue(result.body())
                stockLiveData.postValue(Response.Success(result.body()))
            }else{
                stockLiveData.postValue(Response.Error("Error Fetching Data from API"))
            }
        }else{
            stockLiveData.postValue(Response.Error("Issue In Network Connection"))
        }
    }

    suspend fun getAllStocks(){
        allStockLiveData.postValue(Response.Loading())
        Log.e("Dhruvi Repo" , " Initiate Loading")

        if(NetworkUtil.isNetworkAvailable(MyApplication.applicationContext())){
            val result = stockService.getAllStocks()
            if(result.body() != null){
                Log.e("Dhruvi Repo" , " Result is not null")
                //stockLiveData.postValue(result.body())
                allStockLiveData.postValue(Response.Success(result.body()))
            }else{
                allStockLiveData.postValue(Response.Error("Error Fetching Data from API"))
            }
        }else{
            allStockLiveData.postValue(Response.Error("Issue In Network Connection"))
        }
    }

    suspend fun getMalformedStockData(){
        falseStockLiveData.postValue(Response.Loading())

        if(NetworkUtil.isNetworkAvailable(MyApplication.applicationContext())){
            val result = stockService.getMulStockData()

            val jsonObj = JSONTokener(result.toString()).nextValue()
            when(jsonObj){
                is JSONObject -> {
                }
                else -> {
                    Log.e("Dhruvi Repo" , " JSON Object Format issue " )
                    falseStockLiveData.postValue(Response.Error("Response is malformed"))
                }
            }
        }else{
            falseStockLiveData.postValue(Response.Error("Issue In Network Connection"))
        }
    }

}