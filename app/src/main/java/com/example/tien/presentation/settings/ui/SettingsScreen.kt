package com.example.tien.presentation.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.graphicsLayer
import com.example.tien.data.preferences.ThemeMode
import com.example.tien.presentation.settings.SettingsViewModel
import com.example.tien.ui.theme.PrimaryYellow
import com.example.tien.ui.theme.AccentGold
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit
) {
    val currentTheme by viewModel.themeMode.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    var showComingSoonDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showFeedbackDialog by remember { mutableStateOf(false) }
    var comingSoonFeature by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, "Quay lại", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFBBF24),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Appearance Section
            item {
                Text(
                    "Giao diện",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD97706),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                SettingItem(
                    icon = Icons.Filled.SettingsBrightness,
                    title = "Chế độ hiển thị",
                    subtitle = when (currentTheme) {
                        ThemeMode.LIGHT -> "Chế độ sáng"
                        ThemeMode.DARK -> "Chế độ tối"
                        ThemeMode.SYSTEM -> "Theo hệ thống"
                    },
                    onClick = { showThemeDialog = true }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            // Currency & Format Section
            item {
                Text(
                    "Tiền tệ & Định dạng",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD97706),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                SettingItem(
                    icon = Icons.Filled.AttachMoney,
                    title = "Đơn vị tiền tệ",
                    subtitle = "VNĐ",
                    onClick = { 
                        comingSoonFeature = "Đơn vị tiền tệ"
                        showComingSoonDialog = true 
                    }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                SettingItem(
                    icon = Icons.Filled.FormatListNumbered,
                    title = "Định dạng số",
                    subtitle = "1.000.000",
                    onClick = { 
                        comingSoonFeature = "Định dạng số"
                        showComingSoonDialog = true 
                    }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            // Date & Time Section
            item {
                Text(
                    "Ngày giờ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD97706),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                SettingItem(
                    icon = Icons.Filled.CalendarToday,
                    title = "Định dạng ngày",
                    subtitle = "DD/MM/YYYY",
                    onClick = { 
                        comingSoonFeature = "Định dạng ngày"
                        showComingSoonDialog = true 
                    }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                SettingItem(
                    icon = Icons.Filled.Event,
                    title = "Ngày bắt đầu tuần",
                    subtitle = "Thứ Hai",
                    onClick = { 
                        comingSoonFeature = "Ngày bắt đầu tuần"
                        showComingSoonDialog = true 
                    }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            // Notifications Section
            item {
                Text(
                    "Thông báo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD97706),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                SettingItem(
                    icon = Icons.Filled.Notifications,
                    title = "Nhắc nhở công việc",
                    subtitle = "Tắt",
                    onClick = { 
                        comingSoonFeature = "Nhắc nhở công việc"
                        showComingSoonDialog = true 
                    }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                SettingItem(
                    icon = Icons.Filled.NotificationsActive,
                    title = "Nhắc thanh toán",
                    subtitle = "Tắt",
                    onClick = { 
                        comingSoonFeature = "Nhắc thanh toán"
                        showComingSoonDialog = true 
                    }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            // Backup & Restore Section
            item {
                Text(
                    "Sao lưu & Khôi phục",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD97706),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                SettingItem(
                    icon = Icons.Filled.CloudUpload,
                    title = "Xuất dữ liệu",
                    subtitle = "Lưu file backup",
                    onClick = { 
                        comingSoonFeature = "Xuất dữ liệu"
                        showComingSoonDialog = true 
                    }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                SettingItem(
                    icon = Icons.Filled.CloudDownload,
                    title = "Nhập dữ liệu",
                    subtitle = "Khôi phục từ file",
                    onClick = { 
                        comingSoonFeature = "Nhập dữ liệu"
                        showComingSoonDialog = true 
                    }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            // About Section
            item {
                Text(
                    "Thông tin",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD97706),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                SettingItem(
                    icon = Icons.Filled.Info,
                    title = "Về ứng dụng",
                    subtitle = "Phiên bản 1.0.0",
                    onClick = { showAboutDialog = true }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                SettingItem(
                    icon = Icons.Filled.BugReport,
                    title = "Báo lỗi",
                    subtitle = "Gửi phản hồi",
                    onClick = { showFeedbackDialog = true }
                )
            }
        }

        if (showThemeDialog) {
            ThemeSelectionDialog(
                currentTheme = currentTheme,
                onThemeSelected = { theme ->
                    viewModel.setThemeMode(theme)
                    showThemeDialog = false
                },
                onDismiss = { showThemeDialog = false }
            )
        }

        if (showComingSoonDialog) {
            ComingSoonDialog(
                featureName = comingSoonFeature,
                onDismiss = { showComingSoonDialog = false }
            )
        }

        if (showAboutDialog) {
            AboutDialog(
                onDismiss = { showAboutDialog = false }
            )
        }

        if (showFeedbackDialog) {
            FeedbackDialog(
                onDismiss = { showFeedbackDialog = false }
            )
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color(0xFFD97706),
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                subtitle,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ThemeSelectionDialog(
    currentTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    // Animation cho dialog
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        launch { scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy)) }
        launch { alpha.animateTo(1f, tween(300)) }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
            this.alpha = alpha.value
        },
        containerColor = Color(0xFFFFFBF0),
        icon = {
            Icon(
                Icons.Filled.SettingsBrightness,
                contentDescription = null,
                tint = Color(0xFFD97706),
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                "Chọn chế độ hiển thị",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD97706)
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ThemeOptionItem(
                    icon = Icons.Filled.LightMode,
                    label = "Chế độ sáng",
                    description = "Giao diện sáng",
                    isSelected = currentTheme == ThemeMode.LIGHT,
                    onClick = { onThemeSelected(ThemeMode.LIGHT) }
                )

                ThemeOptionItem(
                    icon = Icons.Filled.DarkMode,
                    label = "Chế độ tối",
                    description = "Dễ nhìn ban đêm",
                    isSelected = currentTheme == ThemeMode.DARK,
                    onClick = { onThemeSelected(ThemeMode.DARK) }
                )

                ThemeOptionItem(
                    icon = Icons.Filled.SettingsBrightness,
                    label = "Theo hệ thống",
                    description = "Tự động theo thiết bị",
                    isSelected = currentTheme == ThemeMode.SYSTEM,
                    onClick = { onThemeSelected(ThemeMode.SYSTEM) }
                )
            }
        },
        confirmButton = {},
        dismissButton = {},
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun ThemeOptionItem(
    icon: ImageVector,
    label: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) Color(0xFFFBBF24).copy(alpha = 0.15f)
                else Color(0xFFFFFBF0)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (isSelected) Color(0xFFD97706) else Color(0xFF92400E),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                label,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) Color(0xFFD97706) else Color(0xFF92400E)
            )
            Text(
                description,
                fontSize = 12.sp,
                color = Color(0xFFB45309)
            )
        }

        // Radio button circle
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) Color(0xFFFBBF24)
                    else Color(0xFFD97706).copy(alpha = 0.3f)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }
    }
}

@Composable
fun ComingSoonDialog(
    featureName: String,
    onDismiss: () -> Unit
) {
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        launch { scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy)) }
        launch { alpha.animateTo(1f, tween(300)) }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
            this.alpha = alpha.value
        },
        containerColor = Color(0xFFFFFBF0),
        shape = RoundedCornerShape(16.dp),
        icon = {
            Icon(
                Icons.Filled.Build,
                contentDescription = null,
                tint = Color(0xFFFBBF24),
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                "Đang phát triển",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD97706)
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Tính năng \"$featureName\" đang được phát triển và sẽ có mặt trong phiên bản tiếp theo.",
                    color = Color(0xFF92400E),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Cảm ơn bạn đã quan tâm! 🙏",
                    color = Color(0xFFB45309),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFBBF24)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Đã hiểu", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun AboutDialog(
    onDismiss: () -> Unit
) {
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        launch { scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy)) }
        launch { alpha.animateTo(1f, tween(300)) }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
            this.alpha = alpha.value
        },
        containerColor = Color(0xFFFFFBF0),
        shape = RoundedCornerShape(16.dp),
        icon = {
            Icon(
                Icons.Filled.AccountCircle,
                contentDescription = null,
                tint = Color(0xFFFBBF24),
                modifier = Modifier.size(56.dp)
            )
        },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Công Việc Của Tôi",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD97706),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Phiên bản 1.0.0",
                    color = Color(0xFF92400E),
                    fontSize = 14.sp
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HorizontalDivider(color = Color(0xFFFBBF24).copy(alpha = 0.3f))
                
                Text(
                    "Ứng dụng quản lý công việc và thu nhập cá nhân",
                    color = Color(0xFF92400E),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    AboutInfoRow(Icons.Filled.Code, "Phát triển bởi: Kiệt")
                    AboutInfoRow(Icons.Filled.CalendarToday, "Năm phát hành: 2026")
                    AboutInfoRow(Icons.Filled.Android, "Nền tảng: Android")
                }
                
                HorizontalDivider(color = Color(0xFFFBBF24).copy(alpha = 0.3f))
                
                Text(
                    "© 2026 All Rights Reserved",
                    color = Color(0xFFB45309),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFBBF24)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Đóng", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun AboutInfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color(0xFFF59E0B),
            modifier = Modifier.size(20.dp)
        )
        Text(
            text,
            color = Color(0xFF92400E),
            fontSize = 14.sp
        )
    }
}

