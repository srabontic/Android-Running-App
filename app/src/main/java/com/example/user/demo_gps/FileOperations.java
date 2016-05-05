package com.example.user.demo_gps;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Srabonti on 01-05-2016.
 */
/********Created By: Srabonti Chakraborty**************/
//this class hadles all the file operation, we will create objects of this class in all other activities
//to do the file oprations
public class FileOperations {

    /********Created By: Srabonti Chakraborty**************/
    public boolean createPath(File path) throws IOException {
        Log.i("create path :", "True");
        File directory = new File(path, "Trip");
        File file = new File(path, "Trip.txt");

        if (!directory.exists()) {
            // Creates a dir. referenced by this file
            directory.mkdir();
        }
        if (!file.exists()) {
            // Creates a file
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fileIsEmpty(file)) {
            Log.i("Is file empty ? :", "True");

            String lineWrite = "00:30:00|2.000|4500|4.000|2016-05-02|";
            System.out.println(lineWrite);
            saveToDisk(path, lineWrite);

        }
        return true;
    }

    /********Created By: Srabonti Chakraborty**************/
    //function checks if the disk is empty
    private boolean fileIsEmpty(File file) throws IOException {
        Boolean flag = true;
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        while ((line = reader.readLine()) != null) {
            String delimited[] = line.split("\\|");
            if (delimited.length >= 1) {
                flag = false;
            }
        }
        reader.close();
        fileReader.close();
        return flag;

    }

    /********Created By: Srabonti Chakraborty**************/
    //Saves file in the directory
    public boolean saveToDisk(File path, String line) throws IOException {
        File file = new File(path, "Trip.txt");
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter writer = new BufferedWriter(fileWriter);

        writer.write(line);
        writer.newLine();

        fileWriter.flush();
        writer.flush();
        writer.close();
        fileWriter.close();
        return true;
    }

    /********Created By: Srabonti Chakraborty**************/
    //Reads records from the file
    public List<Trip> readFile(File path) throws IOException {
        //Toast.makeText(FileOperations.this, message1, Toast.LENGTH_LONG).show();

        File file = new File(path, "Trip.txt");
        List<Trip> tripList = new ArrayList<Trip>();
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;

        while ((line = reader.readLine()) != null) {
            //Log.i("data: ", line);
            String [] delimited = line.split("\\|");

            Trip p1 = new Trip(delimited[0], delimited[1], delimited[2], delimited[3], delimited[4]);
            tripList.add(p1);

        }
        reader.close();
        fileReader.close();
        return tripList;
    }

    /********Created By: Srabonti Chakraborty**************/
    //Writes recorsd to the file
    public boolean writeList(File file, List<Trip> listToWrite) throws IOException {

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);
        for (Trip p : listToWrite) {
            bufferedWriter.write(p.getDuration() + "|" + p.getDistance() + "|" + p.getNumSteps() + "|" + p.getAvgSpeed() + "|" + p.getDateOfTrip()+"|");
            bufferedWriter.newLine();
        }

        fileWriter.flush();
        bufferedWriter.flush();
        bufferedWriter.close();
        fileWriter.close();
        Log.i("Whole list written : ", "success");
        return true;
    }

    /********Created By: Srabonti Chakraborty**************/
    //deletes record from the file
    public boolean deleteRecord(File path, String dutaion_show, String distance_show, String numsteps_show, String angspeed_show, String date_show) throws IOException {
        File file = new File(path, "Trip.txt");
        boolean isDeleted = false;
        List<Trip> list = new ArrayList<Trip>();
        list = readFile(path);
        file.delete();
        file.createNewFile();
        for(Trip p: list){
            if(p.getDateOfTrip().equalsIgnoreCase(date_show) && p.getAvgSpeed().equalsIgnoreCase(angspeed_show) && p.getNumSteps().equalsIgnoreCase(numsteps_show) && p.getDistance().equalsIgnoreCase(distance_show) && p.getDuration().equalsIgnoreCase(dutaion_show))
            {
                list.remove(p);
                isDeleted = true;
                break;
            }
        }
        if(isDeleted){
            boolean iswritten = writeList(file, list);
            isDeleted = iswritten;
        }
        return isDeleted;
    }

}
