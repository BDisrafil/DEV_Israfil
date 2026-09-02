package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.DevIsrafilViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun ProjectsScreen(
    viewModel: DevIsrafilViewModel,
    onNavigate: (Screen) -> Unit
) {
    val appLanguage by viewModel.appLanguage.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val sortBy by viewModel.sortBy.collectAsState()
    val projects by viewModel.displayedProjects.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var showSortMenu by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header & Search Block
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = com.example.util.Strings.projectsLibraryTitle(appLanguage),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Text(
                    text = com.example.util.Strings.projectsLibrarySubtitle(appLanguage),
                    fontSize = 13.sp,
                    color = DevTextSecondary
                )

                // Search Bar
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, DevBorderGlass, RoundedCornerShape(16.dp)),
                    color = DevCardGlass
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = DevCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        TextField(
                            value = searchQuery,
                            onValueChange = { viewModel.setSearchQuery(it) },
                            placeholder = { Text(com.example.util.Strings.filterPlaceholder(appLanguage), fontSize = 13.sp, color = DevTextMuted) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = DevTextMuted)
                            }
                        }
                    }
                }

                // Category Filter Pills
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { viewModel.setSelectedCategory(null) },
                            label = { Text(com.example.util.Strings.allCategoriesLabel(appLanguage, categories.size)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DevCyan,
                                selectedLabelColor = Color(0xFF030712)
                            )
                        )
                    }
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory.equals(cat.name, ignoreCase = true),
                            onClick = {
                                if (selectedCategory.equals(cat.name, ignoreCase = true)) {
                                    viewModel.setSelectedCategory(null)
                                } else {
                                    viewModel.setSelectedCategory(cat.name)
                                }
                            },
                            label = { Text(cat.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DevCyan,
                                selectedLabelColor = Color(0xFF030712)
                            )
                        )
                    }
                }

                // Results Count & Sorting Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = com.example.util.Strings.projectsAvailable(appLanguage, projects.size),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DevCyan
                    )

                    Box {
                        OutlinedButton(
                            onClick = { showSortMenu = true },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFF1E293B)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "Sort",
                                tint = DevTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when (sortBy) {
                                    "popular" -> com.example.util.Strings.sortPopular(appLanguage)
                                    "newest" -> com.example.util.Strings.sortNewest(appLanguage)
                                    "name" -> com.example.util.Strings.sortName(appLanguage)
                                    else -> com.example.util.Strings.sortBy(appLanguage)
                                },
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false },
                            modifier = Modifier.background(DevCardSurface)
                        ) {
                            DropdownMenuItem(
                                text = { Text(com.example.util.Strings.sortPopular(appLanguage), color = Color.White) },
                                onClick = {
                                    viewModel.setSortBy("popular")
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(com.example.util.Strings.sortNewest(appLanguage), color = Color.White) },
                                onClick = {
                                    viewModel.setSortBy("newest")
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(com.example.util.Strings.sortName(appLanguage), color = Color.White) },
                                onClick = {
                                    viewModel.setSortBy("name")
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Projects List Content
        if (projects.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = DevTextMuted,
                            modifier = Modifier.size(56.dp)
                        )
                        Text(
                            text = "No projects match your search",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Try clearing your filters or searching for another language.",
                            fontSize = 13.sp,
                            color = DevTextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = {
                                viewModel.setSearchQuery("")
                                viewModel.setSelectedCategory(null)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DevCyan)
                        ) {
                            Text("Reset Search Filters", color = Color(0xFF030712), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(projects, key = { it.id }) { project ->
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ProjectCard(
                        project = project,
                        onViewProject = { viewModel.openProjectDetails(project) },
                        onQuickDownload = { viewModel.startProjectDownload(project) }
                    )
                }
            }
        }

        item {
            DevFooter(onNavigate = onNavigate)
        }
    }
}
