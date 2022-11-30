package net.vidalibarraquer.httpjson2324

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity() : AppCompatActivity() {
    private val elements: MutableList<Personatge> = ArrayList()
    private var queue: RequestQueue? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = MyRecyclerViewAdapter(elements)
        val viewLlista: RecyclerView = findViewById<RecyclerView>(R.id.viewLlista)
        viewLlista.setAdapter(adapter)

        // Quan es fa click al botó es carreguen les dades i s'actualitza
        // el recycler view
        findViewById<View>(R.id.btnLoadData).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (hiHaConnexio()) loadData(
                    viewLlista,
                    "https://www.vidalibarraquer.net/android/DragonBall.json"
                ) else Toast.makeText(
                    getApplicationContext(),
                    R.string.noInternet,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    /**
     * Comprova si el dispositiu té connexió
     * @return cert si en té i fals si no en té
     */
    private fun hiHaConnexio(): Boolean {
        var resultat = false
        val connectivityManager: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Comprovem la versió del dispositiu Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities: NetworkCapabilities? =
                connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork())
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                ) {
                    resultat = true
                }
            }
        } else {
            val activeNetwork: NetworkInfo? = connectivityManager.getActiveNetworkInfo()
            resultat = if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                true
            } else {
                false
            }
        }
        return resultat
    }

    /**
     * Carega les dades que es troben a la url indicada
     * @param viewLlista el recyclerview que s'ha d'actualitzar
     * @param url Una url on hi ha un objecte JSON
     */
    private fun loadData(viewLlista: RecyclerView, url: String) {
        if (queue == null) queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(url,
            object : Response.Listener<JSONObject?> {
                override fun onResponse(response: JSONObject?) {
                    try {
                        // S'esborra la llista
                        elements.clear()
                        // Obtenim l'array que té per nom data
                        val jsonArray: JSONArray? = response?.getJSONArray("data")
                        //Recorrem tots els elements
                        for (i in 0 until jsonArray?.length()!!) {
                            // Afegim el personatge a la llista
                            val personatge = Personatge(
                                jsonArray.getJSONObject(i).getString("name"),
                                jsonArray.getJSONObject(i).getString("planet"),
                                jsonArray.getJSONObject(i).getString("imatge")
                            )
                            elements.add(personatge)
                        }
                        viewLlista.getAdapter()?.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(this@MainActivity, "Error en obtenir dades", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        queue!!.add(request)
    }
}


