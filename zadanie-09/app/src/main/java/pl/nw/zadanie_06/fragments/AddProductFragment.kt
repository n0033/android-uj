package pl.nw.zadanie_06.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.nw.zadanie_06.Constants.COLLECTION
import pl.nw.zadanie_06.R
import pl.nw.zadanie_06.common.realtime_db.RealtimeDatabase
import pl.nw.zadanie_06.databinding.AddProductBinding
import pl.nw.zadanie_06.models.data.Category
import pl.nw.zadanie_06.models.data.Product
import pl.nw.zadanie_06.models.view.CategoryListViewModel
import pl.nw.zadanie_06.utils.UserUtils

class AddProductFragment : Fragment(R.layout.add_product) {

    private var _binding: AddProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = AddProductBinding.inflate(layoutInflater)
        UserUtils.ensureAuth(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryNames = arrayListOf<String>()
        val categoryMap = mutableMapOf<String, Category>()
        val spinner: Spinner = view.findViewById(R.id.category_spinner)
        val adapter =
            ArrayAdapter(requireContext(), R.layout.category_dropdown_item, arrayListOf<String>())
        adapter.setDropDownViewResource(R.layout.category_dropdown_item)

        lifecycleScope.launch {
            viewModel.uiState.collect { value ->
                value.categoryList.forEach { categoryNames.add(it.name!!) }
                value.categoryList.forEach { categoryMap[it.name!!] = it }
                adapter.addAll(categoryNames)
            }
        }

        spinner.adapter = adapter


        binding.addProductAddButton.setOnClickListener {
            val name: String = binding.addProductNameInput.text.toString()
            val description = binding.addProductDescriptionInput.text.toString()
            val price = binding.addProductPriceInput.text.toString()
            val categoryName = binding.categorySpinner.selectedItem.toString()
            if (arrayOf(name, description, price).contains("")) {
                return@setOnClickListener
            }
            val category = categoryMap[categoryName]
            val product = Product(
                name = name,
                description = description,
                price = price.toDouble(),
                categoryId = category!!.uid
            )

            RealtimeDatabase<Product>().create(COLLECTION["product"] + "/" + product.uid, product)


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
