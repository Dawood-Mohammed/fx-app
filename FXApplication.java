import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import java.util.*;
import java.util.stream.*;
import java.io.*;
public class FXApplication extends Application{
   private static LinkedList<Student> students = null;
   private static String fileState = "Group_A";
   private static String branchState = "Scientific";
   private static String filePath = "";
   private static final String TEXT_STYLE = "-fx-alignment:center; -fx-font: normal bold 20px 'serif'";
   private static final String PANE_STYLE = "-fx-border-color:#2c3e50; -fx-border-width:0px 2px 0px 0px; -fx-background-color: #16a085; -fx-fill-width:true;";
   private static final String STANDARD_BUTTON_STYLE = "-fx-background-color: #16a085; -fx-text-fill: white; -fx-font: normal bold 16px 'serif'; -fx-border-radius:10px; -fx-border-color:#2c3e50; -fx-border-width:0px 0px 2px 0px;";
   private static final String HOVERED_BUTTON_STYLE  = "-fx-background-color: #ddeeff; -fx-text-fill: black; -fx-font: normal bold 16px 'serif'; -fx-border-radius:10px; -fx-border-color:#2c3e50; -fx-border-width:0px 0px 2px 0px;";
   private static final String PRESSED_BUTTON_STYLE  = "-fx-background-color: #ddeeff; -fx-text-fill: black; -fx-font: normal bold 16px 'serif'; -fx-border-radius:10px; -fx-border-color:#2c3e50; -fx-border-width:0px 0px 2px 0px;";
   private static final String STANDARD_CONTROLS_STYLE = "-fx-background-color: #16a085; -fx-text-fill: white; -fx-font: normal bold 16px 'serif'; -fx-border-radius:10px; -fx-border-color:#2c3e50; -fx-border-width:1px;";
   private static final String HOVERED_CONTROLS_STYLE  = "-fx-background-color: #ddeeff; -fx-text-fill: black; -fx-font: normal bold 16px 'serif'; -fx-border-radius:10px; -fx-border-color:#2c3e50; -fx-border-width:1px;";
   private static final String PRESSED_CONTROLS_STYLE  = "-fx-background-color: #ddeeff; -fx-text-fill: black; -fx-font: normal bold 16px 'serif'; -fx-border-radius:10px; -fx-border-color:#2c3e50; -fx-border-width:1px;";

   private static void changeButtonBackground(Node node,String hover, String color) {
    node.styleProperty().bind(
      Bindings
        .when(node.hoverProperty())
          .then(new SimpleStringProperty(color))
          .otherwise(new SimpleStringProperty(hover))
    );
  }

   public static boolean isADouble(String str){
	for(char c:str.toCharArray()){
	   if(Character.isDigit(c) || c == '.')
	      continue;
	   else
	      return false;
	}
	return true;
   }

   public static Alert createAlert(String title, String msg, Alert.AlertType alertType){
	   Alert alert = new Alert(alertType);
	   alert.initStyle(StageStyle.UTILITY);
	   alert.setTitle(title);
	   alert.setHeaderText(null);
	   alert.setContentText(msg);
	   return alert;
   }

   

   @Override
   public void start(Stage stage)throws Exception{
	LandingFrame primaryStage = new LandingFrame();
	primaryStage.getStage().show();
   }

   public static void main(String args[])throws Exception{
	System.out.println("please enter files path [C://users//your-user//desktop//]");
	filePath = new Scanner(System.in).nextLine().trim();
	launch(args);
   }

   public static LinkedList<Student> getStudents(String branch){
	return students.stream()
			.filter(std -> std.getBranch().equals(branch))
			.collect(Collectors.toCollection(LinkedList::new));
   }

   public static LinkedList<Student> customReport(double grade){
	return students.stream()
			.filter(student -> student.getBranch().equals(branchState))
			.filter(std -> std.getAverage() > grade)
			.collect(Collectors.toCollection(LinkedList::new));
   }

   public static LinkedList<Student> median()throws Exception{
	LinkedList<Student> result = new LinkedList<>();
	if(students != null && students.size() != 0){
	   LinkedList<Student> temp = students.stream().filter(elem -> elem.getBranch().equals(branchState)).collect(Collectors.toCollection(LinkedList::new));
	   int middle = 0;
	   if(temp.size() % 2 == 0){
		middle = temp.size() / 2;
		result.add(temp.get(middle-1));
		result.add(temp.get(middle));
	   }else{
		middle = (temp.size()+1) / 2;
		result.add(temp.get(middle-1));
	   }
	}
	return result;
   }

