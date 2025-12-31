package com.example.tien.presentation.statistics.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChart(
    paidAmount: Long,
    unpaidAmount: Long,
    modifier: Modifier = Modifier
) {
    val total = paidAmount + unpaidAmount
    if (total == 0L) return
    
    val paidPercentage = (paidAmount.toFloat() / total * 100).toInt()
    val unpaidPercentage = 100 - paidPercentage
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Tỷ lệ thanh toán",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD97706)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Canvas(
            modifier = Modifier.size(200.dp)
        ) {
            val radius = size.minDimension / 2
            val centerX = size.width / 2
            val centerY = size.height / 2
            
            // Paid slice (green)
            val paidAngle = 360f * (paidAmount.toFloat() / total)
            drawArc(
                color = Color(0xFF15803D),
                startAngle = -90f,
                sweepAngle = paidAngle,
                useCenter = true,
                topLeft = Offset(centerX - radius, centerY - radius),
                size = Size(radius * 2, radius * 2)
            )
            
            // Unpaid slice (red)
            drawArc(
                color = Color(0xFFDC2626),
                startAngle = -90f + paidAngle,
                sweepAngle = 360f - paidAngle,
                useCenter = true,
                topLeft = Offset(centerX - radius, centerY - radius),
                size = Size(radius * 2, radius * 2)
            )
            
            // White border
            drawCircle(
                color = Color.White,
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 4f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            LegendItem("Đã trả", Color(0xFF15803D), "$paidPercentage%")
            LegendItem("Chưa trả", Color(0xFFDC2626), "$unpaidPercentage%")
        }
    }
}

@Composable
fun BarChart(
    data: List<Pair<String, Long>>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return
    
    val maxValue = data.maxOf { it.second }
    val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    
    Column(modifier = modifier) {
        Text(
            "Thu nhập theo tháng",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD97706)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 8.dp)
        ) {
            val barWidth = size.width / (data.size * 2f)
            val spacing = barWidth * 0.5f
            val maxBarHeight = size.height - 40.dp.toPx()
            
            data.forEachIndexed { index, (label, value) ->
                val barHeight = if (maxValue > 0) {
                    (value.toFloat() / maxValue) * maxBarHeight
                } else 0f
                
                val x = index * (barWidth + spacing) + spacing
                val y = size.height - barHeight
                
                // Draw bar
                drawRect(
                    color = Color(0xFFFBBF24),
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight)
                )
                
                // Draw border
                drawRect(
                    color = Color(0xFFD97706),
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    style = Stroke(width = 2f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            data.forEach { (label, _) ->
                Text(
                    label,
                    fontSize = 12.sp,
                    color = Color(0xFF92400E)
                )
            }
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Canvas(modifier = Modifier.size(16.dp)) {
            drawCircle(color = color)
        }
        Text(
            "$label: $value",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF78350F)
        )
    }
}
