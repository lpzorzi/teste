package com.springboot.teste.teste.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.springboot.teste.teste.entity.Cliente;
import com.springboot.teste.teste.service.ClienteService;

@RestController
@RequestMapping("/api")
public class ClienteRestController {

	private ClienteService clienteService;
	
	@Autowired
	public ClienteRestController (ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	@GetMapping("/clientes")
	public List<Cliente> listar(){
		return clienteService.listar();
	}
	
	@GetMapping("/clientes/{clienteId}")
	public Cliente procurarId(@PathVariable int clienteId) {
		Cliente cliente = clienteService.procurarId(clienteId);
		if (cliente == null) {
			throw new RuntimeException("Id ("+ clienteId +") não encontrado");
		}
		return cliente;
	}
	
	@PostMapping("/cliente")
	public Cliente salvar(@RequestBody Cliente cliente, HttpServletRequest request) throws IOException {
		URL ip = new URL("https://ipvigilante.com/" + request.getRemoteAddr());
        URLConnection yc = ip.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        String inputLine;
        String respId = "";
        Gson gson = new Gson();
        if ((inputLine = in.readLine()) != null) {
        	boolean controle = false;
        	int cont = 1;
        	while (cont < inputLine.length() - 1) {
        		if(inputLine.substring(cont, cont + 1).equals("{")) {
        			controle = true;
        		}
        		if(controle) {
        			respId = respId + inputLine.substring(cont, cont + 1);
        		}
        		if(inputLine.substring(cont, cont + 1).equals("}")) {
        			controle = false;
        		}
        		cont += 1;
        	}
        }
        in.close();
        IpVigilante ipAux = gson.fromJson(respId, IpVigilante.class);
        URL metaURL = new URL("https://www.metaweather.com/api/location/search/?lattlong=" + ipAux.getLatitude() + "," + ipAux.getLongitude());
        System.out.println(metaURL.toString());
        yc = metaURL.openConnection();
        in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        List<MetaWeather> lista = null;
        if ((inputLine = in.readLine()) != null) {
        	System.out.println(inputLine);
        	Type collectionType = new TypeToken<List<MetaWeather>>() {}.getType();
            lista = gson.fromJson(inputLine, collectionType);
        }
		cliente.setId(0);
		cliente.setCidade(Integer.toString(lista.get(0).getWoeid()));
        metaURL = new URL("https://www.metaweather.com/api/location/"+lista.get(0).getWoeid());
        yc = metaURL.openConnection();
        in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        List<MetaWeatherLocation> listaLoc = null;
        if ((inputLine = in.readLine()) != null) {
        	System.out.println(inputLine);
        	String respMetaLoc = "";
            if ((inputLine = in.readLine()) != null) {
            	boolean controle = false;
            	int cont = 1;
            	while (cont < inputLine.length() - 1) {
            		if(inputLine.substring(cont, cont + 1).equals("[")) {
            			controle = true;
            		}
            		if(controle) {
            			respMetaLoc = respMetaLoc + inputLine.substring(cont, cont + 1);
            		}
            		if(inputLine.substring(cont, cont + 1).equals("]")) {
            			controle = false;
            		}
            		cont += 1;
            	}
            }
            Type collectionType = new TypeToken<List<MetaWeatherLocation>>() {}.getType();
            listaLoc = gson.fromJson(respMetaLoc, collectionType);
        }
        cliente.setMinTemp(listaLoc.get(0).getMin_temp());
        cliente.setMaxTemp(listaLoc.get(0).getMax_temp());
		clienteService.salvar(cliente);
		return cliente;
	}
	
	@PutMapping("/cliente")
	public Cliente atualizar(@RequestBody Cliente cliente) {
		clienteService.salvar(cliente);
		return cliente;
	}
	
	@DeleteMapping("/cliente/{clienteId}")
	public String deletar(@PathVariable int clienteId) {
		Cliente cliente = clienteService.procurarId(clienteId);
		if (cliente == null) {
			throw new RuntimeException("Id ("+ clienteId +") não encontrado");
		}
		clienteService.deletar(clienteId);
		return "Id ("+ clienteId +") foi deletado";
	}
}

class IpVigilante{
	
	private String ipv4;
	private String continent_name;
	private String country_name;
	private String subdivision_1_name;
	private String subdivision_2_name;
	private String city_name;
	private String latitude;
	private String longitude;
	
