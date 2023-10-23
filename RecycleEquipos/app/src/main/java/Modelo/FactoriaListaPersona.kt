package Modelo

object FactoriaListaPersona {
    fun generaLista(cant:Int):ArrayList<Persona> {
        var lista = ArrayList<Persona>(1)
        for(i in 1..cant){
            lista.add(FactoriaPersona.generaPersona())
        }
        return lista
    }
}