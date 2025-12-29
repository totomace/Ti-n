package com.example.tien

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tien.data.WorkEntryDatabase
import com.example.tien.data.repository.RoomWorkEntryRepository
import com.example.tien.domain.usecase.GetWorkEntriesUseCase
import com.example.tien.domain.usecase.DeleteWorkEntryUseCase
import com.example.tien.presentation.worklist.WorkEntryListViewModel
import com.example.tien.presentation.worklist.ui.WorkEntryListScreen
import com.example.tien.ui.theme.TienTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TienTheme {
                WorkEntryApp()
            }
        }
    }
}

@Composable
fun WorkEntryApp() {
    var showList by remember { mutableStateOf(true) }
    val context = LocalContext.current.applicationContext as Application
    val db = remember(context) { WorkEntryDatabase.getInstance(context) }
    val repository = remember(db) { RoomWorkEntryRepository(db.workEntryDao()) }
    
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
    
    AnimatedContent(
        targetState = showList,
        transitionSpec = {
            fadeIn(animationSpec = tween(400)) togetherWith
                    fadeOut(animationSpec = tween(400))
        },
        label = "screen_transition"
    ) { isShowingList ->
        if (isShowingList) {
            WorkEntryListScreen(
                viewModel = listViewModel,
                onAddClick = { showList = false }
            )
        } else {
            com.example.tien.presentation.workentry.ui.WorkEntryScreen(
                onViewHistory = { 
                    listViewModel.loadEntries() // Reload when coming back
                    showList = true
                }
            )
        }
    }
}