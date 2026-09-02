package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val slug: String,
    val description: String,
    val category: String,
    val technologies: String, // Comma-separated or JSON list
    val version: String = "1.0.0",
    val fileSize: String = "1.2 MB",
    val thumbnailUrl: String = "",
    val youtubeUrl: String = "",
    val liveDemoUrl: String = "",
    val sourceZipPath: String = "",
    val sourceFilesJson: String = "", // JSON array of ProjectFile
    val downloadCount: Int = 0,
    val status: String = "PUBLISHED", // "PUBLISHED" or "DRAFT"
    val isFeatured: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val techList: List<String>
        get() = technologies.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val iconName: String = "code",
    val colorHex: String = "#00F0FF",
    val description: String = ""
)

@Entity(tableName = "download_logs")
data class DownloadLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: String,
    val projectTitle: String,
    val timestamp: Long = System.currentTimeMillis(),
    val fileSize: String = "1.0 MB"
)

data class ProjectFile(
    val name: String,
    val path: String, // e.g., "index.html" or "assets/style.css"
    val content: String,
    val language: String = "text"
)

data class ContactMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
