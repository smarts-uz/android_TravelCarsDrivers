package uz.qwerty.travelcarsdrivers.data.remote.response.course

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class CurrencyResponse(
    @field:SerializedName("data")
    val data: List<CurrencyItem>,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: List<Any?>? = null
)

@Keep
@Parcelize
data class CurrencyItem(
    @SerializedName("Ccy")
    val ccy: String,
    @SerializedName("CcyNm_EN")
    val ccyNmEN: String,
    @SerializedName("CcyNm_RU")
    val ccyNmRU: String,
    @SerializedName("CcyNm_UZ")
    val ccyNmUZ: String,
    @SerializedName("CcyNm_UZC")
    val ccyNmUZC: String,
    @SerializedName("Code")
    val code: String,
    @SerializedName("Date")
    val date: String,
    @SerializedName("Diff")
    val diff: String,
    @SerializedName("Nominal")
    val nominal: String,
    @SerializedName("Rate")
    val rate: String,
    @SerializedName("id")
    val id: Int
) : Parcelable