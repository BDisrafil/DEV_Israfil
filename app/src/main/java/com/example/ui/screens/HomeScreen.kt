package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryEntity
import com.example.data.model.ProjectEntity
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.DevIsrafilViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun HomeScreen(
    viewModel: DevIsrafilViewModel,
    onNavigate: (Screen) -> Unit
) {
    val appLanguage by viewModel.appLanguage.collectAsState()
    val featuredProjects by viewModel.featuredProjects.collectAsState()
    val allProjects by viewModel.displayedProjects.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val totalDownloads by viewModel.totalDownloadsCount.collectAsState()
    val totalProjects by viewModel.totalProjectsCount.collectAsState()

    val popularProjects = remember(allProjects) {
        allProjects.sortedByDescending { it.downloadCount }.take(4)
    }
    val latestProjects = remember(allProjects) {
        allProjects.sortedByDescending { it.createdAt }.take(4)
    }

    var localSearchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Hero Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Section Title & Tagline
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = com.example.util.Strings.heroHeadline(appLanguage),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        lineHeight = 36.sp,
                        letterSpacing = (-0.5).sp
                    )

                    Text(
                        text = com.example.util.Strings.heroSubtitle(appLanguage),
                        fontSize = 13.sp,
                        color = DevTextSecondary,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 19.sp
                    )
                }

                // Premium SaaS Search Bar (bg-white/5, border-white/10, rounded-2xl)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, DevBorderGlass, RoundedCornerShape(18.dp)),
                    color = DevCardGlass
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = DevTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        TextField(
                            value = localSearchQuery,
                            onValueChange = { localSearchQuery = it },
                            placeholder = {
                                Text(
                                    com.example.util.Strings.searchPlaceholder(appLanguage),
                                    fontSize = 13.sp,
                                    color = DevTextMuted
                                )
                            },
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
                        if (localSearchQuery.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    viewModel.setSearchQuery(localSearchQuery)
                                    onNavigate(Screen.PROJECTS)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Go",
                                    tint = DevCyan
                                )
                            }
                        }
                    }
                }

                // Live Platform Metrics Strip in SaaS Glass
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(DevCardGlass)
                        .border(1.dp, DevBorderGlass, RoundedCornerShape(18.dp))
                        .padding(vertical = 14.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MetricItem(label = com.example.util.Strings.statProjects(appLanguage), value = "$totalProjects+")
                    MetricItem(label = com.example.util.Strings.statDownloads(appLanguage), value = "${totalDownloads ?: 0}+")
                    MetricItem(label = com.example.util.Strings.statCategories(appLanguage), value = "${categories.size}")
                    MetricItem(label = "Source Free", value = "100%")
                }
            }
        }

        // Quick Access Categories (Horizontal Pill Scroller matching SaaS design)
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = com.example.util.Strings.quickAccess(appLanguage),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DevTextMuted,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = com.example.util.Strings.viewAll(appLanguage),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DevCyan,
                    modifier = Modifier.clickable { onNavigate(Screen.CATEGORIES) }
                )
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                viewModel.setSelectedCategory(cat.name)
                                onNavigate(Screen.PROJECTS)
                            },
                        color = DevCardGlass,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DevBorderGlass)
                    ) {
                        Text(
                            text = cat.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = DevTextPrimary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)
                        )
                    }
                }
            }
        }

        // Featured Project Section (Styling aligned with SaaS Landing Featured Project card)
        if (featuredProjects.isNotEmpty()) {
            val featured = featuredProjects.first()
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = com.example.util.Strings.featuredProject(appLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DevTextMuted,
                        letterSpacing = 1.2.sp
                    )

                    Surface(
                        color = DevCyan.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, DevCyan.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = com.example.util.Strings.newRelease(appLanguage),
                            fontSize = 9.sp,
                            color = DevCyanLight,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // SaaS Featured Hero Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(DevCardGlass)
                        .border(1.dp, DevBorderGlass, RoundedCornerShape(24.dp))
                        .padding(16.dp)
                ) {
                    // Ambient Cyan Glow inside card
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 20.dp, y = (-20).dp)
                            .clip(CircleShape)
                            .background(DevCyanGlow)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0x4D06B6D4), Color(0x4D9333EA))
                                        )
                                    )
                                    .border(1.dp, DevBorderGlass, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Code,
                                    contentDescription = null,
                                    tint = DevCyan,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Surface(
                                    color = Color(0x3306B6D4),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, Color(0x4D06B6D4))
                                ) {
                                    Text(
                                        text = "${featured.category.uppercase()} • SOURCE",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DevCyanLight,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Text(
                                    text = featured.title,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Download,
                                            contentDescription = null,
                                            tint = DevTextSecondary,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = "${featured.downloadCount}",
                                            fontSize = 11.sp,
                                            color = DevTextSecondary
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FolderZip,
                                            contentDescription = null,
                                            tint = DevTextSecondary,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = featured.fileSize,
                                            fontSize = 11.sp,
                                            color = DevTextSecondary
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            text = featured.description,
                            fontSize = 12.sp,
                            color = DevTextSecondary,
                            lineHeight = 17.sp,
                            maxLines = 2
                        )

                        Button(
                            onClick = { viewModel.openProjectDetails(featured) },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Text(
                                text = com.example.util.Strings.viewProject(appLanguage),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }

        // Popular Downloads Section
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = DevCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = com.example.util.Strings.popularProjects(appLanguage),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = com.example.util.Strings.viewAll(appLanguage),
                    fontSize = 13.sp,
                    color = DevCyan,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        viewModel.setSortBy("popular")
                        onNavigate(Screen.PROJECTS)
                    }
                )
            }

            popularProjects.forEach { project ->
                ProjectCard(
                    project = project,
                    onViewProject = { viewModel.openProjectDetails(project) },
                    onQuickDownload = { viewModel.startProjectDownload(project) }
                )
            }
        }

        // Latest Projects Section
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = DevPurple,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = com.example.util.Strings.latestReleases(appLanguage),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = com.example.util.Strings.viewAll(appLanguage),
                    fontSize = 13.sp,
                    color = DevCyan,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        viewModel.setSortBy("newest")
                        onNavigate(Screen.PROJECTS)
                    }
                )
            }

            latestProjects.forEach { project ->
                ProjectCard(
                    project = project,
                    onViewProject = { viewModel.openProjectDetails(project) },
                    onQuickDownload = { viewModel.startProjectDownload(project) }
                )
            }
        }

        // Developer Call to Action Card
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF1E1B4B), Color(0xFF0F172A))
                    )
                )
                .border(1.dp, DevBorderGlass, RoundedCornerShape(20.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Ready to build your next project?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "All projects come with organized file structures, clean documentation, and instant ZIP downloads.",
                    fontSize = 13.sp,
                    color = DevTextSecondary,
                    textAlign = TextAlign.Center
                )
                GradientButton(
                    text = "Search All Coding Resources",
                    icon = Icons.Default.Code,
                    onClick = { onNavigate(Screen.PROJECTS) }
                )
            }
        }

        // Footer
        DevFooter(onNavigate = onNavigate)
    }
}

@Composable
fun MetricItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = DevCyan
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = DevTextSecondary
        )
    }
}
