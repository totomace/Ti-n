package com.example.tien.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Utility object for formatting currency input with thousand separators and decimal support
 * Optimized for Vietnamese currency format
 */
object CurrencyInputFormatter {
    
    // Vietnamese locale for formatting
    private val viLocale = Locale("vi", "VN")
    
    // Format symbols for Vietnamese (using dot as thousand separator, comma as decimal)
    private val symbols = DecimalFormatSymbols(viLocale).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    
    // Formatter for display (with thousand separators)
    private val displayFormatter = DecimalFormat("#,##0.##", symbols).apply {
        maximumFractionDigits = 2
    }
    
    /**
     * Parse input string to Long (for integer currency values)
     * Removes thousand separators (dots) and converts to Long
     */
    fun parseToLong(input: String): Long? {
        if (input.isBlank()) return null
        
        // Remove all dots, commas, spaces (thousand separators)
        val cleaned = input.replace(".", "")
            .replace(",", "")
            .replace(" ", "")
            .replace("VNĐ", "")
            .trim()
            .filter { it.isDigit() }
        
        return cleaned.toLongOrNull()
    }
    
    /**
     * Format Long value for display with thousand separators
     */
    fun formatForDisplay(value: Long): String {
        return if (value == 0L) "" else displayFormatter.format(value)
    }
    
    /**
     * Clean and format input string as user types
     * This method is optimized for real-time input formatting
     * Automatically adds thousand separators (dots) as user types
     * 
     * Examples:
     * Input: "1086700" -> Display: "1.086.700"
     * Input: "10000" -> Display: "10.000"
     * Input: "500000" -> Display: "500.000"
     */
    fun formatInputString(input: String): String {
        if (input.isBlank()) return ""
        
        // Remove all non-digit characters
        val digitsOnly = input.replace(".", "")
            .replace(",", "")
            .replace(" ", "")
            .replace("VNĐ", "")
            .trim()
            .filter { it.isDigit() }
        
        // Format with thousand separators
        return if (digitsOnly.isEmpty()) "" else formatIntegerWithSeparators(digitsOnly)
    }
    
    /**
     * Format integer string with thousand separators (dots)
     * Optimized for performance
     */
    private fun formatIntegerWithSeparators(input: String): String {
        if (input.length < 4) return input
        
        val reversed = input.reversed()
        val formatted = StringBuilder()
        
        for (i in reversed.indices) {
            if (i > 0 && i % 3 == 0) {
                formatted.append('.')
            }
            formatted.append(reversed[i])
        }
        
        return formatted.reverse().toString()
    }
}

/**
 * Visual transformation for currency input fields
 * Automatically formats input with thousand separators as user types
 */
class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val formattedText = CurrencyInputFormatter.formatInputString(originalText)
        
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Count dots before offset in formatted text
                val originalBeforeOffset = originalText.take(offset)
                val dotsInOriginal = originalBeforeOffset.count { it == '.' }
                
                // Calculate position in formatted text
                val cleanOriginal = originalBeforeOffset.replace(".", "").replace(",", "").replace(" ", "")
                val dotsToAdd = (cleanOriginal.length - 1) / 3
                
                return minOf(offset + dotsToAdd - dotsInOriginal, formattedText.length)
            }
            
            override fun transformedToOriginal(offset: Int): Int {
                // Count dots before offset in formatted text
                val formattedBeforeOffset = formattedText.take(offset)
                val dotsCount = formattedBeforeOffset.count { it == '.' }
                
                return minOf(offset - dotsCount, originalText.length)
            }
        }
        
        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}