   public static double std()throws Exception{
	double m = mean();
	LinkedList<Double> doubles = students.stream().filter(elem -> elem.getBranch().equals(branchState)).map(std -> std.getAverage()).collect(Collectors.toCollection(LinkedList::new));
	LinkedList<Double> mList = new LinkedList<>();
	for(Double d : doubles){mList.add(d-m);}
	double m2 = mList.stream().mapToDouble(e -> e).sum() / mList.size();
	return Math.sqrt(m2);
   }

   public static double mean()throws Exception{
	if(students != null && students.size() != 0){
	    LinkedList<Student> temp = students.stream().filter(elem -> elem.getBranch().equals(branchState)).collect(Collectors.toCollection(LinkedList::new));
	    return temp.stream().mapToDouble(std -> std.getAverage()).sum() / temp.size();
	}
	return 0;
   }

   public static double mode()throws Exception{
	if(students != null && students.size() != 0){
	    Map<Double, Long> result = students.stream().filter(elem -> elem.getBranch().equals(branchState)).collect(Collectors.groupingBy(std -> std.getAverage(), Collectors.counting()));
	    Long max = 0l;
	    Map.Entry maxEntry = null;
	    for(Map.Entry<Double,Long> entry:result.entrySet()) {
      		if(entry.getValue()>max) {
        	    max=entry.getValue();
		    maxEntry = entry;
      		}
    	    }
	    return (double)maxEntry.getKey();
	}
	return 0;
   }

   public static LinkedList<Student> topTenReport()throws Exception{
	LinkedList<Student> result = new LinkedList<>();
	LinkedList<Student> temp = students.stream().filter(elem -> elem.getBranch().equals(branchState)).collect(Collectors.toCollection(LinkedList::new));
	Collections.sort(temp, (s1, s2) -> {
	    if(s1.getAverage() < s2.getAverage())	
	        return 1;
	    else if(s1.getAverage() > s2.getAverage())
	        return -1;
	    else 
	        return 0;
            }
	);
	for(int i=0; i<10; i++){result.add(temp.get(i));}
	return result;
   }

   public static LinkedList<Student> readCSV(String filePath, String file, String branch)throws Exception{
        students = new LinkedList<>();
	String path = filePath+file+".csv";
	try{
	Scanner lineScanner = new Scanner(new File(path));
	while(lineScanner.hasNextLine()){
	      String line = lineScanner.nextLine();
	      Scanner sc = new Scanner(line);
	      sc.useDelimiter(",");
	      LinkedList<String> list = new LinkedList<>();
	      while(sc.hasNext()){
	         list.add(sc.next().trim());
	      }
	      students.add(new Student(Long.parseLong(list.get(0)),list.get(1),Double.parseDouble(list.get(2))));
	   }
        }catch(Exception e){}
      Collections.sort(students, (s1, s2) -> {
	if(s1.getSeatNumber() > s2.getSeatNumber())	
	   return 1;
	else if(s1.getSeatNumber() < s2.getSeatNumber())
	   return -1;
	else 
	   return 0;
      });
      return students;
   }

   //application frames

