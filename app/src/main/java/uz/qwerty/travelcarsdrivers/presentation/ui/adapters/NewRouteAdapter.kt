package uz.qwerty.travelcarsdrivers.presentation.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_calendar.*
import kotlinx.android.synthetic.main.item_route_list.view.*
import kotlinx.android.synthetic.main.new_item_route_list.view.*
import kotlinx.android.synthetic.main.new_item_route_list.view.bookingCard
import uz.qwerty.travelcarsdrivers.presentation.ui.activity.BookingActivity
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.data.remote.api.TravelCarsApi
import uz.qwerty.travelcarsdrivers.domain.models.Route

class NewRouteAdapter(var onItemClickRv: OnItemClickRv) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val banners: MutableList<Route> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RouteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.new_item_route_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return banners.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RouteViewHolder -> holder.bindModel(banners[position])
        }
    }

    inner class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var id: Int = 0
        var booking_id: Int? = null
        private val cities: TextView = itemView.findViewById(R.id.trip_cities)
        private val car: TextView = itemView.findViewById(R.id.trip_car)
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        private val status: Switch = itemView.findViewById(R.id.status)
        private val trip_booked: TextView = itemView.findViewById(R.id.trip_booked)
        var sk: Boolean = true
        @SuppressLint("SetTextI18n", "CheckResult")
        fun bindModel(banner: Route) {
            id = banner.id
            booking_id = banner.booking_id

            itemView.addCarsList.setOnClickListener {
                if (sk) {
                    sk = false
                    onItemClickRv.clickItem(banner)
                    itemView.addCarsList.setBackgroundResource(R.color.colorOffWhite)
                    itemView.txt_btn.setBackgroundResource(R.color.btn_bg)
                    itemView.icon_true.setBackgroundResource(R.color.btn_bg)

                }else {
                    sk = true
                }
            }

            var citiesText = banner.city_from + " - " + banner.city_to
            if (banner.reverse == 1) {
                citiesText += " - " + banner.city_from
            }
            cities.text = citiesText
            car.text = banner.car + " (" + banner.car_number + ")"
            status.setOnCheckedChangeListener(null)
            status.isChecked = banner.status == 1
            status.visibility = View.VISIBLE
            trip_booked.visibility = View.INVISIBLE


            if (booking_id != null) {
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, BookingActivity::class.java)
                    intent.putExtra("id", booking_id!!)
                    itemView.context.startActivity(intent)
                }
            }

            if (banner.booked == 0) {
                status.setOnCheckedChangeListener { buttonView, isChecked ->

                    val sharedPref =
                        itemView.context.getSharedPreferences("config", Context.MODE_PRIVATE)
                            ?: return@setOnCheckedChangeListener
                    val authKey = sharedPref.getString("auth_key", null)

                    val apiService = TravelCarsApi.createService(true)

                    apiService.routeStatus(id, authKey)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({

                            if (it.isSuccessful && it.code() == 200) {
                                banner.status = if (banner.status == 1) {
                                    0
                                } else {
                                    1
                                }
                                val toast =
                                    Toast.makeText(
                                        itemView.context,
                                        "Статус успешно изменен",
                                        Toast.LENGTH_SHORT
                                    )
                                toast.show()
                            } else {
                                status.isChecked = banner.status == 1
                                val toast =
                                    Toast.makeText(
                                        itemView.context,
                                        "Не удалось соединиться с сервером. Повторите попытку позже",
                                        Toast.LENGTH_SHORT
                                    )
                                toast.show()
                            }
                        }, {
                            status.isChecked = banner.status == 1
                            val toast =
                                Toast.makeText(
                                    itemView.context,
                                    "Не удалось соединиться с сервером. Повторите попытку позже",
                                    Toast.LENGTH_SHORT
                                )
                            toast.show()
                        })
                }
            } else {
                status.visibility = View.INVISIBLE
                trip_booked.visibility = View.VISIBLE
                itemView.bookingCard.setBackgroundResource(R.color.colorAccent2)
            }
        }

        override fun onClick(itemView: View) {
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBanners(data: Route) {
        clear()
        banners.add(data)
        notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        banners.clear()
        notifyDataSetChanged()
    }
}

interface OnItemClickRv {
    fun clickItem(data: Route)
}