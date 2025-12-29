package com.example.tien.presentation.statistics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tien.domain.model.WorkEntry
import com.example.tien.presentation.statistics.MonthStat
import com.example.tien.presentation.statistics.StatisticsViewModel
import com.example.tien.presentation.statistics.WeekStat
import com.example.tien.ui.theme.PrimaryBlue
import com.example.tien.ui.theme.AccentGreen
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Tuần", "Tháng")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thống kê", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, "Quay lại", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = PrimaryBlue,
                contentColor = Color.White
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            when (selectedTab) {
                0 -> WeeklyStatsView(viewModel.weeklyStats, viewModel::togglePaymentStatus)
                1 -> MonthlyStatsView(viewModel.monthlyStats, viewModel::togglePaymentStatus)
            }
        }
    }
}

@Composable
fun WeeklyStatsView(
    stats: List<WeekStat>,
    onTogglePayment: (Long, Boolean) -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM")
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(stats, key = { it.weekLabel }) { weekStat ->
            val label = "Tuần ${weekStat.startDate.format(dateFormatter)} - ${weekStat.endDate.format(dateFormatter)}"
            StatCard(
                label = label,
                totalSalary = weekStat.totalSalary,
                paidSalary = weekStat.paidSalary,
                unpaidCount = weekStat.unpaidCount,
                entries = weekStat.entries,
                onTogglePayment = onTogglePayment
            )
        }
    }
}

@Composable
fun MonthlyStatsView(
    stats: List<MonthStat>,
    onTogglePayment: (Long, Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(stats, key = { it.monthLabel }) { monthStat ->
            val monthName = "Tháng ${monthStat.month} năm ${monthStat.year}"
            StatCard(
                label = monthName,
                totalSalary = monthStat.totalSalary,
                paidSalary = monthStat.paidSalary,
                unpaidCount = monthStat.unpaidCount,
                entries = monthStat.entries,
                onTogglePayment = onTogglePayment
            )
        }
    }
}

@Composable
fun StatCard(
    label: String,
    totalSalary: Long,
    paidSalary: Long,
    unpaidCount: Int,
    entries: List<WorkEntry>,
    onTogglePayment: (Long, Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Tổng tiền:", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        "${formatter.format(totalSalary)} đ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Đã trả:", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        "${formatter.format(paidSalary)} đ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGreen
                    )
                }
            }

            if (unpaidCount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Chưa trả: ${formatter.format(totalSalary - paidSalary)} đ ($unpaidCount entry)",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) "Ẩn chi tiết" else "Xem chi tiết (${entries.size} entry)")
            }

            if (expanded) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                entries.forEach { entry ->
                    EntryItem(entry, onTogglePayment)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun EntryItem(
    entry: WorkEntry,
    onTogglePayment: (Long, Boolean) -> Unit
) {
    val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.small)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onTogglePayment(entry.id, !entry.isPaid) },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = if (entry.isPaid) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                contentDescription = if (entry.isPaid) "Đã trả" else "Chưa trả",
                tint = if (entry.isPaid) AccentGreen else Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                entry.task,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                entry.date.format(dateFormatter),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Text(
            "${formatter.format(entry.salary)} đ",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (entry.isPaid) AccentGreen else MaterialTheme.colorScheme.error
        )
    }
}