   static class HomeFrame{
	private Stage stage;
	public HomeFrame() throws Exception{
	   stage = new Stage();
	   stage.setTitle("Home");
	   stage.setScene(drawHomeScene());
	   stage.setResizable(false);
	}
	public Scene drawHomeScene() throws Exception{
	   //side navigation bar
	    VBox sideBar = new VBox();
	    Button manage = new Button("Management");
	    manage.setMaxWidth(Double.MAX_VALUE);
	    changeButtonBackground(manage, STANDARD_BUTTON_STYLE, HOVERED_BUTTON_STYLE);
	    Button topTen = new Button("Top Ten");
	    topTen.setMaxWidth(Double.MAX_VALUE);
	    changeButtonBackground(topTen, STANDARD_BUTTON_STYLE, HOVERED_BUTTON_STYLE);
	    Button mean = new Button("Mean");
	    mean.setMaxWidth(Double.MAX_VALUE);
	    changeButtonBackground(mean, STANDARD_BUTTON_STYLE, HOVERED_BUTTON_STYLE);
	    Button median = new Button("Median");
	    median.setMaxWidth(Double.MAX_VALUE);
	    changeButtonBackground(median, STANDARD_BUTTON_STYLE, HOVERED_BUTTON_STYLE);
	    Button mode = new Button("Mode");
	    mode.setMaxWidth(Double.MAX_VALUE);
	    changeButtonBackground(mode, STANDARD_BUTTON_STYLE, HOVERED_BUTTON_STYLE);
	    Button std = new Button("Standard Diviation");
	    std.setMaxWidth(Double.MAX_VALUE);
	    changeButtonBackground(std, STANDARD_BUTTON_STYLE, HOVERED_BUTTON_STYLE);
	    Button custom = new Button("Custom Report");
	    custom.setMaxWidth(Double.MAX_VALUE);
	    changeButtonBackground(custom, STANDARD_BUTTON_STYLE, HOVERED_BUTTON_STYLE);
	    Button back = new Button("Go Back");
	    back.setMaxWidth(Double.MAX_VALUE);
	    changeButtonBackground(back, STANDARD_BUTTON_STYLE, HOVERED_BUTTON_STYLE);
	    sideBar.setStyle("-fx-border-color:#2c3e50; -fx-border-width:0px 2px 0px 2px; -fx-padding: 10px 0px 0px 0px; -fx-background-color: #16a085; -fx-fill-width:true;");
	    sideBar.setSpacing(10);
	    ObservableList barList = sideBar.getChildren();
	    barList.addAll(manage, topTen, mean, median, mode, std, custom, back);

	    //title
	    HBox title = new HBox();
	    title.setAlignment(Pos.CENTER);
	    Text titleText = new Text("Student Management System");
	    titleText.setStyle(TEXT_STYLE);
	    title.setStyle("-fx-border-color:#2c3e50; -fx-border-width:0px 0px 2px 0px; -fx-background-color: #16a085; -fx-fill-width:true;");
	    title.setMargin(titleText,new Insets(10,10,10,10));
	    title.getChildren().addAll(titleText);

	    //center 
	    StackPane stkPane = new StackPane();
	    stkPane.setStyle("-fx-background-color: #16a085;");
	    stkPane.setPadding(new Insets(0, 0, 0, 0));

	    //bottom
	    HBox footer = new HBox();
	    footer.setStyle("-fx-border-color:#2c3e50; -fx-border-width:2px 0px 2px 0px; -fx-background-color: white; -fx-fill-width:true;");
	    footer.setAlignment(Pos.CENTER);
	    footer.setPadding(new Insets(10, 0, 10, 0));
	    footer.setSpacing(10);
	    Button sendReport = new Button("Export Report");
	    changeButtonBackground(sendReport, STANDARD_CONTROLS_STYLE, HOVERED_CONTROLS_STYLE);
	    Text txt = new Text("Enter Grade");
	    txt.setStyle("-fx-font: normal bold 16px 'serif';");  
            TextField reportGrade = new TextField(); 
	    reportGrade.setStyle("-fx-font: small bold 16px 'serif';");   
	    footer.getChildren().addAll(txt, reportGrade, sendReport);

	    BorderPane border  = new BorderPane();
	    border.setLeft(sideBar);
	    border.setTop(title);
	    border.setCenter(stkPane);
	    border.setBottom(footer);
	
	    VBox mngPane = new VBox();
	    TableView tableView = new TableView();
            TableColumn<Student, String> column1 = new TableColumn<>("Seat Number");
            column1.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
            TableColumn<Student, String> column2 = new TableColumn<>("Branch");
            column2.setCellValueFactory(new PropertyValueFactory<>("branch"));
	    TableColumn<Student, String> column3 = new TableColumn<>("Average");
            column3.setCellValueFactory(new PropertyValueFactory<>("average"));
            tableView.getColumns().addAll(column1,column2,column3);
	    if(students == null || students.size() != 0){
                readCSV(filePath, fileState, branchState).stream().filter(elem -> elem.getBranch().equals(branchState)).forEach(sd -> tableView.getItems().add(sd));
	    }else{
	        getStudents(branchState).forEach(s -> tableView.getItems().add(s));
	    }
	    HBox controls = new HBox();
            controls.setStyle("-fx-background-color: #16a085; -fx-fill-width:true;");
	    controls.setAlignment(Pos.CENTER);
	    Button insert = new Button("Insert");
	    changeButtonBackground(insert, STANDARD_CONTROLS_STYLE, HOVERED_CONTROLS_STYLE);
	    Button delete = new Button("Delete");
	    changeButtonBackground(delete, STANDARD_CONTROLS_STYLE, HOVERED_CONTROLS_STYLE);
	    Button search = new Button("Search");
	    changeButtonBackground(search, STANDARD_CONTROLS_STYLE, HOVERED_CONTROLS_STYLE);
	    controls.setMargin(insert,new Insets(10,30,10,30));
	    controls.setMargin(delete,new Insets(10,30,10,30));
	    controls.setMargin(search,new Insets(10,30,10,30));
	    controls.getChildren().addAll(insert,delete,search);
	    mngPane.getChildren().addAll(tableView, controls);
	    mngPane.setStyle(PANE_STYLE);

	    VBox top = new VBox();
	    top.setStyle(PANE_STYLE);
	    top.setSpacing(20);
	    TableView tableView0 = new TableView();
            TableColumn<Student, String> tableCol1 = new TableColumn<>("Seat Number");
            tableCol1.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
            TableColumn<Student, String> tableCol2 = new TableColumn<>("Branch");
            tableCol2.setCellValueFactory(new PropertyValueFactory<>("Branch"));
	    TableColumn<Student, String> tableCol3 = new TableColumn<>("Average");
            tableCol3.setCellValueFactory(new PropertyValueFactory<>("Average"));
            tableView0.getColumns().addAll(tableCol1, tableCol2, tableCol3);
	    top.getChildren().addAll(tableView0);
	    if(students == null || students.size() == 0){
	        readCSV(filePath, fileState, branchState);
	        topTenReport().stream().forEach(s -> tableView0.getItems().add(s));
	    }else if(students != null || students.size() != 0){
                topTenReport().forEach(s -> tableView0.getItems().add(s));
	    }else{
	        Alert alert = createAlert("top ten report", "no data", Alert.AlertType.INFORMATION);
	        alert.showAndWait();
	    }

	    VBox meanPane = new VBox();
	    meanPane.setStyle(PANE_STYLE);
	    meanPane.setSpacing(20);
	    meanPane.setAlignment(Pos.CENTER);
	    Text meanHolder = new Text();
	    meanHolder.setStyle(TEXT_STYLE);
	    meanHolder.setFill(Color.WHITE);
	    meanHolder.setText("Mean : "+mean());
	    meanPane.getChildren().addAll(meanHolder);

	    VBox medianPane = new VBox();
	    medianPane.setStyle(PANE_STYLE);
	    medianPane.setSpacing(20);
	    TableView tableView2 = new TableView();
            TableColumn<Student, String> tblColumn1 = new TableColumn<>("Seat Number");
            tblColumn1.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
            TableColumn<Student, String> tblColumn2 = new TableColumn<>("Branch");
            tblColumn2.setCellValueFactory(new PropertyValueFactory<>("Branch"));
	    TableColumn<Student, String> tblColumn3 = new TableColumn<>("Average");
            tblColumn3.setCellValueFactory(new PropertyValueFactory<>("Average"));
            tableView2.getColumns().addAll(tblColumn1,tblColumn2,tblColumn3);
	    medianPane.getChildren().addAll(tableView2);
	    if(students == null || students.size() != 0){
                median().stream().filter(elem -> elem.getBranch().equals(branchState)).forEach(s -> tableView2.getItems().add(s));
	    }else{
	        Alert alert = createAlert("median", "no data", Alert.AlertType.INFORMATION);
	        alert.showAndWait();
	    }

	    VBox modePane = new VBox();
	    modePane.setStyle(PANE_STYLE);
	    modePane.setSpacing(20);
	    modePane.setAlignment(Pos.CENTER);
	    Text modeHolder = new Text();
	    modeHolder.setStyle(TEXT_STYLE);
	    modeHolder.setFill(Color.WHITE);
	    modeHolder.setText("Mode : "+mode());
	    modePane.getChildren().addAll(modeHolder);

	    VBox stdPane = new VBox();
	    stdPane.setStyle(PANE_STYLE);
	    stdPane.setSpacing(20);
	    stdPane.setAlignment(Pos.CENTER);
	    Text stdHolder = new Text();
	    stdHolder.setStyle(TEXT_STYLE);
	    stdHolder.setFill(Color.WHITE);
	    stdHolder.setText("Standard Deviation : "+std());
	    stdPane.getChildren().addAll(stdHolder);

	    VBox customPane = new VBox();
	    customPane.setStyle(PANE_STYLE);
	    HBox customBox = new HBox();
	    customBox.setStyle(PANE_STYLE);
	    Text seatNumTxt = new Text("Enter Grade");
	    seatNumTxt.setStyle("-fx-font: normal bold 16px 'serif';");  
            TextField snField = new TextField(); 
	    snField.setStyle("-fx-font: small bold 16px 'serif';");   
            Button process = new Button("Process"); 
	    changeButtonBackground(process, STANDARD_CONTROLS_STYLE, HOVERED_CONTROLS_STYLE);
	    customBox.setSpacing(10);
            customBox.setPadding(new Insets(10, 10, 10, 10));     
            customBox.setAlignment(Pos.CENTER); 
    	    VBox textContainer = new VBox();
	    Text customHolder = new Text();
	    customHolder.setStyle(TEXT_STYLE);
	    customHolder.setFill(Color.WHITE);
	    textContainer.setAlignment(Pos.CENTER);
	    textContainer.setStyle("-fx-alignment:center; -fx-background-color: #16a085; -fx-fill-width:true;");
	    textContainer.getChildren().add(customHolder);
	    customPane.setMargin(textContainer, new Insets(100, 10, 10, 10));
	    customBox.getChildren().addAll(seatNumTxt,snField,process);
	    customPane.getChildren().addAll(customBox,textContainer);

	    //default center
	    VBox defaultPane = new VBox();
	    defaultPane.setStyle(PANE_STYLE);
	    defaultPane.setSpacing(20);
	    defaultPane.setAlignment(Pos.CENTER);
	    Text defaultHolder = new Text("Mode : ");
	    defaultHolder.setStyle(TEXT_STYLE);
	    defaultHolder.setFill(Color.WHITE);
	    defaultHolder.setText("\"Welcome To Student Management System\"");
	    defaultPane.getChildren().addAll(defaultHolder);
	    stkPane.getChildren().add(defaultPane);

	    manage.setOnAction(e -> {
	        stkPane.getChildren().clear();
	        stkPane.getChildren().add(mngPane);
	    });
	    topTen.setOnAction(e -> {
	        stkPane.getChildren().clear();
	        stkPane.getChildren().add(top);
	    });
	    mean.setOnAction(e -> {
	        stkPane.getChildren().clear();
	        stkPane.getChildren().add(meanPane);
	    });
	    median.setOnAction(e -> {
	        stkPane.getChildren().clear();
	        stkPane.getChildren().add(medianPane);
	    });
	    mode.setOnAction(e -> {
	        stkPane.getChildren().clear();
	        stkPane.getChildren().add(modePane);
	    });
	    std.setOnAction(e -> {
	        stkPane.getChildren().clear();
	        stkPane.getChildren().add(stdPane);
	    });
	    custom.setOnAction(e -> {
	        stkPane.getChildren().clear();
	        stkPane.getChildren().add(customPane);
	    });
	    back.setOnAction(e -> {
	        try{
	            LandingFrame frame = new LandingFrame();
	            frame.getStage().show();
		    getStage().hide();
	        }catch(Exception ex){}
	    });
	    insert.setOnAction(e -> {
	        try{
	            InsertFrame frame = new InsertFrame();
	            frame.getStage().show();
	        }catch(Exception ex){}
	    });
	    delete.setOnAction(e -> {
	        try{
	            DeleteFrame frame = new DeleteFrame();
	            frame.getStage().show();
	        }catch(Exception ex){}
	    });
	    search.setOnAction(e -> {
	        try{
	            SearchFrame frame = new SearchFrame();
	            frame.getStage().show();
	        }catch(Exception ex){}
	    });
	    process.setOnAction(e -> {
	        if(snField.getText() != "" && isADouble(snField.getText())){
		    LinkedList<Student> stds = customReport(Double.parseDouble(snField.getText()));
		    if(stds != null){
			int studentSize = customReport(Double.parseDouble(snField.getText())).size();
			double studentPercentage = (studentSize * 100) / students.size();
			customHolder.setText("Number Of Students : "+studentSize+"  With Percentage  :  "+studentPercentage+"%");
		    }else{
			Alert alert = createAlert("alert", "no student found with seat number : "+snField.getText(), Alert.AlertType.INFORMATION);
		        alert.showAndWait();
		    }
		}else{
		    Alert alert = createAlert("error", "invalid input", Alert.AlertType.ERROR);
		    alert.showAndWait();
	        }
	    });
	    sendReport.setOnAction(e -> {
	       if(students != null){
	           if(reportGrade.getText() != "" && isADouble(reportGrade.getText())){
		       try{
	  	           String report = "";
		           PrintWriter writer = new PrintWriter(new FileOutputStream(new File(filePath+"report.txt")));
		           LinkedList<String> top10 = topTenReport().stream().map(st -> "["+st.getSeatNumber()+"\t"+st.getBranch()+"\t"+st.getAverage()+"]").collect(Collectors.toCollection(LinkedList::new));
		           int size = customReport(Double.parseDouble(reportGrade.getText())).size();
		           double percentage = (size * 100) / students.size();
		           String custom1 = "NUMBER OF STUDENTS WHOM GRADE IS ABOVE OR EQUAL TO "+reportGrade.getText()+"  IS  "+size+"  AND PERCENTAGE IS  "+percentage+"%"; 
		           LinkedList<String> median1 = median().stream().map(ss -> "["+ss.getSeatNumber()+"\t"+ss.getBranch()+"\t"+ss.getAverage()+"]").collect(Collectors.toCollection(LinkedList::new));
		           String std1 = "STANDARD DEVIATION  :  "+std();
		           String mean1 = "MEAN  :  "+mean();
		           String mode1 = "MODE  :  "+mode();
		           writer.println("");
		           writer.println("TOP TEN :-");
		           top10.forEach(element -> writer.println("\t"+element));
		           writer.println("");
		           writer.println("");
		           writer.println(mean1);
		           writer.println("");
		           writer.println("");
		           writer.println(mode1);
		           writer.println("");
		           writer.println("");
		           writer.println(std1);
		           writer.println("");
		           writer.println("");
		           writer.println("Median :-");
		           median1.forEach(element -> writer.println("\t"+element));
		           writer.println("");
		           writer.println("");
		           writer.println(custom1);
		           writer.close();
		           Alert alert = createAlert("hurrrray", "exported report to ["+filePath+"] successfully", Alert.AlertType.INFORMATION);
		           alert.showAndWait();
		       }catch(Exception ex){}
		   }else{
		       Alert alert = createAlert("error", "invalid input", Alert.AlertType.ERROR);
		       alert.showAndWait();
		   }
	       }else{
		   Alert alert = createAlert("oops!!!", "no data available", Alert.AlertType.INFORMATION);
		   alert.showAndWait();
	       }
	    });

	    Scene scene = new Scene(border,700,480);
	    return scene;
	}
	public Stage getStage(){return this.stage;}
   }

