import 'package:flutter/material.dart';
import '../models/work_entry.dart';
import '../enums/filter_type.dart';
import '../enums/sort_type.dart';

class WorkEntryListViewModel extends ChangeNotifier {
  List<WorkEntry> _allEntries = [];
  List<WorkEntry> _filteredEntries = [];
  String _searchQuery = '';
  FilterType _filterType = FilterType.all;
  SortType _sortType = SortType.dateDesc;

  List<WorkEntry> get entries => _filteredEntries;
  String get searchQuery => _searchQuery;
  FilterType get filterType => _filterType;
  SortType get sortType => _sortType;

  WorkEntryListViewModel() {
    _loadSampleData();
  }

  void _loadSampleData() {
    // Sample data để test
    _allEntries = [
      WorkEntry(
        id: 1,
        date: DateTime.now().subtract(const Duration(days: 1)),
        startTime: DateTime.now().subtract(const Duration(days: 1, hours: 8)),
        endTime: DateTime.now().subtract(const Duration(days: 1, hours: 0)),
        breakMinutes: 60,
        task: 'Làm việc tại văn phòng',
        salary: 500000,
        paidAmount: 500000,
        notes: 'Hoàn thành tốt',
      ),
      WorkEntry(
        id: 2,
        date: DateTime.now().subtract(const Duration(days: 2)),
        startTime: DateTime.now().subtract(const Duration(days: 2, hours: 8)),
        endTime: DateTime.now().subtract(const Duration(days: 2, hours: 0)),
        breakMinutes: 60,
        task: 'Dự án ABC',
        salary: 600000,
        paidAmount: 0,
        notes: '',
      ),
      WorkEntry(
        id: 3,
        date: DateTime.now().subtract(const Duration(days: 3)),
        startTime: DateTime.now().subtract(const Duration(days: 3, hours: 8)),
        endTime: DateTime.now().subtract(const Duration(days: 3, hours: 0)),
        breakMinutes: 60,
        task: 'Họp khách hàng',
        salary: 400000,
        paidAmount: 200000,
        notes: 'Còn nợ 200k',
      ),
    ];
    _applyFilters();
  }

  void loadEntries() {
    // TODO: Load from database
    _loadSampleData();
  }

  void onSearchQueryChange(String query) {
    _searchQuery = query;
    _applyFilters();
  }

  void onFilterChange(FilterType filter) {
    _filterType = filter;
    _applyFilters();
  }

  void onSortChange(SortType sort) {
    _sortType = sort;
    _applyFilters();
  }

  void _applyFilters() {
    var filtered = List<WorkEntry>.from(_allEntries);

    // Apply search
    if (_searchQuery.isNotEmpty) {
      filtered = filtered.where((entry) {
        return entry.task.toLowerCase().contains(_searchQuery.toLowerCase()) ||
            entry.notes.toLowerCase().contains(_searchQuery.toLowerCase());
      }).toList();
    }

    // Apply filter type
    filtered = switch (_filterType) {
      FilterType.all => filtered,
      FilterType.paid => filtered.where((e) => e.paidAmount >= e.salary).toList(),
      FilterType.unpaid => filtered.where((e) => e.paidAmount == 0).toList(),
      FilterType.partial =>
        filtered.where((e) => e.paidAmount > 0 && e.paidAmount < e.salary).toList(),
    };

    // Apply sorting
    filtered = switch (_sortType) {
      SortType.dateDesc => filtered..sort((a, b) => b.date.compareTo(a.date)),
      SortType.dateAsc => filtered..sort((a, b) => a.date.compareTo(b.date)),
      SortType.salaryDesc => filtered..sort((a, b) => b.salary.compareTo(a.salary)),
      SortType.salaryAsc => filtered..sort((a, b) => a.salary.compareTo(b.salary)),
      SortType.nameAsc => filtered..sort((a, b) => a.task.compareTo(b.task)),
      SortType.nameDesc => filtered..sort((a, b) => b.task.compareTo(a.task)),
    };

    _filteredEntries = filtered;
    notifyListeners();
  }

  void deleteEntry(int id) {
    _allEntries.removeWhere((entry) => entry.id == id);
    _applyFilters();
    notifyListeners();
  }

  void addEntry(WorkEntry entry) {
    _allEntries.add(entry);
    _applyFilters();
    notifyListeners();
  }

  void updateEntry(WorkEntry entry) {
    final index = _allEntries.indexWhere((e) => e.id == entry.id);
    if (index != -1) {
      _allEntries[index] = entry;
      _applyFilters();
      notifyListeners();
    }
  }
}
