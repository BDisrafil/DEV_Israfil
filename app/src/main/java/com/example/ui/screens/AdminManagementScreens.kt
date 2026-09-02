package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.ProjectFile
import com.example.ui.components.AdminDeleteConfirmDialog
import com.example.ui.components.GradientButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.DevIsrafilViewModel
import com.example.ui.viewmodel.Screen
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminUploadScreen(
    viewModel: DevIsrafilViewModel,
    onNavigate: (Screen) -> Unit
) {
    val title by viewModel.formTitle.collectAsState()
    val description by viewModel.formDescription.collectAsState()
    val category by viewModel.formCategory.collectAsState()
    val technologies by viewModel.formTechnologies.collectAsState()
    val version by viewModel.formVersion.collectAsState()
    val fileSize by viewModel.formFileSize.collectAsState()
    val youtubeUrl by viewModel.formYoutubeUrl.collectAsState()
    val liveDemoUrl by viewModel.formLiveDemoUrl.collectAsState()
    val files by viewModel.formFiles.collectAsState()
    val isFeatured by viewModel.formIsFeatured.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var showAddFileDialog by remember { mutableStateOf(false) }
    var selectedFileIndex by remember { mutableStateOf(0) }
    var editingFileName by remember { mutableStateOf("") }
    var editingFileContent by remember { mutableStateOf("") }

    val isEditing = viewModel.editProjectId != null

    // Sync active editor file
    LaunchedEffect(files, selectedFileIndex) {
        if (files.isNotEmpty() && selectedFileIndex in files.indices) {
            editingFileName = files[selectedFileIndex].name
            editingFileContent = files[selectedFileIndex].content
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.clickable { onNavigate(Screen.ADMIN_DASHBOARD) }
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DevCyan)
                Text(
                    text = if (isEditing) "Edit Project" else "Upload New Project",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { viewModel.saveProjectFromForm("DRAFT") },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DevAmber)
                ) {
                    Text("Save Draft", fontSize = 12.sp)
                }

                Button(
                    onClick = { viewModel.saveProjectFromForm("PUBLISHED") },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DevCyan)
                ) {
                    Text("Publish Project", color = Color(0xFF030712), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Form Fields
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Project Title
            OutlinedTextField(
                value = title,
                onValueChange = { viewModel.formTitle.value = it },
                label = { Text("Project Title *") },
                placeholder = { Text("e.g. Modern Portfolio Website Template") },
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

            // Category & Version Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                var expandedCategory by remember { mutableStateOf(false) }
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = {
                            IconButton(onClick = { expandedCategory = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = DevCyan)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DevCyan,
                            unfocusedBorderColor = Color(0xFF334155),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    DropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false },
                        modifier = Modifier.background(DevCardSurface)
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.name, color = Color.White) },
                                onClick = {
                                    viewModel.formCategory.value = cat.name
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = version,
                    onValueChange = { viewModel.formVersion.value = it },
                    label = { Text("Version") },
                    placeholder = { Text("1.0.0") },
                    modifier = Modifier.weight(0.7f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevCyan,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )
            }

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { viewModel.formDescription.value = it },
                label = { Text("Project Description") },
                placeholder = { Text("Comprehensive overview of the codebase, setup instructions, and features...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DevCyan,
                    unfocusedBorderColor = Color(0xFF334155),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                maxLines = 5
            )

            // Technologies
            OutlinedTextField(
                value = technologies,
                onValueChange = { viewModel.formTechnologies.value = it },
                label = { Text("Technologies (comma separated)") },
                placeholder = { Text("HTML, CSS, JavaScript, React") },
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

            // Links (YouTube & Demo)
            OutlinedTextField(
                value = youtubeUrl,
                onValueChange = { viewModel.formYoutubeUrl.value = it },
                label = { Text("YouTube Tutorial Video URL") },
                placeholder = { Text("https://youtube.com/watch?v=...") },
                leadingIcon = { Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color(0xFFFF0000)) },
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

            OutlinedTextField(
                value = liveDemoUrl,
                onValueChange = { viewModel.formLiveDemoUrl.value = it },
                label = { Text("Live Demo URL (optional)") },
                placeholder = { Text("https://demo.devisrafil.com/app") },
                leadingIcon = { Icon(Icons.Default.Language, contentDescription = null, tint = DevCyan) },
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

            // Featured Switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Featured on Homepage", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Highlight this repository on the public hero", color = DevTextSecondary, fontSize = 11.sp)
                }
                Switch(
                    checked = isFeatured,
                    onCheckedChange = { viewModel.formIsFeatured.value = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = DevCyan)
                )
            }

            // SOURCE FILES MANAGER
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF090E1A),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DevBorderGlass)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Source Code & File Manager",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${files.size} files packaged in ZIP",
                                fontSize = 11.sp,
                                color = DevTextSecondary
                            )
                        }

                        Button(
                            onClick = { showAddFileDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DevCyan)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF030712), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add File", color = Color(0xFF030712), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Horizontal Files Selector Tabs
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        itemsIndexed(files) { index, file ->
                            val isSelected = index == selectedFileIndex
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedFileIndex = index },
                                color = if (isSelected) DevCyan.copy(alpha = 0.2f) else Color(0xFF1E293B),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, if (isSelected) DevCyan else Color.Transparent)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = file.name,
                                        fontSize = 11.sp,
                                        color = if (isSelected) DevCyan else Color.White,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    if (files.size > 1) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove file",
                                            tint = DevTextMuted,
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clickable {
                                                    viewModel.removeProjectFile(index)
                                                    if (selectedFileIndex >= files.size - 1) {
                                                        selectedFileIndex = 0
                                                    }
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Active File Code Editor Box
                    if (files.isNotEmpty() && selectedFileIndex in files.indices) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = editingFileContent,
                                onValueChange = {
                                    editingFileContent = it
                                    viewModel.updateProjectFile(selectedFileIndex, editingFileName, it)
                                },
                                label = { Text("Editing: $editingFileName") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = DevCyan,
                                    unfocusedBorderColor = Color(0xFF334155),
                                    focusedTextColor = Color(0xFF38BDF8),
                                    unfocusedTextColor = Color(0xFF38BDF8)
                                ),
                                textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                            )
                        }
                    }
                }
            }

            // Publish Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.saveProjectFromForm("DRAFT") },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, DevAmber),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DevAmber)
                ) {
                    Text("Save Draft", fontWeight = FontWeight.Bold)
                }

                GradientButton(
                    text = "Publish to Platform",
                    icon = Icons.Default.RocketLaunch,
                    onClick = { viewModel.saveProjectFromForm("PUBLISHED") },
                    modifier = Modifier.weight(1.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // Add File Dialog
    if (showAddFileDialog) {
        var newFileName by remember { mutableStateOf("") }
        var newFileContent by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddFileDialog = false },
            title = { Text("Add New Source File", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newFileName,
                        onValueChange = { newFileName = it },
                        label = { Text("File Name (e.g. script.js, style.css)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newFileContent,
                        onValueChange = { newFileContent = it },
                        label = { Text("Initial Code / Content") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newFileName.isNotBlank()) {
                            viewModel.addProjectFile(newFileName.trim(), newFileName.trim(), newFileContent)
                            showAddFileDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DevCyan)
                ) {
                    Text("Add File", color = Color(0xFF030712), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddFileDialog = false }) {
                    Text("Cancel", color = DevTextSecondary)
                }
            },
            containerColor = DevCardSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun AdminAllProjectsScreen(
    viewModel: DevIsrafilViewModel,
    onNavigate: (Screen) -> Unit
) {
    val allProjects by viewModel.allProjectsAdmin.collectAsState()
    var filterStatus by remember { mutableStateOf("ALL") } // "ALL", "PUBLISHED", "DRAFT"
    var searchQuery by remember { mutableStateOf("") }
    var projectToDelete by remember { mutableStateOf<ProjectEntity?>(null) }
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    val filteredProjects = remember(allProjects, filterStatus, searchQuery) {
        allProjects.filter { proj ->
            val matchesStatus = when (filterStatus) {
                "PUBLISHED" -> proj.status == "PUBLISHED"
                "DRAFT" -> proj.status == "DRAFT"
                else -> true
            }
            val matchesSearch = searchQuery.isBlank() || proj.title.contains(searchQuery, ignoreCase = true) || proj.category.contains(searchQuery, ignoreCase = true)
            matchesStatus && matchesSearch
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.clickable { onNavigate(Screen.ADMIN_DASHBOARD) }
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DevCyan)
                    Text("All Projects (${allProjects.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Button(
                    onClick = { viewModel.prepareNewProjectForm() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DevCyan)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF030712), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Upload", color = Color(0xFF030712), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Filter status tabs
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = filterStatus == "ALL",
                    onClick = { filterStatus = "ALL" },
                    label = { Text("All (${allProjects.size})") }
                )
                FilterChip(
                    selected = filterStatus == "PUBLISHED",
                    onClick = { filterStatus = "PUBLISHED" },
                    label = { Text("Published") }
                )
                FilterChip(
                    selected = filterStatus == "DRAFT",
                    onClick = { filterStatus = "DRAFT" },
                    label = { Text("Drafts") }
                )
            }
        }

        // Projects List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredProjects, key = { it.id }) { proj ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = DevCardSurface,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, DevBorderGlass)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = proj.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                color = if (proj.status == "PUBLISHED") DevEmerald.copy(alpha = 0.2f) else DevAmber.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = proj.status,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (proj.status == "PUBLISHED") DevEmerald else DevAmber,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Category: ${proj.category}", fontSize = 11.sp, color = DevCyan)
                            Text("Downloads: ${proj.downloadCount}", fontSize = 11.sp, color = DevTextSecondary)
                            Text("Date: ${dateFormat.format(Date(proj.createdAt))}", fontSize = 11.sp, color = DevTextMuted)
                        }

                        HorizontalDivider(color = Color(0xFF1E293B))

                        // Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { viewModel.toggleProjectPublishStatus(proj) }
                            ) {
                                Text(
                                    text = if (proj.status == "PUBLISHED") "Unpublish (Draft)" else "Publish",
                                    fontSize = 11.sp,
                                    color = if (proj.status == "PUBLISHED") DevAmber else DevEmerald,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { viewModel.prepareEditProjectForm(proj) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, tint = DevCyan, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Edit", fontSize = 11.sp, color = Color.White)
                                }

                                Button(
                                    onClick = { projectToDelete = proj },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = DevRose, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Delete", fontSize = 11.sp, color = DevRose)
                                }
                            }
                        }
                    }
                }
            }
        }
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
fun AdminCategoriesScreen(
    viewModel: DevIsrafilViewModel,
    onNavigate: (Screen) -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    val allProjects by viewModel.allProjectsAdmin.collectAsState()

    var newCatName by remember { mutableStateOf("") }
    var newCatDesc by remember { mutableStateOf("") }
    var newCatColor by remember { mutableStateOf("#00F0FF") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = DevCyan,
                modifier = Modifier.clickable { onNavigate(Screen.ADMIN_DASHBOARD) }
            )
            Text("Manage Categories", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        // Add Category Card
        Surface(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            color = DevCardSurface,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DevBorderGlass)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Add New Category", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)

                OutlinedTextField(
                    value = newCatName,
                    onValueChange = { newCatName = it },
                    label = { Text("Category Name (e.g. Flutter, Rust)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = newCatDesc,
                    onValueChange = { newCatDesc = it },
                    label = { Text("Category Description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (newCatName.isNotBlank()) {
                            viewModel.addCategory(newCatName, "Terminal", newCatColor, newCatDesc)
                            newCatName = ""
                            newCatDesc = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DevCyan)
                ) {
                    Text("+ Create Category", color = Color(0xFF030712), fontWeight = FontWeight.Bold)
                }
            }
        }

        // Categories List
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Existing Categories (${categories.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)

            categories.forEach { cat ->
                val count = allProjects.count { it.category.equals(cat.name, ignoreCase = true) }
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF0F172A),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF1E293B))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(cat.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Text("$count projects assigned", fontSize = 11.sp, color = DevTextSecondary)
                        }

                        IconButton(onClick = { viewModel.deleteCategory(cat) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DevRose, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStatisticsScreen(
    viewModel: DevIsrafilViewModel,
    onNavigate: (Screen) -> Unit
) {
    val totalDownloads by viewModel.totalDownloadsCount.collectAsState()
    val totalProjects by viewModel.totalProjectsCount.collectAsState()
    val allProjects by viewModel.allProjectsAdmin.collectAsState()
    val downloadLogs by viewModel.downloadLogs.collectAsState()
    val dateFormat = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }

    val topDownloadedProjects = remember(allProjects) {
        allProjects.sortedByDescending { it.downloadCount }.take(5)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = DevCyan,
                modifier = Modifier.clickable { onNavigate(Screen.ADMIN_DASHBOARD) }
            )
            Text("Analytics & Download Stats", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        // Summary Stats Grid
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier.weight(1f),
                color = DevCardSurface,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, DevBorderGlass)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Total Downloads", fontSize = 11.sp, color = DevTextSecondary)
                    Text("${totalDownloads ?: 0}", fontSize = 22.sp, fontWeight = FontWeight.Black, color = DevCyan)
                }
            }

            Surface(
                modifier = Modifier.weight(1f),
                color = DevCardSurface,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, DevBorderGlass)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Total Projects", fontSize = 11.sp, color = DevTextSecondary)
                    Text("$totalProjects", fontSize = 22.sp, fontWeight = FontWeight.Black, color = DevPurple)
                }
            }
        }

        // Top Ranking Projects
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Most Downloaded Projects", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)

            topDownloadedProjects.forEachIndexed { index, proj ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF0F172A),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF1E293B))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("#${index + 1}", fontWeight = FontWeight.Black, color = DevCyan, fontSize = 14.sp)
                            Column {
                                Text(proj.title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp, maxLines = 1)
                                Text("${proj.category} • ${proj.fileSize}", fontSize = 11.sp, color = DevTextMuted)
                            }
                        }

                        Text("${proj.downloadCount} dl", fontWeight = FontWeight.Bold, color = DevEmerald, fontSize = 13.sp)
                    }
                }
            }
        }

        // Recent Download Activity Logs
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Recent Download Events Log", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)

            if (downloadLogs.isEmpty()) {
                Text("No download events logged yet.", fontSize = 12.sp, color = DevTextMuted)
            } else {
                downloadLogs.take(10).forEach { log ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF0B101D),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF1E293B))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(log.projectTitle, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                Text(dateFormat.format(Date(log.timestamp)), fontSize = 10.sp, color = DevTextMuted)
                            }
                            Text(log.fileSize, fontSize = 11.sp, color = DevCyan, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun AdminSettingsScreen(
    viewModel: DevIsrafilViewModel,
    onNavigate: (Screen) -> Unit
) {
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    val adminEmail by viewModel.adminEmail.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = DevCyan,
                modifier = Modifier.clickable { onNavigate(Screen.ADMIN_DASHBOARD) }
            )
            Text("Admin & Platform Settings", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        // Change Password Card
        Surface(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            color = DevCardSurface,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DevBorderGlass)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Security Credentials", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Admin Account: $adminEmail", fontSize = 12.sp, color = DevCyan)

                OutlinedTextField(
                    value = oldPass,
                    onValueChange = { oldPass = it },
                    label = { Text("Current Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = newPass,
                    onValueChange = { newPass = it },
                    label = { Text("New Password (min 6 chars)") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (viewModel.updateAdminPassword(oldPass, newPass)) {
                            oldPass = ""
                            newPass = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DevCyan)
                ) {
                    Text("Update Admin Password", color = Color(0xFF030712), fontWeight = FontWeight.Bold)
                }
            }
        }

        // Database Maintenance
        Surface(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            color = Color(0xFF0F172A),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFF1E293B))
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Database Maintenance", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    "Restore initial sample repositories (Modern Portfolio, Task Manager, Weather App, Flask API, E-commerce, Python Automation).",
                    fontSize = 12.sp,
                    color = DevTextSecondary
                )

                OutlinedButton(
                    onClick = { viewModel.resetDatabase() },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, DevAmber),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DevAmber)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reset to Default Sample Projects")
                }
            }
        }
    }
}
