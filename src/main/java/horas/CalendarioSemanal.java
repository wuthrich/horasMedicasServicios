package horas;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import horas.pojo.Calendariosemanal;
import horas.pojo.Hora;

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
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("calendariosemanal GET " + request.getPathInfo());
		response.setContentType("application/json");
		String ids[] = Util.instancia.getIdFromPath(request).split("/");
		JsonObjectBuilder respuesta;
		try {
			respuesta = ConexionFire.con.calendariosGet(ids[0], Integer.parseInt(ids[1]), Integer.parseInt(ids[2]));
		} catch (Exception e) {
			respuesta = Json.createObjectBuilder();
			respuesta.add("traedatos", false);
			respuesta.add("error", e.getMessage());
		}
		String res = respuesta.build().toString();
		System.out.println("calendarios: " + res);
		response.getWriter().append(res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("calendariosemanal POST");
		response.setContentType("application/json");
		String respuesta = null;

		String servicio = Util.instancia.getIdFromPath(request);
		
		try {
			JsonObject body = Util.instancia.jsonBody(request);
			
			switch (servicio) {
			case "actualizar":
				Calendariosemanal aActualizar = new Calendariosemanal(body);				
				JsonObjectBuilder respuestaTotal = ConexionFire.con.calendarioActualizaTodasHoras(aActualizar);
				respuesta = respuestaTotal.build().toString();
				break;

			default:
				
				Calendariosemanal semana = new Calendariosemanal(body.getJsonObject("calendario"));
				Hora hora = new Hora(body.getJsonObject("hora"));
				Calendariosemanal calendario = ConexionFire.con.calendarioActualizaHora(semana, hora);
				JsonObjectBuilder constructor = Json.createObjectBuilder();
				constructor.add("ok", true);
				constructor.add("calendarioActualizado", calendario.toJson());
				respuesta = constructor.build().toString();
				System.out.println("calendario: "+calendario.toJson().toString());
				break;
			}
			
			
			
		

		} catch (Exception e) {
			e.printStackTrace();

			JsonObjectBuilder constructor = Json.createObjectBuilder();
			constructor.add("ok", false);
			constructor.add("error", e.toString());
			
			if (e instanceof HoraTomadaException) {
				HoraTomadaException datosExtra = (HoraTomadaException) e;
				constructor.add("problema", datosExtra.getProblema().toJson());
				constructor.add("calendarioActualizado", datosExtra.getCalendarioActualizado().toJson());				
			}
			
			respuesta = constructor.build().toString();

		}

		System.out.println(servicio);
		System.out.println(respuesta);
		response.getWriter().append(respuesta);

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("calendariosemanal PUT");
		response.setContentType("application/json");

		response.getWriter().append(Util.instancia.calendarioUpset(request));
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("DELETE");
	}

}
