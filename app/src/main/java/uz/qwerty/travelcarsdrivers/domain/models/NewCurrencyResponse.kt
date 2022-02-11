package uz.qwerty.travelcarsdrivers.domain.models

import com.google.gson.annotations.SerializedName


/**
 * Created by Abdurashidov Shahzod on 12/02/22 00:39.
 * company QQBank
 * shahzod9933@gmail.com
 */
data class NewCurrencyResponse(
    @SerializedName("title") var title: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("cb_price") var cbPrice: String? = null,
    @SerializedName("nbu_buy_price") var nbuBuyPrice: String? = null,
    @SerializedName("nbu_cell_price") var nbuCellPrice: String? = null,
    @SerializedName("date") var date: String? = null
)
