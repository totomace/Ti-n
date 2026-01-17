import 'package:flutter/material.dart';
import '../theme/app_colors.dart';

class SettingsScreen extends StatefulWidget {
  const SettingsScreen({Key? key}) : super(key: key);

  @override
  State<SettingsScreen> createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  bool _notificationsEnabled = true;
  bool _darkModeEnabled = false;
  String _currency = 'VNĐ';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.backgroundLight,
      appBar: AppBar(
        backgroundColor: AppColors.primaryBlue,
        title: const Text(
          'Cài đặt',
          style: TextStyle(fontWeight: FontWeight.bold, color: Colors.white),
        ),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: ListView(
        children: [
          // Thông tin ứng dụng
          Container(
            padding: const EdgeInsets.all(24),
            decoration: BoxDecoration(
              color: AppColors.primaryYellow.withOpacity(0.1),
            ),
            child: Column(
              children: [
                Icon(
                  Icons.work,
                  size: 80,
                  color: AppColors.primaryYellow,
                ),
                const SizedBox(height: 16),
                const Text(
                  'Lịch sử làm việc',
                  style: TextStyle(
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                    color: AppColors.textPrimary,
                  ),
                ),
                const SizedBox(height: 8),
                Text(
                  'Version 1.0.0',
                  style: TextStyle(
                    fontSize: 14,
                    color: AppColors.textSecondary,
                  ),
                ),
              ],
            ),
          ),
          
          // Cài đặt chung
          _buildSectionTitle('CÀI ĐẶT CHUNG'),
          Card(
            margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            child: Column(
              children: [
                SwitchListTile(
                  secondary: const Icon(Icons.notifications, color: AppColors.primaryYellow),
                  title: const Text('Thông báo'),
                  subtitle: const Text('Bật/tắt thông báo'),
                  value: _notificationsEnabled,
                  onChanged: (value) {
                    setState(() {
                      _notificationsEnabled = value;
                    });
                  },
                  activeColor: AppColors.primaryYellow,
                ),
                const Divider(height: 1),
                SwitchListTile(
                  secondary: const Icon(Icons.dark_mode, color: AppColors.primaryYellow),
                  title: const Text('Chế độ tối'),
                  subtitle: const Text('Giao diện tối'),
                  value: _darkModeEnabled,
                  onChanged: (value) {
                    setState(() {
                      _darkModeEnabled = value;
                    });
                  },
                  activeColor: AppColors.primaryYellow,
                ),
                const Divider(height: 1),
                ListTile(
                  leading: const Icon(Icons.monetization_on, color: AppColors.primaryYellow),
                  title: const Text('Đơn vị tiền tệ'),
                  subtitle: Text(_currency),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () {
                    showDialog(
                      context: context,
                      builder: (context) => AlertDialog(
                        title: const Text('Chọn đơn vị tiền tệ'),
                        content: Column(
                          mainAxisSize: MainAxisSize.min,
                          children: ['VNĐ', 'USD', 'EUR'].map((currency) {
                            return RadioListTile<String>(
                              title: Text(currency),
                              value: currency,
                              groupValue: _currency,
                              onChanged: (value) {
                                setState(() {
                                  _currency = value!;
                                });
                                Navigator.pop(context);
                              },
                              activeColor: AppColors.primaryYellow,
                            );
                          }).toList(),
                        ),
                      ),
                    );
                  },
                ),
              ],
            ),
          ),
          
          // Dữ liệu
          _buildSectionTitle('DỮ LIỆU'),
          Card(
            margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            child: Column(
              children: [
                ListTile(
                  leading: const Icon(Icons.backup, color: AppColors.infoBlue),
                  title: const Text('Sao lưu dữ liệu'),
                  subtitle: const Text('Xuất dữ liệu ra file'),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Chức năng đang phát triển')),
                    );
                  },
                ),
                const Divider(height: 1),
                ListTile(
                  leading: const Icon(Icons.restore, color: AppColors.successGreen),
                  title: const Text('Khôi phục dữ liệu'),
                  subtitle: const Text('Nhập dữ liệu từ file'),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Chức năng đang phát triển')),
                    );
                  },
                ),
                const Divider(height: 1),
                ListTile(
                  leading: const Icon(Icons.delete_forever, color: AppColors.errorRed),
                  title: const Text('Xóa toàn bộ dữ liệu'),
                  subtitle: const Text('Cẩn thận! Không thể hoàn tác'),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () {
                    showDialog(
                      context: context,
                      builder: (context) => AlertDialog(
                        title: const Text('Xác nhận xóa'),
                        content: const Text(
                          'Bạn có chắc muốn xóa toàn bộ dữ liệu? Hành động này không thể hoàn tác!',
                        ),
                        actions: [
                          TextButton(
                            onPressed: () => Navigator.pop(context),
                            child: const Text('Hủy'),
                          ),
                          ElevatedButton(
                            onPressed: () {
                              Navigator.pop(context);
                              ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(content: Text('Chức năng đang phát triển')),
                              );
                            },
                            style: ElevatedButton.styleFrom(
                              backgroundColor: AppColors.errorRed,
                            ),
                            child: const Text('Xóa', style: TextStyle(color: Colors.white)),
                          ),
                        ],
                      ),
                    );
                  },
                ),
              ],
            ),
          ),
          
          // Thông tin
          _buildSectionTitle('THÔNG TIN'),
          Card(
            margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            child: Column(
              children: [
                ListTile(
                  leading: const Icon(Icons.info, color: AppColors.infoBlue),
                  title: const Text('Giới thiệu'),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () {
                    showAboutDialog(
                      context: context,
                      applicationName: 'Lịch sử làm việc',
                      applicationVersion: '1.0.0',
                      applicationIcon: const Icon(
                        Icons.work,
                        size: 48,
                        color: AppColors.primaryYellow,
                      ),
                      children: [
                        const Text(
                          'Ứng dụng quản lý lịch sử làm việc và thu nhập',
                        ),
                      ],
                    );
                  },
                ),
                const Divider(height: 1),
                ListTile(
                  leading: const Icon(Icons.help, color: AppColors.warningOrange),
                  title: const Text('Hướng dẫn sử dụng'),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Chức năng đang phát triển')),
                    );
                  },
                ),
                const Divider(height: 1),
                ListTile(
                  leading: const Icon(Icons.contact_support, color: AppColors.successGreen),
                  title: const Text('Liên hệ hỗ trợ'),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Email: support@example.com')),
                    );
                  },
                ),
              ],
            ),
          ),
          
          const SizedBox(height: 32),
        ],
      ),
    );
  }

  Widget _buildSectionTitle(String title) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(16, 24, 16, 8),
      child: Text(
        title,
        style: const TextStyle(
          fontSize: 14,
          fontWeight: FontWeight.bold,
          color: AppColors.textSecondary,
          letterSpacing: 1.2,
        ),
      ),
    );
  }
}
