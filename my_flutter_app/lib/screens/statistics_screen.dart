import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:intl/intl.dart';
import '../viewmodels/work_entry_list_viewmodel.dart';
import '../theme/app_colors.dart';

class StatisticsScreen extends StatelessWidget {
  const StatisticsScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.backgroundLight,
      appBar: AppBar(
        backgroundColor: AppColors.primaryBlue,
        title: const Text(
          'Thống kê',
          style: TextStyle(fontWeight: FontWeight.bold, color: Colors.white),
        ),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: Consumer<WorkEntryListViewModel>(
        builder: (context, viewModel, child) {
          final entries = viewModel.entries;
          final currencyFormat = NumberFormat('#,###', 'vi_VN');
          
          // Tính toán thống kê
          final totalEntries = entries.length;
          final totalSalary = entries.fold<int>(0, (sum, entry) => sum + entry.salary);
          final totalPaid = entries.fold<int>(0, (sum, entry) => sum + entry.paidAmount);
          final totalDebt = totalSalary - totalPaid;
          
          final paidEntries = entries.where((e) => e.paidAmount >= e.salary).length;
          final unpaidEntries = entries.where((e) => e.paidAmount == 0).length;
          final partialEntries = entries.where((e) => e.paidAmount > 0 && e.paidAmount < e.salary).length;

          return ListView(
            padding: const EdgeInsets.all(16),
            children: [
              // Tổng quan
              _buildStatCard(
                title: 'TỔNG QUAN',
                icon: Icons.analytics,
                iconColor: AppColors.primaryYellow,
                children: [
                  _buildStatRow('Tổng số bản ghi', '$totalEntries', Icons.list),
                  _buildStatRow('Đã trả đầy đủ', '$paidEntries', Icons.check_circle, color: AppColors.successGreen),
                  _buildStatRow('Chưa trả', '$unpaidEntries', Icons.cancel, color: AppColors.errorRed),
                  _buildStatRow('Trả một phần', '$partialEntries', Icons.pending, color: AppColors.warningOrange),
                ],
              ),
              const SizedBox(height: 16),
              
              // Thống kê tài chính
              _buildStatCard(
                title: 'TÀI CHÍNH',
                icon: Icons.attach_money,
                iconColor: AppColors.successGreen,
                children: [
                  _buildStatRow(
                    'Tổng lương',
                    '${currencyFormat.format(totalSalary)} VNĐ',
                    Icons.money,
                    color: AppColors.accentGreen,
                  ),
                  _buildStatRow(
                    'Đã nhận',
                    '${currencyFormat.format(totalPaid)} VNĐ',
                    Icons.account_balance_wallet,
                    color: AppColors.successGreen,
                  ),
                  _buildStatRow(
                    'Còn nợ',
                    '${currencyFormat.format(totalDebt)} VNĐ',
                    Icons.warning,
                    color: AppColors.errorRed,
                  ),
                ],
              ),
              const SizedBox(height: 16),
              
              // Thời gian làm việc
              if (entries.isNotEmpty) ...[
                _buildStatCard(
                  title: 'THỜI GIAN',
                  icon: Icons.schedule,
                  iconColor: AppColors.infoBlue,
                  children: [
                    _buildStatRow(
                      'Ngày đầu tiên',
                      DateFormat('dd/MM/yyyy', 'vi_VN').format(
                        entries.map((e) => e.date).reduce((a, b) => a.isBefore(b) ? a : b),
                      ),
                      Icons.calendar_today,
                    ),
                    _buildStatRow(
                      'Ngày gần nhất',
                      DateFormat('dd/MM/yyyy', 'vi_VN').format(
                        entries.map((e) => e.date).reduce((a, b) => a.isAfter(b) ? a : b),
                      ),
                      Icons.event,
                    ),
                    _buildStatRow(
                      'Tổng giờ làm việc',
                      '${entries.fold<double>(0, (sum, e) => sum + e.totalHours).toStringAsFixed(1)} giờ',
                      Icons.timer,
                    ),
                  ],
                ),
              ],
            ],
          );
        },
      ),
    );
  }

  Widget _buildStatCard({
    required String title,
    required IconData icon,
    required Color iconColor,
    required List<Widget> children,
  }) {
    return Card(
      elevation: 4,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Icon(icon, color: iconColor, size: 28),
                const SizedBox(width: 12),
                Text(
                  title,
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                    color: AppColors.textPrimary,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 16),
            const Divider(),
            ...children,
          ],
        ),
      ),
    );
  }

  Widget _buildStatRow(String label, String value, IconData icon, {Color? color}) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: Row(
        children: [
          Icon(icon, size: 20, color: color ?? AppColors.textSecondary),
          const SizedBox(width: 12),
          Expanded(
            child: Text(
              label,
              style: const TextStyle(fontSize: 15, color: AppColors.textSecondary),
            ),
          ),
          Text(
            value,
            style: TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.bold,
              color: color ?? AppColors.textPrimary,
            ),
          ),
        ],
      ),
    );
  }
}
