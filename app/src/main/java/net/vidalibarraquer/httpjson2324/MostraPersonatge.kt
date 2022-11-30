package net.vidalibarraquer.httpjson2324

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley

class MostraPersonatge : AppCompatActivity() {
    var personatges: List<Personatge>? = null
    var im1: ImageView? = null
    var tv1: TextView? = null
    var tv2: TextView? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostra_personatge)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        //Recuperem l'extra de les dades anteriors
        val intent: Intent = getIntent()
        val imatge: String? = intent.getStringExtra("image")
        val planeta: String? = intent.getStringExtra("planet")
        val nom: String? = intent.getStringExtra("name")


        //Creem URL
        val URL = "https://www.vidalibarraquer.net/android/$imatge"
        im1 = findViewById<View>(R.id.imageView) as ImageView?
        tv1 = findViewById<View>(R.id.textView) as TextView?
        tv2 = findViewById<View>(R.id.textView2) as TextView?
        val imageRequest = ImageRequest(URL, object : Response.Listener<Bitmap?> {
            override fun onResponse(response: Bitmap?) {
                im1?.setImageBitmap(response)
                tv1?.setText(getString(R.string.nom) + nom)
                tv2?.setText(getString(R.string.planeta) + planeta)
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, null, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Toast.makeText(this@MostraPersonatge, error.toString(), Toast.LENGTH_LONG).show()
            }
        })
        val requestQueue: RequestQueue = Volley.newRequestQueue(this@MostraPersonatge)
        requestQueue.add(imageRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}