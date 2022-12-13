package pl.nw.zadanie_06.models.view

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import pl.nw.zadanie_06.Constants.COLLECTION
import pl.nw.zadanie_06.models.Cart
import pl.nw.zadanie_06.models.CartItem
import pl.nw.zadanie_06.models.data.Product
import pl.nw.zadanie_06.repository.RealtimeDatabase

data class CartUiState(
    var items: List<CartItem> = listOf(),
    var products: List<Product> = listOf()
)

// TODO: CartViewModel(userId: String)
class CartViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState

    init {
        val cartReference = RealtimeDatabase<Cart>().read("${COLLECTION["cart"]!!}/test")
        val cartListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cart = dataSnapshot.getValue<Cart>()!!
                _uiState.update { currentState ->
                    currentState.copy(items = cart.items as List<CartItem>)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", error.toException())
            }
        }

        cartReference.addValueEventListener(cartListener);

        val productReference = RealtimeDatabase<Product>().read(COLLECTION["product"]!!)
        val productListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cartProducts = mutableListOf<Product>();
                val cartItems = _uiState.value.items.map{it.productId}
                var tempProduct: Product?
                for (productSnapshot in dataSnapshot.children) {
                    tempProduct = productSnapshot.getValue<Product>()
                    if (tempProduct == null) continue;
                    cartProducts.add(tempProduct)
               }
                _uiState.update{currentState -> currentState.copy(products = cartProducts)}
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", error.toException())
            }
        }

        productReference.addValueEventListener(productListener);

    }

}