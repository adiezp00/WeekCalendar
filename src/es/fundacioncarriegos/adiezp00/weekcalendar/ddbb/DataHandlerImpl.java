package es.fundacioncarriegos.adiezp00.weekcalendar.ddbb;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 *
 * @author adrian
 * @version 0
 *          <p/>
 *          All right reserved.
 * @since 11/06/13 19:55
 */
public class DataHandlerImpl implements DataHandlerInterface {

    /** The connection to DDBB */
    private Connection con;

    /**
     * Constructor of DataHandlerImpl.
     *
     * @param con MySQLConnection
     */
    public DataHandlerImpl(MySQLConnection con) {
        if(con.connect()) {
            this.con = con.getCon();
        }
    }

    /**
     * Getter of all the worker's names in the ddbb.
     *
     * @return worker's names String[]
     */
    @Override
    public String[] getNamesOfWorkers() throws SQLException {
        String[] result;
        // Must be connected! Take care about that.
        if(!con.isClosed()) {
            int i = 0;
            Statement stat = con.createStatement();
            // Final value of getNamesOfWorkers Query
            String WORKERS_NAMES = "SELECT nombre_completo FROM trabajador;";
            ResultSet rs = stat.executeQuery(WORKERS_NAMES);
            result = new String[this.numberOfRows(rs)];
            do {
                // Final value of getter name Query
                String NAME = "nombre_completo";
                result[i++] = rs.getString(NAME);
            } while(rs.next());
            stat.close();
        } else {
            //TODO Valorar el ACK en la GUI.
            result = new String[2];
            result[0] = "Connection closed, you will spoke with your Admin.";
            result[1] = "END_ACK";
        }

        return result;
    }

    private int numberOfRows(ResultSet rs) {
        int counter = 0;
        try {
            while(rs.next()) {
                counter++;
            }
            rs.first();
        } catch (SQLException e) {
            System.exit(0);
        }
        return counter;
    }

    /**
     * Return the week calendar between two dates
     *
     * @param dateInit String
     * @param dateEnd String
     * @param person String
     * @return week calendar String[]
     */
    @Override
    public String[] getWeekCalendar(String dateInit, String dateEnd, String person) throws SQLException {
        String query = "SELECT nombre_alumno,fecha,lugar,hora_fin,hora_inicio,caballo,nombre_proyecto " +
                "FROM alumno_sesion " +
                "WHERE fecha >= '" + dateInit + "' " +
                "AND fecha <= '" + dateEnd + "' " +
                "AND nombre_responsable = '" + person + "';";
        String[] result;
        // Must be connected! Take care about that.
        if(!con.isClosed()) {
            int i = 0;
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);
            result = new String[this.numberOfRows(rs)];
            do {
                // Space for format Strings
                String SPACE = "         ";
                result[i++] = rs.getString("fecha") + SPACE + rs.getString("hora_inicio")
                + SPACE + rs.getString("hora_fin") + SPACE + rs.getString("lugar")
                + SPACE + rs.getString("caballo") + SPACE + rs.getString("nombre_alumno")
                + SPACE + rs.getString("nombre_proyecto") + SPACE + "FIRMA AQUI.";
            } while(rs.next());
            stat.close();
        } else {
            //TODO Valorar el ACK en la GUI.
            result = new String[2];
            result[0] = "Connection closed, you will spoke with your Admin.";
            result[1] = "END_ACK";
        }
        return result;
    }

    /**
     * Close the current connection
     *
     * @return true if all is correct, false in other case.
     */
    @Override
    public boolean close() {
        boolean result;
        try {
            if(!this.con.isClosed()) {
                this.con.close();
            }
            result = true;
        } catch (SQLException e) {
            result = false;
        }
        return result;
    }
}