@Composable
fun FeedbackDialog(
    onDismiss: () -> Unit
) {
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        launch { scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy)) }
        launch { alpha.animateTo(1f, tween(300)) }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
            this.alpha = alpha.value
        },
        containerColor = Color(0xFFFFFBF0),
        shape = RoundedCornerShape(16.dp),
        icon = {
            Icon(
                Icons.Filled.BugReport,
                contentDescription = null,
                tint = Color(0xFFDC2626),
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                "Báo lỗi & Phản hồi",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD97706)
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Bạn gặp vấn đề hoặc có ý kiến đóng góp?",
                    color = Color(0xFF92400E),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Email contact
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            "Email hỗ trợ",
                            fontSize = 12.sp,
                            color = Color(0xFFB45309),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "khanhkietbt2020@gmail.com",
                            fontSize = 14.sp,
                            color = Color(0xFF92400E),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                // GitHub Issues
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Phone,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            "Hotline hỗ trợ",
                            fontSize = 12.sp,
                            color = Color(0xFFB45309),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "0782 987 602",
                            fontSize = 14.sp,
                            color = Color(0xFF92400E),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Text(
                    "Chúng tôi sẽ phản hồi trong vòng 24-48 giờ.",
                    fontSize = 13.sp,
                    color = Color(0xFFB45309),
                    fontWeight = FontWeight.Medium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFBBF24)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Đã hiểu", fontWeight = FontWeight.Bold)
            }
        }
    )
}
