package LibraryManagementService_Async.Utils;

import LibraryManagementService_Async.Models.DBConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Reminder {
    private DBConnection connection = new DBConnection();
    private Timer timer;
    private String transactionID;
    private static List<Reminder> runningReminders = new ArrayList<>();
    private CheckOperationTask task = new CheckOperationTask();
    private boolean hasStarted = false;

    public class CheckOperationTask extends TimerTask {
        @Override
        public void run(){
            connection.execUpdate("transactions", "Action=null", String.format("TransactionID=\"%s\"", transactionID));
            timer.cancel();
            timer = null;
            hasStarted = true;
        }

        public boolean hasRunStarted(){
            return hasStarted;
        }

        public void setHasStarted(boolean value){
            hasStarted = value;
        }
    }


    public Reminder(String transactionID){
        this.transactionID = transactionID;
    }

    public CheckOperationTask getTask(){
        return task;
    }

    public void start(int sec){
        timer = new Timer();
        timer.schedule(new CheckOperationTask(), sec * 1000);
    }

    public void stop(){
        timer.cancel();
        timer = null;
    }

    public String getTransactionID(){
        return this.transactionID;
    }

    public static void addReminders(Reminder reminder){
        runningReminders.add(reminder);
    }

    public static boolean exists(String transactionID){
        for(Reminder r: runningReminders){
            if(transactionID.equals(r.getTransactionID())){
                return true;
            }
        }
        return false;
    }

    public static Reminder getInstance(String transactionID) {
        for (Reminder r : runningReminders) {
            if(transactionID.equals(r.getTransactionID())){
                return r;
            }
        }
        return null;
    }

    public static void removeFromRunningReminders(){
        runningReminders.clear();
    }
}
