package dhruvi.patel.cashstockapp.repository

import dhruvi.patel.cashstockapp.models.Stocks


//Complete Generic Response Class
sealed class  Response<T>(
    val data: T? = null ,
    val errorMessage: String? = null,
){
    class Loading<T> : Response<T>()
    class Success<T>(data : T?) : Response<T>(data = data)
    class Error<T>(errorMessage: String) : Response<T>(errorMessage =  errorMessage)
}



/*

// For Few Responses  **** Version 1 ****
sealed class Response2(){
    class Loading : Response2()
    class Success(val stocks: Stocks) : Response2()
    class Error (val errorMessage : String): Response2()
}


// More Generic class   **** Version 2 ****
sealed class  Response3(
    val data: Stocks? = null ,
    val errorMessage: String? = null
){
    class Loading : Response3()
    class Success(stocks : Stocks) : Response3(data = stocks)
    class Error(errorMessage: String) : Response3(errorMessage =  errorMessage)
}*/
