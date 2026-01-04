package com.example.tien.presentation.statistics.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tien.domain.model.WorkEntry
import com.example.tien.presentation.statistics.MonthStat
import com.example.tien.presentation.statistics.StatisticsViewModel
import com.example.tien.presentation.statistics.WeekStat
import com.example.tien.presentation.statistics.YearStat
import com.example.tien.ui.theme.PrimaryBlue
import com.example.tien.ui.theme.AccentGreen
import com.example.tien.util.CurrencyInputFormatter
import com.example.tien.util.CurrencyVisualTransformation
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
    val tabs = listOf("Tuần", "Tháng", "Năm")

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
                        text = { 
                            Text(
                                title, 
                                fontWeight = if (selectedTab == index) FontWeight.ExtraBold else FontWeight.Normal,
                                fontSize = if (selectedTab == index) 16.sp else 14.sp,
                                color = if (selectedTab == index) Color.White else Color(0xFFE5E7EB)
                            ) 
                        },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color(0xFFE5E7EB)
                    )
                }
            }

            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(500)) + 
                     slideInHorizontally(animationSpec = tween(500)) { if (targetState > initialState) it else -it })
                        .togetherWith(
                            fadeOut(animationSpec = tween(300)) +
                            slideOutHorizontally(animationSpec = tween(300)) { if (targetState > initialState) -it else it }
                        )
                },
                label = "tab_transition"
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> WeeklyStatsView(viewModel.weeklyStats, viewModel::updatePaidAmount)
                    1 -> MonthlyStatsView(viewModel.monthlyStats, viewModel::updatePaidAmount)
                    2 -> YearlyStatsView(viewModel.yearlyStats, viewModel::updatePaidAmount)
                }
            }
        }
    }
}

@Composable
fun WeeklyStatsView(
    stats: List<WeekStat>,
    onTogglePayment: (Long, Long) -> Unit
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
    onTogglePayment: (Long, Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Bar chart
        if (stats.isNotEmpty()) {
            item {
                val chartData = stats.take(6).reversed().map { monthStat ->
                    "T${monthStat.month}" to monthStat.totalSalary
                }
                BarChart(
                    data = chartData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        }
        
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
fun YearlyStatsView(
    stats: List<YearStat>,
    onTogglePayment: (Long, Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Pie chart
        if (stats.isNotEmpty()) {
            item {
                val totalPaid = stats.sumOf { it.paidSalary }
                val totalUnpaid = stats.sumOf { it.totalSalary - it.paidSalary }
                PieChart(
                    paidAmount = totalPaid,
                    unpaidAmount = totalUnpaid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }
        }
        
        items(stats, key = { it.year }) { yearStat ->
            val yearName = "Năm ${yearStat.year}"
            StatCard(
                label = yearName,
                totalSalary = yearStat.totalSalary,
                paidSalary = yearStat.paidSalary,
                unpaidCount = yearStat.unpaidCount,
                entries = yearStat.entries,
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
    onTogglePayment: (Long, Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFBEB)
        )
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
                color = Color(0xFFD97706)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Tổng tiền:", fontSize = 14.sp, color = Color(0xFF92400E))
                    Text(
                        NumberFormat.getNumberInstance(Locale("vi", "VN")).format(totalSalary) + " VNĐ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF78350F)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Đã trả:", fontSize = 14.sp, color = Color(0xFF92400E))
                    Text(
                        NumberFormat.getNumberInstance(Locale("vi", "VN")).format(paidSalary) + " VNĐ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D)
                    )
                }
            }

            if (totalSalary > paidSalary) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Còn nợ: ${NumberFormat.getNumberInstance(Locale("vi", "VN")).format(totalSalary - paidSalary)} VNĐ ($unpaidCount entry)",
                    fontSize = 13.sp,
                    color = Color(0xFFDC2626),
                    fontWeight = FontWeight.Medium
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
    onTogglePayment: (Long, Long) -> Unit
) {
    val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var showPaymentDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFEF3C7),
                shape = MaterialTheme.shapes.medium
            )
            .clickable { showPaymentDialog = true }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (entry.paidAmount >= entry.salary) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
            contentDescription = if (entry.paidAmount >= entry.salary) "Đã trả đủ" else "Chưa trả đủ",
            tint = if (entry.paidAmount >= entry.salary) Color(0xFFD97706) else Color(0xFFF59E0B),
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                entry.task,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF78350F)
            )
            Text(
                entry.date.format(dateFormatter),
                fontSize = 12.sp,
                color = Color(0xFF92400E)
            )
            if (entry.paidAmount > 0 && entry.paidAmount < entry.salary) {
                val remaining = entry.salary - entry.paidAmount
                Text(
                    "Đã trả: ${NumberFormat.getNumberInstance(Locale("vi", "VN")).format(entry.paidAmount)} VNĐ",
                    fontSize = 11.sp,
                    color = Color(0xFF15803D)
                )
                Text(
                    "Còn: ${NumberFormat.getNumberInstance(Locale("vi", "VN")).format(remaining)} VNĐ",
                    fontSize = 11.sp,
                    color = Color(0xFFDC2626)
                )
            }
        }

        Text(
            NumberFormat.getNumberInstance(Locale("vi", "VN")).format(entry.salary) + " VNĐ",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (entry.paidAmount >= entry.salary) Color(0xFF15803D) else Color(0xFFDC2626)
        )
    }

    if (showPaymentDialog) {
        PaymentDialog(
            entry = entry,
            onDismiss = { showPaymentDialog = false },
            onConfirm = { amount ->
                onTogglePayment(entry.id, amount)
                showPaymentDialog = false
            }
        )
    }
}

@Composable
fun PaymentDialog(
    entry: WorkEntry,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    var paidAmountText by remember { 
        mutableStateOf(
            if (entry.paidAmount == 0L) "" else CurrencyInputFormatter.formatForDisplay(entry.paidAmount)
        )
    }
    val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    val remaining = entry.salary - (CurrencyInputFormatter.parseToLong(paidAmountText) ?: 0L)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cập nhật thanh toán", fontWeight = FontWeight.Bold, color = Color(0xFFD97706)) },
        containerColor = Color(0xFFFFFBEB),
        text = {
            Column {
                Text(
                    "Công việc: ${entry.task}",
                    fontSize = 14.sp,
                    color = Color(0xFF92400E)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Tổng lương: ${formatter.format(entry.salary)} VNĐ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF78350F)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = paidAmountText,
                    onValueChange = { paidAmountText = it },
                    label = { Text("Số tiền đã trả") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = CurrencyVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    suffix = { Text("VNĐ") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFBBF24),
                        focusedLabelColor = Color(0xFFD97706),
                        cursorColor = Color(0xFFD97706)
                    )
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                if (remaining > 0) {
                    Text(
                        "Còn nợ: ${formatter.format(remaining)} VNĐ",
                        color = Color(0xFFDC2626),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                } else if (remaining < 0) {
                    Text(
                        "Thừa: ${formatter.format(-remaining)} VNĐ",
                        color = Color(0xFF15803D),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                } else {
                    Text(
                        "✓ Đã thanh toán đủ",
                        color = Color(0xFF15803D),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amount = CurrencyInputFormatter.parseToLong(paidAmountText) ?: 0L
                    onConfirm(amount)
                }
            ) {
                Text("Lưu", color = Color(0xFFD97706), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color(0xFF92400E))
            }
        }
    )
}
