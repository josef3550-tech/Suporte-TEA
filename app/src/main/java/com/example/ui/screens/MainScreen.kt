package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.CommunicationCard
import com.example.data.CommunicationData
import com.example.data.RoutineItem
import com.example.ui.RoutineViewModel
import kotlin.random.Random

@Composable
fun MainScreen(
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RoutineViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Comunicação, 1 = Agenda Visual
    var isParentModeEnabled by remember { mutableStateOf(false) }
    var showParentGate by remember { mutableStateOf(false) }

    val lastSpokenCard by viewModel.lastSpokenCard.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            TopAppBarSection(
                isParentModeEnabled = isParentModeEnabled,
                onParentModeToggle = {
                    if (isParentModeEnabled) {
                        isParentModeEnabled = false
                    } else {
                        showParentGate = true
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBarSection(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F9FC))
        ) {
            when (selectedTab) {
                0 -> CommunicationTab(
                    viewModel = viewModel,
                    lastSpokenCard = lastSpokenCard,
                    onSpeak = onSpeak
                )
                1 -> RoutineTab(
                    viewModel = viewModel,
                    isParentModeEnabled = isParentModeEnabled
                )
            }

            // Math Puzzle dialog for Parental Gate
            if (showParentGate) {
                ParentalGateDialog(
                    onDismiss = { showParentGate = false },
                    onSuccess = {
                        isParentModeEnabled = true
                        showParentGate = false
                    }
                )
            }
        }
    }
}

@Composable
fun TopAppBarSection(
    isParentModeEnabled: Boolean,
    onParentModeToggle: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        tonalElevation = 6.dp,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🧩",
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Column {
                    Text(
                        text = "SUPORTE TEA",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp,
                        color = Color(0xFF1A1C1E)
                    )
                    Text(
                        text = "COMUNICAÇÃO ASSISTIVA",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = Color(0xFF2563EB),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Parent/Teacher controls toggle
            Button(
                onClick = onParentModeToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isParentModeEnabled) Color(0xFF10B981) else Color(0xFF2563EB)
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                modifier = Modifier.testTag("parent_mode_button")
            ) {
                Icon(
                    imageVector = if (isParentModeEnabled) Icons.Filled.LockOpen else Icons.Filled.Lock,
                    contentDescription = "Área dos Pais",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isParentModeEnabled) "ÁREA PAIS: ATIVA" else "ÁREA DOS PAIS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBarSection(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 10.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = {
                Text(
                    text = "🗣️",
                    fontSize = 26.sp,
                    modifier = Modifier.alpha(if (selectedTab == 0) 1.0f else 0.6f)
                )
            },
            label = {
                Text(
                    text = "COMUNICAÇÃO",
                    fontWeight = if (selectedTab == 0) FontWeight.Black else FontWeight.Bold,
                    fontSize = 11.sp,
                    color = if (selectedTab == 0) Color(0xFF2563EB) else Color(0xFF64748B),
                    letterSpacing = 0.5.sp
                )
            },
            modifier = Modifier.testTag("nav_communication")
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = {
                Text(
                    text = "📅",
                    fontSize = 26.sp,
                    modifier = Modifier.alpha(if (selectedTab == 1) 1.0f else 0.6f)
                )
            },
            label = {
                Text(
                    text = "AGENDA VISUAL",
                    fontWeight = if (selectedTab == 1) FontWeight.Black else FontWeight.Bold,
                    fontSize = 11.sp,
                    color = if (selectedTab == 1) Color(0xFF2563EB) else Color(0xFF64748B),
                    letterSpacing = 0.5.sp
                )
            },
            modifier = Modifier.testTag("nav_routine")
        )
    }
}

// ==================== COMMUNICATION TAB ====================

