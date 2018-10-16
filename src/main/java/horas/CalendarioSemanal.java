package horas;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CalendarioSemanal
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/rest/calendariosemanal/*" })
public class CalendarioSemanal extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CalendarioSemanal() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("calendariosemanal GET "+request.getPathInfo());
		response.setContentType("application/json");
		String ids[]=Util.instancia.getIdFromPath(request).split("/");
		JsonObjectBuilder respuesta;
		try {
			respuesta=ConexionFire.con.calendariosGet(ids[0], Integer.parseInt(ids[1]) , Integer.parseInt(ids[2]));		
		} catch (Exception e) {			
			respuesta = Json.createObjectBuilder();
			respuesta.add("traedatos", false);
			respuesta.add("error", e.getMessage());
		}
		
		response.getWriter().append(respuesta.build().toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		System.out.println("POST");

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("calendariosemanal PUT");
		response.setContentType("application/json");
		
		response.getWriter().append(Util.instancia.calendarioUpset(request));
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("DELETE");
	}

}
