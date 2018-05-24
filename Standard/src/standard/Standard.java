package Standard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.application.Application;
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
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author Jacob Heim
 */

public class Standard extends Application {

    static List<String> files = new ArrayList<String>();
    static String outputLocation = "";
    static String name = "";

    ;
    
    @Override
    public void start(Stage primaryStage) {
    Label jake = new Label("Created by Jacob Heim");
    	jake.setFont(new Font("Arial", 8));
        Label nameLabel = new Label("Enter the name of the new File below and press \"Enter\"");
        nameLabel.setWrapText(true);
        Label nameConfirmation = new Label("No name has been entered");
        nameConfirmation.setWrapText(true);

        TextField nameField = new TextField();
        nameField.setAlignment(Pos.CENTER);
        nameField.setPrefColumnCount(20);
        nameField.setOnAction((ActionEvent event) -> {
            name = nameField.getText();
            if (!nameField.getText().equals("")){
                nameConfirmation.setText("You have Entered: "+nameField.getText());
            }
            else{
                nameConfirmation.setText("No name has been entered");
            }
        });

        Label space = new Label("");
        Button btn = new Button();

        Label error = new Label("");
        error.setTextFill(Color.RED);
        btn.setText("Standardize");

        Label outputLabel = new Label("Output Location");
        Button output = new Button();
        output.setText("No Location selected");
        output.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // READING IN DIRECTORY==================================
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Open Resource File");
                File selectedDirectory = chooser.showDialog(primaryStage);

                if (selectedDirectory == null) {
                    output.setText("No Location selected");
                } else {
                    output.setText(selectedDirectory.getAbsolutePath());
                }
                if (!output.getText().equals("No Location selected")) {
                    outputLocation = output.getText();
                    error.setText("");
                }
            }
        });

        Label inputLabel = new Label("Input Location");
        Button input = new Button("No Location selected");
        input.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // READING IN DIRECTORY==================================
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Open Resource File");
                File selectedDirectory = chooser.showDialog(primaryStage);

                if (selectedDirectory == null) {
                    input.setText("No Location selected");
                } else {
                    input.setText(selectedDirectory.getAbsolutePath());
                }
                if (!input.getText().equals("No Location selected")) {
                    listFile(input.getText());
                    error.setText("");
                }
            }
        });

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (name != "" && outputLocation != "" && input.getText() != "No Location selected") {
                    try {
                        error.setTextFill(Color.BLACK);
                        error.setText("ERROR");
                        Standardize();
                        error.setTextFill(Color.GREEN);
                        error.setText("DONE");
                    } catch (IOException ex) {
                        error.setText("ERROR: IO Exception");
                        Logger.getLogger(Standard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (outputLocation == "" && input.getText() == "No Location selected" && name == "") {
                    error.setText("MUST SELECT OUTPUT LOCATION, INPUT LOCATION, AND ENTER NAME");
                } else if (outputLocation == "" && name == "") {
                    error.setText("MUST SELECT OUTPUT LOCATION AND ENTER NAME");
                } else if (input.getText() == "No Location selected" && name == "") {
                    error.setText("MUST SELECT INPUT LOCATION AND ENTER NAME");
                } else if (outputLocation == "" && input.getText() == "No Location selected") {
                    error.setText("MUST SELECT OUTPUT AND INPUT LOCATIONS");
                } else if (outputLocation == "") {
                    error.setText("MUST SELECT OUTPUT LOCATION");
                } else if (input.getText() == "No Location selected") {
                    error.setText("MUST SELECT INPUT LOCATION");
                } else if (name == "") {
                    error.setText("MUST ENTER NAME");
                }
            }
        });

        input.setWrapText(true);
        output.setWrapText(true);
        StackPane root = new StackPane();
        VBox total = new VBox();
        
        HBox textField = new HBox();
        textField.getChildren().add(nameField);
        textField.setPrefWidth(30);
        textField.setAlignment(Pos.CENTER);
        
        total.getChildren().addAll(nameLabel, textField, nameConfirmation,inputLabel, input, outputLabel, output, space, btn, error, jake);
        total.setAlignment(Pos.CENTER);
        root.getChildren().addAll(total);

        Scene scene = new Scene(root, 600, 350);

        primaryStage.setTitle("Standardize");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void readFiles(File selectedDirectory, Button input) {
        try (Stream<Path> paths = Files.walk(Paths.get(input.getText()))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    // LOOPS THROUGH ALL FILES
                    if (filePath.toString().endsWith(".xls")) {
                        files.add(filePath.toString());
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(Standard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void listFile(String directoryName) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile() && file.toString().endsWith(".xls")) {
                files.add(file.toString());
            } else if (file.isDirectory()) {
                listFile(file.getAbsolutePath());
            }
        }
    }

    public void Standardize() throws FileNotFoundException, IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < files.size(); i++) {
            List<String> originalList = new ArrayList<String>();
            String line;
            int rowLength = -1;
            FileReader f = new FileReader(files.get(i));
            BufferedReader bufferReader = new BufferedReader(f);
            line = bufferReader.readLine();
            originalList.add(line);
            line = bufferReader.readLine();
            originalList.add(line);
            line = bufferReader.readLine();
            originalList.add(line);
            List<List<String>> elements = new ArrayList<List<String>>();
            while (line != null) {
                List<String> items = Arrays.asList(line.split("\t"));
                for (int j = 0; j < items.size(); j++) {
                    items.get(j).trim();
                    if (rowLength == -1) {
                        elements.add(new ArrayList<String>());
                    }
                    elements.get(j).add(items.get(j));
                }
                rowLength = elements.size();
                line = bufferReader.readLine();
                originalList.add(line);

            }
            sb.append(fullList(originalList));
        }
        try {
            // WRITE FILES===================================
            File newFile = new File(outputLocation + "\\"+name+".csv");
            PrintWriter writer = new PrintWriter(newFile);
            writer.print(sb.toString());

            writer.close();
        } catch (IOException e) {
            // do something
        }
    }

    public StringBuilder fullList(List<String> originalList) {
        for (int i = 0; i < originalList.size(); i++) {
            System.out.println(originalList.get(i));
        }
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
        String elementalLine = "filename	SectionID	Section Depth (cm)	position (mm)	sample surface	validity	"
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
                    for (int j=2; j<original.size();j++){
                        original.get(j).remove(count);
                    }
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
                    fullList.get(i).add("-00.00");
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        try {
            File newFile = new File(outputLocation + "\\testExcelDoc.csv");
            PrintWriter writer = new PrintWriter(newFile);

            for (int i = 0; i < fullList.size(); i++) {
                for (int j = 0; j < fullList.get(i).size(); j++) {
                    sb.append(fullList.get(i).get(j) + ",");
                }
                sb.append("\n");
            }
            writer.print(sb.toString());

            writer.close();
        } catch (IOException e) {
            // do something
        }
        return sb;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //launch(args);
        double list[] = new double[500];
        int count=0;
        for (int i=1; i<400; i++){
            list[i]=(i*(i-1))/2;
            count++;
        }
        double fill[] = new double[1600];
        count=0;
        for (int i=0; i<40;i++){
            for (int j=0; j<40; j++){
                fill[count]=list[i]+list[j];
                count++;
            }
        }
        
        Arrays.sort(fill);
        double st[] = new double[1600];
        for (int i=0; i<1600;i++){
            st[i]=i;
        }
        for (int i=0; i<1600;i++){
            for (int j=0; j<1600; j++){
                if (st[i]==fill[j]){
                    st[i]=0;
                }
            }
        }
        Arrays.sort(st);
        for (int i=0; i<1600;i++){
            System.out.println(st[i]);
        }
    }
}
