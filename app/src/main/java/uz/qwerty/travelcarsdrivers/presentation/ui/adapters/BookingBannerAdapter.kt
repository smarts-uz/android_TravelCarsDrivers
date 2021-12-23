package uz.qwerty.travelcarsdrivers.presentation.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import uz.qwerty.travelcarsdrivers.presentation.ui.activity.BookingActivity
import uz.qwerty.travelcarsdrivers.domain.models.Booking
import uz.qwerty.travelcarsdrivers.R

class BookingBannerAdapter : PagerAdapter() {

    private val banners: MutableList<Booking> = mutableListOf()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return banners.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val holder = BannerViewHolder(LayoutInflater.from(container.context).inflate(R.layout.item_banner_layout, container, false), container)
        holder.bindModel(banners[position])
        container.addView(holder.itemView)
        return holder.itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    }

    inner class BannerViewHolder(itemView: View, parent: ViewGroup) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var id: Int = 0
        private val cities: TextView = itemView.findViewById(R.id.new_trip_cities)
        private val date: TextView = itemView.findViewById(R.id.new_trip_date)
        private val car_name: TextView = itemView.findViewById(R.id.car_name)
        private val date_reverse: TextView = itemView.findViewById(R.id.new_trip_date_reverse)
        private val new_trip_end: TextView = itemView.findViewById(R.id.new_trip_end)

        fun bindModel(banner: Booking) {
            id = banner.id
            itemView.setOnClickListener(this)

            date_reverse.visibility = View.INVISIBLE
            new_trip_end.visibility = View.INVISIBLE

            var citiesText = banner.city_from + " - " + banner.city_to
            if (banner.reverse == 1) {
                citiesText += " - " + banner.city_from

                date_reverse.text = banner.date_reverse

                date_reverse.visibility = View.VISIBLE
                new_trip_end.visibility = View.VISIBLE
            }
            cities.text = citiesText
            car_name.text = banner.car + " (" + banner.car_number + ")"
            date.text = banner.date
        }

        override fun onClick(itemView: View) {
            val intent = Intent(itemView.context, BookingActivity::class.java)
            intent.putExtra("id", id)
            itemView.context.startActivity(intent)
        }
    }

    fun setBanners(data: List<Booking>) {
        clear()
        banners.addAll(data)
        notifyDataSetChanged()
    }

    fun clear() {
        banners.clear()
        notifyDataSetChanged()
    }
}