package com.example.tien.presentation.notes.ui

import android.app.Application
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tien.data.WorkEntryDatabase
import com.example.tien.data.repository.RoomNoteRepository
import com.example.tien.domain.model.Note
import com.example.tien.domain.usecase.*
import com.example.tien.presentation.notes.NotesViewModel
import com.example.tien.ui.theme.PrimaryBlue
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(onBack: () -> Unit) {
    val context = LocalContext.current.applicationContext as Application
    val db = remember(context) { WorkEntryDatabase.getInstance(context) }
    val repository = remember(db) { RoomNoteRepository(db.noteDao()) }
    val viewModel: NotesViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return NotesViewModel(
                    getNotes = GetNotesUseCase(repository),
                    addNote = AddNoteUseCase(repository),
                    updateNote = UpdateNoteUseCase(repository),
                    deleteNote = DeleteNoteUseCase(repository)
                ) as T
            }
        }
    )

    val searchQuery by viewModel.searchQuery.collectAsState()
    var showSearch by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    
    val filteredNotes = remember(viewModel.notes.collectAsState().value, searchQuery) {
        viewModel.getFilteredNotes()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AnimatedContent(
                        targetState = showSearch,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                        },
                        label = "notes_title"
                    ) { isSearching ->
                        if (isSearching) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = viewModel::onSearchQueryChange,
                                placeholder = { Text("Tìm kiếm ghi chú...", color = Color.White.copy(alpha = 0.7f)) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    cursorColor = Color.White
                                )
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Notes,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text("Ghi chú", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Quay lại", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showSearch = !showSearch
                        if (!showSearch) viewModel.onSearchQueryChange("")
                    }) {
                        Icon(
                            if (showSearch) Icons.Filled.Close else Icons.Filled.Search,
                            if (showSearch) "Đóng" else "Tìm kiếm",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            val fabScale = remember { Animatable(0f) }
            
            LaunchedEffect(Unit) {
                fabScale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }
            
            FloatingActionButton(
                onClick = {
                    editingNote = null
                    showAddDialog = true
                },
                containerColor = Color(0xFFFBBF24),
                contentColor = Color.White,
                modifier = Modifier.graphicsLayer {
                    scaleX = fabScale.value
                    scaleY = fabScale.value
                }
            ) {
                Icon(Icons.Filled.Add, "Thêm ghi chú")
            }
        }
    ) { padding ->
        if (filteredNotes.isEmpty()) {
            val emptyAlpha = remember { Animatable(0f) }
            
            LaunchedEffect(Unit) {
                emptyAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(800)
                )
            }
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.graphicsLayer { alpha = emptyAlpha.value }
                ) {
                    Icon(
                        Icons.Filled.Notes,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        if (searchQuery.isBlank()) "Chưa có ghi chú nào" else "Không tìm thấy ghi chú",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredNotes, key = { it.id }) { note ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(500)) + 
                                slideInVertically(animationSpec = tween(500)) { it / 2 },
                        exit = fadeOut(animationSpec = tween(300)) + 
                               slideOutHorizontally(animationSpec = tween(300)) { -it }
                    ) {
                        NoteCard(
                            note = note,
                            onEdit = {
                                editingNote = note
                                showAddDialog = true
                            },
                            onDelete = { viewModel.deleteNoteById(note.id) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        NoteDialog(
            note = editingNote,
            onDismiss = {
                showAddDialog = false
                editingNote = null
            },
            onSave = { title, content ->
                viewModel.saveNote(title, content, editingNote?.id) {
                    showAddDialog = false
                    editingNote = null
                }
            }
        )
    }
}

@Composable
fun NoteCard(
    note: Note,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc muốn xóa ghi chú này không?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Xóa")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = note.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD97706),
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Xóa",
                        tint = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.content,
                fontSize = 14.sp,
                color = Color(0xFF92400E),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Cập nhật: ${note.updatedAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun NoteDialog(
    note: Note? = null,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    
    // Animation cho dialog
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        launch { scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy)) }
        launch { alpha.animateTo(1f, tween(300)) }
    }
    
    // Animation cho nút lưu
    val buttonScale = remember { Animatable(1f) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
            this.alpha = alpha.value
        },
        containerColor = Color(0xFFFFFBF0),
        title = { 
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (note == null) Icons.Filled.Add else Icons.Filled.Edit,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if (note == null) "Thêm ghi chú mới" else "Sửa ghi chú",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD97706)
                )
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tiêu đề") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFBBF24),
                        focusedLabelColor = Color(0xFFD97706),
                        cursorColor = Color(0xFFFBBF24)
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Title,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B)
                        )
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Nội dung") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 5,
                    maxLines = 10,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFBBF24),
                        focusedLabelColor = Color(0xFFD97706),
                        cursorColor = Color(0xFFFBBF24)
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Description,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.padding(bottom = 80.dp)
                        )
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        kotlinx.coroutines.MainScope().launch {
                            buttonScale.animateTo(0.9f, tween(100))
                            buttonScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                        }
                        onSave(title, content)
                    }
                },
                modifier = Modifier.graphicsLayer {
                    scaleX = buttonScale.value
                    scaleY = buttonScale.value
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFBBF24)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Filled.Save,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Lưu", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF92400E)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Hủy", color = Color(0xFF92400E))
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
