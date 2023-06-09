package dhruvi.patel.cashstockapp.api

import dhruvi.patel.cashstockapp.models.Stocks
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface StockService {

    //@GET("portfolio.json")
    //@GET("portfolios.json")
    @GET("portfolio_empty.json")
    suspend fun getEmptyStocks( ) : Response<Stocks>

    @GET("portfolio.json")
    suspend fun getAllStocks( ) : Response<Stocks>

    @GET("portfolio_malformed.json")
    suspend fun getMulStockData( )  : ResponseBody

}