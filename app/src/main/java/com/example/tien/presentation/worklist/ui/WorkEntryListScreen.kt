package com.example.tien.presentation.worklist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tien.domain.model.WorkEntry
import com.example.tien.ui.theme.AccentGreen
import com.example.tien.ui.theme.PrimaryBlue
import com.example.tien.ui.theme.TextSecondary
import java.time.format.DateTimeFormatter

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun WorkEntryListScreen(
    viewModel: com.example.tien.presentation.worklist.WorkEntryListViewModel,
    onAddClick: () -> Unit
) {
    val entries = viewModel.entries.collectAsState().value
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lịch sử làm việc", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = AccentGreen,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Thêm mới")
            }
        }
    ) { padding ->
        if (entries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Chưa có dữ liệu. Nhấn nút + để thêm!",
                    color = TextSecondary,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                items(entries) { entry ->
                    WorkEntryCard(
                        entry = entry,
                        onDelete = { viewModel.deleteEntry(entry.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun WorkEntryCard(entry: WorkEntry, onDelete: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc muốn xóa bản ghi này không?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Xóa")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                Text("Tiền lương:", fontWeight = FontWeight.SemiBold)
                Text(
                    "${entry.salary} VNĐ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = AccentGreen
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
