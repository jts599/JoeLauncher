package app.joelauncher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels // If using ViewModel
import androidx.lifecycle.ViewModel // Placeholder ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.joelauncher.data.MultiSelectOption
import app.joelauncher.databinding.FragmentMultiSelectBinding
import app.joelauncher.ui.adapters.MultiSelectAdapter

// Define a ViewModel to hold the list and selection state (recommended)
class MultiSelectViewModel : ViewModel() {
    // In a real app, this would come from a repository or be passed in
    val options = mutableListOf<MultiSelectOption>()
    val selectedOptionKeys = mutableSetOf<String>() // To store keys of selected items

    init {
        // Sample Data - Replace with your actual data source
        options.addAll(listOf(
            MultiSelectOption("Option 1", "key1"),
            MultiSelectOption("Option 2", "key2", true), // Example pre-selected
            MultiSelectOption("Option 3", "key3"),
            MultiSelectOption("Option 4", "key4"),
            MultiSelectOption("Option 5", "key5")
        ))
        // Initialize selectedOptionKeys based on pre-selected items
        options.filter { it.selected }.forEach { selectedOptionKeys.add(it.key) }
    }

    fun toggleSelection(option: MultiSelectOption) {
        option.selected = !option.selected
        if (option.selected) {
            selectedOptionKeys.add(option.key)
        } else {
            selectedOptionKeys.remove(option.key)
        }
    }

    // You might also want a LiveData to observe changes if needed elsewhere
    // val optionsLiveData = MutableLiveData<List<MultiSelectOption>>()
    // fun loadOptions() { optionsLiveData.value = options }
}


class MultiSelectFragment : Fragment() {

    private var _binding: FragmentMultiSelectBinding? = null
    private val binding get() = _binding!!

    private lateinit var multiSelectAdapter: MultiSelectAdapter
    private val viewModel: MultiSelectViewModel by viewModels() // Use a ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultiSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadOptions() // Load options into the adapter

        binding.buttonOk.setOnClickListener {
            // Get the selected items
            val selectedItems = viewModel.options.filter { it.selected }
            // Or use viewModel.selectedOptionKeys if you only need the keys

            // TODO: Do something with the selectedItems
            // For example, pass them back to the previous fragment using Navigation Component's
            // `setFragmentResult` or a Shared ViewModel.

            // Example: Log them
            selectedItems.forEach {
                android.util.Log.d("MultiSelectFragment", "Selected: ${it.title} (Key: ${it.key})")
            }

            // Example: Navigate back
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        multiSelectAdapter = MultiSelectAdapter { option ->
            viewModel.toggleSelection(option)
            // Notify the adapter that this specific item has changed to rebind and update checkbox
            val index = viewModel.options.indexOf(option)
            if (index != -1) {
                multiSelectAdapter.notifyItemChanged(index)
            }
        }
        binding.recyclerViewMultiSelect.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = multiSelectAdapter
            // Optional: Add item decorations if needed (like AppDrawerFragment)
        }
    }

    private fun loadOptions() {
        // Submit the list from the ViewModel to the adapter
        multiSelectAdapter.submitList(viewModel.options.toList()) // Submit a new list for DiffUtil
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}