   static class LandingFrame{
	private Stage stage;
	public LandingFrame() throws Exception{
	   stage = new Stage();
	   stage.setTitle("Initialization...");
	   stage.setScene(drawLandingScene());
	   stage.setResizable(false);
	}

	public Scene drawLandingScene() throws Exception{

	    //title
	    HBox title = new HBox();
	    title.setAlignment(Pos.CENTER);
	    Text titleText = new Text("Student Management System");
	    titleText.setStyle(TEXT_STYLE);
	    title.setStyle("-fx-background-color: #16a085; -fx-fill-width:true;");
	    title.setMargin(titleText,new Insets(10,10,10,10));
	    ObservableList titleList = title.getChildren();
	    titleList.addAll(titleText);

	    VBox selection = new VBox();
	    selection.setSpacing(20);
	    selection.setAlignment(Pos.CENTER);
	    selection.setPadding(new Insets(10, 0, 10, 0));

	    HBox branchSelection = new HBox();
	    branchSelection.setSpacing(10);
	    branchSelection.setAlignment(Pos.CENTER);
	    Text branchLabel = new Text("Select Branch");
	    branchLabel.setStyle("-fx-font: normal bold 16px 'serif'; -fx-text-fill:black;");
	    branchLabel.setFill(Color.BLACK);
            ChoiceBox branchChooser = new ChoiceBox();
	    branchChooser.setStyle("-fx-font: normal bold 16px 'serif'; -fx-text-fill:black;"); 
      	    branchChooser.getItems().addAll("Scientific", "Literary");
	    branchChooser.getSelectionModel().selectFirst();
	    branchSelection.getChildren().addAll(branchLabel, branchChooser);

	    HBox fileSelection = new HBox(); 
	    fileSelection.setSpacing(10);
	    fileSelection.setAlignment(Pos.CENTER);
	    Text fileLabel = new Text("Loaded Data");
	    fileLabel.setFill(Color.BLACK);
	    fileLabel.setStyle("-fx-font: small bold 14px 'serif'; -fx-text-fill:black;");
	    ToggleGroup fileGroup = new ToggleGroup(); 
	    RadioButton west = new RadioButton("Group A"); 
	    west.setStyle("-fx-font: small bold 14px 'serif'; -fx-text-fill:black;");
            west.setToggleGroup(fileGroup);
	    west.setSelected(true);
            RadioButton gaza = new RadioButton("Group B"); 
	    gaza.setStyle("-fx-font: small bold 14px 'serif'; -fx-text-fill:black;");
            gaza.setToggleGroup(fileGroup); 
	    fileSelection.getChildren().addAll(fileLabel, west, gaza);

	    HBox buttonHolder = new HBox();
	    buttonHolder.setSpacing(20);
	    buttonHolder.setAlignment(Pos.CENTER);
	    Button next = new Button("Next"); 
	    changeButtonBackground(next, STANDARD_CONTROLS_STYLE, HOVERED_CONTROLS_STYLE);
	    buttonHolder.getChildren().addAll(next);
	    selection.getChildren().addAll(fileSelection, branchSelection, buttonHolder);

	

	    VBox container = new VBox();
	    container.setPadding(new Insets(10, 0, 10, 0));
	    container.setSpacing(50);
	    container.setStyle("-fx-border-color:#2c3e50; -fx-border-width:0px 0px 2px 0px; -fx-background-color: white;");
	    container.getChildren().addAll(title, selection);

	
	    fileGroup.selectedToggleProperty().addListener(( ob, o, n) -> {
                RadioButton rb = (RadioButton)fileGroup.getSelectedToggle();
                if (rb != null) {
                    String s = rb.getText();
		    if(s.equals("Group A")){
			fileState = "Group_A";
		    }else if(s.equals("Group B")){
                        fileState = "Group_B";
		    }
                }
            
            });
	    branchChooser.getSelectionModel().selectedIndexProperty().addListener(( ob, o, n) -> {
	        if(n.intValue() == 0){ 
		    branchState = "Scientific";
	        }else if(n.intValue() == 1){
		    branchState = "Literary";
	        }
            });
	    next.setOnAction(e -> {
	        try{
	            HomeFrame home = new HomeFrame();
	            home.getStage().show();
		    getStage().hide();
	        }catch(Exception ex){}
	    });

	    Scene scene = new Scene(container,600,380);
	    return scene;
       }
	public Stage getStage(){return this.stage;}
   }