@Composable
fun CommunicationTab(
    viewModel: RoutineViewModel,
    lastSpokenCard: CommunicationCard?,
    onSpeak: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Todos") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // SPOKEN PHRASE DISPLAY BAR (PECS display strip)
        AnimatedVisibility(visible = lastSpokenCard != null) {
            if (lastSpokenCard != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("last_spoken_display"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                    border = BorderStroke(3.dp, Color(0xFF3B82F6))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = lastSpokenCard.emoji,
                                fontSize = 48.sp,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Column {
                                Text(
                                    text = "Estou dizendo:",
                                    fontSize = 12.sp,
                                    color = Color(0xFF3B82F6),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = lastSpokenCard.text,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E3A8A)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Replay audio button
                            IconButton(
                                onClick = { onSpeak(lastSpokenCard.text) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFFDBEAFE), RoundedCornerShape(12.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.VolumeUp,
                                    contentDescription = "Falar de novo",
                                    tint = Color(0xFF1E40AF)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Clear bar button
                            IconButton(
                                onClick = { viewModel.clearLastSpoken() },
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFFFEE2E2), RoundedCornerShape(12.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Limpar",
                                    tint = Color(0xFFEF4444)
                                )
                            }
                        }
                    }
                }
            }
        }

        // CATEGORY PILLS FILTER
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            item {
                CategoryPill(
                    title = "Todos",
                    isSelected = selectedCategory == "Todos",
                    emoji = "⭐",
                    onClick = { selectedCategory = "Todos" }
                )
            }
            items(CommunicationData.categories) { category ->
                val categoryEmoji = when (category) {
                    "Emoções" -> "😊"
                    "Necessidades" -> "💧"
                    "Ações" -> "✔"
                    "Escola" -> "✏"
                    "Saúde" -> "🤒"
                    else -> "📁"
                }
                CategoryPill(
                    title = category,
                    isSelected = selectedCategory == category,
                    emoji = categoryEmoji,
                    onClick = { selectedCategory = category }
                )
            }
        }

        // CARDS GRID
        val filteredCards = remember(selectedCategory) {
            if (selectedCategory == "Todos") {
                CommunicationData.cards
            } else {
                CommunicationData.cards.filter { it.category == selectedCategory }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredCards) { card ->
                    PECSCommunicationCard(
                        card = card,
                        onClick = {
                            viewModel.speakCard(card, onSpeak)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Actions Footer inspired directly from HTML Template
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // SIM button
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(68.dp)
                    .clickable {
                        viewModel.speakCard(CommunicationCard("✔️", "Sim.", "Ações"), onSpeak)
                    }
                    .testTag("quick_action_sim"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFDFF7E5)),
                border = BorderStroke(2.5.dp, Color(0xFFA8E6CF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SIM",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF15803D),
                        letterSpacing = 1.sp
                    )
                    Text(text = "✔️", fontSize = 20.sp)
                }
            }

            // NÃO button
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(68.dp)
                    .clickable {
                        viewModel.speakCard(CommunicationCard("❌", "Não.", "Ações"), onSpeak)
                    }
                    .testTag("quick_action_nao"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEDF1)),
                border = BorderStroke(2.5.dp, Color(0xFFFFD1DC)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "NÃO",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFB91C1C),
                        letterSpacing = 1.sp
                    )
                    Text(text = "❌", fontSize = 20.sp)
                }
            }

            // PARE button
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(68.dp)
                    .clickable {
                        viewModel.speakCard(CommunicationCard("🔇", "Pare.", "Ações"), onSpeak)
                    }
                    .testTag("quick_action_pare"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                border = BorderStroke(2.5.dp, Color(0xFF0F172A)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "PARE",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    Text(text = "🔇", fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun CategoryPill(
    title: String,
    isSelected: Boolean,
    emoji: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag("category_pill_$title"),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color(0xFF2563EB) else Color.White,
        border = BorderStroke(2.5.dp, if (isSelected) Color(0xFF1D4ED8) else Color(0xFFCBD5E1)),
        tonalElevation = if (isSelected) 6.dp else 2.dp,
        shadowElevation = if (isSelected) 4.dp else 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 18.sp, modifier = Modifier.padding(end = 6.dp))
            Text(
                text = title.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp,
                color = if (isSelected) Color.White else Color(0xFF475569)
            )
        }
    }
}

