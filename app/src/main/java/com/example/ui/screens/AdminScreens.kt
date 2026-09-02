package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.ProjectFile
import com.example.ui.components.AdminDeleteConfirmDialog
import com.example.ui.components.DevLogo
import com.example.ui.components.GradientButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.DevIsrafilViewModel
import com.example.ui.viewmodel.Screen
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminLoginScreen(
    viewModel: DevIsrafilViewModel,
    onNavigate: (Screen) -> Unit
) {
    val appLanguage by viewModel.appLanguage.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    listOf(Color(0xFF13112E), Color(0xFF080C14))
                )
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, DevBorderGlass, RoundedCornerShape(24.dp)),
            color = DevCardSurface,
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DevLogo()

                Text(
                    text = if (appLanguage == com.example.util.AppLanguage.BANGLA) "এডমিন প্যানেল লগইন" else "Admin Portal Login",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Text(
                    text = if (appLanguage == com.example.util.AppLanguage.BANGLA) 
                        "প্রজেক্ট ও ক্যাটাগরি ম্যানেজ করতে এডমিন ইমেইল ও পাসওয়ার্ড দিয়ে লগইন করুন।" 
                        else "Sign in with admin credentials to manage coding projects and categories.",
                    fontSize = 12.sp,
                    color = DevTextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(if (appLanguage == com.example.util.AppLanguage.BANGLA) "এডমিন ইমেইল" else "Admin Email") },
                    placeholder = { Text("admin@example.com", color = DevTextMuted) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = DevCyan) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevCyan,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                // Password (Obscured by default)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(if (appLanguage == com.example.util.AppLanguage.BANGLA) "পাসওয়ার্ড" else "Password") },
                    placeholder = { Text("••••••••", color = DevTextMuted) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = DevPurple) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password",
                                tint = DevTextMuted
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevCyan,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                // Remember Me
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkedColor = DevCyan)
                    )
                    Text(
                        text = if (appLanguage == com.example.util.AppLanguage.BANGLA) "লগইন তথ্য মনে রাখুন" else "Remember this device",
                        fontSize = 12.sp,
                        color = DevTextSecondary
                    )
                }

                GradientButton(
                    text = if (appLanguage == com.example.util.AppLanguage.BANGLA) "এডমিন লগইন করুন" else "Sign In as Admin",
                    icon = Icons.Default.Login,
                    onClick = {
                        viewModel.adminLogin(email, password, rememberMe)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(onClick = { onNavigate(Screen.HOME) }) {
                    Text(
                        if (appLanguage == com.example.util.AppLanguage.BANGLA) "← মূল ওয়েবসাইটে ফিরে যান" else "← Return to Public Website",
                        color = DevTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AdminDashboardScreen(
    viewModel: DevIsrafilViewModel,
    onNavigate: (Screen) -> Unit
) {
    val totalProjects by viewModel.totalProjectsCount.collectAsState()
    val publishedProjects by viewModel.publishedProjectsCount.collectAsState()
    val draftProjects by viewModel.draftProjectsCount.collectAsState()
    val totalDownloads by viewModel.totalDownloadsCount.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val allProjects by viewModel.allProjectsAdmin.collectAsState()
    val dateFormat = remember { SimpleDateFormat("MMM dd", Locale.getDefault()) }

    var projectToDelete by remember { mutableStateOf<ProjectEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Admin Top Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Admin Dashboard",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Manage repositories, uploads, categories & analytics",
                        fontSize = 12.sp,
                        color = DevTextSecondary
                    )
                }

                IconButton(
                    onClick = { viewModel.adminLogout() },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1E293B))
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint = DevRose,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Big Upload Action CTA
            GradientButton(
                text = "+ Upload New Project",
                icon = Icons.Default.CloudUpload,
                onClick = { viewModel.prepareNewProjectForm() },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Metrics Grid
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Platform Statistics",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminStatCard(
                    title = "Total Projects",
                    value = "$totalProjects",
                    icon = Icons.Default.Folder,
                    color = DevCyan,
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    title = "Total Downloads",
                    value = "${totalDownloads ?: 0}",
                    icon = Icons.Default.Download,
                    color = DevEmerald,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminStatCard(
                    title = "Categories",
                    value = "${categories.size}",
                    icon = Icons.Default.Category,
                    color = DevPurple,
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    title = "Published / Drafts",
                    value = "$publishedProjects / $draftProjects",
                    icon = Icons.Default.Publish,
                    color = DevAmber,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Quick Navigation Tabs
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Quick Management",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminNavButton(
                    label = "All Projects",
                    icon = Icons.Default.ListAlt,
                    onClick = { onNavigate(Screen.ADMIN_ALL_PROJECTS) },
                    modifier = Modifier.weight(1f)
                )
                AdminNavButton(
                    label = "Categories",
                    icon = Icons.Default.Category,
                    onClick = { onNavigate(Screen.ADMIN_CATEGORIES) },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminNavButton(
                    label = "Download Stats",
                    icon = Icons.Default.BarChart,
                    onClick = { onNavigate(Screen.ADMIN_STATS) },
                    modifier = Modifier.weight(1f)
                )
                AdminNavButton(
                    label = "Settings",
                    icon = Icons.Default.Settings,
                    onClick = { onNavigate(Screen.ADMIN_SETTINGS) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Recent Projects Table
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
                    text = "Recent Projects",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Manage All (${allProjects.size}) →",
                    fontSize = 12.sp,
                    color = DevCyan,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigate(Screen.ADMIN_ALL_PROJECTS) }
                )
            }

            allProjects.take(5).forEach { proj ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = DevCardSurface,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, DevBorderGlass)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = proj.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = if (proj.status == "PUBLISHED") DevEmerald.copy(alpha = 0.2f) else DevAmber.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = proj.status,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (proj.status == "PUBLISHED") DevEmerald else DevAmber,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = "${proj.category} • ${proj.downloadCount} dl",
                                    fontSize = 11.sp,
                                    color = DevTextMuted
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IconButton(
                                onClick = { viewModel.prepareEditProjectForm(proj) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DevCyan, modifier = Modifier.size(16.dp))
                            }
                            IconButton(
                                onClick = { projectToDelete = proj },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DevRose, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (projectToDelete != null) {
        AdminDeleteConfirmDialog(
            projectTitle = projectToDelete!!.title,
            onConfirm = {
                viewModel.deleteProject(projectToDelete!!)
                projectToDelete = null
            },
            onDismiss = { projectToDelete = null }
        )
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = DevCardSurface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, DevBorderGlass)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
            Text(text = title, fontSize = 11.sp, color = DevTextSecondary)
        }
    }
}

@Composable
fun AdminNavButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        color = Color(0xFF0F172A),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = DevCyan, modifier = Modifier.size(20.dp))
            Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
