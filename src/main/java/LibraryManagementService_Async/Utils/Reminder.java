package LibraryManagementService_Async.Utils;

import LibraryManagementService_Async.Models.DBConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class that handles TTL configuration for commit/cancel
 */
public class Reminder {

    /**
     * An instance of DBConnection Class that handles all read/write operations within mysql database
     */
    private DBConnection connection = new DBConnection();

    /**
     * Timer instance that keeps track of amount of time passed since generation of an action
     */
    private Timer timer;

    /**
     * Transaction id that helps to identify a given Reminder instance
     */
    private String transactionID;

    /**
     * List of currently running Reminder instances
     */
    private static List<Reminder> runningReminders = new ArrayList<>();

    /**
     * An instance of CheckOperationTask
     */
    private CheckOperationTask task = new CheckOperationTask();

    /**
     * Boolean variable to keep track of status of each timer
     */
    private boolean hasStarted = false;

    /**
     * Inner class that inherits TimerTask Class
     */
    public class CheckOperationTask extends TimerTask {

        /**
         * Method that executes a given task whenever the timer has expired
         */
        @Override
        public void run(){
            connection.execUpdate("transactions", "Action=null", String.format("TransactionID=\"%s\"", transactionID));
            timer.cancel();
            timer = null;
            hasStarted = true;
        }

        /**
         * Method that checks if timer has started
         *
         * @return a boolean variable to indicate the status of currently running timer
         */
        public boolean hasRunStarted(){
            return hasStarted;
        }

        /**
         * @param value sets hasStarted variable to either true or false
         */
        public void setHasStarted(boolean value){
            hasStarted = value;
        }
    }

    /**
     * User-defined constructor for a Reminder Class
     *
     * @param transactionID id of the transaction where timer needs to be applied
     */
    public Reminder(String transactionID){
        this.transactionID = transactionID;
    }

    /**
     * @return CheckOperationTask instance
     */
    public CheckOperationTask getTask(){
        return task;
    }

    /**
     * Method that starts a given timer and schedule the task to be completed after certain period of time
     *
     * @param sec indicates TTL until task needs to be executed
     */
    public void start(int sec){
        timer = new Timer();
        timer.schedule(new CheckOperationTask(), sec * 1000);
    }

    /**
     * Method that stops a given timer within an instance
     */
    public void stop(){
        timer.cancel();
        timer = null;
    }

    /**
     * @return transaction id of an instance
     */
    public String getTransactionID(){
        return this.transactionID;
    }

    /**
     * Method that adds a Reminder instance into the list of running Reminder instances
     *
     * @param reminder a Reminder instance that needs to be added
     */
    public static void addReminders(Reminder reminder){
        runningReminders.add(reminder);
    }

    /**
     * Method that checks existence of a Reminder instance within list of running Reminder instances
     *
     * @param transactionID id that helps to identify a given Reminder instance
     * @return true or false depending on the existence of a Reminder instance with given transaction ID
     */
    public static boolean exists(String transactionID){
        for(Reminder r: runningReminders){
            if(transactionID.equals(r.getTransactionID())){
                return true;
            }
        }
        return false;
    }

    /**
     * Method that retrieves a Reminder instance from list of running Reminder instances
     *
     * @param transactionID id that helps to identify a given Reminder instance
     * @return a Reminder if exists, otherwise, null
     */
    public static Reminder getInstance(String transactionID) {
        for (Reminder r : runningReminders) {
            if(transactionID.equals(r.getTransactionID())){
                return r;
            }
        }
        return null;
    }

    /**
     * Method that removes a running Reminder instance from a list of running Reminder instances
     */
    public static void removeFromRunningReminders(){
        runningReminders.clear();
    }
}