@Composable
fun PECSCommunicationCard(
    card: CommunicationCard,
    onClick: () -> Unit
) {
    // Elegant color mapping matching formal PECS therapy colors
    val (bgColor, textColor, borderColor) = when (card.category) {
        "Emoções" -> Triple(Color(0xFFFFF1F1), Color(0xFFC2185B), Color(0xFFFFCDD2))
        "Necessidades" -> Triple(Color(0xFFEBF5FF), Color(0xFF1976D2), Color(0xFFBBDEFB))
        "Ações" -> Triple(Color(0xFFEEF9EE), Color(0xFF388E3C), Color(0xFFC8E6C9))
        "Escola" -> Triple(Color(0xFFFFFDF0), Color(0xFF9A6A00), Color(0xFFFFF9C4))
        "Saúde" -> Triple(Color(0xFFFFF3ED), Color(0xFFD32F2F), Color(0xFFFFCCBC))
        else -> Triple(Color(0xFFF8FAFC), Color(0xFF334155), Color(0xFFE2E8F0))
    }

    var isPressed by remember { mutableStateOf(false) }
    val scaleAnim by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "click_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(135.dp)
            .scale(scaleAnim)
            .clickable {
                onClick()
            }
            .testTag("pecs_card_${card.text.replace(" ", "_")}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(4.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = card.emoji,
                fontSize = 44.sp,
                modifier = Modifier.padding(bottom = 6.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = card.text.uppercase(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp
            )
        }
    }
}

// ==================== VISUAL SCHEDULE (AGENDA VISUAL) ====================

