package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CategoryEntity
import com.example.data.model.ContactMessage
import com.example.data.model.ProjectEntity
import com.example.data.model.ProjectFile
import com.example.data.repository.AdminRepository
import com.example.data.repository.ProjectRepository
import com.example.util.AppLanguage
import com.example.util.LanguagePreferences
import com.example.util.Strings
import com.example.util.ZipManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

enum class Screen {
    HOME,
    PROJECTS,
    PROJECT_DETAILS,
    CATEGORIES,
    ABOUT,
    CONTACT,
    ADMIN_LOGIN,
    ADMIN_DASHBOARD,
    ADMIN_UPLOAD,
    ADMIN_ALL_PROJECTS,
    ADMIN_CATEGORIES,
    ADMIN_STATS,
    ADMIN_SETTINGS
}

class DevIsrafilViewModel(application: Application) : AndroidViewModel(application) {
    private val projectRepo = ProjectRepository(application)
    private val adminRepo = AdminRepository(application)
    private val langPrefs = LanguagePreferences(application)

    // Language State
    private val _appLanguage = MutableStateFlow(langPrefs.currentLanguage)
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    // Show Language Selection on first launch or when requested
    private val _showLanguageDialog = MutableStateFlow(!langPrefs.hasSelectedLanguageOnce)
    val showLanguageDialog: StateFlow<Boolean> = _showLanguageDialog.asStateFlow()

    // Navigation
    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _selectedProject = MutableStateFlow<ProjectEntity?>(null)
    val selectedProject: StateFlow<ProjectEntity?> = _selectedProject.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _sortBy = MutableStateFlow("popular") // "popular", "newest", "name"
    val sortBy: StateFlow<String> = _sortBy.asStateFlow()

    // Active Modals / Interactive Overlays
    private val _livePreviewProject = MutableStateFlow<ProjectEntity?>(null)
    val livePreviewProject: StateFlow<ProjectEntity?> = _livePreviewProject.asStateFlow()

    private val _codeExplorerProject = MutableStateFlow<ProjectEntity?>(null)
    val codeExplorerProject: StateFlow<ProjectEntity?> = _codeExplorerProject.asStateFlow()

    private val _downloadingProject = MutableStateFlow<ProjectEntity?>(null)
    val downloadingProject: StateFlow<ProjectEntity?> = _downloadingProject.asStateFlow()

    private val _downloadProgress = MutableStateFlow(0f)
    val downloadProgress: StateFlow<Float> = _downloadProgress.asStateFlow()

    private val _downloadedZipFile = MutableStateFlow<File?>(null)
    val downloadedZipFile: StateFlow<File?> = _downloadedZipFile.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Admin Auth State
    val isAdminLoggedIn = adminRepo.isAdminLoggedIn
    val adminEmail = adminRepo.adminEmail

