package pl.nw.zadanie_06.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pl.nw.zadanie_06.R
import pl.nw.zadanie_06.databinding.ProductDetailsBinding
import pl.nw.zadanie_06.models.view.ProductDetailsViewModel
import pl.nw.zadanie_06.utils.CartUtils


class ProductDetailsFragment : Fragment(R.layout.product_details) {

    private var _binding: ProductDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProductDetailsViewModel;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductDetailsBinding.inflate(inflater, container, false)
        val productId = arguments?.getString("productId")
            ?: throw Exception("ProductId missing in fragment bundle.")
        val categoryId = arguments?.getString("categoryId")
            ?: throw Exception("ProductId missing in fragment bundle.")
        viewModel = ProductDetailsViewModel(productId, categoryId)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleScope.launch {
                viewModel.uiState.collect { value ->
                    binding.productDetailsName.text = value.product?.name
                    binding.productDetailsPrice.text = "${value.product?.price.toString()} $";
                    binding.productDetailsDescription.text = value.product?.description;
                    binding.productDetailsCategory.text = value.category?.name;
                }
            }
        }

        binding.productDetailsAddToCart.setOnClickListener{
            lifecycleScope.launch {
                viewModel.uiState.collect { value ->
                    do {
                        if (value.product != null) {
                            CartUtils.addToCart(value.product!!)
                        }
                    } while (value.product == null)
                }
            };
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}