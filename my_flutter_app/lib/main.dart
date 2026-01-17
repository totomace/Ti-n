import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import 'package:intl/date_symbol_data_local.dart';
import 'screens/work_entry_list_screen.dart';
import 'screens/work_entry_form_screen.dart';
import 'screens/statistics_screen.dart';
import 'screens/notes_screen.dart';
import 'screens/settings_screen.dart';
import 'viewmodels/work_entry_list_viewmodel.dart';
import 'theme/app_colors.dart';
import 'models/work_entry.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  // Initialize Vietnamese locale for date formatting
  await initializeDateFormatting('vi_VN', null);
  
  // Set preferred orientations
  SystemChrome.setPreferredOrientations([
    DeviceOrientation.portraitUp,
    DeviceOrientation.portraitDown,
  ]);

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => WorkEntryListViewModel(),
      child: MaterialApp(
        title: 'Lịch sử làm việc',
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          primaryColor: AppColors.primaryBlue,
          scaffoldBackgroundColor: AppColors.backgroundLight,
          colorScheme: ColorScheme.fromSeed(
            seedColor: AppColors.primaryYellow,
            primary: AppColors.primaryBlue,
            secondary: AppColors.accentGreen,
          ),
          appBarTheme: const AppBarTheme(
            backgroundColor: AppColors.primaryBlue,
            elevation: 0,
            iconTheme: IconThemeData(color: Colors.white),
            titleTextStyle: TextStyle(
              color: Colors.white,
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),
          floatingActionButtonTheme: const FloatingActionButtonThemeData(
            backgroundColor: AppColors.accentGreen,
            foregroundColor: Colors.white,
          ),
          cardTheme: const CardThemeData(
            color: AppColors.cardBackground,
            elevation: 4,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.all(Radius.circular(12)),
            ),
          ),
          useMaterial3: true,
        ),
        home: const MainScreen(),
      ),
    );
  }
}

class MainScreen extends StatelessWidget {
  const MainScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final viewModel = Provider.of<WorkEntryListViewModel>(context, listen: false);
    
    return WorkEntryListScreen(
      onAddClick: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const WorkEntryFormScreen(),
          ),
        );
      },
      onEditClick: (id) {
        final viewModel = Provider.of<WorkEntryListViewModel>(context, listen: false);
        final entry = viewModel.entries.firstWhere((e) => e.id == id);
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => WorkEntryFormScreen(entry: entry),
          ),
        );
      },
      onStatisticsClick: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const StatisticsScreen(),
          ),
        );
      },
      onSettingsClick: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const SettingsScreen(),
          ),
        );
      },
      onNotesClick: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const NotesScreen(),
          ),
        );
      },
    );
  }
}
