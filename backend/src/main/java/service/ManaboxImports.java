package service;
import  java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import config.Config;
import model.ScrId;

public class ManaboxImports {  

    public static ScrId getScryfallID(String name){  //name of the csv file , gives scryfall ids back
    String datapath = Config.DATA_PATH;
    String csvFilepath = (datapath + name + ".csv");
    String csvFile = csvFilepath;
    int lines = 0;
    List<String> data = new ArrayList<>();
        System.out.println(csvFile); 
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String[] header = br.readLine().split(",");
            int length = header.length;
            int idat = 0;
            String line;

            for(int i = 0; i<length;i++){
                if(header[i].trim().equals("Scryfall ID")){     //checks whitch place the id is in 
                    idat = i;
                    break;
                }
            }
            
            while ((line = br.readLine()) != null) {    
                lines ++;    
                String[] lineData = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if(lineData.length == length){          //gets the place only if the lenght is right 
                String id = lineData[idat];
                data.add(id);
            }
        }
    System.out.println(lines + " von " + data.size());
    String[] ids = data.toArray(String[]::new);
    ScrId x = new ScrId(ids);
    return x;
    } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            return null;
        }
}

public static ScrId getScryfallID2(InputStream input){ //name of the csv file , gives scryfall ids back
    
    try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
        int lines = 0;
        List<String> data = new ArrayList<>();

        String[] header = br.readLine().split(",");
            int length = header.length;
            int idat = 0;
            String line;

            for(int i = 0; i<length;i++){
                if(header[i].trim().equals("Scryfall ID")){     //checks whitch place the id is in 
                    idat = i;
                    break;
                }
            }
            
            while ((line = br.readLine()) != null) {    
                lines ++;    
                String[] lineData = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if(lineData.length == length){          //gets the place only if the lenght is right 
                String id = lineData[idat];
                data.add(id);
            }
        }
    System.out.println(lines + " von " + data.size());
    String[] ids = data.toArray(String[]::new);
    ScrId x = new ScrId(ids);
    System.out.println(x);
    return x;
    } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            return null;
        }
}
}


