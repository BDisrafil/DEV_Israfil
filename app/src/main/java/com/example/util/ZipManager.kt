package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.model.ProjectEntity
import com.example.data.model.ProjectFile
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ZipManager {

    fun parseProjectFiles(json: String): List<ProjectFile> {
        if (json.isBlank()) return emptyList()
        val list = mutableListOf<ProjectFile>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val name = obj.optString("name", "file.txt")
                val path = obj.optString("path", name)
                val content = obj.optString("content", "")
                val language = obj.optString("language", inferLanguage(name))
                list.add(ProjectFile(name = name, path = path, content = content, language = language))
            }
        } catch (e: Exception) {
            // fallback if string is plain text
            list.add(ProjectFile("main.txt", "main.txt", json, "text"))
        }
        return list
    }

    fun serializeProjectFiles(files: List<ProjectFile>): String {
        val array = JSONArray()
        for (f in files) {
            val obj = JSONObject()
            obj.put("name", f.name)
            obj.put("path", f.path)
            obj.put("content", f.content)
            obj.put("language", f.language)
            array.put(obj)
        }
        return array.toString()
    }

    fun inferLanguage(filename: String): String {
        val ext = filename.substringAfterLast('.', "").lowercase()
        return when (ext) {
            "html", "htm" -> "html"
            "css", "scss", "sass" -> "css"
            "js", "jsx", "mjs", "ts", "tsx" -> "javascript"
            "php" -> "php"
            "py" -> "python"
            "kt", "kts" -> "kotlin"
            "java" -> "java"
            "json" -> "json"
            "xml" -> "xml"
            "sql" -> "sql"
            "md", "txt" -> "markdown"
            else -> "text"
        }
    }

    /**
     * Creates a real .zip file on disk preserving folder structures,
     * file names, extensions, and standard DEV_Israfil metadata.
     */
    fun createZipPackage(context: Context, project: ProjectEntity): File {
        val safeSlug = project.slug.ifBlank { project.title.lowercase().replace("[^a-z0-9]".toRegex(), "-") }
        val zipFileName = "DEV_Israfil-${safeSlug}.zip"
        val outputDir = File(context.filesDir, "downloads")
        if (!outputDir.exists()) outputDir.mkdirs()

        val zipFile = File(outputDir, zipFileName)
        val files = parseProjectFiles(project.sourceFilesJson)

        val rootFolder = "DEV_Israfil-${project.title.replace(" ", "-")}/"

        ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
            // Always ensure a README.txt exists in root
            var hasReadme = false

            for (f in files) {
                if (f.name.equals("README.txt", ignoreCase = true)) hasReadme = true
                val entryPath = rootFolder + f.path.trimStart('/')
                val entry = ZipEntry(entryPath)
                zos.putNextEntry(entry)
                zos.write(f.content.toByteArray(Charsets.UTF_8))
                zos.closeEntry()
            }

            if (!hasReadme) {
                val readmeEntry = ZipEntry("${rootFolder}README.txt")
                val readmeContent = """
                    ====================================================
                     DEV_Israfil - Premium Coding Resource Platform
                     Project: ${project.title}
                     Category: ${project.category}
                     Version: ${project.version}
                     Author: DEV_Israfil (Israfil Islam)
                     Tagline: "Code. Create. Download."
                    ====================================================

                    DESCRIPTION:
                    ${project.description}

                    TECHNOLOGIES USED:
                    ${project.technologies}

                    HOW TO RUN / USE:
                    1. Extract the contents of this archive.
                    2. If this is a Web/HTML/JS project, open index.html in a web browser.
                    3. If this is a React/Node.js project, run `npm install` followed by `npm run dev`.
                    4. If this is a Python project, run `pip install -r requirements.txt` and `python main.py`.
                    5. If this is a PHP project, host on XAMPP/Apache with MySQL.

                    ====================================================
                    Download more premium coding resources at DEV_Israfil!
                    © 2026 DEV_Israfil. All Rights Reserved.
                """.trimIndent()
                zos.putNextEntry(readmeEntry)
                zos.write(readmeContent.toByteArray(Charsets.UTF_8))
                zos.closeEntry()
            }
        }

        return zipFile
    }

    /**
     * Share or trigger intent for the downloaded ZIP
     */
    fun shareOrOpenZip(context: Context, zipFile: File) {
        try {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                zipFile
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/zip"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "DEV_Israfil Project Archive - ${zipFile.name}")
                putExtra(Intent.EXTRA_TEXT, "Downloaded source code archive from DEV_Israfil platform.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Open or Share Source Code ZIP"))
        } catch (e: Exception) {
            Toast.makeText(context, "Archive saved: ${zipFile.name}", Toast.LENGTH_SHORT).show()
        }
    }
}
