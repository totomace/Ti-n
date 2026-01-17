import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../models/work_entry.dart';
import '../theme/app_colors.dart';

class WorkEntryCard extends StatefulWidget {
  final WorkEntry entry;
  final VoidCallback onEdit;
  final VoidCallback onDelete;

  const WorkEntryCard({
    Key? key,
    required this.entry,
    required this.onEdit,
    required this.onDelete,
  }) : super(key: key);

  @override
  State<WorkEntryCard> createState() => _WorkEntryCardState();
}

class _WorkEntryCardState extends State<WorkEntryCard>
    with SingleTickerProviderStateMixin {
  bool showDeleteDialog = false;
  late AnimationController _controller;
  late Animation<double> _scaleAnimation;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: const Duration(milliseconds: 500),
      vsync: this,
    );
    _scaleAnimation = Tween<double>(begin: 0.8, end: 1.0).animate(
      CurvedAnimation(
        parent: _controller,
        curve: Curves.easeOut,
      ),
    );
    _controller.forward();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  void _showDeleteDialog() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          backgroundColor: const Color(0xFFFFFBF0),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16),
          ),
          title: Row(
            children: [
              Icon(
                Icons.delete,
                color: AppColors.errorRed,
                size: 24,
              ),
              const SizedBox(width: 8),
              const Text(
                'Xác nhận xóa',
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  color: AppColors.errorRed,
                ),
              ),
            ],
          ),
          content: const Text(
            'Bạn có chắc muốn xóa bản ghi này không?',
            style: TextStyle(color: Color(0xFF92400E)),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              style: TextButton.styleFrom(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
              ),
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(
                    Icons.close,
                    size: 18,
                    color: Color(0xFF92400E),
                  ),
                  const SizedBox(width: 4),
                  const Text(
                    'Hủy',
                    style: TextStyle(color: Color(0xFF92400E)),
                  ),
                ],
              ),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.of(context).pop();
                widget.onDelete();
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: AppColors.errorRed,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
              ),
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: const [
                  Icon(Icons.delete, size: 18),
                  SizedBox(width: 4),
                  Text(
                    'Xóa',
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                ],
              ),
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    final dateFormat = DateFormat('dd/MM/yyyy - EEEE', 'vi_VN');
    final timeFormat = DateFormat('HH:mm');
    final currencyFormat = NumberFormat('#,###', 'vi_VN');

    return ScaleTransition(
      scale: _scaleAnimation,
      child: Card(
        elevation: 4,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
        ),
        child: InkWell(
          onTap: widget.onEdit,
          borderRadius: BorderRadius.circular(12),
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Header: Date with Delete button
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Expanded(
                      child: Text(
                        dateFormat.format(widget.entry.date),
                        style: const TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 16,
                          color: AppColors.primaryBlue,
                        ),
                      ),
                    ),
                    IconButton(
                      icon: const Icon(
                        Icons.delete,
                        color: Colors.red,
                      ),
                      onPressed: _showDeleteDialog,
                    ),
                  ],
                ),
                const SizedBox(height: 8),
                const Divider(color: Color(0xFFE5E7EB)),
                const SizedBox(height: 8),
                // Time info
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    _InfoItem(
                      label: 'Giờ bắt đầu',
                      value: timeFormat.format(widget.entry.startTime),
                    ),
                    _InfoItem(
                      label: 'Giờ kết thúc',
                      value: timeFormat.format(widget.entry.endTime),
                    ),
                  ],
                ),
                const SizedBox(height: 8),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    _InfoItem(
                      label: 'Nghỉ',
                      value: '${widget.entry.breakMinutes} phút',
                    ),
                    Expanded(
                      child: _InfoItem(
                        label: 'Công việc',
                        value: widget.entry.task,
                      ),
                    ),
                  ],
                ),
                // Notes section
                if (widget.entry.notes.isNotEmpty) ...[
                  const SizedBox(height: 8),
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(12),
                    decoration: BoxDecoration(
                      color: const Color(0xFFFFF9E6),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text(
                          'Ghi chú:',
                          style: TextStyle(
                            fontSize: 12,
                            fontWeight: FontWeight.w600,
                            color: Color(0xFFD97706),
                          ),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          widget.entry.notes,
                          style: const TextStyle(
                            fontSize: 13,
                            color: Color(0xFF92400E),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
                const SizedBox(height: 12),
                // Salary - highlighted
                Container(
                  width: double.infinity,
                  padding: const EdgeInsets.all(12),
                  decoration: BoxDecoration(
                    color: AppColors.accentGreen.withOpacity(0.1),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text(
                              'Tiền lương:',
                              style: TextStyle(
                                fontWeight: FontWeight.w600,
                                fontSize: 14,
                              ),
                            ),
                            if (widget.entry.paidAmount > 0) ...[
                              const SizedBox(height: 4),
                              Text(
                                'Đã trả: ${currencyFormat.format(widget.entry.paidAmount)} VNĐ',
                                style: const TextStyle(
                                  fontSize: 13,
                                  color: AppColors.successGreen,
                                ),
                              ),
                              if (widget.entry.paidAmount < widget.entry.salary)
                                Text(
                                  'Còn nợ: ${currencyFormat.format(widget.entry.salary - widget.entry.paidAmount)} VNĐ',
                                  style: const TextStyle(
                                    fontSize: 13,
                                    color: AppColors.errorRed,
                                    fontWeight: FontWeight.w500,
                                  ),
                                ),
                            ],
                          ],
                        ),
                      ),
                      Text(
                        '${currencyFormat.format(widget.entry.salary)} VNĐ',
                        style: const TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 18,
                          color: Color(0xFF15803D),
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _InfoItem extends StatelessWidget {
  final String label;
  final String value;

  const _InfoItem({
    Key? key,
    required this.label,
    required this.value,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          label,
          style: const TextStyle(
            fontSize: 12,
            color: AppColors.textSecondary,
          ),
        ),
        Text(
          value,
          style: const TextStyle(
            fontSize: 14,
            fontWeight: FontWeight.w500,
          ),
        ),
      ],
    );
  }
}
