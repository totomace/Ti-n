package com.example.tien.presentation.worklist.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.tien.domain.model.WorkEntry
import com.example.tien.ui.theme.AccentGreen
import com.example.tien.ui.theme.PrimaryBlue
import com.example.tien.ui.theme.TextSecondary
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun WorkEntryListScreen(
    viewModel: com.example.tien.presentation.worklist.WorkEntryListViewModel,
    onAddClick: () -> Unit,
    onEditClick: (WorkEntry) -> Unit,
    onStatisticsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onNotesClick: () -> Unit
) {
    val entries = viewModel.entries.collectAsState().value
    val searchQuery = viewModel.searchQuery.collectAsState().value
    val filterType = viewModel.filterType.collectAsState().value
    val sortType = viewModel.sortType.collectAsState().value
    var showSearch by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    AnimatedContent(
                        targetState = showSearch,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                        },
                        label = "title_search"
                    ) { isSearching ->
                        if (isSearching) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = viewModel::onSearchQueryChange,
                                placeholder = { Text("Tìm kiếm công việc...", color = Color.White.copy(alpha = 0.7f)) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    cursorColor = Color.White
                                )
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Work,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text("Lịch sử làm việc", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { 
                        showSearch = !showSearch
                        if (!showSearch) viewModel.onSearchQueryChange("")
                    }) {
                        Icon(
                            if (showSearch) Icons.Filled.Close else Icons.Filled.Search,
                            if (showSearch) "Đóng" else "Tìm kiếm",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showSortMenu = !showSortMenu }) {
                        Icon(Icons.Filled.Sort, "Sắp xếp", tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "Ngày mới nhất",
                                    color = if (sortType == com.example.tien.presentation.worklist.SortType.DATE_DESC) 
                                        Color(0xFFFBBF24) else Color.Black
                                )
                            },
                            onClick = {
                                viewModel.onSortChange(com.example.tien.presentation.worklist.SortType.DATE_DESC)
                                showSortMenu = false
                            },
                            leadingIcon = {
                                if (sortType == com.example.tien.presentation.worklist.SortType.DATE_DESC) {
                                    Icon(Icons.Default.Check, null, tint = Color(0xFFFBBF24))
                                }
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = Color.Black
                            )
                        )
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "Ngày cũ nhất",
                                    color = if (sortType == com.example.tien.presentation.worklist.SortType.DATE_ASC) 
                                        Color(0xFFFBBF24) else Color.Black
                                )
                            },
                            onClick = {
                                viewModel.onSortChange(com.example.tien.presentation.worklist.SortType.DATE_ASC)
                                showSortMenu = false
                            },
                            leadingIcon = {
                                if (sortType == com.example.tien.presentation.worklist.SortType.DATE_ASC) {
                                    Icon(Icons.Default.Check, null, tint = Color(0xFFFBBF24))
                                }
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = Color.Black
                            )
                        )
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "Lương cao nhất",
                                    color = if (sortType == com.example.tien.presentation.worklist.SortType.SALARY_DESC) 
                                        Color(0xFFFBBF24) else Color.Black
                                )
                            },
                            onClick = {
                                viewModel.onSortChange(com.example.tien.presentation.worklist.SortType.SALARY_DESC)
                                showSortMenu = false
                            },
                            leadingIcon = {
                                if (sortType == com.example.tien.presentation.worklist.SortType.SALARY_DESC) {
                                    Icon(Icons.Default.Check, null, tint = Color(0xFFFBBF24))
                                }
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = Color.Black
                            )
                        )
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "Lương thấp nhất",
                                    color = if (sortType == com.example.tien.presentation.worklist.SortType.SALARY_ASC) 
                                        Color(0xFFFBBF24) else Color.Black
                                )
                            },
                            onClick = {
                                viewModel.onSortChange(com.example.tien.presentation.worklist.SortType.SALARY_ASC)
                                showSortMenu = false
                            },
                            leadingIcon = {
                                if (sortType == com.example.tien.presentation.worklist.SortType.SALARY_ASC) {
                                    Icon(Icons.Default.Check, null, tint = Color(0xFFFBBF24))
                                }
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = Color.Black
                            )
                        )
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "Tên A-Z",
                                    color = if (sortType == com.example.tien.presentation.worklist.SortType.NAME_ASC) 
                                        Color(0xFFFBBF24) else Color.Black
                                )
                            },
                            onClick = {
                                viewModel.onSortChange(com.example.tien.presentation.worklist.SortType.NAME_ASC)
                                showSortMenu = false
                            },
                            leadingIcon = {
                                if (sortType == com.example.tien.presentation.worklist.SortType.NAME_ASC) {
                                    Icon(Icons.Default.Check, null, tint = Color(0xFFFBBF24))
                                }
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = Color.Black
                            )
                        )
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "Tên Z-A",
                                    color = if (sortType == com.example.tien.presentation.worklist.SortType.NAME_DESC) 
                                        Color(0xFFFBBF24) else Color.Black
                                )
                            },
                            onClick = {
                                viewModel.onSortChange(com.example.tien.presentation.worklist.SortType.NAME_DESC)
                                showSortMenu = false
                            },
                            leadingIcon = {
                                if (sortType == com.example.tien.presentation.worklist.SortType.NAME_DESC) {
                                    Icon(Icons.Default.Check, null, tint = Color(0xFFFBBF24))
                                }
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = Color.Black
                            )
                        )
                    }
                    IconButton(onClick = onNotesClick) {
                        Icon(Icons.Filled.Notes, "Ghi chú", tint = Color.White)
                    }
                    IconButton(onClick = onStatisticsClick) {
                        Icon(Icons.Filled.BarChart, "Thống kê", tint = Color.White)
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Filled.Settings, "Cài đặt", tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            val fabScale = remember { Animatable(0f) }
            
            LaunchedEffect(Unit) {
                fabScale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }
            
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = AccentGreen,
                contentColor = Color.White,
                modifier = Modifier.graphicsLayer {
                    scaleX = fabScale.value
                    scaleY = fabScale.value
                }
            ) {
                Icon(Icons.Filled.Add, "Thêm mới")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedFilterChip(
                    selected = filterType == com.example.tien.presentation.worklist.FilterType.ALL,
                    onClick = { viewModel.onFilterChange(com.example.tien.presentation.worklist.FilterType.ALL) },
                    label = "Tất cả",
                    selectedColor = Color(0xFFFBBF24),
                    modifier = Modifier.weight(1f)
                )
                AnimatedFilterChip(
                    selected = filterType == com.example.tien.presentation.worklist.FilterType.PAID,
                    onClick = { viewModel.onFilterChange(com.example.tien.presentation.worklist.FilterType.PAID) },
                    label = "Đã trả",
                    selectedColor = Color(0xFF15803D),
                    modifier = Modifier.weight(1f)
                )
                AnimatedFilterChip(
                    selected = filterType == com.example.tien.presentation.worklist.FilterType.UNPAID,
                    onClick = { viewModel.onFilterChange(com.example.tien.presentation.worklist.FilterType.UNPAID) },
                    label = "Chưa trả",
                    selectedColor = Color(0xFFDC2626),
                    modifier = Modifier.weight(1f)
                )
                AnimatedFilterChip(
                    selected = filterType == com.example.tien.presentation.worklist.FilterType.PARTIAL,
                    onClick = { viewModel.onFilterChange(com.example.tien.presentation.worklist.FilterType.PARTIAL) },
                    label = "1 phần",
                    selectedColor = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                )
            }
            
            if (entries.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val emptyAlpha = remember { Animatable(0f) }
                    
                    LaunchedEffect(Unit) {
                        emptyAlpha.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(800)
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.graphicsLayer { alpha = emptyAlpha.value }
                    ) {
                        Icon(
                            Icons.Filled.Work,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Không tìm thấy kết quả!",
                            color = TextSecondary,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                items(
                    items = entries,
                    key = { it.id }
                ) { entry ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(500)) + 
                                slideInVertically(animationSpec = tween(500)) { it / 2 },
                        exit = fadeOut(animationSpec = tween(300)) + 
                               slideOutHorizontally(animationSpec = tween(300)) { -it }
                    ) {
                        WorkEntryCard(
                            entry = entry,
                            onEdit = { onEditClick(entry) },
                            onDelete = { viewModel.deleteEntry(entry.id) }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
            }
        }
    }
}

