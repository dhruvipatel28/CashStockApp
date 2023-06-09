package dhruvi.patel.cashstockapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dhruvi.patel.cashstockapp.adapter.StockAdapter
import dhruvi.patel.cashstockapp.api.RetrofitHelper
import dhruvi.patel.cashstockapp.api.StockService
import dhruvi.patel.cashstockapp.databinding.ActivityMainBinding
import dhruvi.patel.cashstockapp.models.Stock
import dhruvi.patel.cashstockapp.repository.Response
import dhruvi.patel.cashstockapp.repository.StockRepository
import dhruvi.patel.cashstockapp.viewmodels.MainViewModel
import dhruvi.patel.cashstockapp.viewmodels.MainViewModelFactory

class MainActivity : ComponentActivity() {

    lateinit var  binding : ActivityMainBinding
    lateinit var stockService : StockService
    lateinit var stockRepository : StockRepository
    private lateinit var mainViewModel : MainViewModel


    var viewState : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpInstance()
    }

    private fun setUpInstance() {
        // Get an Instance of Service of Retrofit
        stockService = RetrofitHelper.getInstance().create(StockService::class.java)
        Log.e("Dhruvi in getStocksData " , " Got Retrofit Instance")

        // Get an Instance of Repository
        stockRepository = StockRepository(stockService)

        //Get a ViewModel Instance from Repository Instance
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(stockRepository))[MainViewModel::class.java]

    }

    private fun getStocksData() {
        //Go get the stocks from API call
        mainViewModel.getAllStocks()

        // If response is success and not null observe the result and perform UI
        mainViewModel.allStocks.observe(this) {
            Log.e("Dhruvi in Observer ", it.toString())

            when (it) {
                is Response.Loading -> {
                    showLoader(true)
                 }

                is Response.Success -> {
                    showLoader(false)
                    Log.e("Dhruvi in Success ", it.data!!.stocks.toString())
                    showStockData(it.data.stocks as ArrayList<Stock>)
                }

                is Response.Error -> {
                    showLoader(false)
                }
            }
        }
    }

    private fun getStocksEmptyState() {
        //Go get the stocks from API call
        mainViewModel.getStocks()

        mainViewModel.stocks.observe(this){
            when(it){
                is Response.Error -> { showLoader(false)}
                is Response.Loading -> {}
                is Response.Success -> {
                    showLoader(false)
                    showStockData(it.data!!.stocks as ArrayList<Stock>)
                }
            }
        }
    }

    fun tryGetStockData(view:View) {
        mainViewModel.getStockDataAgain()

        mainViewModel.falseDataStocks.observe(this){
            when(it){
                is Response.Error -> {
                    showLoader(false)
                    Log.e("Adapter" , "  ---  False Data API Call Return")
                    binding.cardviewNoData.visibility = View.VISIBLE
                    binding.txtNoDataFound.text = this.resources.getString(R.string.malformed_data)
                }
                is Response.Loading -> {}
                is Response.Success -> {
                    showLoader(false)
                    Log.e("Adapter" , "  ---  Malformed Data Success")
                }
            }
        }
    }

    private fun showStockData(stocks: ArrayList<Stock>) {

        if(stocks.size == 0){
            binding.txtNoDataFound.text = this.resources.getString(R.string.no_data)
            binding.cardviewNoData.visibility = View.VISIBLE
            binding.rvStock.visibility = View.GONE
        }else{
            binding.cardviewNoData.visibility = View.GONE
            binding.rvStock.visibility = View.VISIBLE


            val stockAdapter = StockAdapter(stocks)
            binding.rvStock.layoutManager = LinearLayoutManager(this)

            with(binding){
                rvStock.adapter = stockAdapter
            }
        }
    }

    fun showMoreData(view :View) {
        showLoader(true)
        if(viewState){
            getStocksEmptyState()
            viewState = false
            binding.txtSeeData.text = this.resources.getString(R.string.see_more)
        }else{
            getStocksData()
            viewState = true
            binding.txtSeeData.text = this.resources.getString(R.string.see_less)
        }
    }





    fun showLoader(state: Boolean){
        if(state){
            binding.progressbar.visibility = View.VISIBLE
        }else{
            binding.progressbar.visibility = View.GONE
        }
    }
}
