package dhruvi.patel.cashstockapp.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import dhruvi.patel.cashstockapp.databinding.ItemViewBinding
import dhruvi.patel.cashstockapp.models.Stock
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class StockAdapter(private val dataSet: ArrayList<Stock>) :
    RecyclerView.Adapter<StockAdapter.ViewHolder>() {

    lateinit var binding : ItemViewBinding

    class ViewHolder(var view: ItemViewBinding) : RecyclerView.ViewHolder(view.root) {

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemViewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.view.name = dataSet[position]
        viewHolder.view.valCurrentPrice.text = getStockPrice(dataSet[position])
        viewHolder.view.valLastUpdate.text = getStockLastUpdateData(dataSet[position])

        if(dataSet[position].quantity != null){
            viewHolder.view.valQuantity.text = dataSet[position].quantity.toString()
        }else{
            viewHolder.view.lblQuantity.visibility = View.GONE
            viewHolder.view.valQuantity.visibility = View.GONE
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getStockLastUpdateData(stock: Stock): CharSequence? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        return formatter.format(
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(stock.current_price_timestamp.toLong()),
                ZoneId.systemDefault()
            )
        )
    }

    private fun getStockPrice(stock: Stock): CharSequence? {
        val currentPrice = NumberFormat
            .getCurrencyInstance(Locale.US)
            .format(stock.current_price_cents.toDouble() / 100)

        return (stock.currency).uppercase().plus(" : ").plus(currentPrice)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() =  dataSet.size


}
