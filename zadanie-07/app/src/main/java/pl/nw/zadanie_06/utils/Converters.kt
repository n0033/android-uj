package pl.nw.zadanie_06.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import pl.nw.zadanie_06.models.data.CartItemList

class Converters {
    @TypeConverter
    fun fromJSONToCartItemList(json: String): CartItemList {
        return Gson().fromJson(json, CartItemList::class.java)
    }

    @TypeConverter
    fun fromCartItemListToJSON(cartItems: CartItemList) : String {
        return Gson().toJson(cartItems)
    }
}