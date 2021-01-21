package com.example.scryfallmobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cards : Array<String> = resources.getStringArray(R.array.cards)
        val autoTextView : AutoCompleteTextView = findViewById(R.id.autoCompleteTextView3)
        val arrayAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, cards)
        autoTextView.setAdapter(arrayAdapter)

        val searchButton: Button = findViewById(R.id.btnSearch)

        searchButton.setOnClickListener {
            switchIntent()
            //sendGet(urlBuilder())
        } // setOnClickListener

    } // onCreate()

    private fun switchIntent() {
        val searchText : EditText = findViewById(R.id.autoCompleteTextView3)
        val intent = Intent(this@MainActivity, CardViewerActivity::class.java)

        intent.putExtra("Card_Name", searchText.text.toString())
        startActivity(intent)
    }


    private fun searchCard(url : String) {
        val progressBar : ProgressBar = findViewById(R.id.progressBar)
        val searchButton : Button = findViewById(R.id.btnSearch)
        val searchText : EditText = findViewById(R.id.autoCompleteTextView3)
        val cardName : String = searchText.text.toString()

        searchButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        val intent = Intent(this@MainActivity, CardViewerActivity::class.java)
        intent.putExtra("Card_Name", cardName)
        intent.putExtra("URL", url)
        startActivity(intent)


        // debugTxt.text = cardName.text

//        val path = System.getProperty("user.dir")
//        val file : File = File("raw/opt.json")
//        val dataIS : DataInputStream = DataInputStream(file)
//        debugTxt.text = filesDir

        //val streamReader : InputStreamReader = InputStreamReader(in, "UTF-8")
        //val reader : JsonReader = JsonReader()
    } // searchCard()

    fun urlBuilder () : String {
        val searchText : EditText = findViewById(R.id.autoCompleteTextView3)
        val baseURL : String = "https://api.scryfall.com/cards/named?exact="

        closeKeyboard(searchText)

        return baseURL + searchText.text
    } //urlBuilder

    private fun closeKeyboard(view : View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    } //closeKeyboard


    private fun sendGet(url : String) {
        val debugText : TextView = findViewById(R.id.txtDebug)

        // Instantiate the RequestQueue

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    //debugText.text = "Card: %s".format(response.toString())
                    cardObjectParser(response)
                },
                Response.ErrorListener { error ->
                    debugText.text = "That didn't work!"
                }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    } // sendGet()

    private fun cardObjectParser(card : JSONObject) {
        val debugText : TextView = findViewById(R.id.txtDebug)
        val mtgCard : ImageView = findViewById(R.id.imageView)

        val name : String = card.getString("name")
        val image_uris : JSONObject = card.getJSONObject("image_uris")
        val small : String = image_uris.getString("small")
        val normal : String = image_uris.getString("normal")
        //val small : String = "http://i.imgur.com/7spzG.png"

        searchCard(small)

        /*val imageObjectRequest = ImageRequest(normal,
            Response.Listener<Bitmap> { response ->
                //mtgCard.setImageBitmap(response)
                //debugText.text = "$normal"
                searchCard(response)
            }, 0 , 0, null,
            Response.ErrorListener {
                debugText.text = "That picture didn't load"
            }
        )
        //debugText.text = "$small"
        MySingleton.getInstance(this).addToRequestQueue(imageObjectRequest)*/
    }

} // MainActivity
