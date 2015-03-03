package by.iharkaratkou.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.iharkaratkou.beans.FinalRoutesForPrint;
import by.iharkaratkou.beans.FinalRoutesForView;
import by.iharkaratkou.bsnlogic.BusinessLogicUtils;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/MainController")
public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer start_locality = Integer.parseInt(request.getParameter("start_locality"));
		request.setAttribute("start_locality", start_locality.toString());
		System.out.println(start_locality);
		Integer end_locality = Integer.parseInt(request.getParameter("end_locality"));
		request.setAttribute("end_locality", end_locality.toString());
		System.out.println(end_locality);
		
		BusinessLogicUtils blu = new BusinessLogicUtils();
		List<LinkedHashMap<Integer, List<String>>> finalRoutes = new ArrayList<LinkedHashMap<Integer,List<String>>>();
		List<LinkedHashMap<String, List<String>>> finalRoutesForView = new ArrayList<LinkedHashMap<String,List<String>>>();
		ArrayList<ArrayList<String>> finalRoutesForPrint = new ArrayList<ArrayList<String>>();
		try {
			//localities = blu.getLocalities();
			finalRoutes = blu.getRoutsByLocalities(start_locality,end_locality);
			finalRoutesForView = blu.getRoutesForView(finalRoutes);
			finalRoutesForPrint = blu.getRoutesForPrint(finalRoutes);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		for(LinkedHashMap<Integer, List<String>> routeInFor: finalRoutes){
			System.out.println(routeInFor);
		}
		for(LinkedHashMap<String, List<String>> routeInFor: finalRoutesForView){
			System.out.println(routeInFor);
		}
		
		FinalRoutesForView frfv = new FinalRoutesForView();
		frfv.setFinalRoutesForViewValues(finalRoutesForView);
		FinalRoutesForPrint frfp = new FinalRoutesForPrint();
		frfp.setFinalRoutesForPrintValues(finalRoutesForPrint);
		System.out.println(frfp.getFinalRoutesForPrintValues());
		//request.setAttribute("localities", localities);
		request.setAttribute("frfv", frfv);
		request.setAttribute("frfp", frfp);
		//this.getServletConfig().getServletContext().setAttribute("frfv", frfv);
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
