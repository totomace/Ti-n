enum SortType {
  dateDesc,
  dateAsc,
  salaryDesc,
  salaryAsc,
  nameAsc,
  nameDesc,
}

extension SortTypeExtension on SortType {
  String get displayName {
    switch (this) {
      case SortType.dateDesc:
        return 'Ngày mới nhất';
      case SortType.dateAsc:
        return 'Ngày cũ nhất';
      case SortType.salaryDesc:
        return 'Lương cao nhất';
      case SortType.salaryAsc:
        return 'Lương thấp nhất';
      case SortType.nameAsc:
        return 'Tên A-Z';
      case SortType.nameDesc:
        return 'Tên Z-A';
    }
  }
}
