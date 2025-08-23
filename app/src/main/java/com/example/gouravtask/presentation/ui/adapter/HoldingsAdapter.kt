package com.example.gouravtask.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gouravtask.R
import com.example.gouravtask.data.db.entity.Holding
import com.example.gouravtask.databinding.ItemHoldingBinding
import com.example.gouravtask.presentation.model.UiHolding
import com.example.gouravtask.utils.setSpannableTextWithResources


class HoldingsAdapter :
    ListAdapter<UiHolding, HoldingsAdapter.HoldingViewHolder>(HoldingsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val binding = ItemHoldingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HoldingViewHolder(private val binding: ItemHoldingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(holding: UiHolding) {
            binding.symbol.text = holding.symbol
            binding.quantity.setSpannableTextWithResources(
                R.string.net_qty, 5f,
                R.color.gray_500, holding.quantity, R.color.black, 8f,isFormattedAmount = false, false)
            binding.ltp.setSpannableTextWithResources(R.string.ltp,5f, R.color.gray_500, holding.ltp, R.color.black, 8f,isFormattedAmount = true, false)
            val pnl = (holding.ltp - holding.avgPrice) * holding.quantity
            binding.pnl.setSpannableTextWithResources(
                R.string.profit_n_loss, 5f,
                R.color.gray_500, pnl, if (pnl > 0) R.color.green_500 else R.color.red_500, 8f, isFormattedAmount = true, true)
        }
    }
}

class HoldingsDiffCallback : DiffUtil.ItemCallback<UiHolding>() {
    override fun areItemsTheSame(oldItem: UiHolding, newItem: UiHolding): Boolean {
        return oldItem.symbol == newItem.symbol
    }

    override fun areContentsTheSame(oldItem: UiHolding, newItem: UiHolding): Boolean {
        return oldItem == newItem
    }
}
