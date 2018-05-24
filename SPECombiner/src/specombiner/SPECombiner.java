/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package specombiner;
//BY JACOB HEIM


import java.io.BufferedReader;
import java.io.File;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.*;
import static javafx.application.Application.launch;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author jth33_000
 */

public class SPECombiner extends Application {
    static String outputLocation="";
    static String inputLocation="";
    static int outputNumber;
    static int numInputFiles=0;
    @Override
    public void start(Stage primaryStage) {
        
        // LABELS ==================================
        Label inputLabel = new Label("SELECT INPUT LOCATION\n");
        inputLabel.setWrapText(true);
        inputLabel.setFont(Font.font("Arial", 22));
        
        Label outputLabel = new Label("SELECT OUTPUT LOCATION\n");
        outputLabel.setWrapText(true);
        outputLabel.setFont(Font.font("Arial", 22));
        
        Label error = new Label("");
        error.setWrapText(true);
        error.setFont(Font.font("Arial", 20));
        
        Label outputNumLabel = new Label("INPUT NUMBER OF FILES TO COMBINE IN EACH NEW FILE THEN PRESS ENTER");
        outputNumLabel.setWrapText(true);
        outputNumLabel.setFont(Font.font("Arial", 16));
        
        Label outputFileLabel = new Label("INPUT NUMBER OF FILES TO CREATE THEN PRESS ENTER");
        outputFileLabel.setWrapText(true);
        outputFileLabel.setFont(Font.font("Ariel", 16));
        
        Label jake = new Label("CREATED BY JACOB HEIM");
        jake.setWrapText(true);
        jake.setFont(Font.font("Arial", 10));
        
        Label inputFiles = new Label("NUMBER OF FILES SELECTED: ");
        inputFiles.setWrapText(true);
        inputFiles.setFont(Font.font("Arial", 16));
        
        Label numFiles = new Label("0");
        numFiles.setWrapText(true);
        numFiles.setFont(Font.font("Ariel", 16));
        
        // NUMBER OF OUTPUT FILES SELECTER=======================
        TextField numOutputPerFile = new TextField("0");
        numOutputPerFile.setPrefColumnCount(7);
        
        
        TextField numNewFiles = new TextField("0");
        numNewFiles.setPrefColumnCount(7);
        
        numNewFiles.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                System.out.println("outputFileNum");
                System.out.println(numOutputPerFile.getText());
                if (tryParseDoIt(numFiles.getText())!=null && 
                        tryParseDoIt(numNewFiles.getText())!=null){
                
                    int inputFilesNum = tryParseDoIt(numFiles.getText());
                
                    int numNewFilesNum = tryParseDoIt(numNewFiles.getText());
                
                    if (numNewFilesNum >0){
                        error.setText("");
                        if (inputFilesNum%numNewFilesNum==0){
                            numOutputPerFile.setText(""+(inputFilesNum/numNewFilesNum));
                        }
                        else
                            numOutputPerFile.setText(""+((inputFilesNum/numNewFilesNum)+1));
                    }
                
                    else{
                        error.setText("NUMBER OF NEW FILES MUST BE GREATER THAN 0");
                        error.setTextFill(Color.RED);
                    }
                }
                else{
                    error.setText("ONLY NUMBERS MAY BE ENTERED");
                    error.setTextFill(Color.RED);
                }
            }
        });
        
        numOutputPerFile.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                System.out.println("outputNum");
                System.out.println(numNewFiles.getText());
                if (tryParseDoIt(numFiles.getText())!=null &&
                        tryParseDoIt(numOutputPerFile.getText())!=null){
                    
                    int inputFilesNum = tryParseDoIt(numFiles.getText());
                    int numOutputPerFileNUM = tryParseDoIt(numOutputPerFile.getText());
                
                    if (numOutputPerFileNUM >0){
                        error.setText("");
                        if (inputFilesNum%numOutputPerFileNUM==0){
                            numNewFiles.setText(""+(inputFilesNum/numOutputPerFileNUM));
                        }
                        else
                            numNewFiles.setText(""+((inputFilesNum/numOutputPerFileNUM)+1));
                    }
                    else{
                        error.setText("NUMBER OF FILES TO COMBINE IN EACH NEW FILE MUST BE GREATER THAN 0");
                        error.setTextFill(Color.RED);
                    }
                }
                else {
                    error.setText("ONLY NUMBERS MAY BE ENTERED");
                    error.setTextFill(Color.RED);
                }
            }
        });
        
        //INPUT BUTTON===========================
        Button input = new Button("No Location selected");
        input.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                // READING IN DIRECTORY==================================
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Open Resource File");
                File selectedDirectory =  chooser.showDialog(primaryStage);
                 
                if(selectedDirectory == null){
                    input.setText("No Location selected");
                }else{
                   input.setText(selectedDirectory.getAbsolutePath());
                }
                if (!input.getText().equals("No Location selected")){
                    inputLocation = input.getText();
                    List<String> files = new ArrayList<String>();
                    try(Stream<Path> paths = Files.walk(Paths.get(inputLocation))) {
                        paths.forEach(filePath -> {
                            if (Files.isRegularFile(filePath)) {
                                // LOOPS THROUGH ALL FILES
                                files.add(filePath.toString());
                            }
                        });
                    }      
                    catch (IOException ex) {
                        Logger.getLogger(SPECombiner.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    inputFiles.setText("NUMBER OF FILES SELECTED: ");
                    numFiles.setText(""+files.size());
                    
                }
            }
        });
        
        //OUTPUT BUTTON===========================
        Button output = new Button("No Location selected");
        output.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                // READING IN DIRECTORY==================================
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Open Resource File");
                File selectedDirectory =  chooser.showDialog(primaryStage);
                 
                if(selectedDirectory == null){
                    output.setText("No Location selected");
                }else{
                   output.setText(selectedDirectory.getAbsolutePath());
                }
            }
        });
        
        
        // LAUNCH BUTTON===============================
        Button btn = new Button();
        btn.setText("Launch");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                if (!input.getText().contains("No Location selected") && 
                        !output.getText().contains("No Location selected")&&
                        !numOutputPerFile.getText().equals("") &&
                        tryParseDoIt(numOutputPerFile.getText())>0){
                    error.setText("loading...");
                    error.setTextFill(Color.RED);
                    try {
                        outputLocation = output.getText();
                        inputLocation = input.getText();
                        outputNumber = tryParseDoIt(numOutputPerFile.getText());
                        error.setText("loading...");
                        error.setTextFill(Color.RED);
                        
                        doIt(primaryStage);
                        
                        error.setTextFill(Color.GREEN);
                        error.setText("DONE");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(SPECombiner.class.getName()).log(Level.SEVERE, null, ex);
                        error.setText("FILE NOT FOUND EXCEPTION");
                        error.setTextFill(Color.RED);
                        System.out.println("file not found exception");
                    } catch (IOException ex) {
                        Logger.getLogger(SPECombiner.class.getName()).log(Level.SEVERE, null, ex);
                        error.setText("IO EXCEPTION");
                        error.setTextFill(Color.RED);
                        System.out.println("ioexception");
                    }
                    catch (NumberFormatException e){
                        error.setText("NUMBER FORMAT EXCEPTION");
                        error.setTextFill(Color.RED);
                        System.out.println("Number format Exception");
                    }
                }
                else {
                    System.out.println("Error");
                    if (input.getText().contains("No Location selected")){
                        error.setText("NO INPUT SELECTED");
                        error.setTextFill(Color.RED);
                    }
                    else if (output.getText().contains("No Location selected")){
                        error.setText("NO OUTPUT SELECTED");
                        error.setTextFill(Color.RED);
                    }
                    else if (numOutputPerFile.getText().equals("")||
                            (!numOutputPerFile.getText().equals("")&&tryParseDoIt(numOutputPerFile.getText())<=0)){
                        error.setText("NUMBER OF FILES MUST BE GREATER THAN 0");
                        error.setTextFill(Color.RED);
                    }
                }
                
            }
        });
        //=======================
        
        HBox text = new HBox();
        text.getChildren().add(numOutputPerFile);
        text.setPrefWidth(15);
        text.setAlignment(Pos.CENTER);
        
        HBox text2 = new HBox();
        text2.getChildren().add(numNewFiles);
        text2.setPrefWidth(15);
        text2.setAlignment(Pos.CENTER);
        
        HBox numFilesBox = new HBox();
        numFilesBox.getChildren().addAll(inputFiles, numFiles);
        numFilesBox.setAlignment(Pos.CENTER);
        
        StackPane root = new StackPane();
        VBox all = new VBox();
        all.setAlignment(Pos.CENTER);
        // add all labels and buttons and stuff to the VBox all=========================
        all.getChildren().addAll(inputLabel, input, numFilesBox,outputLabel, output, outputNumLabel, text, 
                outputFileLabel, text2, btn, error, jake);
        root.getChildren().addAll(all);
        
        Scene scene = new Scene(root, 1000, 500);
        
        primaryStage.setTitle("SPE COMBINER");
        
        
        outputLocation = output.getText();
        
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void doIt(Stage primaryStage) throws FileNotFoundException, IOException{
        
        // FILE LOCATION=============================================
        
        //===================================================
        
        List<Integer> numbers = new ArrayList<Integer>();
        int[] combined = new int[1024];
        List<String> files = new ArrayList<String>();
        try(Stream<Path> paths = Files.walk(Paths.get(inputLocation))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    // LOOPS THROUGH ALL FILES
                    files.add(filePath.toString());
                }
            });
        }
        
        String sectionName="";
        System.out.println(inputLocation);
        int index;
        if (inputLocation.contains("(1"))
            index = inputLocation.indexOf("(1");
        else if (inputLocation.contains("(2"))
            index = inputLocation.indexOf("(2");
        else if (inputLocation.contains("(3"))
            index = inputLocation.indexOf("(3");
        else if (inputLocation.contains("(4"))
            index = inputLocation.indexOf("(4");
        else if (inputLocation.contains("(5"))
            index = inputLocation.indexOf("(5");
        else if (inputLocation.contains("(6"))
            index = inputLocation.indexOf("(6");
        else if (inputLocation.contains("(7"))
            index = inputLocation.indexOf("(7");
        else if (inputLocation.contains("(8"))
            index = inputLocation.indexOf("(8");
        else if (inputLocation.contains("(9"))
            index = inputLocation.indexOf("(9");
        else
            index = inputLocation.indexOf("(0");
        
        sectionName = inputLocation.substring(0, index);
        System.out.println(sectionName);
        while (sectionName.contains("\\")){
            index = sectionName.indexOf("\\");
            sectionName = sectionName.substring(index+1, sectionName.length());
        }
        
        
        
        int newFilesNeeded = outputNumber;
        int fileNum =0;
        String str="";
        int count =-1;
        for (int i = 0; i<files.size(); i++){
            count=-1;
            String test;
            FileReader f = new FileReader(files.get(i));
            BufferedReader bufferReader = new BufferedReader(f);
            test = bufferReader.readLine();
            boolean contain = false;
            while (test != null){
                if (contain){
                    count++;
                    if (count >= 1){
                        test=test.trim();
                        String justNum = test.substring(String.valueOf(count).length());
                        justNum = justNum.trim();
                        if (tryParse(justNum)!=null){
                            numbers.add(tryParse(justNum));
                        }
                    }
                }
                if (test.contains("content")){
                      contain=true;  
                    }
                if (!str.contains("cps")&& i%(newFilesNeeded)==0){
                    str+=test;
                }
                test="";
                test = bufferReader.readLine();
            }
            // COMBINING NUMBERS===================================
            for (int j = 0; j<numbers.size(); j++){
                combined[j] +=numbers.get(j);
            }
            numbers.clear();
            
            
            
            if (i%(newFilesNeeded)==newFilesNeeded-1){
                
                try{
                    // WRITE FILES===================================
                    File newFile = new File(outputLocation+"\\"+sectionName+"Part "+(fileNum+1)+".spe");
                    PrintWriter writer = new PrintWriter(newFile);
                    writer.println(str+"    channel content     ");
                    for (int j=0; j<combined.length;j++){
                        writer.println((j+1)+"      "+combined[j]);
                    }
                    
                    writer.close();
                    fileNum++;
                } catch (IOException e) {
                    // do something
                }
                str ="";
                for (int k =0; k<combined.length;k++){
                    combined[k]=0;
                }
            }
        }
        // =========================
        boolean extra=false;
        for (int h=0;h<combined.length; h++){
            if (combined[h]!=0)
                extra = true;
        }
        if(!str.equals("") || extra){
            try{
            // WRITE FILES===================================
                File newFile = new File(outputLocation+"\\"+sectionName+"Part "+fileNum+".spe");
                PrintWriter writer = new PrintWriter(newFile);
                writer.println(str+"    channel content     ");
                for (int j=0; j<combined.length;j++){
                    writer.println((j+1)+"      "+combined[j]);
                }
                   
                writer.close();
                fileNum++;
            } catch (IOException e) {
                // do something
            }
            str ="";
            for (int k =0; k<combined.length;k++){
                    combined[k]=0;
            }
        }
    }
    
    
    
    
    
    public Integer tryParse(String text) {
        if (isInteger(text)){
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    public Integer tryParseDoIt(String text) {
        if (isInteger(text)){
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return null;
    }
    
    public boolean isInteger(String string) {
    try {
        Integer.valueOf(string);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}