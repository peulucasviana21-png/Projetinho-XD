package com.example.util

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ImageStorageHelper {

  /**
   * Salva a imagem selecionada da galeria no armazenamento interno do aplicativo.
   * Retorna o caminho do arquivo local (ex: "file:///...")
   */
  suspend fun saveImageToInternalStorage(
    context: Context,
    sourceUri: Uri,
    imageType: String // "avatar" ou "banner"
  ): String? = withContext(Dispatchers.IO) {
    try {
      val contentResolver = context.contentResolver
      val inputStream: InputStream? = contentResolver.openInputStream(sourceUri)
      if (inputStream != null) {
        val fileName = "custom_${imageType}_${System.currentTimeMillis()}.png"
        val destinationFile = File(context.filesDir, fileName)

        // Limpar arquivos antigos do mesmo tipo
        context.filesDir.listFiles()?.forEach { file ->
          if (file.name.startsWith("custom_${imageType}_")) {
            file.delete()
          }
        }

        FileOutputStream(destinationFile).use { output ->
          inputStream.copyTo(output)
        }
        inputStream.close()
        return@withContext destinationFile.absolutePath
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return@withContext null
  }
}