    // Base Data Flows
    val categories: StateFlow<List<CategoryEntity>> = projectRepo.categories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredProjects: StateFlow<List<ProjectEntity>> = projectRepo.featuredProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProjectsAdmin: StateFlow<List<ProjectEntity>> = projectRepo.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalProjectsCount: StateFlow<Int> = projectRepo.totalProjectsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val publishedProjectsCount: StateFlow<Int> = projectRepo.publishedProjectsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val draftProjectsCount: StateFlow<Int> = projectRepo.draftProjectsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalDownloadsCount: StateFlow<Int?> = projectRepo.totalDownloadsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val downloadLogs = projectRepo.downloadLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered / Searched Public Projects Flow
    @OptIn(ExperimentalCoroutinesApi::class)
    val displayedProjects: StateFlow<List<ProjectEntity>> = combine(
        _searchQuery,
        _selectedCategory,
        _sortBy
    ) { query, category, sort ->
        Triple(query, category, sort)
    }.flatMapLatest { (query, category, sort) ->
        val baseFlow = if (query.isNotBlank()) {
            projectRepo.searchProjects(query.trim())
        } else if (category != null) {
            projectRepo.getProjectsByCategory(category)
        } else {
            projectRepo.publishedProjects
        }

        combine(baseFlow) { projectListArray ->
            var list = projectListArray[0]
            if (category != null && query.isNotBlank()) {
                list = list.filter { it.category.equals(category, ignoreCase = true) }
            }
            when (sort) {
                "popular" -> list.sortedByDescending { it.downloadCount }
                "newest" -> list.sortedByDescending { it.createdAt }
                "name" -> list.sortedBy { it.title.lowercase() }
                else -> list
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin Upload / Edit Form State
    var editProjectId: String? = null
    val formTitle = MutableStateFlow("")
    val formDescription = MutableStateFlow("")
    val formCategory = MutableStateFlow("JavaScript")
    val formTechnologies = MutableStateFlow("HTML, CSS, JavaScript")
    val formVersion = MutableStateFlow("1.0.0")
    val formFileSize = MutableStateFlow("1.2 MB")
    val formThumbnailUrl = MutableStateFlow("hero_coding_banner")
    val formYoutubeUrl = MutableStateFlow("")
    val formLiveDemoUrl = MutableStateFlow("")
    val formFiles = MutableStateFlow<List<ProjectFile>>(
        listOf(
            ProjectFile("index.html", "index.html", "<!DOCTYPE html>\n<html>\n<head><title>New Project</title></head>\n<body>\n  <h1>DEV_Israfil Project</h1>\n</body>\n</html>", "html"),
            ProjectFile("style.css", "style.css", "body { background: #0f172a; color: #fff; }", "css"),
            ProjectFile("script.js", "script.js", "console.log('Project Loaded');", "javascript")
        )
    )
    val formStatus = MutableStateFlow("PUBLISHED")
    val formIsFeatured = MutableStateFlow(false)

    // Language actions
    fun setLanguage(language: AppLanguage) {
        _appLanguage.value = language
        langPrefs.currentLanguage = language
        langPrefs.hasSelectedLanguageOnce = true
        _showLanguageDialog.value = false
        showToast(Strings.langSwitchSuccess(language))
    }

    fun toggleLanguage() {
        val next = if (_appLanguage.value == AppLanguage.BANGLA) AppLanguage.ENGLISH else AppLanguage.BANGLA
        setLanguage(next)
    }

    fun openLanguageDialog() {
        _showLanguageDialog.value = true
    }

    fun dismissLanguageDialog() {
        langPrefs.hasSelectedLanguageOnce = true
        _showLanguageDialog.value = false
    }

    // Navigation actions
    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun openProjectDetails(project: ProjectEntity) {
        _selectedProject.value = project
        _currentScreen.value = Screen.PROJECT_DETAILS
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun setSortBy(sort: String) {
        _sortBy.value = sort
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun openLivePreview(project: ProjectEntity) {
        _livePreviewProject.value = project
    }

    fun closeLivePreview() {
        _livePreviewProject.value = null
    }

    fun openCodeExplorer(project: ProjectEntity) {
        _codeExplorerProject.value = project
    }

    fun closeCodeExplorer() {
        _codeExplorerProject.value = null
    }

    // Download Workflow
    fun startProjectDownload(project: ProjectEntity) {
        viewModelScope.launch {
            _downloadingProject.value = project
            _downloadProgress.value = 0.1f
            delay(200)
            _downloadProgress.value = 0.45f
            delay(300)
            _downloadProgress.value = 0.85f

            val zip = projectRepo.processProjectDownload(project)
            _downloadProgress.value = 1.0f
            delay(200)
            _downloadedZipFile.value = zip

            // Update local selected project if active
            if (_selectedProject.value?.id == project.id) {
                _selectedProject.value = _selectedProject.value?.copy(
                    downloadCount = (_selectedProject.value?.downloadCount ?: 0) + 1
                )
            }
            showToast("Archive ${zip.name} downloaded successfully!")
        }
    }

    fun closeDownloadModal() {
        _downloadingProject.value = null
        _downloadProgress.value = 0f
        _downloadedZipFile.value = null
    }

    // Admin Authentication
    fun adminLogin(email: String, pass: String, rememberMe: Boolean): Boolean {
        val success = adminRepo.login(email, pass, rememberMe)
        if (success) {
            showToast("Welcome back, Admin!")
            _currentScreen.value = Screen.ADMIN_DASHBOARD
        } else {
            showToast("Invalid admin email or password!")
        }
        return success
    }

    fun adminLogout() {
        adminRepo.logout()
        showToast("Logged out from Admin Panel")
        _currentScreen.value = Screen.HOME
    }

    // Admin Upload & Form Handling
    fun prepareNewProjectForm() {
        editProjectId = null
        formTitle.value = ""
        formDescription.value = ""
        formCategory.value = "JavaScript"
        formTechnologies.value = "HTML, CSS, JavaScript"
        formVersion.value = "1.0.0"
        formFileSize.value = "1.2 MB"
        formThumbnailUrl.value = "hero_coding_banner"
        formYoutubeUrl.value = ""
        formLiveDemoUrl.value = ""
        formFiles.value = listOf(
            ProjectFile("index.html", "index.html", "<!DOCTYPE html>\n<html>\n<head><title>DEV_Israfil Project</title></head>\n<body>\n  <h1>Hello World</h1>\n</body>\n</html>", "html"),
            ProjectFile("style.css", "style.css", "body { background: #080c14; color: #00f0ff; }", "css"),
            ProjectFile("script.js", "script.js", "console.log('DEV_Israfil initialized.');", "javascript")
        )
        formStatus.value = "PUBLISHED"
        formIsFeatured.value = false
        _currentScreen.value = Screen.ADMIN_UPLOAD
    }

    fun prepareEditProjectForm(project: ProjectEntity) {
        editProjectId = project.id
        formTitle.value = project.title
        formDescription.value = project.description
        formCategory.value = project.category
        formTechnologies.value = project.technologies
        formVersion.value = project.version
        formFileSize.value = project.fileSize
        formThumbnailUrl.value = project.thumbnailUrl
        formYoutubeUrl.value = project.youtubeUrl
        formLiveDemoUrl.value = project.liveDemoUrl
        formFiles.value = ZipManager.parseProjectFiles(project.sourceFilesJson)
        formStatus.value = project.status
        formIsFeatured.value = project.isFeatured
        _currentScreen.value = Screen.ADMIN_UPLOAD
    }

    fun addProjectFile(name: String, path: String, content: String) {
        val lang = ZipManager.inferLanguage(name)
        val updated = formFiles.value.toMutableList()
        updated.add(ProjectFile(name, path, content, lang))
        formFiles.value = updated
    }

    fun removeProjectFile(index: Int) {
        val updated = formFiles.value.toMutableList()
        if (index in updated.indices) {
            updated.removeAt(index)
            formFiles.value = updated
        }
    }

    fun updateProjectFile(index: Int, name: String, content: String) {
        val updated = formFiles.value.toMutableList()
        if (index in updated.indices) {
            val old = updated[index]
            updated[index] = old.copy(name = name, content = content, language = ZipManager.inferLanguage(name))
            formFiles.value = updated
        }
    }

    fun saveProjectFromForm(status: String) {
        val title = formTitle.value.trim()
        if (title.isBlank()) {
            showToast("Please enter a project title")
            return
        }

        viewModelScope.launch {
            val filesJson = ZipManager.serializeProjectFiles(formFiles.value)
            val slug = title.lowercase().replace("[^a-z0-9]".toRegex(), "-")
            val id = editProjectId ?: "proj-${UUID.randomUUID().toString().take(8)}"

            val project = ProjectEntity(
                id = id,
                title = title,
                slug = slug,
                description = formDescription.value.ifBlank { "A premium coding project created on DEV_Israfil platform." },
                category = formCategory.value,
                technologies = formTechnologies.value,
                version = formVersion.value.ifBlank { "1.0.0" },
                fileSize = formFileSize.value.ifBlank { "1.5 MB" },
                thumbnailUrl = formThumbnailUrl.value.ifBlank { "hero_coding_banner" },
                youtubeUrl = formYoutubeUrl.value,
                liveDemoUrl = formLiveDemoUrl.value,
                sourceFilesJson = filesJson,
                downloadCount = if (editProjectId != null) _selectedProject.value?.downloadCount ?: 0 else 0,
                status = status,
                isFeatured = formIsFeatured.value,
                createdAt = if (editProjectId != null) _selectedProject.value?.createdAt ?: System.currentTimeMillis() else System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            projectRepo.saveProject(project)
            showToast(if (status == "PUBLISHED") "Project published successfully!" else "Draft saved successfully!")
            _currentScreen.value = Screen.ADMIN_ALL_PROJECTS
        }
    }

    fun toggleProjectPublishStatus(project: ProjectEntity) {
        viewModelScope.launch {
            val newStatus = if (project.status == "PUBLISHED") "DRAFT" else "PUBLISHED"
            projectRepo.updateProject(project.copy(status = newStatus, updatedAt = System.currentTimeMillis()))
            showToast("Project status changed to $newStatus")
        }
    }

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch {
            projectRepo.deleteProject(project)
            showToast("Project '${project.title}' deleted.")
        }
    }

    // Category Management
    fun addCategory(name: String, icon: String, colorHex: String, desc: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            val id = "cat-${name.lowercase().replace("[^a-z0-9]".toRegex(), "")}"
            projectRepo.saveCategory(CategoryEntity(id = id, name = name.trim(), iconName = icon, colorHex = colorHex, description = desc))
            showToast("Category '$name' created!")
        }
    }

    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            projectRepo.deleteCategory(category)
            showToast("Category '${category.name}' removed.")
        }
    }

    fun resetDatabase() {
        viewModelScope.launch {
            projectRepo.resetToSeedData()
            showToast("Database restored to default projects & categories!")
        }
    }

    fun updateAdminPassword(oldPass: String, newPass: String): Boolean {
        val success = adminRepo.updatePassword(oldPass, newPass)
        if (success) showToast("Admin password updated successfully!")
        else showToast("Failed to update password. Check old password.")
        return success
    }

    fun submitContactMessage(name: String, email: String, message: String) {
        showToast("Thank you, $name! Your message has been sent to DEV_Israfil support.")
    }
}
