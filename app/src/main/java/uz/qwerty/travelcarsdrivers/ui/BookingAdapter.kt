package uz.qwerty.travelcarsdrivers.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.qwerty.travelcarsdrivers.R
import uz.qwerty.travelcarsdrivers.repository.NetworkState
import uz.qwerty.travelcarsdrivers.repository.Status
import uz.qwerty.travelcarsdrivers.util.ListItemClickListener
import uz.qwerty.travelcarsdrivers.vo.Booking


class BookingAdapter(private val itemClickListener: ListItemClickListener) :
    PagedListAdapter<Booking, RecyclerView.ViewHolder>(Booking.DiffCallback) {

    private var networkState: NetworkState? = null
    var review = false

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = networkState
        val previousExtraRow = hasExtraRow()
        networkState = newNetworkState
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return when (viewType) {
            R.layout.item_booking_list -> {
                view = layoutInflater.inflate(R.layout.item_booking_list, parent, false)
                BookingItemViewHolder(view, parent, itemClickListener)
            }
            R.layout.item_review_list -> {
                view = layoutInflater.inflate(R.layout.item_review_list, parent, false)
                ReviewItemViewHolder(view, parent, itemClickListener)
            }
            R.layout.item_network_state -> {
                view = layoutInflater.inflate(R.layout.item_network_state, parent, false)
                NetworkStateItemViewHolder(view, itemClickListener)
            }
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_booking_list -> (holder as BookingItemViewHolder).bindModel(getItem(position))
            R.layout.item_review_list -> (holder as ReviewItemViewHolder).bindModel(getItem(position))
            R.layout.item_network_state -> (holder as NetworkStateItemViewHolder).bindView(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return (networkState != null && networkState == NetworkState.LOADED)
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else if(review) {
            R.layout.item_review_list
        } else {
            R.layout.item_booking_list
        }
    }

    internal class BookingItemViewHolder(itemView: View, parent: ViewGroup, listItemClickListener: ListItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var id: Int = 0
        private val cities: TextView = itemView.findViewById(R.id.trip_cities)
        private val date: TextView = itemView.findViewById(R.id.trip_date)
        private val car_name: TextView = itemView.findViewById(R.id.trip_car)
        private val date_reverse: TextView = itemView.findViewById(R.id.trip_date_reverse)
        private val new_trip_end: TextView = itemView.findViewById(R.id.trip_end)
        private val id_view: TextView = itemView.findViewById(R.id.trip_id)
        private val user_name: TextView = itemView.findViewById(R.id.user_name)

        private val bookingCard: CardView = itemView.findViewById(R.id.bookingCard)

        init {
            bookingCard.setOnClickListener { view -> listItemClickListener.onItemClick(id) }
        }

        fun bindModel(banner: Booking?) {
            id = banner!!.id
            date_reverse.visibility = View.GONE
            new_trip_end.visibility = View.GONE

            id_view.text = id.toString()
            user_name.text = banner.user_name
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
    }

    internal class ReviewItemViewHolder(itemView: View, parent: ViewGroup, listItemClickListener: ListItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var id: Int = 0
        private val cities: TextView = itemView.findViewById(R.id.trip_cities)
        private val car_name: TextView = itemView.findViewById(R.id.trip_car)
        private val id_view: TextView = itemView.findViewById(R.id.trip_id)

        private val bookingCard: CardView = itemView.findViewById(R.id.bookingCard)

        init {
            bookingCard.setOnClickListener { view -> listItemClickListener.onItemClick(id) }
        }

        fun bindModel(banner: Booking?) {
            id = banner!!.id
            id_view.text = banner.rating
            cities.text = banner.user_name
            car_name.text = banner.comment
        }
    }

    internal class NetworkStateItemViewHolder(itemView: View, listItemClickListener: ListItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        private val errorMsg: TextView = itemView.findViewById(R.id.error_msg)
        private val button: Button = itemView.findViewById(R.id.retry_button)

        init {
            button.setOnClickListener { view -> listItemClickListener.onRetryClick(view, adapterPosition) }
        }

        fun bindView(networkState: NetworkState?) {
            if (networkState != null && networkState.sts === Status.RUNNING) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }

            if (networkState != null && networkState.sts === Status.FAILED) {
                errorMsg.visibility = View.VISIBLE
                errorMsg.text = networkState.mes
            } else if (networkState != null && networkState.sts === Status.EMPTY) {
                errorMsg.visibility = View.VISIBLE
                errorMsg.text = networkState.mes
            }
            else {
                errorMsg.visibility = View.GONE
            }
        }
    }
}