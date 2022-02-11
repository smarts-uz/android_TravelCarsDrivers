package uz.qwerty.travelcarsdrivers.presentation.ui.adapters

import android.view.View
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.databinding.ItemNewCurrencyBinding
import uz.qwerty.travelcarsdrivers.domain.models.NewCurrencyResponse
import uz.qwerty.travelcarsdrivers.presentation.ui.base.SuperAdapter


/**
 * Created by Abdurashidov Shahzod on 01/01/22 17:42.
 * company
 * shahzod9933@gmail.com
 */
class CourseAdapter : SuperAdapter<NewCurrencyResponse>(
    R.layout.item_new_currency,
    { oldItem, newItem -> oldItem == newItem },
    { oldItem, newItem -> oldItem == newItem },
) {

//    override fun bind(t: CurrencyItem, view: View, adapterPosition: Int) {
//        val binding = ItemCourseBinding.bind(view)
//        binding.tv1.text = t.ccyNmRU
//        binding.tv2.text = t.rate
//        binding.tv4.text = t.date
//
//    }

    override fun bind(t: NewCurrencyResponse, view: View, adapterPosition: Int) {
        val binding = ItemNewCurrencyBinding.bind(view)
        binding.currency.text = t.title
        binding.buy.text = t.nbuBuyPrice
        binding.weSell.text = t.nbuCellPrice
        binding.cb.text = t.cbPrice
    }


}