enum FilterType {
  all,
  paid,
  unpaid,
  partial,
}

extension FilterTypeExtension on FilterType {
  String get displayName {
    switch (this) {
      case FilterType.all:
        return 'Tất cả';
      case FilterType.paid:
        return 'Đã trả';
      case FilterType.unpaid:
        return 'Chưa trả';
      case FilterType.partial:
        return '1 phần';
    }
  }
}
