package pl.nw.zadanie_06.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import pl.nw.zadanie_06.Constants
import pl.nw.zadanie_06.Constants.COLLECTION
import pl.nw.zadanie_06.models.Cart
import pl.nw.zadanie_06.models.CartItem
import pl.nw.zadanie_06.models.data.Product
import pl.nw.zadanie_06.repository.RealtimeDatabase

class CartUtils {

    companion object {
        fun addToCart(product: Product, cartId: String = "test") {
            RealtimeDatabase<Cart>()
                .read("${COLLECTION["cart"]}/$cartId/items")
                .get()
                .addOnSuccessListener {
                    var itemPresent = false;
                    val cart = mutableListOf<CartItem>()

                    var item: CartItem? = null;
                    for (cartItemSnapshot in it.children) {
                        item = cartItemSnapshot.getValue<CartItem>()
                        cart.add(item!!)
                        if (item.productId == product.uid) {
                            item.quantity = item.quantity!! + 1;
                            itemPresent = true;
                        }
                    }

                    if (!itemPresent) {
                        cart.add(CartItem(quantity = 1, productId = product.uid))
                    }
                    val updateData = hashMapOf<String, Any>(
                        "${COLLECTION["cart"]}/$cartId/items" to cart
                    )
                    Constants.DB.updateChildren(updateData);

                }.addOnFailureListener{
                    println("add to cart failure") // TODO!
                }

        }
    }
}