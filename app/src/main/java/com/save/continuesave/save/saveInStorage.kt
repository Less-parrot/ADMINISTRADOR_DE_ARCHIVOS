package com.save.continuesave.save

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import java.io.File
import java.io.IOException

class  CreateFoldersAndFiles {

    //*********************************    TEXT FIELD    *******************************************
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun saveInStorage(folder: List<String>) {
        var texto by remember { mutableStateOf("") } //texto que se escriba en el campo de texto
        val contexto = LocalContext.current //contexto de la app
        val folders =
            remember { mutableStateListOf<String>() } // Lista mutable para mantener las carpetas
        var state by remember { mutableStateOf(false) }
        val carpetas = folder + folders

        Box(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxHeight()) {
                OutlinedTextField(
                    value = texto,
                    onValueChange = { userCode ->
                        texto =
                            userCode //hacemos que el texto sea lo que se escriba en el campo de texto
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done //ponemos el icono de hecho
                    ),
                    keyboardActions = KeyboardActions {
                        // Acciones del teclado
                        val inputMethodManager =
                            contexto.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        val currentFocus = (contexto as? Activity)?.currentFocus
                        currentFocus?.let {
                            inputMethodManager.hideSoftInputFromWindow(
                                it.windowToken,
                                0
                            )//cerramos teclado
                            state = true//hacemos que state sea true por un momento
                            folders.add(texto) // Agregar la nueva carpeta a la lista
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Red,
                        cursorColor = Color.Red,
                        focusedBorderColor = Color.Blue,
                        disabledBorderColor = Color.White,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (state) {
                createFolderInDocuments(texto)//le pasamos el texto del campo de texto
                state =
                    false //hacemos que state vuelva a ser false, para no entrar en un bucle infinito
            }
        }
        DropDownFolders(nameFolders = carpetas)//llamamos a la función de menú(carpetas)
    }
    //**********************************************************************************************



    //*******************************      MAIN FUNCTION      **************************************
    @Composable
    fun listFoldersInDocuments() {
        //buscamos la carpeta DOCUMENTS del sistem
        val documentsDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val folders: MutableList<String> =
            mutableListOf() //lista que almacena el nombre de todas las carpetas

        //si existe y es un directorio(carpeta)
        if (documentsDirectory.isDirectory) {
            val listFiles = documentsDirectory.listFiles()//listamos archivos(retorna lista)
            for (file in listFiles) {
                if (file.isDirectory) {
                    folders.add(file.name) //anñadirmo el nombre del directorio a la lista
                }
            }
        }
        saveInStorage(folder = folders)
    }
    //**********************************************************************************************


    @Composable
    fun listFoldersAndFilesInDocuments(nameFolder: String) {
        //hacemos una busqueda de la carpeta DOCUMENTS del sistema
        val documentsDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

        if (documentsDirectory.isDirectory) {
            val listFiles = documentsDirectory.listFiles() //lista de carpetas del sistema

            for (directory in listFiles) { //ahora listamos archivos que estén en las carpetas
                if (directory.isDirectory) {
                    val folderName =
                        directory.name //lista de nombres de los archivos que estén en las carpetas
                    val filesInDirectory = directory.listFiles()

                    for (file in filesInDirectory) {
                        if (file.isFile) {
                            val diccionario =
                                "$folderName: ${file.name}"//nombre de carpeta : archivo

                            //Aquí lo que queremos hacer es un diccionario basandonos en la variable diccionario que tenemos arriba
                            val map = diccionario.lines()
                                .map { it.split(":") }
                                .associate { it[0].trim() to it[1].trim() }


                            val nameFile =
                                map[nameFolder]//usamos el nombre seleccionado de las carpetas para hacer el filtro
                            if (nameFile != null) {
                                Log.d(
                                    "nombreArchivos",
                                    nameFile.toString()
                                ) //imprimimos por LogCat el nombre de el archivo
                            }

                        }
                    }
                }
            }
        }
    }


    @Composable
    fun createFolderInDocuments(nameFolder: String) {
        val contexto = LocalContext.current//contexto de la app
        val folderName = nameFolder //opcional
        //Buscamos en la carpeta DOCUMENTS del sistema
        val folder = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            folderName
        )

        if (!folder.exists()) {//verificamos que no exista la carpeta [nameFolder] en la carpeta DOCUMENTS
            if (folder.mkdirs()) {
                // La carpeta se creó con éxito en la carpeta "Documents".
                Toast.makeText(
                    contexto,
                    "Carpeta $folderName fue creada con exito",
                    Toast.LENGTH_SHORT
                ).show()
                val nameFile = "archivo.py"
                val file = File(
                    folder,
                    nameFile
                )  //¡FALTA: poner que el usuario elija el nombre de el arhcivo a crear!
                if (!file.exists()) {
                    try {
                        if (file.createNewFile()) {
                            // El archivo se creó con éxito.
                            Toast.makeText(
                                contexto,
                                "Archivo '$nameFile' fue creado con exito",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Ocurrió un error al crear el archivo.
                            Toast.makeText(
                                contexto,
                                "Ocurrió un error al crear el archivo '$nameFile'",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: IOException) {
                        // ocurrio un error
                        Toast.makeText(
                            contexto,
                            "Ocurrió un error al crear el archivo '$nameFile'",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // El archivo ya existe.
                    Toast.makeText(
                        contexto,
                        "El archivo '$nameFile' ya existe en la carpeta",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Ocurrió un error al crear la carpeta.
                Toast.makeText(
                    contexto,
                    "Ocurrió un error al crear la Carpeta $folderName",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // La carpeta ya existe.
            Toast.makeText(contexto, "Carpeta $folderName ya existe", Toast.LENGTH_SHORT).show()
        }
    }



    /*Función para abrir el administrador de archivos | talvez implementamos en el futuro*/
    fun openFileManager(context: Context) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"  // Puedes ajustar el tipo de archivo según tus necesidades
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Manejar la excepción si no se encuentra una aplicación de administración de archivos.
            // Puedes mostrar un mensaje al usuario para instalar una aplicación de administración de archivos.
            Toast.makeText(
                context,
                "No se pudo abrir el administrador de arhivos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}