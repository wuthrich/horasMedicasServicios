package horas;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Persona
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/rest/Persona/*" })
public class Persona extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Persona() {
        super();        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		String json = null;

			 try {	
				 String[] path=Util.instancia.getIdFromPath(request).split("/");
				 if(path.length>1) {
					 
					 switch (path[0]) {
					case "especialistas":
						//Un conjunto de personas
						json=ConexionFire.con.especialistasConHorariosGet(path[1], Integer.parseInt(path[2]) , Integer.parseInt(path[3])).build().toString();
						break;
						
					case "horastomadas":
						//Un conjunto de personas
						json=ConexionFire.con.personaHorasTomadas(path[1]).build().toString();
						break;

					default:
						throw new Exception("No existe este servicio: "+path[0]);
					}
					 
					 
					 
				 }else {
					 //Se busca solo una persona
					 json=Util.instancia.MapToJson( ConexionFire.con.getPersonaAsMap(Util.instancia.getIdFromPath(request)) );
				 }
				 
				 
			} catch (Exception e) {				
				e.printStackTrace();
				json=Util.instancia.catchProblemToJson(e);
			}		
	
		System.out.println("id: "+Util.instancia.getIdFromPath(request)+" respuesta:"+json);
		response.getWriter().append(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");		
		response.getWriter().append(Util.instancia.personaUpset(request));
	}

}
