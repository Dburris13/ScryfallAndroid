package com.example.scryfallmobile

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.GsonBuilder
import org.json.JSONObject

class CardViewerActivity : AppCompatActivity() {
    lateinit var recyclerMain: RecyclerView

    val autoCompleteOptions : Array<String> = arrayOf("Afghanistan", "Albania", "Algeria", "Andorra", "Angola")
    val cardList : MutableList<Card> = mutableListOf<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_viewer)

        val cardName = intent.getStringExtra("Card_Name").toLowerCase()
        val btnSearch : Button = findViewById(R.id.btnSearch2)
        btnSearch.setOnClickListener {
            additonalCard()
        }

        val cards : Array<String> = resources.getStringArray(R.array.cards)
        val autoTextView : AutoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        val arrayAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, cards)
        autoTextView.setAdapter(arrayAdapter)



        recyclerMain = findViewById(R.id.recyclerView_main)
        recyclerMain.layoutManager = LinearLayoutManager(this)

        Toast.makeText(this, "${cardName}.jpg", Toast.LENGTH_LONG).show()

        //val queue = MySingleton.getInstance(this.applicationContext).requestQueue

        println("Attempting to fetch the first card")
        fetchJson(cardName)
    }

    fun fetchJson(cardName : String) {
        val baseURL : String = "https://api.scryfall.com/cards/named?exact="
        val jsonURL : String = baseURL + cardName

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, jsonURL, null,
                { response ->
                    // cardObjectParser(response) // Downloads the image for the card
                    val body = response.toString()
                    println(body)

                    val gson = GsonBuilder().create()
                    val firstCard : Card = gson.fromJson(body, Card::class.java)
                    cardList.add(firstCard)
                    val homeFeed : HomeFeed = HomeFeed(cardList)

                    recyclerMain.adapter = MainAdapter(homeFeed)

                    println(gson.toString())
                },
                { error ->
                    Toast.makeText(this, "That didn't work", Toast.LENGTH_LONG).show()
                    println("Failed to collected JSON data from url $jsonURL")
                }
        ) //jsonObjectRequest

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun additonalCard() {
        val baseURL : String = "https://api.scryfall.com/cards/named?exact="
        val newCardSearch : AutoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        val newCard : String = newCardSearch.text.toString()
        val jsonURL : String = baseURL + newCard

        closeKeyboard(newCardSearch)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, jsonURL, null,
            { response ->
                // cardObjectParser(response) // Downloads the image for the card
                val body = response.toString()
                println(body)

                val gson = GsonBuilder().create()
                val firstCard : Card = gson.fromJson(body, Card::class.java)
                cardList.add(0, firstCard)
                val homeFeed : HomeFeed = HomeFeed(cardList)

                recyclerMain.adapter = MainAdapter(homeFeed)

                println(gson.toString())
            },
            { error ->
                Toast.makeText(this, "That didn't work", Toast.LENGTH_LONG).show()
                println("Failed to collected JSON data from url $jsonURL")
            }
        ) //jsonObjectRequest

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    private fun searchCard(url : String) {
        val progressBar : ProgressBar = findViewById(R.id.progressBar)
        val searchButton : Button = findViewById(R.id.btnSearch)
        val searchText : EditText = findViewById(R.id.autoCompleteTextView3)
        val cardName : String = searchText.text.toString()

        searchButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

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

        val fullURL = baseURL + searchText.text
        return fullURL
    }

    private fun closeKeyboard(view : View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    private fun sendGet(url : String) {
        val debugText : TextView = findViewById(R.id.txtDebug)

        // Instantiate the RequestQueue

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    //debugText.text = "Card: %s".format(response.toString())
                    cardObjectParser(response)
                },
                { error ->
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

//        val imageObjectRequest = ImageRequest(normal,
//                { response ->
//                    //mtgCard.setImageBitmap(response)
//
//                    //debugText.text = "$normal"
//                    //searchCard(response)
//                    val homeFeed : HomeFeed = HomeFeed(listOf<Card>(Card(cardName, normal)))
//
//                    runOnUiThread {
//                        recycler_main.adapter = MainAdapter(homeFeed)
//                    }
//                }, 0 , 0, null, Bitmap.Config.RGB_565,
//                {
//                    Toast.makeText(this, "That didn't work", Toast.LENGTH_LONG).show()
//                }
//        )

//        MySingleton.getInstance(this).addToRequestQueue(imageObjectRequest)

//        cardImage.setImageResource(drawableID)
//        cardImage.contentDescription = cardImage.toString()

}