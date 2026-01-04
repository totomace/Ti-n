
package com.example.tien.presentation.workentry.ui

import com.example.tien.presentation.workentry.WorkEntryFormViewModel
import com.example.tien.util.CurrencyVisualTransformation
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tien.ui.theme.PrimaryBlue
import com.example.tien.ui.theme.AccentGreen
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun WorkEntryForm(
    state: WorkEntryFormViewModel.FormState,
    onDateChange: (LocalDate) -> Unit,
    onStartTimeChange: (LocalTime) -> Unit,
    onEndTimeChange: (LocalTime) -> Unit,
    onBreakMinutesChange: (String) -> Unit,
    onTaskChange: (String) -> Unit,
    onSalaryChange: (String) -> Unit,
    onPaidAmountChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onViewHistory: () -> Unit,
    onSaveSuccess: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Text("Nhập liệu thủ công", fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onViewHistory) {
                        Icon(Icons.Filled.List, "Lịch sử", tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        var visible by remember { mutableStateOf(false) }
        
        LaunchedEffect(Unit) {
            visible = true
        }
        
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(600)) + 
                    slideInVertically(animationSpec = tween(600)) { it / 4 }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
            var showDatePicker by remember { mutableStateOf(false) }
            var showStartTimePicker by remember { mutableStateOf(false) }
            var showEndTimePicker by remember { mutableStateOf(false) }

            if (showDatePicker) {
                DatePickerModal(
                    currentDate = state.date,
                    onDateSelected = { onDateChange(it) },
                    onDismiss = { showDatePicker = false }
                )
            }

            if (showStartTimePicker) {
                TimePickerModal(
                    currentTime = state.startTime,
                    onTimeSelected = { onStartTimeChange(it) },
                    onDismiss = { showStartTimePicker = false }
                )
            }

            if (showEndTimePicker) {
                TimePickerModal(
                    currentTime = state.endTime,
                    onTimeSelected = { onEndTimeChange(it) },
                    onDismiss = { showEndTimePicker = false }
                )
            }

        // Date Picker
        OutlinedTextField(
            value = state.date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy - EEEE")),
            onValueChange = { },
            label = { Text("Ngày làm") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            readOnly = true,
            trailingIcon = { 
                IconButton(onClick = { showDatePicker = true }) { 
                    Icon(Icons.Filled.DateRange, "Chọn ngày") 
                } 
            }
        )
        // Start Time
        OutlinedTextField(
            value = state.startTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
            onValueChange = { },
            label = { Text("Giờ bắt đầu") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            readOnly = true,
            trailingIcon = { 
                IconButton(onClick = { showStartTimePicker = true }) { 
                    Icon(Icons.Filled.Schedule, "Chọn giờ") 
                } 
            }
        )
        // End Time
        OutlinedTextField(
            value = state.endTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
            onValueChange = { },
            label = { Text("Giờ kết thúc") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            readOnly = true,
            trailingIcon = { 
                IconButton(onClick = { showEndTimePicker = true }) { 
                    Icon(Icons.Filled.Schedule, "Chọn giờ") 
                } 
            }
        )
        // Break Minutes
        OutlinedTextField(
            value = if (state.breakMinutes == 0) "" else state.breakMinutes.toString(),
            onValueChange = onBreakMinutesChange,
            label = { Text("Thời gian nghỉ (phút)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        // Task
        OutlinedTextField(
            value = state.task,
            onValueChange = onTaskChange,
            label = { Text("Công việc") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        // Salary
        OutlinedTextField(
            value = state.salaryInput,
            onValueChange = onSalaryChange,
            label = { Text("Tiền lương") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = CurrencyVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        // Paid Amount
        OutlinedTextField(
            value = state.paidAmountInput,
            onValueChange = onPaidAmountChange,
            label = { Text("Đã trả") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = CurrencyVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        
        // Notes
        OutlinedTextField(
            value = state.notes,
            onValueChange = onNotesChange,
            label = { Text("Ghi chú") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            minLines = 3,
            maxLines = 5
        )
        
        // Show remaining amount if partially paid
        if (state.paidAmount > 0 && state.paidAmount < state.salary) {
            val remaining = state.salary - state.paidAmount
            val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
            Text(
                "Còn nợ: ${formatter.format(remaining)} VNĐ",
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        // Error
        state.error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
        }
        // Submit Button
        val buttonScale = remember { Animatable(0.9f) }
        
        LaunchedEffect(Unit) {
            buttonScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
        
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = buttonScale.value
                    scaleY = buttonScale.value
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentGreen
            )
        ) {
            Text("Lưu", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
            }
        }
    }
}
