package pl.nw.zadanie_06.models

import java.util.UUID

data class CartItem (
    var quantity: Int,
    var product: Product
)


data class Cart(
    override val uid: String = UUID.randomUUID().toString(),
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