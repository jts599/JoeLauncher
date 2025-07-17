package app.joelauncher.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.joelauncher.R // Import your R class
import app.joelauncher.data.MultiSelectOption
import app.joelauncher.databinding.ItemMultiSelectOptionBinding

class MultiSelectAdapter(
    private val onItemClicked: (MultiSelectOption) -> Unit
) : ListAdapter<MultiSelectOption, MultiSelectAdapter.ViewHolder>(MultiSelectDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMultiSelectOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemMultiSelectOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    onItemClicked(item)
                }
            }
        }

        fun bind(option: MultiSelectOption) {
            binding.textViewOptionTitle.text = option.title
            // Change background based on selection
            if (option.selected) {
                binding.itemLayout.setBackgroundResource(R.drawable.item_background_selected)
                // Optional: Change text color or other attributes for selected state
                // binding.textViewOptionTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.selected_text_color))
            } else {
                binding.itemLayout.setBackgroundResource(R.drawable.item_background_default)
                // Optional: Reset text color or other attributes for default state
                // binding.textViewOptionTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.default_text_color))
            }
        }
    }
}

// MultiSelectDiffCallback remains the same
class MultiSelectDiffCallback : DiffUtil.ItemCallback<MultiSelectOption>() {
    override fun areItemsTheSame(oldItem: MultiSelectOption, newItem: MultiSelectOption): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: MultiSelectOption, newItem: MultiSelectOption): Boolean {
        return oldItem == newItem
    }
}