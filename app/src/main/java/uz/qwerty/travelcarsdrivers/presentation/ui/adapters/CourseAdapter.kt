package uz.qwerty.travelcarsdrivers.presentation.ui.adapters

import android.view.View
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CurrencyItem
import uz.qwerty.travelcarsdrivers.data.remote.response.course.CurrencyResponseItem
import uz.qwerty.travelcarsdrivers.databinding.ItemCourseBinding
import uz.qwerty.travelcarsdrivers.presentation.ui.base.SuperAdapter
import uz.qwerty.travelcarsdrivers.presentation.ui.base.SuperListAdapter


/**
 * Created by Abdurashidov Shahzod on 01/01/22 17:42.
 * company
 * shahzod9933@gmail.com
 */
class CourseAdapter : SuperAdapter<CurrencyItem>(
    R.layout.item_course,
    { oldItem, newItem -> oldItem == newItem },
    { oldItem, newItem -> oldItem == newItem },
) {

    override fun bind(t: CurrencyItem, view: View, adapterPosition: Int) {
        val binding = ItemCourseBinding.bind(view)
        binding.currencyTypeTextview.text = t.ccy
        binding.ccyNameEng.text = t.ccyNmEN
        binding.currencySellTextview.text = t.rate
        binding.dateCurrency.text = t.date
    }


}