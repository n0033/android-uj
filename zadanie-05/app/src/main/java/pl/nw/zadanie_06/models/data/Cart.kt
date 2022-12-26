package pl.nw.zadanie_06.models

import pl.nw.zadanie_06.models.data.EntityBase
import pl.nw.zadanie_06.models.data.Product
import java.util.UUID

data class CartItem (
    var quantity: Int? = null,
    var productId: String? = null
)


data class Cart(
    override val uid: String = UUID.randomUUID().toString(),
// TODO:    val userId: String
    var items: ArrayList<CartItem>? = null
) : EntityBase(), Serializable<Cart>{
    override fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "uid" to uid,
            "items" to items
        )
    }
    companion object : Deserializable<Cart> {
        override fun fromHashMap(map: HashMap<String, Any>): Cart {
            return Cart(
                map["uid"] as String,
                map["items"] as ArrayList<CartItem>
            )
        }
    }


}