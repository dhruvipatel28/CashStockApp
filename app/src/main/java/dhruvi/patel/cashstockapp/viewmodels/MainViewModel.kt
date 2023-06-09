package dhruvi.patel.cashstockapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dhruvi.patel.cashstockapp.models.Stocks
import dhruvi.patel.cashstockapp.repository.Response
import dhruvi.patel.cashstockapp.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StockRepository) : ViewModel() {

    fun getStocks(){
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("Main View Model","GO get the stocks from Repository Empty")
            repository.getEmptyStockData()
        }
    }

    fun getAllStocks(){
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("Main View Model","GO get the stocks from Repository ALl")
            repository.getAllStocks()
        }
    }

    fun getStockDataAgain(){
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("Main View Model","GO get the false stocks Data")
            repository.getMalformedStockData()
        }
    }


    val stocks : LiveData<Response<Stocks>>
        get() = repository.stocksData

    val allStocks : LiveData<Response<Stocks>>
        get() = repository.allStockData

    val falseDataStocks : LiveData<Response<Stocks>>
        get() = repository.falseStockData
}