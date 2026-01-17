class WorkEntry {
  final int id;
  final DateTime date;
  final DateTime startTime;
  final DateTime endTime;
  final int breakMinutes;
  final String task;
  final int salary;
  final bool isPaid;
  final int paidAmount;
  final String notes;

  WorkEntry({
    required this.id,
    required this.date,
    required this.startTime,
    required this.endTime,
    required this.breakMinutes,
    required this.task,
    required this.salary,
    this.isPaid = false,
    this.paidAmount = 0,
    this.notes = '',
  });

  // Tính tổng thời gian làm việc (giờ)
  double get totalHours {
    final duration = endTime.difference(startTime);
    final minutes = duration.inMinutes - breakMinutes;
    return minutes / 60.0;
  }

  // Kiểm tra trạng thái thanh toán
  PaymentStatus get paymentStatus {
    if (paidAmount == 0) return PaymentStatus.unpaid;
    if (paidAmount >= salary) return PaymentStatus.paid;
    return PaymentStatus.partial;
  }

  WorkEntry copyWith({
    int? id,
    DateTime? date,
    DateTime? startTime,
    DateTime? endTime,
    int? breakMinutes,
    String? task,
    int? salary,
    bool? isPaid,
    int? paidAmount,
    String? notes,
  }) {
    return WorkEntry(
      id: id ?? this.id,
      date: date ?? this.date,
      startTime: startTime ?? this.startTime,
      endTime: endTime ?? this.endTime,
      breakMinutes: breakMinutes ?? this.breakMinutes,
      task: task ?? this.task,
      salary: salary ?? this.salary,
      isPaid: isPaid ?? this.isPaid,
      paidAmount: paidAmount ?? this.paidAmount,
      notes: notes ?? this.notes,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'date': date.toIso8601String(),
      'startTime': startTime.toIso8601String(),
      'endTime': endTime.toIso8601String(),
      'breakMinutes': breakMinutes,
      'task': task,
      'salary': salary,
      'isPaid': isPaid ? 1 : 0,
      'paidAmount': paidAmount,
      'notes': notes,
    };
  }

  factory WorkEntry.fromMap(Map<String, dynamic> map) {
    return WorkEntry(
      id: map['id'] ?? 0,
      date: DateTime.parse(map['date']),
      startTime: DateTime.parse(map['startTime']),
      endTime: DateTime.parse(map['endTime']),
      breakMinutes: map['breakMinutes'] ?? 0,
      task: map['task'] ?? '',
      salary: map['salary'] ?? 0,
      isPaid: map['isPaid'] == 1,
      paidAmount: map['paidAmount'] ?? 0,
      notes: map['notes'] ?? '',
    );
  }
}

enum PaymentStatus {
  paid,
  unpaid,
  partial,
}
