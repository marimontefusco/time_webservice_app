package com.webservice.TimeApp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import android.view.View
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import com.webservice.TimeApp.databinding.ActivityMainBinding
import com.webservice.TimeApp.data.constant.Const
import com.webservice.TimeApp.data.model.Main
import com.webservice.TimeApp.data.service.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {

    // Variável viewBinding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Config viewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Trocar tema
        binding.trocarTema.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) { //Dark Mode:
                binding.containerPrincipal.setBackgroundColor(Color.parseColor("#091042"))
                binding.containerInfo.setBackgroundResource(R.drawable.container_info_dark_mode)
                binding.txtTituloInfo.setTextColor(Color.parseColor("#091042"))
                binding.txtInfo01.setTextColor(Color.parseColor("#091042"))
                binding.txtInfo02.setTextColor(Color.parseColor("#091042"))
                window.statusBarColor = Color.parseColor("#FFFFFFFF")

            } else { //Light Mode:
                binding.containerPrincipal.setBackgroundColor(Color.parseColor("#9AA3E4"))
                binding.containerInfo.setBackgroundResource(R.drawable.container_info_light_mode)
                binding.txtTituloInfo.setTextColor(Color.parseColor("#091042"))
                binding.txtInfo01.setTextColor(Color.parseColor("#091042"))
                binding.txtInfo02.setTextColor(Color.parseColor("#091042"))
                binding.editBuscarCidade.setTextColor(Color.parseColor("#000D51"))
                window.statusBarColor = Color.parseColor("#FFFFFFFF")
            }
        }

        // Config ação do btn após evento click
        binding.btBuscar.setOnClickListener {
            val city = binding.editBuscarCidade.text.toString() //val cidadee???

            binding.progressBar.visibility = View.VISIBLE

            // Config retrofit -> acesso à api
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build()
                .create(Api::class.java)

            retrofit.weatherMapping(city, Const.API_KEY).enqueue(object : Callback<Main> {
                override fun onResponse(call: Call<Main>, response: Response<Main>) {
                    if(response.isSuccessful) {
                        respostaServidor(response)

                    } else {
                        Toast.makeText(applicationContext, "Cidade inválida", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = GONE
                    }
                }

                override fun onFailure(call: Call<Main>, t: Throwable) {
                    Toast.makeText(applicationContext, "Erro de Servidor", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = GONE
                }
            })

        }

    }

    override fun onResume() {
        super.onResume()
    }

    @SuppressLint("SetTextI18n")
    private fun respostaServidor(response: Response<Main>) {

        val main = response.body()!!.main
        val temp     = main.get("temp").toString()
        val tempMin  = main.get("temp_min").toString()
        val tempMax  = main.get("temp_max").toString()
        val humidity = main.get("humidity").toString()

        val sys = response.body()!!.sys
        val country = sys.get("country").asString
        var pais = ""

        val weather = response.body()!!.weather
        val mainWeather = weather[0].main
        val description = weather[0].description

        val name = response.body()!!.name

        val kelsius = 273.15 //

        val tempC = (temp.toDouble() - kelsius)
        val tempMinC = (tempMin.toDouble() - kelsius)
        val tempMaxC = (tempMax.toDouble() - kelsius)
        val decimalFormat = DecimalFormat("00")

        // Setando país
        if (country.equals("BR")) {
            pais = "Brasil"
        } else if(country.equals("US")) {
            pais = "EUA"
        } else if(country.equals("IT")) {
            pais = "Itália"
        }

        //Setando ícones
        if (mainWeather.equals("Clouds") && description.equals("few clouds")) {
            binding.imgClima.setBackgroundResource(R.drawable.ic_partly_cloudy_day)

        } else if(mainWeather.equals("Clouds") && description.equals("scattered clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.ic_partly_cloudy_day)

        } else if(mainWeather.equals("Clouds") && description.equals("broken clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.ic_cloud)

        } else if(mainWeather.equals("Clouds") && description.equals("overcast clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.ic_cloud)

        } else if(mainWeather.equals("Clear") && description.equals("clear sky")){
            binding.imgClima.setBackgroundResource(R.drawable.ic_clear_night)

        } else if(mainWeather.equals("Snow")){
            binding.imgClima.setBackgroundResource(R.drawable.ic_snowy)

        } else if(mainWeather.equals("Rain")){
            binding.imgClima.setBackgroundResource(R.drawable.ic_rainy)

        } else if(mainWeather.equals("Drizzle")){
            binding.imgClima.setBackgroundResource(R.drawable.ic_rainy)

        } else if(mainWeather.equals("Thunderstorm")){
            binding.imgClima.setBackgroundResource(R.drawable.ic_thunderstorm)
        }

        val descricaoClima = when(description) {
            "clear sky" -> {"Céu Limpo"}
            "few clouds" -> {"Poucas nuvens"}
            "scattered clouds" -> {"Nuvens dispersas"}
            "broken clouds" -> {"Nuvens dispersas"}
            "overcast clouds" -> {"Nublado"}
            "shower rain" -> {"Chuva Forte"}
            "rain" -> {"Chuva"}
            "thunderstorm" -> {"Tempestade"}
            "snow" -> {"Nevando"}

            else -> {"Nublado"}
        }

        //                             "${decimalFormat.format(temp_c)} ºC")

        binding.txtTemperatura.setText("${decimalFormat.format(tempC)} °C")
        binding.txtPaisCidade.setText("$name - $pais")
        binding.txtInfo01.setText("Clima \n$descricaoClima \n\n Umidade\n$humidity %")
        binding.txtInfo02.setText("Temperatura \nMin ${decimalFormat.format(tempMinC)} °C \nMáx ${decimalFormat.format(tempMaxC)} °C")
        binding.progressBar.visibility = GONE

    }

}





