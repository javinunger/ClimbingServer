/**
 * ClimbingServer.java contains all of the server code for our app, On Belay.
 */
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

    private static String DB_URI = "jdbc:postgresql://localhost:5433/ClimbingLog";
    private static String DB_LOGINID = "postgres";   //Login information for the database
    private static String DB_PASSWORD = "LegendOfZelda567890";

    /**
     * Gets a welcome message and puts it on the screen for the user.
     * @return "Welcome to On Belay's Server!", a welcome String.
     */
    @GET
    @Produces("text/plain")
    public String getMessage() {
        // Return a welcome message
        return "Welcome to On Belay's Server!";
    }

    /**
     * Gets all of the Climbs in the database.
     * @return result, a String containing all of the data associated with all of the Climbs in the database.
     */
    @GET
    @Path("/climbs/")
    @Produces("text/plain")
    public String getClimbs() {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Climb");  //Query for the data of the Climbs
            if (resultSet.next()) {
                result += "Name: " + resultSet.getString(3) + "\nColor: "
                        + resultSet.getString(4) + "\nDifficulty: " + resultSet.getString(5) + "\nType: " + resultSet.getString(6) + "\nNotes: "
                        + resultSet.getString(7) + "\nTime: " + resultSet.getTimestamp(8) + ";\n";
                while(resultSet.next()) {
                    result += "Name: " + resultSet.getString(3) + "\nColor: "
                            + resultSet.getString(4) + "\nDifficulty: " + resultSet.getString(5) + "\nType: " + resultSet.getString(6) + "\nNotes: "
                            + resultSet.getString(7) + "\nTime: " + resultSet.getTimestamp(8) + ";\n";
                }
            } else {
                result = "nothing found...";  //No Climbs found
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            result = e.getMessage().toString();  //Something went wrong...
        }
        return result;
    }

    /**
     * Gets all of the Climbs associated with a specific Climber.
     * @param id the integer ID associated with a specific Climber.
     * @return result, a String containing all of the Climb data associated with a specific Climber.
     */
    @GET
    @Path("/climbs/climber/{id}")
    @Produces("text/plain")
    public String getClimbsClimber(@PathParam("id") int id) {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Climb WHERE climberID =" + id); //Order by most recent timestamp
            if (resultSet.next()) {
                result += "Name:\n" + resultSet.getString(3) + "\nColor:\n"
                        + resultSet.getString(4) + "\nDifficulty:\n" + resultSet.getString(5) + "\nType:\n" + resultSet.getString(6) + "\nTime:\n" + resultSet.getTimestamp(8) + ";\n";
                while(resultSet.next()) {
                    result += "Name:\n" + resultSet.getString(3) + "\nColor:\n"
                            + resultSet.getString(4) + "\nDifficulty:\n" + resultSet.getString(5) + "\nType:\n" + resultSet.getString(6) + "\nTime:\n" + resultSet.getTimestamp(8) + ";\n";
                }
            } else {
                result = "nothing found...";  //No Climbs found :(
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            result = e.getMessage().toString();  //Something went wrong...
        }
        return result;
    }

    /**
     * Gets the most recent Climbs associated with a specific Climber.
     * @param id the integer ID associated with the specific Climber.
     * @return result, a String containing two recent Climbs associated with the Climber.
     */
    @GET
    @Path("/climbs/recent/{id}")
    @Produces("text/plain")
    public String getClimbsRecent(@PathParam("id") int id) {
        String result = "";
        int count = 0;  //Stopping point for the most recent climbs
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Climb WHERE climberID =" + id + " ORDER BY time DESC"); //Order by most recent timestamp
            if (resultSet.next()) {
                result += "Name:\n" + resultSet.getString(3) + "\nColor:\n"
                        + resultSet.getString(4) + "\nDifficulty:\n" + resultSet.getString(5) + "\nType:\n" + resultSet.getString(6) + "\nTime:\n" + resultSet.getTimestamp(8) + ";\n";
                while(resultSet.next() && count < 1) {
                    result += "Name:\n" + resultSet.getString(3) + "\nColor:\n"
                            + resultSet.getString(4) + "\nDifficulty:\n" + resultSet.getString(5) + "\nType:\n" + resultSet.getString(6) + "\nTime:\n" + resultSet.getTimestamp(8) + ";\n";
                    count++;
                }
            } else {
                result = "nothing found...";
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            result = e.getMessage().toString();  //Something went wrong...
        }
        return result;
    }

    /**
     * Gets all of the Climbers that are in the database.
     * @return result, which is a String that contains all of the data associated with all of the Climbers in the database.
     */
    @GET
    @Path("/climbers/")
    @Produces("text/plain")
    public String getClimbers() {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Climber");  //Query for the Climbers
            if (resultSet.next()) {
                result += resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(4) + "\n";
                while(resultSet.next()) {
                    result += resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(4) + "\n";
                }
            } else {
                result = "nothing found...";  //None found! :(
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            result = e.getMessage().toString();  //Something went wrong...
        }
        return result;
    }

    /**
     * Gets a specific Climber from the database.
     * @param id the integer ID associated with the Climber.
     * @return result, which is a String that contains all of the Climber data associated with the Climber.
     */
    @GET
    @Path("/climber/{id}")
    @Produces("text/plain")
    public String getClimber(@PathParam("id") int id) {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Climber WHERE ID = " + id); //Query for the Climber data
            if (resultSet.next()) {
                result = resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(4) + "\n";
            } else {
                result = "nothing found...";  //Climber not found :(
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            result = e.getMessage().toString();  //Something went wrong...
        }
        return result;
    }

    /**
     * Gets the friends for a specific Climber.
     * @param id the integer id associated with the specific Climber.
     * @return result, which is a String containing the list of friends that the specific Climber has.
     */
    @GET
    @Path("/friends/{id}")
    public String getFriends(@PathParam("id") int id) {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Climber, ClimberClimber WHERE ClimberClimber.ID= " + id +
                                                            " AND ClimberClimber.friendId= Climber.ID");  //Join query of the Climb and ClimberClimber table
            if(resultSet.next()) {                                                                       //It gets the ids from the ClimberClimber table and the username
                result += resultSet.getString("userName") + ";\n";                                      //of the friend of the specific Climber from the Climber table
                while(resultSet.next()) {
                    result += resultSet.getString("userName") + ";\n";
                }
            } else {
                result = "No friends... :(";  //No friends found :(
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            result = e.getMessage().toString();  //Something went wrong...
        }
        return result;
    }

    /**
     * Gets a specific Climb from the database.
     * @param id the integer ID associated with the Climb.
     * @return result, a String that contains all of the Climb data associated with the specific Climb.
     */
    @GET
    @Path("/climb/{id}")
    @Produces("text/plain")
    public String getClimb(@PathParam("id") int id) {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Climb WHERE ID ="  + id);  //Query for the specific Climb
            if (resultSet.next()) {
                result += "Climb " + resultSet.getInt(1) + ", Climber: " + resultSet.getInt(2) + ", Name: " + resultSet.getString(3) + ", Color: "
                        + resultSet.getString(4) + ", Difficulty: " + resultSet.getString(5) + ", Type: " + resultSet.getString(6) + ", Notes: "
                        + resultSet.getString(7) + ", Time: " + resultSet.getTimestamp(8) + "\n";  //Append the data to the result
            } else {
                result = "nothing found...";
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            result = e.getMessage().toString();  //Something went wrong...
        }
        return result;
    }

    /**
     * Adds a new Climb to the database.
     * (The ID, climberID, and timestamp will be provided by the method).
     * @param climbLine data to store about the climb, should be in this format: routeName color difficulty types notes
     * @return result, which is a String that determines if the Climb was sucessfully added or not.
     */
    @POST
    @Path("/climb")
    @Consumes("text/plain")
    @Produces("text/plain")
    public String postClimb(String climbLine) {
        String result;
        StringTokenizer st = new StringTokenizer(climbLine);
        int id = -1;  //ID for Climb
        int climberID = 0;

        //Generate a timestamp
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //Some conversions in order to get the timestamp
        Date date = new Date();
        dateFormat.format(date);
        Timestamp timestamp = new Timestamp(date.getTime());

        //Get the entered information (Get the full word until a colon)
        String routeName = st.nextToken(":"), color = st.nextToken(":"), diff = st.nextToken(":");
        String type = st.nextToken(":"), notes = st.nextToken(":");

        //Add it to the database
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(ID) FROM Climb");  //Query for the ID for the Climb
            if (resultSet.next()) {
                id = resultSet.getInt(1) + 1;  //ID For Climb
            }
            statement.executeUpdate("INSERT INTO Climb VALUES (" + id + ", " + climberID + ", '" + routeName + "', '" +  //Insert the Climb
                                                                   color + "', '" + diff + "', '" + type
                                                                + "', '" + notes + "', '" + timestamp + "')");
            resultSet.close();
            statement.close();
            connection.close();
            result = "Climb " + id + " added...";   //Climb added successfully
        } catch (Exception e) {
            result = e.getMessage();   //Climb NOT added successfully
        }
        return result;
    }

    //Main method
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create("http://localhost:9998/");  //Create the server
        server.start();  //Start it up

        System.out.println("Server running");
        System.out.println("Visit: http://localhost:9998/climbingserver");
        System.out.println("Hit return to stop...");
        System.in.read();
        System.out.println("Stopping server");
        server.stop(0);
        System.out.println("Server stopped");
    }


}
