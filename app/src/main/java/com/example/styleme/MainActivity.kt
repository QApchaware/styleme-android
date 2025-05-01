
package com.example.styleme

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private val backendUrl = "https://your-api-url.ngrok.io/restyle" // Replace with actual backend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val eventInput = findViewById<EditText>(R.id.event_input)
        val submitButton = findViewById<Button>(R.id.submit_button)
        val imageView = findViewById<ImageView>(R.id.image_result)

        submitButton.setOnClickListener {
            val eventText = eventInput.text.toString().trim()
            if (eventText.isEmpty()) {
                Toast.makeText(this, "Please enter an event", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("event", eventText)
                .build()

            val request = Request.Builder()
                .url(backendUrl)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val imageUrl = response.request.url.toString()
                    runOnUiThread {
                        Glide.with(this@MainActivity)
                            .load(imageUrl)
                            .into(imageView)
                    }
                }
            })
        }
    }
}
