package pl.nw.zadanie_06.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.nw.zadanie_06.R
import pl.nw.zadanie_06.models.CartItem
import pl.nw.zadanie_06.models.data.Product


class CartAdapter(
    private val removeItemListener: (Product) -> Unit,
    private val addItemListener: (Product) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private var cartItems: List<CartItem> = listOf()
    private var products: List<Product> = listOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView
        val itemQuantity: TextView
        val removeItemSymbol: TextView
        val addItemSymbol: TextView

        init {
            itemName = view.findViewById(R.id.cart_item_name)
            itemQuantity = view.findViewById(R.id.cart_item_quantity)
            removeItemSymbol = view.findViewById(R.id.cart_item_remove)
            addItemSymbol = view.findViewById(R.id.cart_item_add)

        }

    }


    fun setData(products: List<Product>, cartItems: List<CartItem>) {
        this.cartItems = cartItems;
        this.products = products;
        notifyDataSetChanged();
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cart_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val product = products.filter { it.uid == cartItem.productId }[0]
        holder.itemName.text = product.name
        holder.itemQuantity.text = cartItem.quantity.toString();
        holder.removeItemSymbol.setOnClickListener { removeItemListener(product) }
        holder.addItemSymbol.setOnClickListener { addItemListener(product) }
    }

    override fun getItemCount() = cartItems.size

}