@Composable
fun WorkEntryCard(entry: WorkEntry, onEdit: () -> Unit, onDelete: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scale = remember { Animatable(0.8f) }
    
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Color(0xFFFFFBF0),
            shape = RoundedCornerShape(16.dp),
            title = { 
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = null,
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        "Xác nhận xóa",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626)
                    )
                }
            },
            text = { 
                Text(
                    "Bạn có chắc muốn xóa bản ghi này không?",
                    color = Color(0xFF92400E)
                ) 
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Xóa", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFF92400E)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Hủy", color = Color(0xFF92400E))
                }
            }
        )
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .clickable { onEdit() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Date with Delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - EEEE")),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = PrimaryBlue
                )
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Xóa",
                        tint = Color.Red
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))
            
            // Time info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem("Giờ bắt đầu", entry.startTime.toString())
                InfoItem("Giờ kết thúc", entry.endTime.toString())
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem("Nghỉ", "${entry.breakMinutes} phút")
                InfoItem("Công việc", entry.task)
            }
            
            // Notes section
            if (entry.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFF9E6), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        "Ghi chú:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD97706)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        entry.notes,
                        fontSize = 13.sp,
                        color = Color(0xFF92400E)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Salary - highlighted
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AccentGreen.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Tiền lương:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    if (entry.paidAmount > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Đã trả: ${NumberFormat.getNumberInstance(Locale("vi", "VN")).format(entry.paidAmount)} VNĐ",
                            fontSize = 13.sp,
                            color = AccentGreen
                        )
                        if (entry.paidAmount < entry.salary) {
                            val remaining = entry.salary - entry.paidAmount
                            Text(
                                "Còn nợ: ${NumberFormat.getNumberInstance(Locale("vi", "VN")).format(remaining)} VNĐ",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                Text(
                    NumberFormat.getNumberInstance(Locale("vi", "VN")).format(entry.salary) + " VNĐ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF15803D)
                )
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AnimatedFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    selectedColor: Color,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "chip_scale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) selectedColor else Color(0xFFE5E7EB),
        animationSpec = tween(300),
        label = "chip_color"
    )
    
    val textColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color(0xFF6B7280),
        animationSpec = tween(300),
        label = "text_color"
    )
    
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { 
            Text(
                label,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = backgroundColor,
            selectedLabelColor = textColor,
            containerColor = backgroundColor,
            labelColor = textColor
        )
    )
}
