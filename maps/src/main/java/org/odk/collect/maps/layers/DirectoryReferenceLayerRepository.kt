package org.odk.collect.maps.layers

import org.odk.collect.maps.MapConfigurator
import org.odk.collect.shared.PathUtils
import org.odk.collect.shared.files.DirectoryUtils.listFilesRecursively
import java.io.File

class DirectoryReferenceLayerRepository(
    private val sharedLayersDirPath: String,
    private val projectLayersDirPath: String,
    private val mapConfigurator: MapConfigurator
) : ReferenceLayerRepository {

    override fun getAll(): List<ReferenceLayer> {
        return getAllFilesWithDirectory()
            .map { ReferenceLayer(getIdForFile(it.second, it.first), it.first, getName(it.first)) }
            .distinctBy { it.id }
            .filter { mapConfigurator.supportsLayer(it.file) }
    }

    override fun get(id: String): ReferenceLayer? {
        val file = getAllFilesWithDirectory().firstOrNull { getIdForFile(it.second, it.first) == id }

        return if (file != null) {
            ReferenceLayer(getIdForFile(file.second, file.first), file.first, getName(file.first))
        } else {
            null
        }
    }

    override fun getSupported(id: String): ReferenceLayer? {
        val layer = get(id)
        if (layer != null && mapConfigurator.supportsLayer(layer.file)) {
            return layer
        }
        return null
    }

    override fun addLayer(file: File, shared: Boolean) {
        if (shared) {
            file.copyTo(File(sharedLayersDirPath, file.name), true)
        } else {
            file.copyTo(File(projectLayersDirPath, file.name), true)
        }
    }

    override fun delete(id: String) {
        get(id)?.file?.delete()
    }

    private fun getAllFilesWithDirectory() = listOf(sharedLayersDirPath, projectLayersDirPath).flatMap { dir ->
        listFilesRecursively(File(dir)).map { file ->
            Pair(file, dir)
        }
    }

    private fun getIdForFile(directoryPath: String, file: File) =
        PathUtils.getRelativeFilePath(directoryPath, file.absolutePath)

    private fun getName(file: File): String {
        return mapConfigurator.getDisplayName(file)
    }
}
