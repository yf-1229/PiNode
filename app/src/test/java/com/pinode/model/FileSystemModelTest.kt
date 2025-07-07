package com.pinode.model

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for FileSystemModel
 */
class FileSystemModelTest {
    
    @Test
    fun testFileSystemItemCreation() {
        val item = FileSystemItem(
            id = "test",
            name = "test.kt",
            path = "/test.kt",
            type = FileSystemItemType.FILE
        )
        
        assertEquals("test", item.id)
        assertEquals("test.kt", item.name)
        assertEquals("/test.kt", item.path)
        assertEquals(FileSystemItemType.FILE, item.type)
        assertTrue(item.isFile)
        assertFalse(item.isDirectory)
        assertEquals("kt", item.extension)
    }
    
    @Test
    fun testDirectoryItemCreation() {
        val item = FileSystemItem(
            id = "folder",
            name = "src",
            path = "/src",
            type = FileSystemItemType.DIRECTORY
        )
        
        assertTrue(item.isDirectory)
        assertFalse(item.isFile)
        assertEquals("", item.extension)
    }
    
    @Test
    fun testFileTypeFromExtension() {
        assertEquals(FileType.KOTLIN, FileType.fromExtension("kt"))
        assertEquals(FileType.JAVA, FileType.fromExtension("java"))
        assertEquals(FileType.XML, FileType.fromExtension("xml"))
        assertEquals(FileType.GRADLE, FileType.fromExtension("gradle"))
        assertEquals(FileType.GRADLE, FileType.fromExtension("kts"))
        assertEquals(FileType.MARKDOWN, FileType.fromExtension("md"))
        assertEquals(FileType.JSON, FileType.fromExtension("json"))
        assertEquals(FileType.YAML, FileType.fromExtension("yml"))
        assertEquals(FileType.YAML, FileType.fromExtension("yaml"))
        assertEquals(FileType.PROPERTIES, FileType.fromExtension("properties"))
        assertEquals(FileType.OTHER, FileType.fromExtension("unknown"))
    }
    
    @Test
    fun testFileTypeFromExtensionCaseInsensitive() {
        assertEquals(FileType.KOTLIN, FileType.fromExtension("KT"))
        assertEquals(FileType.JAVA, FileType.fromExtension("JAVA"))
        assertEquals(FileType.XML, FileType.fromExtension("XML"))
    }
    
    @Test
    fun testSampleDataStructure() {
        val root = SampleFileSystemData.createSampleStructure()
        
        assertNotNull(root)
        assertEquals("PiNode", root.name)
        assertTrue(root.isDirectory)
        assertTrue(root.children.isNotEmpty())
        
        // Check that src directory exists
        val srcDir = root.children.find { it.name == "src" }
        assertNotNull("src directory should exist", srcDir)
        assertTrue("src should be a directory", srcDir!!.isDirectory)
        
        // Check that some files exist
        val hasFiles = hasAnyFiles(root)
        assertTrue("Sample data should contain files", hasFiles)
    }
    
    @Test
    fun testFileExtensionExtraction() {
        val kotlinFile = FileSystemItem(
            id = "test1",
            name = "MainActivity.kt",
            path = "/MainActivity.kt",
            type = FileSystemItemType.FILE
        )
        assertEquals("kt", kotlinFile.extension)
        
        val xmlFile = FileSystemItem(
            id = "test2",
            name = "activity_main.xml",
            path = "/activity_main.xml",
            type = FileSystemItemType.FILE
        )
        assertEquals("xml", xmlFile.extension)
        
        val noExtensionFile = FileSystemItem(
            id = "test3",
            name = "README",
            path = "/README",
            type = FileSystemItemType.FILE
        )
        assertEquals("", noExtensionFile.extension)
    }
    
    private fun hasAnyFiles(item: FileSystemItem): Boolean {
        if (item.isFile) return true
        return item.children.any { hasAnyFiles(it) }
    }
}