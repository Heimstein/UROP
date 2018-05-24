package nist.standard.average;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;

/**
 *
 * @author Jacob Heim
 */
public class NISTStandardAverage extends Application {

    static List<String> files = new ArrayList<>();
    static List<Date> fileDates = new ArrayList<>();
    static List<Date> NISTDates = new ArrayList<>();
    static String outputLocation = "";
    static List<List<String>> fileContents = new ArrayList<>();
    static List<List<String>> fileContentsNIST = new ArrayList<>();

    ;
    
    @Override
    public void start(Stage primaryStage) {
        Button actionButton = new Button();
        Button inputNIST = new Button();
        inputNIST.setText("Input NIST folder");

        Label error = new Label("");
        error.setTextFill(Color.RED);
        actionButton.setText("Perform Action");

        Label inputNISTLabel = new Label("NIST folder Location");

        Label outputLabel = new Label("Output Location");
        Button output = new Button();
        output.setText("No Location selected");
        output.setOnAction((ActionEvent event) -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Open Resource File");
            File selectedDirectory = chooser.showDialog(primaryStage);

            if (selectedDirectory == null) {
                output.setText("No Location selected");
            } else {
                output.setText(selectedDirectory.getAbsolutePath());
            }
        });

        inputNIST.setOnAction((ActionEvent event) -> {
            // READING IN DIRECTORY==================================
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Open Resource File");
            File selectedDirectory = chooser.showDialog(primaryStage);

            if (selectedDirectory == null) {
                inputNIST.setText("No Location selected");
            } else {
                inputNIST.setText(selectedDirectory.getAbsolutePath());
            }
            if (!inputNIST.getText().equals("No Location selected")) {
                error.setText("");
            }

        });

        Label inputLabel = new Label("Combined File Location");
        Button input = new Button("No Location selected");

