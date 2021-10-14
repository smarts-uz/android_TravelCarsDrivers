package uz.qwerty.travelcarsdrivers.api.response

import uz.qwerty.travelcarsdrivers.vo.Booking
import uz.qwerty.travelcarsdrivers.vo.Meta

class BookingHystoryResponse {
    lateinit var data: List<Booking>
    lateinit var meta: Meta
}