   static class InsertFrame{
	private Stage stage;
	public InsertFrame() throws Exception{
	   stage = new Stage();
	   stage.setTitle("Insert Student");
	   stage.setScene(drawInsertScene());
	   stage.setResizable(false);
	}
	public Scene drawInsertScene() throws Exception{
	   Text seatNumTxt = new Text("Seat Number");
	   seatNumTxt.setStyle("-fx-font: normal bold 16px 'serif';");  
      	   Text branchTxt = new Text("Branch"); 
	   branchTxt.setStyle("-fx-font: normal bold 16px 'serif';");
           Text avgTxt = new Text("Average");  
	   avgTxt.setStyle("-fx-font: normal bold 16px 'serif';"); 
           TextField snField = new TextField(); 
	   snField.setStyle("-fx-font: small bold 16px 'serif';");      
           TextField bField = new TextField(); 
	   bField.setStyle("-fx-font: small bold 16px 'serif';");
	   TextField avgField = new TextField();
	   avgField.setStyle("-fx-font: small bold 16px 'serif';");
           Button submit = new Button("Submit"); 
	   changeButtonBackground(submit, STANDARD_CONTROLS_STYLE, HOVERED_CONTROLS_STYLE);
           Button clear = new Button("Clear"); 
	   changeButtonBackground(clear, STANDARD_CONTROLS_STYLE, HOVERED_CONTROLS_STYLE); 
     	   GridPane gridPane = new GridPane();
           gridPane.setMinSize(400, 200);
           gridPane.setPadding(new Insets(10, 10, 10, 10)); 
           gridPane.setVgap(10); 
           gridPane.setHgap(10);       
           gridPane.setAlignment(Pos.CENTER); 
           gridPane.add(seatNumTxt, 0, 0); 
           gridPane.add(snField, 1, 0); 
           gridPane.add(branchTxt, 0, 1);       
           gridPane.add(bField, 1, 1);
	   gridPane.add(avgTxt, 0, 2);       
           gridPane.add(avgField, 1, 2); 
           gridPane.add(submit, 0, 3); 
           gridPane.add(clear, 1, 3); 
	   
	   clear.setOnAction(e -> {
	      snField.setText("");
	      bField.setText("");
	      avgField.setText("");
	   });
	   submit.setOnAction(e -> {
		if(snField.getText().chars().allMatch(Character::isDigit) && (bField.getText().equals("Literary") || bField.getText().equals("Scientific")) && isADouble(avgField.getText())){
		    Student student = new Student(Long.parseLong(snField.getText()), bField.getText(), Double.parseDouble(avgField.getText()));
		    students.add(student);
		    Collections.sort(students, (s1, s2) -> {
			if(s1.getSeatNumber() > s2.getSeatNumber())	
	   		    return 1;
			else if(s1.getSeatNumber() < s2.getSeatNumber())
	   		    return -1;
			else 
	   		    return 0;
      		    });
		    Alert success = createAlert("success", "new student has been added", Alert.AlertType.INFORMATION);
		    success.showAndWait();
		}else{
		    Alert error = createAlert("error", "invalid input/s", Alert.AlertType.ERROR);
		    error.showAndWait();
 		}
	   });

	   Scene scene = new Scene(gridPane, 450, 250);
	   return scene;
	}
	
