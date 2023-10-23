package Modelo


object FactoriaPersona {
    fun generaPersona() : Persona
    {
        var nombres = listOf<String>("Miguel","Ricardo","Raquel","Cristiano","Danjuma")
        var nacion = listOf<String>("Italia", "España", "Alemania", "Holanda","Croacia")
        var imagenes = listOf<String>("barsa","madrid","milan_","inter_milan");
        var nombrePersonaje = nombres[(nombres.indices).random()]
        var p = Persona(nombrePersonaje, nacion[(nacion.indices).random()],imagenes[(imagenes.indices).random()])

        return p
    }
}