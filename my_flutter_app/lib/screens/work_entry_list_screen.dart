import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../viewmodels/work_entry_list_viewmodel.dart';
import '../enums/filter_type.dart';
import '../enums/sort_type.dart';
import '../theme/app_colors.dart';
import '../widgets/animated_filter_chip.dart';
import '../widgets/work_entry_card.dart';

class WorkEntryListScreen extends StatefulWidget {
  final VoidCallback onAddClick;
  final Function(int) onEditClick;
  final VoidCallback onStatisticsClick;
  final VoidCallback onSettingsClick;
  final VoidCallback onNotesClick;

  const WorkEntryListScreen({
    Key? key,
    required this.onAddClick,
    required this.onEditClick,
    required this.onStatisticsClick,
    required this.onSettingsClick,
    required this.onNotesClick,
  }) : super(key: key);

  @override
  State<WorkEntryListScreen> createState() => _WorkEntryListScreenState();
}

class _WorkEntryListScreenState extends State<WorkEntryListScreen>
    with SingleTickerProviderStateMixin {
  bool showSearch = false;
  bool showSortMenu = false;
  late AnimationController _fabController;
  late Animation<double> _fabAnimation;

  @override
  void initState() {
    super.initState();
    _fabController = AnimationController(
      duration: const Duration(milliseconds: 800),
      vsync: this,
    );
    _fabAnimation = CurvedAnimation(
      parent: _fabController,
      curve: Curves.elasticOut,
    );
    _fabController.forward();
  }

  @override
  void dispose() {
    _fabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<WorkEntryListViewModel>(
      builder: (context, viewModel, child) {
        return Scaffold(
          backgroundColor: AppColors.backgroundLight,
          appBar: AppBar(
            backgroundColor: AppColors.primaryBlue,
            elevation: 0,
            title: AnimatedSwitcher(
              duration: const Duration(milliseconds: 300),
              child: showSearch
                  ? TextField(
                      key: const ValueKey('search'),
                      onChanged: viewModel.onSearchQueryChange,
                      autofocus: true,
                      style: const TextStyle(color: Colors.white),
                      decoration: InputDecoration(
                        hintText: 'Tìm kiếm công việc...',
                        hintStyle: TextStyle(
                          color: Colors.white.withOpacity(0.7),
                        ),
                        border: InputBorder.none,
                      ),
                    )
                  : Row(
                      key: const ValueKey('title'),
                      children: const [
                        Icon(Icons.work, color: Colors.white, size: 24),
                        SizedBox(width: 8),
                        Text(
                          'Lịch sử làm việc',
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            color: Colors.white,
                          ),
                        ),
                      ],
                    ),
            ),
            actions: [
              IconButton(
                icon: Icon(showSearch ? Icons.close : Icons.search),
                color: Colors.white,
                onPressed: () {
                  setState(() {
                    showSearch = !showSearch;
                    if (!showSearch) {
                      viewModel.onSearchQueryChange('');
                    }
                  });
                },
              ),
              PopupMenuButton<SortType>(
                icon: const Icon(Icons.sort, color: Colors.white),
                onSelected: viewModel.onSortChange,
                itemBuilder: (context) => SortType.values.map((sortType) {
                  final isSelected = viewModel.sortType == sortType;
                  return PopupMenuItem<SortType>(
                    value: sortType,
                    child: Row(
                      children: [
                        if (isSelected)
                          const Icon(Icons.check, color: AppColors.primaryYellow),
                        if (isSelected) const SizedBox(width: 8),
                        Text(
                          sortType.displayName,
                          style: TextStyle(
                            color: isSelected
                                ? AppColors.primaryYellow
                                : Colors.black,
                          ),
                        ),
                      ],
                    ),
                  );
                }).toList(),
              ),
              IconButton(
                icon: const Icon(Icons.notes, color: Colors.white),
                onPressed: widget.onNotesClick,
              ),
              IconButton(
                icon: const Icon(Icons.bar_chart, color: Colors.white),
                onPressed: widget.onStatisticsClick,
              ),
              IconButton(
                icon: const Icon(Icons.settings, color: Colors.white),
                onPressed: widget.onSettingsClick,
              ),
            ],
          ),
          body: Column(
            children: [
              // Filter chips
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                child: Row(
                  children: [
                    AnimatedFilterChip(
                      selected: viewModel.filterType == FilterType.all,
                      onClick: () => viewModel.onFilterChange(FilterType.all),
                      label: 'Tất cả',
                      selectedColor: AppColors.filterAll,
                    ),
                    const SizedBox(width: 8),
                    AnimatedFilterChip(
                      selected: viewModel.filterType == FilterType.paid,
                      onClick: () => viewModel.onFilterChange(FilterType.paid),
                      label: 'Đã trả',
                      selectedColor: AppColors.filterPaid,
                    ),
                    const SizedBox(width: 8),
                    AnimatedFilterChip(
                      selected: viewModel.filterType == FilterType.unpaid,
                      onClick: () => viewModel.onFilterChange(FilterType.unpaid),
                      label: 'Chưa trả',
                      selectedColor: AppColors.filterUnpaid,
                    ),
                    const SizedBox(width: 8),
                    AnimatedFilterChip(
                      selected: viewModel.filterType == FilterType.partial,
                      onClick: () => viewModel.onFilterChange(FilterType.partial),
                      label: '1 phần',
                      selectedColor: AppColors.filterPartial,
                    ),
                  ],
                ),
              ),
              // List or empty state
              Expanded(
                child: viewModel.entries.isEmpty
                    ? Center(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Icon(
                              Icons.work,
                              size: 64,
                              color: Colors.grey.withOpacity(0.5),
                            ),
                            const SizedBox(height: 16),
                            Text(
                              'Không tìm thấy kết quả!',
                              style: TextStyle(
                                color: AppColors.textSecondary,
                                fontSize: 16,
                              ),
                            ),
                          ],
                        ),
                      )
                    : ListView.builder(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 16,
                          vertical: 8,
                        ),
                        itemCount: viewModel.entries.length,
                        itemBuilder: (context, index) {
                          final entry = viewModel.entries[index];
                          return Padding(
                            padding: const EdgeInsets.only(bottom: 12),
                            child: WorkEntryCard(
                              entry: entry,
                              onEdit: () => widget.onEditClick(entry.id),
                              onDelete: () => viewModel.deleteEntry(entry.id),
                            ),
                          );
                        },
                      ),
              ),
            ],
          ),
          floatingActionButton: ScaleTransition(
            scale: _fabAnimation,
            child: FloatingActionButton(
              onPressed: widget.onAddClick,
              backgroundColor: AppColors.accentGreen,
              child: const Icon(Icons.add, color: Colors.white),
            ),
          ),
        );
      },
    );
  }
}