	public Stage getStage(){return this.stage;}
   }

   static class DeleteFrame{
	private Stage stage;
	public DeleteFrame() throws Exception{
	   stage = new Stage();
	   stage.setTitle("Delete Student");
	   stage.setScene(drawDeleteScene());
	   stage.setResizable(false);
	}
	public Scene drawDeleteScene() throws Exception{
	   Text seatNumTxt = new Text("Seat Number");
	   seatNumTxt.setStyle("-fx-font: normal bold 16px 'serif';");  
           TextField snField = new TextField(); 
	   snField.setStyle("-fx-font: small bold 16px 'serif';");   
           Button submit = new Button("Submit"); 
	   changeButtonBackground(submit, STANDARD_CONTROLS_STYLE, HOVERED_CONTROLS_STYLE);
     	   HBox  box = new HBox();
           box.setSpacing(15);
           box.setPadding(new Insets(10, 10, 10, 10));     
           box.setAlignment(Pos.CENTER); 
           box.getChildren().addAll(seatNumTxt,snField,submit);
	   submit.setOnAction(e -> {
		if(snField.getText().chars().allMatch(Character::isDigit)){
		    Optional<Student> stud = students.stream()
					   .filter(std -> std.getSeatNumber() == Long.parseLong(snField.getText()))
					   .findFirst();
		    if(stud.isPresent()){
			if(students.contains(stud.get())){
			    students = students.stream()
						.filter(s -> s != stud.get())
						.collect(Collectors.toCollection(LinkedList::new));
			    Alert alert = createAlert("success", "student with seat number : "+snField.getText()+" deleted successfully", Alert.AlertType.INFORMATION);
			    alert.showAndWait();
			}
		    }else{
			Alert alert = createAlert("error", "no student found with seat number : "+snField.getText(), Alert.AlertType.ERROR);
		        alert.showAndWait();
		    }
		}else{
		    Alert alert = createAlert("error", "invalid input", Alert.AlertType.ERROR);
		    alert.showAndWait();
		}
	   });

	   Scene scene = new Scene(box, 450, 150);
	   return scene;
	}
	public Stage getStage(){return this.stage;}
   }
	