	public IpVigilante(String ipv4, String continent_name, String country_name, String subdivision_1_name,
			String subdivision_2_name, String city_name, String latitude, String longitude) {
		this.ipv4 = ipv4;
		this.continent_name = continent_name;
		this.country_name = country_name;
		this.subdivision_1_name = subdivision_1_name;
		this.subdivision_2_name = subdivision_2_name;
		this.city_name = city_name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getIpv4() {
		return ipv4;
	}
	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}
	public String getContinent_name() {
		return continent_name;
	}
	public void setContinent_name(String continent_name) {
		this.continent_name = continent_name;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	public String getSubdivision_1_name() {
		return subdivision_1_name;
	}
	public void setSubdivision_1_name(String subdivision_1_name) {
		this.subdivision_1_name = subdivision_1_name;
	}
	public String getSubdivision_2_name() {
		return subdivision_2_name;
	}
	public void setSubdivision_2_name(String subdivision_2_name) {
		this.subdivision_2_name = subdivision_2_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "IpVigilante [ipv4=" + ipv4 + ", continent_name=" + continent_name + ", country_name=" + country_name
				+ ", subdivision_1_name=" + subdivision_1_name + ", subdivision_2_name=" + subdivision_2_name
				+ ", city_name=" + city_name + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
}

class MetaWeather{
	private String title;
	private String location_type;
	private String latt_long;
	private int woeid;
	private int distance;
	public MetaWeather(String title, String location_type, String latt_long, int woeid, int distance) {
		super();
		this.title = title;
		this.location_type = location_type;
		this.latt_long = latt_long;
		this.woeid = woeid;
		this.distance = distance;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLocation_type() {
		return location_type;
	}
	public void setLocation_type(String location_type) {
		this.location_type = location_type;
	}
	public String getLatt_long() {
		return latt_long;
	}
	public void setLatt_long(String latt_long) {
		this.latt_long = latt_long;
	}
	public int getWoeid() {
		return woeid;
	}
	public void setWoeid(int woeid) {
		this.woeid = woeid;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
}

class MetaWeatherLocation{
	private int id;
	private String weather_state_name;
	private String weather_state_abbr;
	private String wind_direction_compass;
	private String created;
	private String applicable_date;
	private float min_temp;
	private float max_temp;
	private float the_temp;
	private float wind_speed;
	private float wind_direction;
	private float air_pressure;
	private int humidity;
	private float visibility;
	private int predictability;
	public MetaWeatherLocation(int id, String weather_state_name, String weather_state_abbr,
			String wind_direction_compass, String created, String applicable_date, float min_temp, float max_temp,
			float the_temp, float wind_speed, float wind_direction, float air_pressure, int humidity, float visibility,
			int predictability) {
		super();
		this.id = id;
		this.weather_state_name = weather_state_name;
		this.weather_state_abbr = weather_state_abbr;
		this.wind_direction_compass = wind_direction_compass;
		this.created = created;
		this.applicable_date = applicable_date;
		this.min_temp = min_temp;
		this.max_temp = max_temp;
		this.the_temp = the_temp;
		this.wind_speed = wind_speed;
		this.wind_direction = wind_direction;
		this.air_pressure = air_pressure;
		this.humidity = humidity;
		this.visibility = visibility;
		this.predictability = predictability;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWeather_state_name() {
		return weather_state_name;
	}
	public void setWeather_state_name(String weather_state_name) {
		this.weather_state_name = weather_state_name;
	}
	public String getWeather_state_abbr() {
		return weather_state_abbr;
	}
	public void setWeather_state_abbr(String weather_state_abbr) {
		this.weather_state_abbr = weather_state_abbr;
	}
	public String getWind_direction_compass() {
		return wind_direction_compass;
	}
	public void setWind_direction_compass(String wind_direction_compass) {
		this.wind_direction_compass = wind_direction_compass;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getApplicable_date() {
		return applicable_date;
	}
	public void setApplicable_date(String applicable_date) {
		this.applicable_date = applicable_date;
	}
	public float getMin_temp() {
		return min_temp;
	}
	public void setMin_temp(float min_temp) {
		this.min_temp = min_temp;
	}
	public float getMax_temp() {
		return max_temp;
	}
	public void setMax_temp(float max_temp) {
		this.max_temp = max_temp;
	}
	public float getThe_temp() {
		return the_temp;
	}
	public void setThe_temp(float the_temp) {
		this.the_temp = the_temp;
	}
	public float getWind_speed() {
		return wind_speed;
	}
	public void setWind_speed(float wind_speed) {
		this.wind_speed = wind_speed;
	}
	public float getWind_direction() {
		return wind_direction;
	}
	public void setWind_direction(float wind_direction) {
		this.wind_direction = wind_direction;
	}
	public float getAir_pressure() {
		return air_pressure;
	}
	public void setAir_pressure(float air_pressure) {
		this.air_pressure = air_pressure;
	}
	public int getHumidity() {
		return humidity;
	}
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	public float getVisibility() {
		return visibility;
	}
	public void setVisibility(float visibility) {
		this.visibility = visibility;
	}
	public int getPredictability() {
		return predictability;
	}
	public void setPredictability(int predictability) {
		this.predictability = predictability;
	}
	
}
