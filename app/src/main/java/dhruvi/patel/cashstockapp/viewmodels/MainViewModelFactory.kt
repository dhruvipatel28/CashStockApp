package dhruvi.patel.cashstockapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dhruvi.patel.cashstockapp.repository.StockRepository

class MainViewModelFactory(private val repository: StockRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}