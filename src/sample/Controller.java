package sample;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class Controller {

    int index =9;
    int [][] grid = new int[index][index];
    int [][] mf = new int[index][index];
    int [][] test = new int[index][index];


     JFXTextField[][] st = new  JFXTextField[index][index];


    @FXML
    private TextField fileBath;

    @FXML
    private GridPane gPane;



    @FXML
    void SolveFromGUI(ActionEvent event) {

            if(solve()){
                printToGP();
            }
            else {
                errordiglog();
            }


    }

    private void printToGP() {
        for (int i = 0 ; i < index ; i++ ){
            for (int j = 0 ; j < index ; j++){
                if(mf[i][j]!=grid[i][j]){
                    st[i][j].setText(String.valueOf(grid[i][j]));
                    st[i][j].setStyle("-fx-background-color:white;-fx-alignment: center");
                }

            }
        }

    }
 

    private boolean solve() {
        int row=0;
        int colum=0;
        boolean isEmpty=true;
        for (int i = 0 ; i<index; i++){
            for (int j = 0 ; j < index ; j++){
                if(grid[i][j]== 0 ){
                    row=i;
                    colum=j;
                    isEmpty=false;
                    break;
                }
            }
            if(!isEmpty)
                break;

        }
        if (isEmpty){
            return true;
        }
        for (int num=1 ; num<=index; num++){
            if(isSafe(grid,row,colum,num)){
                grid[row][colum] = num;
                if(solve()){
                    return true;
                }
                else {
                    grid[row][colum]= 0 ;
                }
            }
        }
        return false;
    }

    private boolean isSafe(int[][] temp, int row, int colum, int num) {
        for (int j=0 ; j<index ; j++){
            if (colum!= j && temp[row][j] == num){

                return false;

            }

        }

        for (int i = 0 ; i < index ; i++){
            if (row!=i && temp[i][colum]== num){

                return false;
            }


        }

        int boxRow= row- row%3;
        int boxColum = colum - colum%3;
        for (int i = boxRow; i < boxRow+3 ; i++){
            for (int j = boxColum; j < boxColum+3 ; j++){
                if (row!=i && colum!=j && temp[i][j] == num){

                    return false;
                }
            }
        }

        return true;


    }



    private void errordiglog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Input");
        alert.setHeaderText("Look, an Error Input");
        alert.setContentText("Ooops, there was an error!");

        alert.showAndWait();
    }



    void clear() {
        for (int i = 0 ; i < index ; i++ ){
            for (int j = 0 ; j < index ; j++){
                st[i][j].setText("");
                grid[i][j]=0;
                mf[i][j]=0;
            }
        }

    }


//        read from file
    void readFile(String filebath){

        Scanner sc = null;
        try {
             sc = new Scanner(new BufferedReader(new FileReader(filebath)));
            while(sc.hasNextLine()) {
                for (int i=0; i<index; i++) {
                    String[] line = sc.nextLine().trim().replaceAll("\\{","").replaceAll("}","").replaceAll(",","").split(String.valueOf(' '));
                    for (int j=0; j<index; j++) {
                        mf[i][j] = Integer.parseInt(line[j]);
                        if(mf[i][j]!=0){
                            if(isSafe(mf,i,j,mf[i][j])){
                                grid[i][j]=mf[i][j];
                            }else {
                                grid[i][j]=0;
                                mf[i][j]=0;
                                errordiglog();
                            }
                        }

                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("error!!!");
        }

        assert sc != null;
        sc.close();


    }
//  for input one number on text filed
    void oneNumInTextFiled(JFXTextField textField){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,1}([.]\\d{0,4})?")) {
                    textField.setText(oldValue);
                }
            }
        });
    }





    public void initialize() {


        for (int i = 0 ; i < index ; i++ ){
            for (int j = 0 ; j < index ; j++){
                if(grid[i][j]!=0){
                    st[i][j]=new  JFXTextField(String.valueOf(grid[i][j]));
                    st[i][j].setEditable(false);
                    st[i][j].setStyle("-fx-background-color:#e5e5e5;-fx-alignment: center");
                    test[i][j]=grid[i][j];
                }
                else{
                    st[i][j]=new  JFXTextField("");
                    st[i][j].setStyle("-fx-background-color:white;-fx-alignment: center");
                }

                oneNumInTextFiled(st[i][j]);

                gPane.add(st[i][j],j,i);
//                        this for soduko style enable it if order this
//                int row_start = (i / 3) * 3;
//                int col_start = (j / 3) * 3;
//                System.out.println(i+" "+j+"->"+row_start+","+col_start);
//                if ((row_start==0 && col_start==3) ||(row_start==3 && col_start==0 )||(row_start==3 && col_start==6 )||(row_start==6 && col_start==3 )){
//                    st[i][j].setStyle("-fx-background-color: green");
//                }
                int finalJ = j;
                int finalI = i;

                st[i][j].textProperty().addListener((observable) -> {
                    if(!st[finalI][finalJ].getText().equals("")){
                        test[finalI][finalJ]=Integer.parseInt(st[finalI][finalJ].getText());
                        if (!isSafe(test,finalI,finalJ,test[finalI][finalJ])){
                            test[finalI][finalJ]=0;
                            st[finalI][finalJ].setStyle("-fx-background-color: red;-fx-alignment: center");
                        }
                        else {
                            st[finalI][finalJ].setStyle("-fx-background-color:white;-fx-alignment: center");
                        }
                    } else {
                        st[finalI][finalJ].setStyle("-fx-background-color:white;-fx-alignment: center");
                    }

                });



            }
        }

    }
    @FXML
     void chooceFile(){
        clear();
        FileChooser fileChooser=new FileChooser();
        fileChooser.setInitialDirectory(new File("src\\sample\\MatrixTest"));
        fileChooser.setTitle("Chooce File ,Please");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text file","*.txt"));
        File f=fileChooser.showOpenDialog(null);
        fileBath.setText(f.getAbsolutePath());
        readFile(f.getAbsolutePath());
        
        initialize();

    }
    void sodokuStyle(int i  ,  int j ){
        int row_start = (i / 3) * 3;
        int col_start = (j / 3) * 3;
        System.out.println(i+" "+j+"->"+row_start+","+col_start);
        if ((row_start==0 && col_start==3) ||(row_start==3 && col_start==0 )||(row_start==3 && col_start==6 )||(row_start==6 && col_start==3 )){
            st[i][j].setStyle("-fx-background-color: green");
        }
    }
    @FXML
    void checkSolution(){
        int numberOfTrue=0;
        if(solve()){
            for (int i=0;i<index;i++){
                for (int j=0;j<index;j++){
                    if(!st[i][j].getText().isEmpty()){
                        if(grid[i][j]!= Integer.parseInt(st[i][j].getText())){
                            st[i][j].setStyle("-fx-background-color: red;-fx-alignment: center");
                        }else
                            numberOfTrue++;
                    }

                }
            }
      }
        if(numberOfTrue==(index*index)){
            Alert a1 = new Alert(Alert.AlertType.NONE,
                    "Congratulations, you won", ButtonType.APPLY);

            a1.setTitle("Win");
            a1.show();

        }
    }

}
