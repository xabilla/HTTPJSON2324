package net.vidalibarraquer.httpjson2324

import net.vidalibarraquer.httpjson2324.Personatge
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.View
import android.view.LayoutInflater
import net.vidalibarraquer.httpjson2324.R
import android.widget.TextView
import android.content.Intent
import net.vidalibarraquer.httpjson2324.MostraPersonatge

class MyRecyclerViewAdapter(private val elements: List<Personatge>) :
    RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewElement: View =
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false)
        return ViewHolder(viewElement)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtElement.text = elements[position].name
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtElement: TextView

        init {
            //Quan fem click a la llista mostrem l'element
            itemView.setOnClickListener { v -> mostraElement(v) }
            txtElement = itemView.findViewById(R.id.textElement)
        }

        private fun mostraElement(v: View) {

            // Cridem la pantalla de mostrar personatge i li passem les dades
            val mostrarPersonatge = Intent(v.context, MostraPersonatge::class.java)
            val personatge = elements[adapterPosition]
            mostrarPersonatge.putExtra("name", personatge.name)
            mostrarPersonatge.putExtra("planet", personatge.planet)
            mostrarPersonatge.putExtra("image", personatge.image)
            v.context.startActivity(mostrarPersonatge)
        }
    }
}