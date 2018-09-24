package horas;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Util {
	
	public final static Util instancia = new Util();
	
	private int ini=0;
	private Util() {
		ini++;
		System.out.println("Se inicializo Util "+ini);
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
			json.append("\"traedatos\":\"false\"");
		}else {
			json.append("\"traedatos\":\"true\", ");			
			
			for (Map.Entry<String, Object> par : map.entrySet()) {			
				json.append("\""+par.getKey()+"\":\""+par.getValue()+"\",");
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
