package com.example.tien.presentation.statistics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tien.domain.model.WorkEntry
import com.example.tien.domain.usecase.GetWorkEntriesUseCase
import com.example.tien.domain.usecase.UpdatePaymentStatusUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

class StatisticsViewModel(
    private val getWorkEntries: GetWorkEntriesUseCase,
    private val updatePaymentStatus: UpdatePaymentStatusUseCase
) : ViewModel() {

    var weeklyStats by mutableStateOf<List<WeekStat>>(emptyList())
        private set

    var monthlyStats by mutableStateOf<List<MonthStat>>(emptyList())
        private set

    init {
        loadStatistics()
    }

    fun loadStatistics() {
        viewModelScope.launch {
            val entries = getWorkEntries()
            
            // Group by week
            val weekGroups = entries.groupBy { entry ->
                val weekFields = WeekFields.of(Locale.getDefault())
                val weekNumber = entry.date.get(weekFields.weekOfWeekBasedYear())
                val year = entry.date.year
                "$year-W$weekNumber"
            }
            
            weeklyStats = weekGroups.map { (weekKey, weekEntries) ->
                val sortedEntries = weekEntries.sortedBy { it.date }
                WeekStat(
                    weekLabel = weekKey,
                    startDate = sortedEntries.first().date,
                    endDate = sortedEntries.last().date,
                    entries = weekEntries.sortedByDescending { it.date },
                    totalSalary = weekEntries.sumOf { it.salary },
                    paidSalary = weekEntries.filter { it.isPaid }.sumOf { it.salary },
                    unpaidCount = weekEntries.count { !it.isPaid }
                )
            }.sortedByDescending { it.weekLabel }

            // Group by month
            val monthGroups = entries.groupBy { entry ->
                "${entry.date.year}-${entry.date.monthValue.toString().padStart(2, '0')}"
            }
            
            monthlyStats = monthGroups.map { (monthKey, monthEntries) ->
                val parts = monthKey.split("-")
                MonthStat(
                    monthLabel = monthKey,
                    month = parts[1].toInt(),
                    year = parts[0].toInt(),
                    entries = monthEntries.sortedByDescending { it.date },
                    totalSalary = monthEntries.sumOf { it.salary },
                    paidSalary = monthEntries.filter { it.isPaid }.sumOf { it.salary },
                    unpaidCount = monthEntries.count { !it.isPaid }
                )
            }.sortedByDescending { it.monthLabel }
        }
    }

    fun togglePaymentStatus(id: Long, isPaid: Boolean) {
        viewModelScope.launch {
            updatePaymentStatus(id, isPaid)
            loadStatistics()
        }
    }
}

data class WeekStat(
    val weekLabel: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val entries: List<WorkEntry>,
    val totalSalary: Long,
    val paidSalary: Long,
    val unpaidCount: Int
)

data class MonthStat(
    val monthLabel: String,
    val month: Int,
    val year: Int,
    val entries: List<WorkEntry>,
    val totalSalary: Long,
    val paidSalary: Long,
    val unpaidCount: Int
)