@Composable
fun RoutineTab(
    viewModel: RoutineViewModel,
    isParentModeEnabled: Boolean
) {
    val routineItems by viewModel.uiState.collectAsState()
    var showAddEditDialog by remember { mutableStateOf(false) }
    var selectedItemToEdit by remember { mutableStateOf<RoutineItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Quick info / reset bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ROTINA DIÁRIA",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
                color = Color(0xFF1A1C1E)
            )

            Row {
                // Clear / Reset all completions button
                OutlinedButton(
                    onClick = { viewModel.resetAllCompletions() },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, Color(0xFFEF4444)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("reset_routine_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Reiniciar Dia",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reiniciar Dia", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                if (isParentModeEnabled) {
                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            selectedItemToEdit = null
                            showAddEditDialog = true
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("add_routine_item_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Adicionar Atividade",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Criar Nova", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Display Info banner in Parent Mode
        if (isParentModeEnabled) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                border = BorderStroke(1.dp, Color(0xFFA7F3D0))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Info",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Modo de Edição Ativo. Você pode reordenar (🔼 🔽), editar (✏️), ocultar (👁️), ou excluir (🗑️) atividades.",
                        fontSize = 11.sp,
                        color = Color(0xFF065F46),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Active list of routine items
        val enabledItems = remember(routineItems, isParentModeEnabled) {
            if (isParentModeEnabled) {
                routineItems // parents see everything to toggle or modify
            } else {
                routineItems.filter { it.isEnabled } // child only sees enabled ones
            }
        }

        if (enabledItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📅", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Nenhuma atividade ativa na rotina.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "Ative a Área dos Pais para criar ou habilitar tarefas.",
                        fontSize = 13.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(enabledItems, key = { it.id }) { item ->
                    RoutineItemRow(
                        item = item,
                        isParentModeEnabled = isParentModeEnabled,
                        onCompletedToggle = { viewModel.toggleItemCompleted(item) },
                        onEnabledToggle = { viewModel.toggleItemEnabled(item) },
                        onMoveUp = { viewModel.moveItemUp(item) },
                        onMoveDown = { viewModel.moveItemDown(item) },
                        onEditClick = {
                            selectedItemToEdit = item
                            showAddEditDialog = true
                        },
                        onDeleteClick = { viewModel.deleteItem(item) }
                    )
                }
            }
        }
    }

    // Modal dialogue to create or edit routine item
    if (showAddEditDialog) {
        AddEditRoutineDialog(
            item = selectedItemToEdit,
            onDismiss = { showAddEditDialog = false },
            onSave = { title, timeText, emoji ->
                if (selectedItemToEdit == null) {
                    viewModel.addNewItem(title, timeText, emoji)
                } else {
                    viewModel.updateItem(selectedItemToEdit!!, title, timeText, emoji)
                }
                showAddEditDialog = false
            }
        )
    }
}

@Composable
fun RoutineItemRow(
    item: RoutineItem,
    isParentModeEnabled: Boolean,
    onCompletedToggle: () -> Unit,
    onEnabledToggle: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val completedAlpha = if (item.isCompleted && !isParentModeEnabled) 0.55f else 1.0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(completedAlpha)
            .testTag("routine_card_${item.id}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isCompleted) Color(0xFFECFDF5) else Color.White
        ),
        border = BorderStroke(
            width = if (item.isCompleted) 4.dp else 3.dp,
            color = if (item.isCompleted) Color(0xFF10B981) else if (!item.isEnabled) Color(0xFFCBD5E1) else Color(0xFFCBD5E1)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Item Visual Representation
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Large clock badge
                    Surface(
                        color = if (item.isCompleted) Color(0xFFD1FAE5) else Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(2.dp, if (item.isCompleted) Color(0xFF10B981) else Color(0xFF3B82F6))
                    ) {
                        Text(
                            text = item.timeText.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                            color = if (item.isCompleted) Color(0xFF065F46) else Color(0xFF1D4ED8)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Large Emoji
                    Text(
                        text = item.emoji,
                        fontSize = 38.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )

                    // Title
                    Column {
                        Text(
                            text = item.title.uppercase(),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                            color = if (item.isCompleted) Color(0xFF065F46) else Color(0xFF1E293B)
                        )
                        if (!item.isEnabled) {
                            Text(
                                text = "OCULTO PARA A CRIANÇA",
                                fontSize = 10.sp,
                                color = Color(0xFFEF4444),
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                // Interaction / Action Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isParentModeEnabled) {
                        // REORDER CONTROLS
                        IconButton(onClick = onMoveUp, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = Icons.Filled.ArrowUpward,
                                contentDescription = "Mover para Cima",
                                tint = Color(0xFF475569)
                            )
                        }
                        IconButton(onClick = onMoveDown, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = Icons.Filled.ArrowDownward,
                                contentDescription = "Mover para Baixo",
                                tint = Color(0xFF475569)
                            )
                        }

                        // VISIBILITY TOGGLE
                        IconButton(onClick = onEnabledToggle, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = if (item.isEnabled) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (item.isEnabled) "Ocultar" else "Mostrar",
                                tint = if (item.isEnabled) Color(0xFF059669) else Color(0xFF94A3B8)
                            )
                        }

                        // EDIT
                        IconButton(onClick = onEditClick, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Editar Atividade",
                                tint = Color(0xFF3B82F6)
                            )
                        }

                        // DELETE
                        IconButton(
                            onClick = onDeleteClick,
                            modifier = Modifier.size(36.dp).testTag("delete_item_button_${item.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Excluir Atividade",
                                tint = Color(0xFFEF4444)
                            )
                        }
                    } else {
                        // Interactive completed checkbox for child (Extra Large!)
                        IconButton(
                            onClick = onCompletedToggle,
                            modifier = Modifier
                                .size(56.dp)
                                .testTag("check_routine_item_${item.id}")
                        ) {
                            Icon(
                                imageVector = if (item.isCompleted) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                                contentDescription = if (item.isCompleted) "Concluído" else "Marcar Concluído",
                                tint = if (item.isCompleted) Color(0xFF10B981) else Color(0xFF94A3B8),
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== PARENTAL GATE (DIÁLOGO ÁREA DOS PAIS) ====================

@Composable
fun ParentalGateDialog(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    // Generate simple random math question suitable for parents but hard for kids
    val context = LocalContext.current
    val num1 = remember { Random.nextInt(4, 9) }
    val num2 = remember { Random.nextInt(3, 7) }
    val isMultiplication = remember { Random.nextBoolean() }
    val questionText = if (isMultiplication) "$num1 x $num2" else "$num1 + $num2"
    val correctAnswer = if (isMultiplication) num1 * num2 else num1 + num2

    var inputAnswer by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔒 Área de Segurança", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Para entrar na Área dos Pais e editar a rotina, por favor resolva a conta abaixo para provar que você é um adulto:",
                    fontSize = 14.sp,
                    color = Color(0xFF475569),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Text(
                        text = "$questionText = ?",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                OutlinedTextField(
                    value = inputAnswer,
                    onValueChange = {
                        inputAnswer = it
                        isError = false
                    },
                    label = { Text("Resposta da conta") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("parent_gate_input"),
                    shape = RoundedCornerShape(12.dp),
                    isError = isError
                )

                if (isError) {
                    Text(
                        text = "Resposta incorreta. Tente novamente!",
                        color = Color(0xFFEF4444),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsed = inputAnswer.toIntOrNull()
                    if (parsed == correctAnswer) {
                        onSuccess()
                    } else {
                        isError = true
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                modifier = Modifier.testTag("parent_gate_confirm")
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("parent_gate_dismiss")
            ) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

// ==================== ADD / EDIT ROUTINE ITEM DIALOG ====================

@Composable
fun AddEditRoutineDialog(
    item: RoutineItem?,
    onDismiss: () -> Unit,
    onSave: (title: String, timeText: String, emoji: String) -> Unit
) {
    var title by remember { mutableStateOf(item?.title ?: "") }
    var timeText by remember { mutableStateOf(item?.timeText ?: "08:00") }
    var emoji by remember { mutableStateOf(item?.emoji ?: "⏰") }

    var titleError by remember { mutableStateOf(false) }

    // Preselected common emojis for kids
    val suggestionEmojis = listOf("⏰", "🪥", "🚿", "🍳", "🏫", "🍲", "💤", "🧠", "🧸", "🍕", "🌙", "🚗", "🏡", "📖", "🎵", "🍎", "🥛", "🏃")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (item == null) "Adicionar Atividade" else "Editar Atividade",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            titleError = false
                        },
                        label = { Text("Nome da atividade") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_routine_title"),
                        shape = RoundedCornerShape(12.dp),
                        isError = titleError
                    )
                    if (titleError) {
                        Text(
                            text = "Por favor, digite o nome da atividade.",
                            color = Color(0xFFEF4444),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = timeText,
                        onValueChange = { timeText = it },
                        label = { Text("Horário (ex: 08:30)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_routine_time"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                item {
                    Text(
                        text = "Emoji Ilustrativo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF475569)
                    )
                }

                item {
                    // Big Preview Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xFFEFF6FF),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(2.dp, Color(0xFF3B82F6)),
                            modifier = Modifier.size(72.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = emoji, fontSize = 42.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Custom emoji text input
                        OutlinedTextField(
                            value = emoji,
                            onValueChange = { if (it.isNotEmpty()) emoji = it },
                            label = { Text("Ou digite outro Emoji") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("edit_routine_emoji_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                item {
                    Text(
                        text = "Toque em um emoji sugerido:",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                item {
                    // suggestion emoji grid row
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val chunks = suggestionEmojis.chunked(6)
                        chunks.forEach { chunk ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                chunk.forEach { suggEmoji ->
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(
                                                color = if (emoji == suggEmoji) Color(0xFFDBEAFE) else Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable { emoji = suggEmoji }
                                            .padding(2.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = suggEmoji, fontSize = 24.sp)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                    } else {
                        onSave(title, timeText, emoji)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                modifier = Modifier.testTag("edit_routine_save")
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("edit_routine_dismiss")
            ) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}
