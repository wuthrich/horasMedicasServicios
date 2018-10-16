package horas;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.servlet.http.HttpServletRequest;

import horas.pojo.Calendariosemanal;
import horas.pojo.Hora;

public class Util {
	
	public final static Util instancia = new Util();
	
	private JsonArray  combos;
	private JsonArray regiones;
	private JsonArray comunas;
	private JsonArray centros;
	private JsonArray especialidades;
	
	private String jsonCombos=
			"[" + 
			"    {" + 
			"        \"key\": 1," + 
			"        \"value\": \"Metropolitana\"," + 
			"        \"tipo\": 0," + 
			"        \"padre\": 0," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 2," + 
			"        \"value\": \"Maule\"," + 
			"        \"tipo\": 0," + 
			"        \"padre\": 0," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 1," + 
			"        \"value\": \"Quinta Normal\"," + 
			"        \"tipo\": 1," + 
			"        \"padre\": 1," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 2," + 
			"        \"value\": \"Recoleta\"," + 
			"        \"tipo\": 1," + 
			"        \"padre\": 1," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 3," + 
			"        \"value\": \"Pudahuel\"," + 
			"        \"tipo\": 1," + 
			"        \"padre\": 1," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 4," + 
			"        \"value\": \"Longaví\"," + 
			"        \"tipo\": 1," + 
			"        \"padre\": 2," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 1," + 
			"        \"value\": \"CESFAM Lo franco\"," + 
			"        \"tipo\": 2," + 
			"        \"padre\": 1," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 2," + 
			"        \"value\": \"Salud familiar Lo Amor\"," + 
			"        \"tipo\": 2," + 
			"        \"padre\": 1," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 3," + 
			"        \"value\": \"Consultorio Recoleta\"," + 
			"        \"tipo\": 2," + 
			"        \"padre\": 2," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 4," + 
			"        \"value\": \"CESFAM Dr. Juan Petrinovic\"," + 
			"        \"tipo\": 2," + 
			"        \"padre\": 2," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 5," + 
			"        \"value\": \"Clínica Siria\"," + 
			"        \"tipo\": 2," + 
			"        \"padre\": 2," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 6," + 
			"        \"value\": \"CESFAM Doctor Gustavo Molina\"," + 
			"        \"tipo\": 2," + 
			"        \"padre\": 3," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 7," + 
			"        \"value\": \"Cosam Pudahuel\"," + 
			"        \"tipo\": 2," + 
			"        \"padre\": 3," + 
			"        \"de\": 0" + 
			"    }," +
			"    {" + 
			"        \"key\": 8," + 
			"        \"value\": \"Cesfam Amanda Benavente\"," + 
			"        \"tipo\": 2," + 
			"        \"padre\": 4," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 1," + 
			"        \"value\": \"Medicina General\"," + 
			"        \"tipo\": 3," + 
			"        \"padre\": 0," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 2," + 
			"        \"value\": \"Cardiología\"," + 
			"        \"tipo\": 3," + 
			"        \"padre\": 0," + 
			"        \"de\": 0" + 
			"    }," + 
			"    {" + 
			"        \"key\": 3," + 
			"        \"value\": \"Dermatología\"," + 
			"        \"tipo\": 3," + 
			"        \"padre\": 0," + 
			"        \"de\": 0" + 
			"    }" + 
			"    " + 
			"]"
			;
	
//	private int ini=0;
	private Util() {
//		ini++;
		System.out.println("Se inicializo Util ");
		ordenarCombos() ;
	}
	
	private List<String> toList(JsonArray json){
	    List<String> list = new ArrayList<String>();
	    if (json != null){
	        for (JsonString elm: json.getValuesAs(JsonString.class)){
	            list.add(elm.getString());
	        }
	    }
	    return list;
	}
	
	private List<Hora> toListHoras(JsonArray json){
		//{"linea":1,"dia":1,"hora":"14:00","ofrecida":true}
	    List<Hora> list = new ArrayList<Hora>();
	    if (json != null){
	       json.forEach(item->{	    	      	   
	    	   list.add(new Hora((JsonObject) item));
	       });
	    }
	    return list;
	}
	
