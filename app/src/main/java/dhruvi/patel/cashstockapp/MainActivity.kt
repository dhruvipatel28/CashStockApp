package dhruvi.patel.cashstockapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.activity.ComponentActivity
import androidx.core.view.children
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

    private lateinit var  binding : ActivityMainBinding
    private lateinit var stockService : StockService
    private lateinit var stockRepository : StockRepository
    private lateinit var mainViewModel : MainViewModel

    private var allDataViewState : Boolean = false
    private lateinit var stockAdapter : StockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpInstance()
        setSwipeRefreshAction()

        binding.searchView.visibility = View.GONE
        binding.searchView.isIconifiedByDefault = false
        binding.searchView.isIconified = true
        binding.searchView.isSubmitButtonEnabled = false
        binding.searchView.queryHint = "Search Here..."

    }

    private fun filterDatList(stocks: ArrayList<Stock>) {
        // ** -----
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                return false
            }
            override fun onQueryTextChange(searchText: String): Boolean {

                if(searchText.isNullOrEmpty()){
                    Log.e("Main Activity", " text is empty")

                    binding.cardviewNoData.visibility = View.GONE
                    binding.rvStock.visibility = View.VISIBLE
                    hideKeyboard()
                    stockAdapter.showFilteredData(stocks)
                }else{
                    //Log.e("Main Activity" , " You are Searching for: $searchText")
                    val filerList : ArrayList<Stock> = ArrayList()

                    stockAdapter.showFilteredData(filerList)

                    for(item in stocks){
                        if(item.ticker.lowercase().equals(searchText,true)){
                            filerList.add(item)
                        }
                    }

                    if(filerList.isNotEmpty()){
                        binding.cardviewNoData.visibility = View.GONE
                        binding.rvStock.visibility = View.VISIBLE
                        stockAdapter.showFilteredData(filerList)
                    }else{
                        binding.cardviewNoData.visibility = View.VISIBLE
                        binding.rvStock.visibility = View.GONE
                        binding.txtNoDataFound.text = getString(R.string.no_data)
                        filerList.clear()
                        stockAdapter.showFilteredData(stocks)
                    }
                }

                return false
            }
        })


    }


    /*    -- --     Data Block         --   */

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
            //Stop refreshing the swiper when response received
            if(binding.lySwipeRefresh.isRefreshing) {
                Log.e("Dhruvi in Observer "," Refresh called to stop")

                binding.lySwipeRefresh.isRefreshing = false
            }

            when (it) {
                is Response.Loading -> {
                    showLoader(true)
                 }

                is Response.Success -> {
                    showLoader(false)
                    Log.e("Dhruvi in Success ", "DATA SIZE : " + it.data!!.stocks.size.toString())
                    showStockData(it.data.stocks.shuffled() as ArrayList<Stock>)
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

    /*    -- --  Handle   UI Block         --   */
    private fun showStockData(stocks: ArrayList<Stock>) {

        if(stocks.size == 0){
            binding.txtNoDataFound.text = this.resources.getString(R.string.no_data)
            binding.cardviewNoData.visibility = View.VISIBLE
            binding.rvStock.visibility = View.GONE
        }else{
            binding.cardviewNoData.visibility = View.GONE
            binding.rvStock.visibility = View.VISIBLE

            stockAdapter = StockAdapter(stocks)
            binding.rvStock.layoutManager = LinearLayoutManager(this)

            filterDatList(stocks)

            with(binding){
                rvStock.adapter = stockAdapter
            }
        }
    }

    fun showMoreData(view :View) {
        showLoader(true)

        if(allDataViewState){
            binding.lySwipeRefresh.isEnabled = false
            binding.lySwipeRefresh.isRefreshing = false
            binding.txtSeeData.text = this.resources.getString(R.string.see_more)
            getStocksEmptyState()
            allDataViewState = false
            binding.searchView.visibility = View.GONE
            binding.cardviewNoData.visibility = View.VISIBLE
            binding.txtMalformedData.visibility = View.VISIBLE
        }else{
            getStocksData()
            binding.txtSeeData.text = this.resources.getString(R.string.see_less)
            binding.lySwipeRefresh.isEnabled = true
            allDataViewState = true
            binding.searchView.visibility = View.VISIBLE
            binding.cardviewNoData.visibility = View.GONE
            binding.txtMalformedData.visibility = View.GONE
        }
    }

    private fun showLoader(state: Boolean){
        if(state){
            binding.progressbar.visibility = View.VISIBLE
        }else{
            binding.progressbar.visibility = View.GONE
        }
    }

    private fun setSwipeRefreshAction() {
        binding.lySwipeRefresh.setOnRefreshListener{
            if(allDataViewState){
                binding.lySwipeRefresh.isRefreshing = true
                getStocksData()
            }else{
                binding.lySwipeRefresh.isRefreshing = false
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
    }

}
