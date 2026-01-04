package com.example.tien

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tien.data.WorkEntryDatabase
import com.example.tien.data.repository.RoomWorkEntryRepository
import com.example.tien.data.preferences.ThemePreferences
import com.example.tien.data.preferences.ThemeMode
import com.example.tien.domain.model.WorkEntry
import com.example.tien.domain.usecase.GetWorkEntriesUseCase
import com.example.tien.domain.usecase.DeleteWorkEntryUseCase
import com.example.tien.domain.usecase.UpdatePaymentStatusUseCase
import com.example.tien.presentation.worklist.WorkEntryListViewModel
import com.example.tien.presentation.worklist.ui.WorkEntryListScreen
import com.example.tien.presentation.statistics.StatisticsViewModel
import com.example.tien.presentation.statistics.ui.StatisticsScreen
import com.example.tien.presentation.settings.SettingsViewModel
import com.example.tien.presentation.settings.ui.SettingsScreen
import com.example.tien.ui.theme.TienTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current.applicationContext as Application
            val themePreferences = remember { ThemePreferences(context) }
            val themeMode by themePreferences.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            val isSystemInDarkTheme = isSystemInDarkTheme()
            
            val useDarkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme
            }
            
            TienTheme(darkTheme = useDarkTheme) {
                WorkEntryApp()
            }
        }
    }
}

@Composable
fun WorkEntryApp() {
    var showSplash by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf("list") } // "list", "form", "stats", "settings", "notes"
    var entryToEdit by remember { mutableStateOf<WorkEntry?>(null) }
    val context = LocalContext.current.applicationContext as Application
    val db = remember(context) { WorkEntryDatabase.getInstance(context) }
    val repository = remember(db) { RoomWorkEntryRepository(db.workEntryDao()) }
    val themePreferences = remember { ThemePreferences(context) }
    
    if (showSplash) {
        com.example.tien.presentation.splash.SplashScreen(
            onSplashFinished = { showSplash = false }
        )
        return
    }
    
    // Create list viewmodel
    val listViewModel: WorkEntryListViewModel = viewModel(
        key = "list_vm",
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return WorkEntryListViewModel(
                    getWorkEntries = GetWorkEntriesUseCase(repository),
                    deleteWorkEntry = DeleteWorkEntryUseCase(repository)
                ) as T
            }
        }
    )
    
    // Create statistics viewmodel
    val statsViewModel: StatisticsViewModel = viewModel(
        key = "stats_vm",
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return StatisticsViewModel(
                    getWorkEntries = GetWorkEntriesUseCase(repository),
                    updatePaymentStatus = UpdatePaymentStatusUseCase(repository)
                ) as T
            }
        }
    )
    
    // Create settings viewmodel
    val settingsViewModel: SettingsViewModel = viewModel(
        key = "settings_vm",
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(
                    themePreferences = themePreferences
                ) as T
            }
        }
    )
    
    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            (fadeIn(animationSpec = tween(600, easing = FastOutSlowInEasing)) +
             slideInHorizontally(animationSpec = tween(600, easing = FastOutSlowInEasing)) { it / 3 })
                .togetherWith(
                    fadeOut(animationSpec = tween(400, easing = FastOutSlowInEasing)) +
                    slideOutHorizontally(animationSpec = tween(400, easing = FastOutSlowInEasing)) { -it / 3 }
                )
        },
        label = "screen_transition"
    ) { screen ->
        when (screen) {
            "list" -> {
                WorkEntryListScreen(
                    viewModel = listViewModel,
                    onAddClick = { 
                        entryToEdit = null
                        currentScreen = "form" 
                    },
                    onEditClick = { entry ->
                        entryToEdit = entry
                        currentScreen = "form"
                    },
                    onStatisticsClick = { 
                        statsViewModel.loadStatistics()
                        currentScreen = "stats" 
                    },
                    onSettingsClick = {
                        currentScreen = "settings"
                    },
                    onNotesClick = {
                        currentScreen = "notes"
                    }
                )
            }
            "form" -> {
                BackHandler {
                    listViewModel.loadEntries()
                    entryToEdit = null
                    currentScreen = "list"
                }
                com.example.tien.presentation.workentry.ui.WorkEntryScreen(
                    entryToEdit = entryToEdit,
                    onViewHistory = { 
                        listViewModel.loadEntries()
                        entryToEdit = null
                        currentScreen = "list"
                    }
                )
            }
            "stats" -> {
                BackHandler { currentScreen = "list" }
                StatisticsScreen(
                    viewModel = statsViewModel,
                    onBackClick = { currentScreen = "list" }
                )
            }
            "settings" -> {
                BackHandler { currentScreen = "list" }
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onBackClick = { currentScreen = "list" }
                )
            }
            "notes" -> {
                BackHandler { currentScreen = "list" }
                com.example.tien.presentation.notes.ui.NotesScreen(
                    onBack = { currentScreen = "list" }
                )
            }
        }
    }
}