	public void llenarListReflect(Field field, Object instancia, String key, JsonObject bodyJson) throws IllegalArgumentException, IllegalAccessException {
		
		switch (field.getGenericType().toString()) {
		case "java.util.List<java.lang.String>":			
			field.set(instancia, this.toList(bodyJson.getJsonArray(key)));
			break;
			
		case "java.util.List<horas.pojo.Hora>":			
			field.set(instancia, this.toListHoras(bodyJson.getJsonArray(key)));
			break;
			

		default:
			System.out.println("Lista sin mapeo: "+field.getGenericType().toString());
			break;
		}
	}
	
	
	public Object fillPojo(JsonObject bodyJson, Object pojo) {
		//Calendariosemanal semana = new Calendariosemanal();
		bodyJson.keySet().forEach(key->{			   
			   //docData.put(key, calendario.get(key).toString());	
        	 Field field;
        	 int problema=0;
			try {
				//System.out.println("key "+ key);
				field = pojo.getClass().getDeclaredField(key);
				
				switch (field.getType().getName()) {
				case "java.lang.String":
					field.set(pojo, bodyJson.getString(key));
					break;
					
				case "java.lang.Integer":
					field.set(pojo, bodyJson.getInt(key));
					break;
					
				case "java.lang.Boolean":
					field.set(pojo, bodyJson.getBoolean(key));
					break;
					
				case "java.util.List":						
					//System.out.println("Esto es una lista "+key+" del tipo: "+field.getGenericType().toString());
					this.llenarListReflect(field, pojo, key, bodyJson);						
					break;

				default:
					System.out.println("Tipo sin mapeo: "+field.getType().getName());
					break;
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				problema++;
				System.out.println("Se agarro problema "+ problema+ " "+e.getClass());
			} 
        	 
	   });	
		
		return pojo;
	}
	
	public String calendarioUpset(HttpServletRequest request) {
		String body = null, respuesta = null;
			
		try {
			body = request.getReader().lines()
				    .reduce("", (accumulator, actual) -> accumulator + actual);
			
			JsonReader reader = Json.createReader(new StringReader(body));
			JsonObject bodyJson = reader.readObject();     
	        reader.close();	        
	        
	        Calendariosemanal semana = new Calendariosemanal(bodyJson);	        
	        String tiempo=ConexionFire.con.calendarioUpset(semana);	       
	        System.out.println(semana.toJson().toString());
	        JsonObjectBuilder constructor = Json.createObjectBuilder();
	        constructor.add("ok", true);
	        constructor.add("time", tiempo);
	        
	        respuesta= constructor.build().toString();
	        
		} catch (Exception e) {		
			e.printStackTrace();
			
			JsonObjectBuilder constructor = Json.createObjectBuilder();
	        constructor.add("ok", false);
	        constructor.add("error", e.toString());
			respuesta= constructor.build().toString();
		}
		//System.out.println(body);
		return respuesta;
	}
	
	public String personaUpset(HttpServletRequest request) {
		String body = null, respuesta = null;
		try {
			body = request.getReader().lines()
				    .reduce("", (accumulator, actual) -> accumulator + actual);
			
			JsonReader reader = Json.createReader(new StringReader(body));
			JsonObject bodyJson = reader.readObject();     
	        reader.close();  
	        
	        String tiempo=ConexionFire.con.personaUpset(bodyJson);
	        respuesta= "{\"ok\":true, \"time\":\""+tiempo+"\"}";
	        
		} catch (Exception e) {		
			e.printStackTrace();
			respuesta= "{\"ok\":false, \"error\":\""+e.toString()+"\"}";
		}
		System.out.println(body);
		return respuesta;
	}
	
	/**
	 * Ordena los datos almacenados en la variable this.jsonCombos en 3 JsonArray
	 * regiones, comunas, centros
	 */
	private void ordenarCombos() {
		
		JsonReader reader = Json.createReader(new StringReader(jsonCombos));
        combos = reader.readArray();     
        reader.close();        
    
        JsonArrayBuilder regionesbuild = Json.createArrayBuilder();
        JsonArrayBuilder comunasbuild = Json.createArrayBuilder();
        JsonArrayBuilder centrosbuild = Json.createArrayBuilder();
        JsonArrayBuilder especialidadesbuild = Json.createArrayBuilder();
        
        combos.forEach(item -> {
        	int tipo=((JsonObject) item).getInt("tipo");
        	switch (tipo) {
			case 0:
				regionesbuild.add(item);
				break;
				
			case 1:
				comunasbuild.add(item);
				break;
				
			case 2:
				centrosbuild.add(item);
				break;
				
			case 3:
				especialidadesbuild.add(item);
				break;

			default:
				break;
			} 
        });
        
        this.regiones=regionesbuild.build();
        this.comunas=comunasbuild.build();
        this.centros=centrosbuild.build();
        this.especialidades=especialidadesbuild.build();
        
//       System.out.println(regiones.toString());
//       System.out.println(comunas.toString());
//       System.out.println(centros.toString());
        
        
	}
		
	public String tiposCombos(HttpServletRequest request) {
		String tipo= this.getIdFromPath(request);
		if(null==tipo) tipo="";
		String jsonArray;
		switch (tipo) {
		case "R":
			jsonArray=this.regiones.toString();
			break;
			
		case "C":
			jsonArray=this.comunas.toString();
			break;
			
		case "CE":
			jsonArray=this.centros.toString();
			break;
			
		case "E":
			jsonArray=this.especialidades.toString();
			break;

		default:
			jsonArray=this.combos.toString();
			break;
		}
		
		System.out.println(tipo+" "+jsonArray);
		return jsonArray;
	}

	/**
	 * Consigue el id de una url
	 * ej: ../rest/api/[id]
	 * @param request HttpServletRequest
	 * @return id
	 */
	public String getIdFromPath(HttpServletRequest request){		
		String id = request.getPathInfo();
		
		if(null!=id && id.length()>1) {
			id= id.substring(1);
		}else {
			id=null;
		}

		return id;
	}
	
	public String MapToJson( Map<String, Object> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if(null==map) {
			json.append("\"traedatos\":false");
		}else {
			json.append("\"traedatos\":true, ");			
			
			for (Map.Entry<String, Object> par : map.entrySet()) {			
				json.append("\""+par.getKey()+"\":"+par.getValue()+",");
			}
			
			json.deleteCharAt(json.length()-1);
		}
		json.append("}");
		
		return json.toString();
	}
	
	public String catchProblemToJson(Exception e) {
		
		StringBuilder json = new StringBuilder();
		json.append("{");
		
			json.append("\"traedatos\":\"false\", ");
			json.append("\"problema\":\""+e.getMessage()+"\"");
	
		json.append("}");
		
		return json.toString();
	}

}