        input.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File inputFile = fileChooser.showOpenDialog(primaryStage);
            input.setText(inputFile.toString());
            try {
                fileToString(inputFile);
            } catch (IOException ex) {
                Logger.getLogger(NISTStandardAverage.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        actionButton.setOnAction((ActionEvent event) -> {
            if (!input.getText().equals("No Location selected") && !inputNIST.getText().equals("No Location selected")
                    && !output.equals("No Location selected")) {
                //inputNIST stuff
                listFile(inputNIST.getText());
                dateNIST();
                //input stuff
                getDates();

                try {
                    fileToStringNIST(files.get(0));
                } catch (IOException ex) {
                    Logger.getLogger(NISTStandardAverage.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        input.setWrapText(true);
        output.setWrapText(true);
        StackPane root = new StackPane();
        VBox total = new VBox();

        total.getChildren().addAll(inputLabel, input, inputNISTLabel, inputNIST, outputLabel, output, actionButton, error);
        total.setAlignment(Pos.CENTER);
        root.getChildren().addAll(total);

        Scene scene = new Scene(root, 600, 350);

        primaryStage.setTitle("NIST calibration");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void fileToStringNIST(String file) throws IOException {
        List<String> originalList = new ArrayList<>();
        String line="";
        int rowLength = -1;
        FileReader f = new FileReader(new File(file));
        System.out.println(f.toString());
        BufferedReader bufferReader = new BufferedReader(f);
        line = bufferReader.readLine();
        originalList.add(line);
        System.out.println(line);
        line = bufferReader.readLine();
        originalList.add(line);
        System.out.println(line);
        line = bufferReader.readLine();
        originalList.add(line);
        System.out.println(line);
        List<List<String>> elements = new ArrayList<>();
        while (line != null) {
            List<String> items = Arrays.asList(line.split("\t"));
            System.out.println(line);
            for (int i = 0; i < items.size(); i++) {
                System.out.println(items.get(i)+items.size());
                items.get(i).trim();
                if (rowLength == -1) {
                    elements.add(new ArrayList<>());
                }
                elements.get(i).add(items.get(i));
            }
            rowLength = elements.size();
            line = bufferReader.readLine();
            originalList.add(line);

        }
        List<List<String>> standardNIST = fullList(originalList);
        
        for (int i=0; i<standardNIST.size();i++){
            for (int j =0; j<standardNIST.get(i).size(); j++){
                System.out.print(standardNIST.get(i).get(j)+"   ");
            }
            System.out.println();
        }
        
        /*{
        for (int i = 0; i < files.size(); i++) {
            List<List<String>> NISTContents = new ArrayList<>();
            String temp = "";
            FileReader f = new FileReader(files.get(i));
            BufferedReader bufferReader = new BufferedReader(f);
            temp = bufferReader.readLine();
            int count = 0;
            while (temp != null) {
                String[] arrayString = temp.split(",");
                fileContentsNIST.add(new ArrayList<>());
                for (int j = 0; j < arrayString.length; j++) {
                    fileContentsNIST.get(count).add(arrayString[j].replace(',', ' ').trim());
                }
                temp = bufferReader.readLine();
                count++;
            }
        }
         */
    }

    public List<List<String>> fullList(List<String> originalList) {
        List<List<String>> original = new ArrayList<>();
        for (int i = 0; i < originalList.size() - 1; i++) {
            String[] temp = originalList.get(i).split("\t");
            List<String> temp2 = new ArrayList<>();
            for (int j = 0; j < temp.length; j++) {
                temp[j] = temp[j].trim();
                temp2.add(temp[j] + "\t");
            }
            original.add(temp2);
        }
        int columnLength = original.size();
        List<List<String>> fullList = new ArrayList<>();
        for (int i = 0; i < columnLength; i++) {
            fullList.add(new ArrayList<>());
        }
        fullList.get(0).add(original.get(0).toString());
        fullList.get(1).add(original.get(1).toString());
        String elementalLine = "filename	position (mm)	sample surface	validity	"
                + "E-gain	E-offset	F-slope	F-offset	kcps	MSE	Mg	Al	Si	P	S	Cl	"
                + "Ar	K	Ca	Sc	Ti	V	Cr	Mn	Fe	Co	Ni	Cu	Zn	Ga	Ge	"
                + "As	Se	Br	Kr	Rb	Sr	Y	Zr	Nb	Mo	Tc	Tu	Rh	Pd	Ag	"
                + "Cd	In	Sn	Sb	Te	I	Xe	Cs	Ba	La	Ce	Pr	Nd	Pm	Sm	"
                + "Eu	Gd	Tb	Dy	Ho	Er	Tm	Yb	Lu	Hf	Ta	W	Re	Os	Ir	"
                + "Pt	Au	Hg	Tl	Pb	Bi	Po	At	Rn	Fr	Ra	Ac	Th	Pa	U	"
                + "Np	Pu	Am	Cm	Bk	Cf	Es	Fm	Md	No	Lr	Rf	Db	Sg	Bh	"
                + "Hs	Mt	Ds	Rg	Cn	Nh	Fl	Mc	Lv	Ts	Og	Cr inc	Cr coh";

        String[] elementalList = elementalLine.split("\t");
        for (int i = 0; i < elementalList.length; i++) {
            elementalList[i] = elementalList[i].trim();
            fullList.get(2).add(elementalList[i]);
        }
        Boolean[] isOriginal = new Boolean[elementalList.length];
        for (int i = 0; i < isOriginal.length; i++) {
            isOriginal[i] = false;
        }
        int count = 12;
        for (int i = 12; i < elementalList.length; i++) {
            if (elementalList[i].trim().equals(original.get(2).get(count).trim())) {
                isOriginal[i] = true;
                count++;
            } else {
                isOriginal[i] = false;
                boolean contains=false;
                for (int j=0; j<elementalList.length;j++) {
                	if (elementalList[j].trim().equals(original.get(2).get(count).trim())){
                		contains=true;
                	}
                }
                if (!contains) {
                	count++;
                }
            }
        }

        for (int i = 3; i < columnLength; i++) {
            count = 12;
            for (int j = 0; j < elementalList.length; j++) {
                if (j <= 11) {
                    fullList.get(i).add(original.get(i).get(j));
                } else if (isOriginal[j]) {
                    fullList.get(i).add(original.get(i).get(count));
                    count++;
                } else {
                    fullList.get(i).add("0");
                }
            }
        }
        return fullList;
    }

    public void dateNIST() {
        String[] fileName = new String[files.size()];
        for (int i = 0; i < files.size(); i++) {
            fileName[i] = files.get(i);
            fileName[i] = fileName[i].substring(fileName[i].lastIndexOf("\\"), fileName[i].lastIndexOf(".xlsx"));
            fileName[i] = fileName[i].substring(1, fileName[i].length());
            fileName[i] = fileName[i].replace("SRM", "");
            fileName[i] = fileName[i].replace("s", "");
            fileName[i] = fileName[i].replace(",", "");
            fileName[i] = fileName[i].replace(".", "");
            fileName[i] = fileName[i].trim();
            NISTDates.add(new Date(fileName[i]));
        }
    }

    public void getDates() {
        List<String> beforeTrimHeader = new ArrayList<>();
        for (int i = 0; i < fileContents.size(); i++) {
            if (fileContents.get(i).toString().contains("peakarea")) {
                beforeTrimHeader.add(fileContents.get(i - 1).get(0));
            }
        }
        List<String> dateString = new ArrayList<>();
        for (int i = 0; i < beforeTrimHeader.size(); i++) {
            String temp = beforeTrimHeader.get(i);
            temp = temp.substring(temp.lastIndexOf("XRF data"));
            temp = temp.substring(13);
            temp = temp.substring(0, 10);
            dateString.add(temp.trim());
        }
        for (int i = 0; i < beforeTrimHeader.size(); i++) {
            fileDates.add(new Date(dateString.get(i)));
        }
    }

    public void listFile(String directoryName) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            String fileString = file.toString();
            fileString = fileString.substring(fileString.lastIndexOf("\\"), fileString.length());
            if (file.isFile() && fileString.contains("SRM") && fileString.endsWith(".xlsx")) {
                files.add(file.toString());
            } else if (file.isDirectory()) {
                listFile(file.getAbsolutePath());
            }
        }
    }

    public void fileToString(File file) throws FileNotFoundException, IOException {
        String temp = "";
        FileReader f = new FileReader(file);
        BufferedReader bufferReader = new BufferedReader(f);
        temp = bufferReader.readLine();
        int count = 0;
        while (temp != null) {
            String[] arrayString = temp.split(",");
            fileContents.add(new ArrayList<>());
            for (int i = 0; i < arrayString.length; i++) {
                fileContents.get(count).add(arrayString[i].replace(',', ' ').trim());
            }
            temp = bufferReader.readLine();
            count++;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