   static class SearchFrame{
	private Stage stage;
	public SearchFrame() throws Exception{
	   stage = new Stage();
	   stage.setTitle("Search For Student");
	   stage.setScene(drawSearchScene());
	   stage.setResizable(false);
	}
	public Scene drawSearchScene() throws Exception{
	   Text seatNumTxt = new Text("Seat Number");
	   seatNumTxt.setStyle("-fx-font: normal bold 16px 'serif';");  
           TextField snField = new TextField(); 
	   snField.setStyle("-fx-font: small bold 16px 'serif';");   
           Button submit = new Button("Submit"); 
	   changeButtonBackground(submit, STANDARD_CONTROLS_STYLE, HOVERED_CONTROLS_STYLE);
     	   HBox  box = new HBox();
           box.setSpacing(15);
           box.setPadding(new Insets(10, 10, 10, 10));     
           box.setAlignment(Pos.CENTER); 
           box.getChildren().addAll(seatNumTxt,snField,submit);
	   VBox vBox = new VBox();
	   vBox.setSpacing(20);
	   TableView tableView = new TableView();
           TableColumn<Student, String> column1 = new TableColumn<>("Seat Number");
           column1.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
           TableColumn<Student, String> column2 = new TableColumn<>("Branch");
           column2.setCellValueFactory(new PropertyValueFactory<>("Branch"));
	   TableColumn<Student, String> column3 = new TableColumn<>("Average");
           column3.setCellValueFactory(new PropertyValueFactory<>("Average"));
           tableView.getColumns().addAll(column1,column2,column3);
	   vBox.getChildren().addAll(box,tableView);

	   submit.setOnAction(e -> {
		if(snField.getText().chars().allMatch(Character::isDigit)){
		    Optional<Student> stud = students.stream()
					   .filter(std -> std.getSeatNumber() == Long.parseLong(snField.getText()))
					   .findFirst();
		    if(stud.isPresent()){
			tableView.getItems().add(stud.get());
		    }else{
			Alert alert = createAlert("error", "no student found with seat number : "+snField.getText(), Alert.AlertType.ERROR);
		        alert.showAndWait();
		    }
		}else{
		    Alert alert = createAlert("error", "invalid input", Alert.AlertType.ERROR);
		    alert.showAndWait();
		}
	   });

	   Scene scene = new Scene(vBox, 450, 200);
	   return scene;
	}
	public Stage getStage(){return this.stage;}
   }
}