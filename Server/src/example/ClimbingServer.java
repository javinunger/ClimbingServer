package example;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
/**
 * Created by Chris on 11/19/2015.
 */
@Path("/climbingserver")
public class ClimbingServer {

    private static String DB_URI = "jdbc:postgresql://localhost:5432/ClimbingLog";
    private static String DB_LOGINID = "postgres";
    private static String DB_PASSWORD = "postgres";   //NEEDS TO BE CHANGED!

    //STILL NEED:
    //+ POST methods - A way to add Climbers (URGENT)
    //+ DELETE method - For a Climb (Add later; focus on POST method for Climbers)
    //+ PUT method - A way to update Climbs and Climbers (Add later; focus on POST method for Climbers)

    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Produces("text/plain")
    public String getMessage() {
        // Return some cliched textual content
        return "Welcome to On Belay's Server!";
    }

    //Gets all of the current Climbs
    @GET
    @Path("/climbs/")
    @Produces("text/plain")
    public String getClimbs() {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Climb");
            if (resultSet.next()) {
                result += resultSet.getInt(1) + " " + resultSet.getString(3) + " " + resultSet.getString(2) + "\n";
                while(resultSet.next()) {
                    result += resultSet.getInt(1) + " " + resultSet.getString(3) + " " + resultSet.getString(2) + "\n";
                }
            } else {
                result = "nothing found...";
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            result = e.getMessage().toString();
        }
        return result;
    }

    //Gets all of the current Climbers
    @GET
    @Path("/climbers/")
    @Produces("text/plain")
    public String getClimbers() {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Climber");
            if (resultSet.next()) {
                result += resultSet.getString(1) + " " + resultSet.getString(3) + " " + resultSet.getString(4) + "\n";
                while(resultSet.next()) {
                    result += resultSet.getString(1) + " " + resultSet.getString(3) + " " + resultSet.getString(4) + "\n";
                }
            } else {
                result = "nothing found...";
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            result = e.getMessage().toString();
        }
        return result;
    }

    //Gets a specific Climber
    @GET
    @Path("/climber/{id}")
    @Produces("text/plain")
    public String getClimber(@PathParam("id") String id) {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Climber WHERE ID = '" + id + "'");
            if (resultSet.next()) {
                result = resultSet.getString(1) + " " + resultSet.getString(3) + " " + resultSet.getString(4) + "\n";
            } else {
                result = "nothing found...";
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            result = e.getMessage().toString();
        }
        return result;
    }

    //Deletes a Climber
    @DELETE
    @Path("/climber/{id}")
    @Produces("text/plain")
    public String deleteClimber(@PathParam("id") int id) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM Climb WHERE climberID= " + id);
            statement.executeUpdate("DELETE FROM Climber WHERE ID= " + id);
            statement.close();
            connection.close();
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Climber " + id + " deleted...";
    }

    //Adds a new climb
    //ClimbLine will look like this:
    //routeName color difficulty types notes
    //ID, climberID, and timestamp will be provided by the method.
    @POST
    @Path("/climb")
    @Consumes("text/plain")
    @Produces("text/plain")
    public String postClimb(String climbLine) {
        String result;
        StringTokenizer st = new StringTokenizer(climbLine);
        int id = -1;  //ID for Climb
        int climberID = 0;  //FOR NOW. THIS WILL ONLY BE USED IN THE USABILITY TEST! WE NEED TO FIGURE OUT A WAY TO GET THIS FROM THE CLIMBER TABLE

        //Generate a timestamp
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //Some conversions in order to get the timestamp
        Date date = new Date();
        dateFormat.format(date);
        Timestamp timestamp = new Timestamp(date.getTime());

        //Get the entered information
        String routeName = st.nextToken(), color = st.nextToken(), diff = st.nextToken();
        String type = st.nextToken(), notes = st.nextToken();

        //Add it to the database
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(ID) FROM Climb");
            if (resultSet.next()) {
                id = resultSet.getInt(1) + 1;  //ID For Climb
            }
            statement.executeUpdate("INSERT INTO Climb VALUES (" + id + ", " + climberID + ", '" + routeName + "', '" +
                                                                   color + "', '" + diff + "', '" + type
                                                                + "', '" + notes + "', '" + timestamp + "')");
            resultSet.close();
            statement.close();
            connection.close();
            result = "Climb " + id + " added...";
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    //Gets a specific Climb - needs to get all data of Climb, currently only grabs id, name, and climberID
    @GET
    @Path("/climb/{id}")
    @Produces("text/plain")
    public String getClimb(@PathParam("id") String id) {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Climb WHERE ID = '" + id + "'");
            if (resultSet.next()) {
                result = resultSet.getString(1) + " " + resultSet.getString(3) + " " + resultSet.getString(4) + "\n";
            } else {
                result = "nothing found...";
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            result = e.getMessage().toString();
        }
        return result;
    }

    //Deletes a Climb
    @DELETE
    @Path("/climb/{id}")
    @Produces("text/plain")
    public String deleteClimb(@PathParam("id") int id) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM Climb WHERE ID= " + id);
            statement.close();
            connection.close();
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Climb " + id + " deleted...";
    }



    //Main method
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create("http://localhost:9998/");
        server.start();

        System.out.println("Server running");
        System.out.println("Visit: http://localhost:9998/climbingserver");
        System.out.println("Hit return to stop...");
        System.in.read();
        System.out.println("Stopping server");
        server.stop(0);
        System.out.println("Server stopped");
    }


}
