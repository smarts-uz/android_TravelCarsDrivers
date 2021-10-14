package uz.qwerty.travelcarsdrivers.vo

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil

class Booking {
    var id: Int = 0
    var user_name: String? = null
    var user_email: String? = null
    var user_phone: String? = null
    var additional: String? = null
    var price: String? = null
    var date: String? = null
    var date_reverse: String? = null
    var active_date: String? = null
    var reverse: Int? = null
    var duration: String? = null
    var city_from: String? = null
    var city_to: String? = null
    var car: String? = null
    var car_number: String? = null
    var rating: String? = null
    var comment: String? = null
    var car_color: String? = null
    var paid: String? = null
    var status: String? = null

    var book_date: String? = null
    var book_time: String? = null


    companion object DiffCallback : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(@NonNull oldItem: Booking, @NonNull newItem: Booking): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(@NonNull oldItem: Booking, @NonNull newItem: Booking): Boolean {
            return (oldItem.id == newItem.id)
        }
    }
}