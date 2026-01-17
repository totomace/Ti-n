import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import 'package:intl/intl.dart';
import '../models/work_entry.dart';
import '../viewmodels/work_entry_list_viewmodel.dart';
import '../theme/app_colors.dart';

class WorkEntryFormScreen extends StatefulWidget {
  final WorkEntry? entry;

  const WorkEntryFormScreen({Key? key, this.entry}) : super(key: key);

  @override
  State<WorkEntryFormScreen> createState() => _WorkEntryFormScreenState();
}

class _WorkEntryFormScreenState extends State<WorkEntryFormScreen> {
  final _formKey = GlobalKey<FormState>();
  late DateTime _selectedDate;
  late TimeOfDay _startTime;
  late TimeOfDay _endTime;
  late TextEditingController _taskController;
  late TextEditingController _breakController;
  late TextEditingController _salaryController;
  late TextEditingController _paidController;
  late TextEditingController _notesController;

  @override
  void initState() {
    super.initState();
    if (widget.entry != null) {
      _selectedDate = widget.entry!.date;
      _startTime = TimeOfDay.fromDateTime(widget.entry!.startTime);
      _endTime = TimeOfDay.fromDateTime(widget.entry!.endTime);
      _taskController = TextEditingController(text: widget.entry!.task);
      _breakController = TextEditingController(text: widget.entry!.breakMinutes.toString());
      _salaryController = TextEditingController(text: widget.entry!.salary.toString());
      _paidController = TextEditingController(text: widget.entry!.paidAmount.toString());
      _notesController = TextEditingController(text: widget.entry!.notes);
    } else {
      _selectedDate = DateTime.now();
      _startTime = const TimeOfDay(hour: 8, minute: 0);
      _endTime = const TimeOfDay(hour: 17, minute: 0);
      _taskController = TextEditingController();
      _breakController = TextEditingController(text: '60');
      _salaryController = TextEditingController();
      _paidController = TextEditingController(text: '0');
      _notesController = TextEditingController();
    }
  }

  @override
  void dispose() {
    _taskController.dispose();
    _breakController.dispose();
    _salaryController.dispose();
    _paidController.dispose();
    _notesController.dispose();
    super.dispose();
  }

