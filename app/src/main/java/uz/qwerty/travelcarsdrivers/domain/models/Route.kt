package uz.qwerty.travelcarsdrivers.domain.models

import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil


class Route {
    var id: Int = 0
    var date: String? = null
    var price: Int = 0
    var booked: Int = 0
    var booking_id: Int? = null
    var status: Int = 0
    var reverse: Int = 0
    var duration: String? = null
    var city_from: String? = null
    var city_to: String? = null
    var car: String? = null
    var car_number: String? = null


    companion object DiffCallback : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(@NonNull oldItem: Booking, @NonNull newItem: Booking): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(@NonNull oldItem: Booking, @NonNull newItem: Booking): Boolean {
            return (oldItem.id == newItem.id)
        }
    }
}