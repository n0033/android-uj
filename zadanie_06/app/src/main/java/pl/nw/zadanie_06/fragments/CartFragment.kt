package pl.nw.zadanie_06.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import pl.nw.zadanie_06.R
import pl.nw.zadanie_06.adapters.CartAdapter
import pl.nw.zadanie_06.databinding.CartBinding
import pl.nw.zadanie_06.models.view.CartViewModel
import pl.nw.zadanie_06.utils.CartUtils

class CartFragment : Fragment(R.layout.cart) {

    private var _binding: CartBinding? = null;
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    private val adapter: CartAdapter = CartAdapter(CartUtils::removeFromCart, CartUtils::addToCart)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CartBinding.inflate(layoutInflater)
        binding.cartitemList.layoutManager = LinearLayoutManager(requireContext())
        binding.cartitemList.adapter = this.adapter;
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleScope.launch {
                viewModel.uiState.collect { value ->
                    adapter.setData(value.products, value.items)
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }
}
