package Adaptadores

import Modelo.Lista
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listas_rgb.ListasRecycler
import com.example.listas_rgb.MainActivity
import com.example.listas_rgb.Notas
import com.example.listas_rgb.R

class ListaAdapter(private val listas: ArrayList<Lista>, context: Context) : RecyclerView.Adapter<ListaAdapter.ViewHolder>() {
    companion object {
        //Esta variable estática nos será muy útil para saber cual está marcado o no.
        var seleccionado:Int = -1
        /*
        PAra marcar o desmarcar un elemento de la lista lo haremos diferente a una listView. En la listView el listener
        está en la activity por lo que podemos controlar desde fuera el valor de seleccionado y pasarlo al adapter, asociamos
        el adapter a la listview y resuelto.
        En las RecyclerView usamos para pintar cada elemento la función bind (ver código más abajo, en la clase ViewHolder).
        Esto se carga una vez, solo una vez, de ahí la eficiencia de las RecyclerView. Si queremos que el click que hagamos
        se vea reflejado debemos recargar la lista, para ello forzamos la recarga con el método: notifyDataSetChanged().
         */
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lista = listas[position]
        holder.bind(lista)
        holder.itemView.setOnLongClickListener {
            // Lógica para abrir otro Activity al mantener presionado
            val context = holder.itemView.context
            val intent = Intent(context, ListasRecycler::class.java)
            // Puedes pasar datos adicionales al nuevo Activity si es necesario
            intent.putExtra("nombreLista", lista.nombre)
            intent.putExtra("diaLista", lista.dia)
            context.startActivity(intent)
            true  // Indica que el evento fue manejado
        }
        holder.itemView.setOnClickListener {
            // Además de notificar al objeto, puedes actualizar la variable estática seleccionado
            ListaAdapter.seleccionado = position

            // Notificar al adaptador sobre el cambio para actualizar la vista
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return listas.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreListaTextView: TextView = itemView.findViewById(R.id.txtNombre)
        private val diaTextView: TextView = itemView.findViewById(R.id.ponDia)
        private val imageView: ImageView = itemView.findViewById(R.id.imgImagen)
        fun bind(lista: Lista) {
            nombreListaTextView.text = lista.nombre
            diaTextView.text = lista.dia.toString()

            // Aquí puedes cargar la imagen usando la URL almacenada en tu objeto Lista
            // Asegúrate de tener la lógica para cargar imágenes desde una URL (por ejemplo, usando Glide o Picasso)

            // Por ejemplo, usando Glide:
            imageView.setImageResource(R.drawable.notas)
        }
    }
}