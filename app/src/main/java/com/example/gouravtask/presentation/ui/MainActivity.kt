package com.example.gouravtask.presentation.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gouravtask.databinding.ActivityMainBinding
import com.example.gouravtask.presentation.ui.adapter.HoldingsAdapter
import com.example.gouravtask.presentation.viewModel.HoldingsViewModel
import com.example.gouravtask.utils.setFormattedAmount
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HoldingsViewModel by viewModels()
    private val holdingsAdapter = HoldingsAdapter()

    private fun setupStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            // Tell system: we handle insets (edge-to-edge)
            WindowCompat.setDecorFitsSystemWindows(window, false) // Keep icons light (white) since background is dark
            WindowInsetsControllerCompat(window, window.decorView)
                .isAppearanceLightStatusBars = false

            // Push toolbar down below status bar
            ViewCompat.setOnApplyWindowInsetsListener(binding.customActionBar) { view, insets ->
                val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
                view.updatePadding(top = statusBarInsets.top)
                insets
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)

        setupStatusBar()

        setupRecyclerView()
        observeUiState()

        setupTabs()
        setupPortfolioSummaryAnimation()
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.tabLayout.getTabAt(1)?.select()


    }

    private fun setupRecyclerView() {
        binding.holdingsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = holdingsAdapter
            itemAnimator = null
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect { state ->

                    Log.i("HoldingsRepositoryImpl_MainActivty", "state: $state")
                    binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                    holdingsAdapter.submitList(state.holdings)

                    state.portfolioSummary?.let { summary ->
                        binding.portfolioSummary.tvCurrentValue.setFormattedAmount(summary.currentValue)
                        binding.portfolioSummary.tvTotalInvestment.setFormattedAmount(summary.totalInvestment)
                        binding.portfolioSummary.tvTodaysPnl.setFormattedAmount(summary.todaysPnl, isForPnl = true)
                        binding.portfolioSummary.tvTotalPnl.setFormattedAmount(summary.totalPnl, isForPnl = true)
                    }

                    state.error?.let {
                       Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Add this method to your MainActivity class
    private fun togglePortfolioSummary() {
        val expandableContent = listOf(
            binding.portfolioSummary.divider,
            binding.portfolioSummary.tvTotalInvestmentLabel,
            binding.portfolioSummary.tvTotalInvestment,
            binding.portfolioSummary.tvTodaysPnlLabel,
            binding.portfolioSummary.tvTodaysPnl,
            binding.portfolioSummary.tvCurrentValueLabel,
            binding.portfolioSummary.tvCurrentValue
        )

        val expandIcon = binding.portfolioSummary.ivExpandCollapse

        if (expandableContent.first().isVisible) {
            // Collapse - Slide down smoothly
            expandableContent.forEach { view ->
                view.animate()
                    .alpha(0f)
                    .translationY(40f)
                    .setDuration(300)
                    .setInterpolator(DecelerateInterpolator())
                    .withEndAction {
                        view.visibility = View.GONE
                        view.translationY = 0f
                    }
                    .start()
            }

            expandIcon.animate()
                .rotation(0f)
                .setDuration(300)
                .setInterpolator(DecelerateInterpolator())
                .start()

        } else {
            // Expand - Slide up smoothly
            expandableContent.forEach { view ->
                view.alpha = 0f
                view.translationY = 40f
                view.visibility = View.VISIBLE

                view.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(300)
                    .setInterpolator(DecelerateInterpolator())
                    .start()
            }

            expandIcon.animate()
                .rotation(180f)
                .setDuration(300)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }

    // Update the click listener setup
    private fun setupPortfolioSummaryAnimation() {
        binding.portfolioSummary.clickableHeaderArea.setOnClickListener {
            togglePortfolioSummary()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}