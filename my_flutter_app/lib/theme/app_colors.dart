import 'package:flutter/material.dart';

class AppColors {
  // Custom colors for the app - Vibrant Yellow/Gold theme
  static const Color primaryYellow = Color(0xFFFBBF24); // Vàng chủ đạo
  static const Color primaryDark = Color(0xFFF59E0B); // Vàng đậm
  static const Color accentOrange = Color(0xFFFF6B35); // Cam nổi bật
  static const Color accentGold = Color(0xFFD97706); // Vàng kim
  static const Color accentCyan = Color(0xFF06B6D4); // Xanh cyan
  static const Color backgroundLight = Color(0xFFFFFBEB); // Nền vàng nhạt
  static const Color cardBackground = Color(0xFFFFFFFF);
  static const Color textPrimary = Color(0xFF1F2937);
  static const Color textSecondary = Color(0xFF6B7280);

  // Aliases for compatibility
  static const Color primaryBlue = primaryYellow;
  static const Color accentGreen = accentGold;

  // Filter chip colors
  static const Color filterAll = Color(0xFFFBBF24);
  static const Color filterPaid = Color(0xFF15803D);
  static const Color filterUnpaid = Color(0xFFDC2626);
  static const Color filterPartial = Color(0xFFF59E0B);

  // Status colors
  static const Color successGreen = Color(0xFF15803D);
  static const Color errorRed = Color(0xFFDC2626);
  static const Color warningOrange = Color(0xFFF59E0B);
  static const Color infoBlue = Color(0xFF06B6D4);
}
