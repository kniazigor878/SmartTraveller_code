package by.iharkaratkou.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.iharkaratkou.beans.Localities;
import by.iharkaratkou.bsnlogic.BusinessLogicUtils;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/FrontController")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BusinessLogicUtils blu = new BusinessLogicUtils();
		ArrayList<ArrayList<String>> localities = new ArrayList<ArrayList<String>>();
		try {
			localities = blu.getLocalities();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		//System.out.println(localities);
		Localities locals = new Localities();
		locals.setLocValues(localities);
		
		//request.setAttribute("localities", localities);
		this.getServletConfig().getServletContext().setAttribute("locals", locals);
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
		//request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