  Future<void> _selectDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: _selectedDate,
      firstDate: DateTime(2020),
      lastDate: DateTime(2030),
      builder: (context, child) {
        return Theme(
          data: Theme.of(context).copyWith(
            colorScheme: const ColorScheme.light(
              primary: AppColors.primaryBlue,
              onPrimary: Colors.white,
            ),
          ),
          child: child!,
        );
      },
    );
    if (picked != null && picked != _selectedDate) {
      setState(() {
        _selectedDate = picked;
      });
    }
  }

  Future<void> _selectTime(BuildContext context, bool isStartTime) async {
    final TimeOfDay? picked = await showTimePicker(
      context: context,
      initialTime: isStartTime ? _startTime : _endTime,
      builder: (context, child) {
        return Theme(
          data: Theme.of(context).copyWith(
            colorScheme: const ColorScheme.light(
              primary: AppColors.primaryBlue,
              onPrimary: Colors.white,
            ),
          ),
          child: child!,
        );
      },
    );
    if (picked != null) {
      setState(() {
        if (isStartTime) {
          _startTime = picked;
        } else {
          _endTime = picked;
        }
      });
    }
  }

  void _saveEntry() {
    if (_formKey.currentState!.validate()) {
      final viewModel = Provider.of<WorkEntryListViewModel>(context, listen: false);
      
      final startDateTime = DateTime(
        _selectedDate.year,
        _selectedDate.month,
        _selectedDate.day,
        _startTime.hour,
        _startTime.minute,
      );
      
      final endDateTime = DateTime(
        _selectedDate.year,
        _selectedDate.month,
        _selectedDate.day,
        _endTime.hour,
        _endTime.minute,
      );

      final entry = WorkEntry(
        id: widget.entry?.id ?? DateTime.now().millisecondsSinceEpoch,
        date: _selectedDate,
        startTime: startDateTime,
        endTime: endDateTime,
        breakMinutes: int.parse(_breakController.text),
        task: _taskController.text,
        salary: int.parse(_salaryController.text),
        paidAmount: int.parse(_paidController.text),
        notes: _notesController.text,
      );

      if (widget.entry != null) {
        viewModel.updateEntry(entry);
      } else {
        viewModel.addEntry(entry);
      }

      Navigator.pop(context);
    }
  }

  @override
  Widget build(BuildContext context) {
    final isEdit = widget.entry != null;

    return Scaffold(
      backgroundColor: AppColors.backgroundLight,
      appBar: AppBar(
        backgroundColor: AppColors.primaryBlue,
        title: Row(
          children: [
            Icon(isEdit ? Icons.edit : Icons.add, color: Colors.white),
            const SizedBox(width: 8),
            Text(
              isEdit ? 'Sửa công việc' : 'Thêm công việc',
              style: const TextStyle(
                fontWeight: FontWeight.bold,
                color: Colors.white,
              ),
            ),
          ],
        ),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Navigator.pop(context),
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.check, color: Colors.white),
            onPressed: _saveEntry,
          ),
        ],
      ),
      body: Form(
        key: _formKey,
        child: ListView(
          padding: const EdgeInsets.all(16.0),
          children: [
            // Date picker
            Card(
              elevation: 2,
              child: ListTile(
                leading: const Icon(Icons.calendar_today, color: AppColors.primaryBlue),
                title: const Text('Ngày làm việc', style: TextStyle(fontWeight: FontWeight.w600)),
                subtitle: Text(DateFormat('dd/MM/yyyy - EEEE', 'vi_VN').format(_selectedDate)),
                trailing: const Icon(Icons.arrow_forward_ios, size: 16),
                onTap: () => _selectDate(context),
              ),
            ),
            const SizedBox(height: 12),

            // Time pickers
            Row(
              children: [
                Expanded(
                  child: Card(
                    elevation: 2,
                    child: ListTile(
                      leading: const Icon(Icons.access_time, color: AppColors.accentGreen),
                      title: const Text('Giờ bắt đầu', style: TextStyle(fontSize: 13)),
                      subtitle: Text(_startTime.format(context), style: const TextStyle(fontWeight: FontWeight.bold)),
                      onTap: () => _selectTime(context, true),
                    ),
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Card(
                    elevation: 2,
                    child: ListTile(
                      leading: const Icon(Icons.access_time_filled, color: AppColors.accentOrange),
                      title: const Text('Giờ kết thúc', style: TextStyle(fontSize: 13)),
                      subtitle: Text(_endTime.format(context), style: const TextStyle(fontWeight: FontWeight.bold)),
                      onTap: () => _selectTime(context, false),
                    ),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 12),

            // Break minutes
            Card(
              elevation: 2,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: TextFormField(
                  controller: _breakController,
                  decoration: const InputDecoration(
                    labelText: 'Thời gian nghỉ (phút)',
                    prefixIcon: Icon(Icons.coffee, color: AppColors.warningOrange),
                    border: OutlineInputBorder(),
                  ),
                  keyboardType: TextInputType.number,
                  inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                  validator: (value) {
                    if (value == null || value.isEmpty) return 'Vui lòng nhập thời gian nghỉ';
                    return null;
                  },
                ),
              ),
            ),
            const SizedBox(height: 12),

            // Task name
            Card(
              elevation: 2,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: TextFormField(
                  controller: _taskController,
                  decoration: const InputDecoration(
                    labelText: 'Tên công việc',
                    prefixIcon: Icon(Icons.work, color: AppColors.primaryBlue),
                    border: OutlineInputBorder(),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) return 'Vui lòng nhập tên công việc';
                    return null;
                  },
                ),
              ),
            ),
            const SizedBox(height: 12),

            // Salary
            Card(
              elevation: 2,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: TextFormField(
                  controller: _salaryController,
                  decoration: const InputDecoration(
                    labelText: 'Tiền lương (VNĐ)',
                    prefixIcon: Icon(Icons.attach_money, color: AppColors.successGreen),
                    border: OutlineInputBorder(),
                  ),
                  keyboardType: TextInputType.number,
                  inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                  validator: (value) {
                    if (value == null || value.isEmpty) return 'Vui lòng nhập tiền lương';
                    return null;
                  },
                ),
              ),
            ),
            const SizedBox(height: 12),

            // Paid amount
            Card(
              elevation: 2,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: TextFormField(
                  controller: _paidController,
                  decoration: const InputDecoration(
                    labelText: 'Đã trả (VNĐ)',
                    prefixIcon: Icon(Icons.payment, color: AppColors.infoBlue),
                    border: OutlineInputBorder(),
                  ),
                  keyboardType: TextInputType.number,
                  inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                  validator: (value) {
                    if (value == null || value.isEmpty) return 'Vui lòng nhập số tiền đã trả';
                    return null;
                  },
                ),
              ),
            ),
            const SizedBox(height: 12),

            // Notes
            Card(
              elevation: 2,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: TextFormField(
                  controller: _notesController,
                  decoration: const InputDecoration(
                    labelText: 'Ghi chú (tùy chọn)',
                    prefixIcon: Icon(Icons.notes, color: AppColors.textSecondary),
                    border: OutlineInputBorder(),
                  ),
                  maxLines: 3,
                ),
              ),
            ),
            const SizedBox(height: 24),

            // Save button
            ElevatedButton(
              onPressed: _saveEntry,
              style: ElevatedButton.styleFrom(
                backgroundColor: AppColors.primaryBlue,
                padding: const EdgeInsets.symmetric(vertical: 16),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Icon(Icons.save, color: Colors.white),
                  const SizedBox(width: 8),
                  Text(
                    isEdit ? 'Cập nhật' : 'Lưu công việc',
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                      color: Colors.white,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
