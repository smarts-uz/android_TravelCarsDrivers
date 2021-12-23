package uz.qwerty.travelcarsdrivers.presentation.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.qwerty.travelcarsdrivers.presentation.ui.activity.BookingActivity
import uz.qwerty.travelcarsdrivers.domain.models.Booking
import uz.qwerty.travelcarsdrivers.R

class BookingActiveAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val banners: MutableList<Booking> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ActiveTripViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_actives_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return banners.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ActiveTripViewHolder -> holder.bindModel(banners[position])
        }
    }

    inner class ActiveTripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var id: Int = 0
        private val city_from: TextView = itemView.findViewById(R.id.city_from)
        private val city_to: TextView = itemView.findViewById(R.id.city_to)
        private val city_back: TextView = itemView.findViewById(R.id.city_back)
        private val date: TextView = itemView.findViewById(R.id.date)

        fun bindModel(banner: Booking) {
            id = banner.id

            city_back.visibility = View.INVISIBLE

            itemView.setOnClickListener(this)
            city_from.text = banner.city_from
            city_to.text = banner.city_to
            if (banner.reverse == 1) {
                city_back.visibility = View.VISIBLE
                city_back.text = banner.city_from
            }
            date.text = banner.active_date
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