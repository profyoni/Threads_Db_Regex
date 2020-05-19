package com.company;


import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.DoublePredicate;

class WorkerThread implements Runnable
{
    static List<String> syncedList = Collections.synchronizedList(new ArrayList<>());
    static int lastUploadCount = 0;
    String hyperLink;
    WorkerThread(String hyperLink)
    {
        this.hyperLink = hyperLink;
    }
    @Override
    public void run() {
        System.out.println("Hi From "+ this.getClass() + Thread.currentThread().toString());
        // dload a webpage (hyperLink) via JSoup (.html())
        // Scrape page for emails and hyperlinks (Regex, JSoup)
        // add emails and links to a List (emails to store in db, links to crawl)
            // email and links must be stored in a thread safe container

        for (int i=0;i<10_000;i++)
            syncedList.add("");
        //synchronized (syncedList) {
            System.out.println("Bye From " + this.getClass() + Thread.currentThread().toString());
            System.out.println(WorkerThread.syncedList.size());
      //  }
    }
}
@FunctionalInterface
interface MathSolver
{
    double solve(double x, double y);
}

class Adder implements MathSolver
{
    public double solve(double x, double y)
    {
        return x + y;
    }
}
class Multiplier implements MathSolver
{
    public double solve(double x, double y)
    {
        return x * y;
    }
}
class Power implements MathSolver
{
    public double solve(double x, double y)
    {
        return Math.pow(x,y);
    }
}
class Subtractor implements MathSolver
{
    public double solve(double x, double y)
    {
        return x-y;
    }
}

class MathProcessor
{
    public static double [] process(double [] dList, MathSolver ms)
    {
       for (int i=0;i< dList.length;i++)
        {
            dList[i] = ms.solve(dList[i], 2);
        }
        return dList;
    }
}

class Sync
{
    final static Object staticObj = new Object();

    public synchronized void foo()
    {
        // beginning



        // middle


        // end
    }
    public synchronized void bar()
    {

    }
    public void manuallySynced()
    {
        // beginning not thread safe

        synchronized (this)
        {
            // middle
        }

        // not thread safe
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Sync s1 = new Sync();
        Sync s2 = new Sync();

        // t1 calls foo, t2 calls bar...also thread safe
        // 2 threads can access s1 and call foo at same time safely

        // t1 calling bar and t2 calling bar at same time and enter the method's body

        Set<String> set = Collections.synchronizedSet(new HashSet<String>());
        synchronized (set){
            String s = set.stream().findFirst().orElse(null);
        }
        // A B C
        String st = (String) set.toArray()[0];
        synchronized (set) {
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String elt = iterator.next();
                //  another thread may call clear
                if (3 == 4) {
                    iterator.remove(); // only safe method of deleting elt during iteration
                }
            }
        }


            double[] d = {0,1,2,3,4,5,6,7,8,-9};
        MathProcessor.process(d, (a,b) -> a*b); // lambda expression
        MathSolver ms2 = (a,b) -> a+b;
        for(double x: d)
        {
            System.out.println(x);
        }

        Arrays.stream(d)
                .filter(value -> value % 2 == 0)
                .sorted()
                .forEach(System.out::println);


        //dbSelect(20);
        System.exit(0);
        // WorkerPool fixed
        // Executor service
        ExecutorService executor = Executors.newFixedThreadPool(100); // maximum of 10
        System.out.println("Hi From Main"+ Thread.currentThread().toString());
       // Thread [] threadList = new Thread[10];

//        for (int i=0;i<threadList.length;i++) {
//            threadList[i] = new Thread(new WorkerThread(null));
//            threadList[i].start();
//        }
//
//        for (int i=0;i<threadList.length;i++) {
//            threadList[i].join();
//        }
        for (int i=0;i<1000;i++) {
            executor.execute(new WorkerThread(null));
        }
        executor.shutdown();

        while (! executor.isShutdown())
        {
            if (batchReadyForUploadToDb())
            {
                    // db upload
                ArrayList<String> localCopy = new ArrayList<String>(WorkerThread.syncedList);
                WorkerThread.lastUploadCount = 0; // number of uploaded emails
            }
        }
        System.out.println("Bye From Main" + Thread.currentThread().toString());
        System.out.println(WorkerThread.syncedList.size());
    }

    private static boolean batchReadyForUploadToDb() {
        final int BATCH_SIZE = 100;
        return WorkerThread.syncedList.size() - WorkerThread.lastUploadCount > BATCH_SIZE;
    }

    public static void dbSelect(int count) {
// see https://docs.microsoft.com/en-us/sql/connect/jdbc/connection-url-sample?view=sql-server-ver15
        // Create a variable for the connection string.
        String url = "database-1.cbjmpwcdjfmq.us-east-1.rds.amazonaws.com:1433"; // should pull from AWS Secrets Manager, environment variable, Properties class (key, value pairs)
        String connectionUrl =
                String.format("jdbc:sqlserver://%s;databaseName=yourLastName;user=admin;password=enterhere", url);

        try (Connection con = DriverManager.getConnection(connectionUrl); // Autoclosable
            Statement stmt = con.createStatement();) {
            String SQL = "SELECT TOP 10 * FROM Persons"; // CRUD
            ResultSet rs = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.
            while (rs.next()) {
                System.out.println(
                        rs.getInt("PersonID") + " " +
                        rs.getString("FirstName") + " " +
                        rs.getString("LastName"));
            }

            String fruit = JOptionPane.showInputDialog("Enter Frucht");
            String brucha = JOptionPane.showInputDialog("Enter Brucha");
            String insertQuery = String.format("INSERT INTO Persons VALUES ('%s','%s')", fruit, brucha);
            stmt.executeUpdate(insertQuery);

            rs = stmt.executeQuery("SELECT * FROM Persons");

            ResultSetMetaData meta = rs.getMetaData();
            int numberOfColumns = meta.getColumnCount();


            for (int i=1; i <= numberOfColumns; i++)
                System.out.printf("%-8s\t", meta.getColumnName(i));
            System.out.println();

            while ( rs.next() )
            {
                for (int i=1; i <= numberOfColumns; i++)
                    System.out.printf("%-8s\t", rs.getObject(i));
                System.out.println();

            }
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
           e.printStackTrace();
        }
    }
}
