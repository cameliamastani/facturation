package fr.laerce.facturation;

import fr.laerce.facturation.model.Client;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class ListeClients extends HttpServlet {
    Connection conn;


    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {


        try {

            Statement req = conn.createStatement();
            String query = "SELECT clt_num, clt_nom, clt_pnom, clt_loc, clt_pays FROM clients";
            ResultSet res = req.executeQuery(query);
            List<Client> clients = new ArrayList<Client>();
            while(res.next()){
                clients.add(new Client(res.getString("clt_num"),
                        res.getString("clt_nom"),
                        res.getString("clt_pnom"),
                        res.getString("clt_loc"),
                        res.getString("clt_pays")));
            }
            httpServletRequest.setAttribute("clients", clients);
            String laVue = "clients.jsp";
            getServletConfig().getServletContext()
                    .getRequestDispatcher("/WEB-INF/jsp/"+laVue).forward(httpServletRequest, httpServletResponse);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            String user = getInitParameter("user");
            String password = getInitParameter("password");
            String driver = getInitParameter("driver");

            Class.forName("org.postgresql.Driver");
            Properties props = new Properties();
            props.setProperty("user", user);
            props.setProperty("password", password);
            conn = DriverManager.getConnection(driver, props);
            //conn = DriverManager.getConnection("jdbc:postgresql://localhost/exemple", props);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServletException("Pas de Driver SQL");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Pas de connexion à la base");
        }

    }


}
