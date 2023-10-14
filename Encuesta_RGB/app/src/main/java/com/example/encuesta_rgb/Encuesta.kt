package com.example.encuesta_rgb
enum class sisOper{
    LINUX,MICORSOFT,MAC
}
enum class espe{
    DAW,DAM,ASIR
}
class Encuesta (
    val nombre: String,
    val sistemaOperativo: sisOper,
    val especialidad: espe,
    val horasEstudio: Int
)    {
        override fun toString(): String {
            return "Nombre: $nombre\nSistema Operativo: $sistemaOperativo\nEspecialidad: $especialidad\nHoras de Estudio: $horasEstudio"
        }
    }