package com.example.encuesta_rgb
class Encuesta (
    val nombre: String,
    val sistemaOperativo: String,
    val especialidad: String,
    val horasEstudio: Int
)    {
        override fun toString(): String {
            return "Nombre: $nombre\nSistema Operativo: $sistemaOperativo\nEspecialidad: $especialidad\nHoras de Estudio: $horasEstudio"
        }
    }