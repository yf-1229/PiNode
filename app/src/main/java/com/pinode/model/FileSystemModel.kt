package com.pinode.model

/**
 * Represents a file system item (file or directory)
 */
data class FileSystemItem(
    val id: String,
    val name: String,
    val path: String,
    val type: FileSystemItemType,
    val children: List<FileSystemItem> = emptyList(),
    val parent: FileSystemItem? = null,
    val isExpanded: Boolean = false
) {
    val isDirectory: Boolean get() = type == FileSystemItemType.DIRECTORY
    val isFile: Boolean get() = !isDirectory
    
    /**
     * Get the file extension for files
     */
    val extension: String
        get() = if (isFile && name.contains(".")) {
            name.substringAfterLast(".")
        } else {
            ""
        }
}

/**
 * Type of file system item
 */
enum class FileSystemItemType {
    DIRECTORY,
    FILE
}

/**
 * File type enumeration for appropriate icon selection
 */
enum class FileType {
    KOTLIN,
    JAVA,
    XML,
    GRADLE,
    MARKDOWN,
    TEXT,
    JSON,
    YAML,
    PROPERTIES,
    OTHER;
    
    companion object {
        fun fromExtension(extension: String): FileType {
            return when (extension.lowercase()) {
                "kt" -> KOTLIN
                "java" -> JAVA
                "xml" -> XML
                "gradle", "kts" -> GRADLE
                "md" -> MARKDOWN
                "txt" -> TEXT
                "json" -> JSON
                "yml", "yaml" -> YAML
                "properties" -> PROPERTIES
                else -> OTHER
            }
        }
    }
}

/**
 * Sample data for the directory tree
 */
object SampleFileSystemData {
    fun createSampleStructure(): FileSystemItem {
        return FileSystemItem(
            id = "root",
            name = "PiNode",
            path = "/",
            type = FileSystemItemType.DIRECTORY,
            children = listOf(
                FileSystemItem(
                    id = "src",
                    name = "src",
                    path = "/src",
                    type = FileSystemItemType.DIRECTORY,
                    children = listOf(
                        FileSystemItem(
                            id = "main",
                            name = "main",
                            path = "/src/main",
                            type = FileSystemItemType.DIRECTORY,
                            children = listOf(
                                FileSystemItem(
                                    id = "java",
                                    name = "java",
                                    path = "/src/main/java",
                                    type = FileSystemItemType.DIRECTORY,
                                    children = listOf(
                                        FileSystemItem(
                                            id = "com",
                                            name = "com",
                                            path = "/src/main/java/com",
                                            type = FileSystemItemType.DIRECTORY,
                                            children = listOf(
                                                FileSystemItem(
                                                    id = "pinode",
                                                    name = "pinode",
                                                    path = "/src/main/java/com/pinode",
                                                    type = FileSystemItemType.DIRECTORY,
                                                    children = listOf(
                                                        FileSystemItem(
                                                            id = "mainactivity",
                                                            name = "MainActivity.kt",
                                                            path = "/src/main/java/com/pinode/MainActivity.kt",
                                                            type = FileSystemItemType.FILE
                                                        ),
                                                        FileSystemItem(
                                                            id = "directoryview",
                                                            name = "DirectoryTreeView.kt",
                                                            path = "/src/main/java/com/pinode/DirectoryTreeView.kt",
                                                            type = FileSystemItemType.FILE
                                                        )
                                                    )
                                                )
                                            )
                                        )
                                    )
                                ),
                                FileSystemItem(
                                    id = "res",
                                    name = "res",
                                    path = "/src/main/res",
                                    type = FileSystemItemType.DIRECTORY,
                                    children = listOf(
                                        FileSystemItem(
                                            id = "layout",
                                            name = "layout",
                                            path = "/src/main/res/layout",
                                            type = FileSystemItemType.DIRECTORY,
                                            children = listOf(
                                                FileSystemItem(
                                                    id = "activity_main",
                                                    name = "activity_main.xml",
                                                    path = "/src/main/res/layout/activity_main.xml",
                                                    type = FileSystemItemType.FILE
                                                )
                                            )
                                        ),
                                        FileSystemItem(
                                            id = "values",
                                            name = "values",
                                            path = "/src/main/res/values",
                                            type = FileSystemItemType.DIRECTORY,
                                            children = listOf(
                                                FileSystemItem(
                                                    id = "strings",
                                                    name = "strings.xml",
                                                    path = "/src/main/res/values/strings.xml",
                                                    type = FileSystemItemType.FILE
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                ),
                FileSystemItem(
                    id = "gradle",
                    name = "gradle",
                    path = "/gradle",
                    type = FileSystemItemType.DIRECTORY,
                    children = listOf(
                        FileSystemItem(
                            id = "wrapper",
                            name = "wrapper",
                            path = "/gradle/wrapper",
                            type = FileSystemItemType.DIRECTORY,
                            children = listOf(
                                FileSystemItem(
                                    id = "gradle_properties",
                                    name = "gradle-wrapper.properties",
                                    path = "/gradle/wrapper/gradle-wrapper.properties",
                                    type = FileSystemItemType.FILE
                                )
                            )
                        )
                    )
                ),
                FileSystemItem(
                    id = "build_gradle",
                    name = "build.gradle",
                    path = "/build.gradle",
                    type = FileSystemItemType.FILE
                ),
                FileSystemItem(
                    id = "readme",
                    name = "README.md",
                    path = "/README.md",
                    type = FileSystemItemType.FILE
                )
            )
        )
    }
}