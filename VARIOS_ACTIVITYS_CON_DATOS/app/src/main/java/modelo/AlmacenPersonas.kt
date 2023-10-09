package modelo

object AlmacenPersonas {
    var personas = ArrayList <Persona>()
    fun aniadirPersonas(p:Persona){
        personas.add(p)
    }
}