/**
 * A game like Bejeweled that informs you how many moves you made to win
 * A win is when all of the buttons are marked with a "*"
 * To mark a button with a "*" you must swap buttons such that the location in which the
 * primary button you select becomes one in which there is at least three in a row or column
 * between that button and its surroundings
 * @author Eddie Xu
 */

// Imported javafx files
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.geometry.Insets;
import javafx.scene.layout.CornerRadii;
import java.util.Random;

public class Jewels extends Application {
  
  // Field to create the gridPane
  private GridPane myGrid;
  // Field that creates the different types of buttons used
  private int sqrN = 4;
  // Field that creates a 2D array of buttons
  private Button[][] sqr;
  // Field that sets each button to one of four colors
  private Color[] color = new Color[]{
    Color.GREEN,Color.RED,Color.CYAN,Color.YELLOW
  };
  // Field that creates an instance of a CornerRadii
  private static CornerRadii rad = new CornerRadii(1);
  // Field that creates an instance of a new Inset
  private static Insets inset = new Insets(1);
  // Field that tests if your button has been clicked or not
  private static boolean isClicked = true;
  // Field that remembers the original color of the button (before darkening)
  private static Color rememberColor;
  // Field that creates an instance of button1, the first clicked
  private Button b1;
  // Field that creates an instance of button2, the second
  private Button b2;
  // Field tests if you can swap the boxes
  private boolean swappable = false;
  // Field that looks at the selected button
  private Button selectedButton = null;
  // Field that counts how many times you moved
  private int counter = 0;
  // Field that counts how many stars there are
  private int steps = 0;
  
  /**
   * Helper method to get the color of a button
   * @param b the name of the button
   * @return the color value of the button in question
   */
  public static Color getButtonColor(Button b) {
    String btnColor = b.getBackground().getFills().get(0).getFill().toString();
    return Color.valueOf(btnColor);
  }
  
  /**
   * Helper method to set the buttons to a color
   * @param c the color of the button
   * @param b the name of the button
   */
  public static void setButtonColor(Color c, Button b) {
    b.setBackground(new Background(new BackgroundFill(c, rad, inset))) ;
  }
  
  /**
   * Method to check the Horizontal grid of the 
   */
  public void checkHorizontal(Button btn2) {
    
    steps++;
    
    int leftIndex = 0;
    int rightIndex = 0;
    
    /**
     * While loop that starts with one button and looks to the left and right of that button
     * If the color next to the button is the same, we keep going until there is three or more of that color 
     * in a row. When that happens, mark them with a star
     */
    
    // While loop that checks the left side of the button being moved
    while((myGrid.getColumnIndex(btn2) - leftIndex >= 0) && Jewels.getButtonColor(btn2).equals(Jewels.getButtonColor(sqr[myGrid.getColumnIndex(btn2) - leftIndex][myGrid.getRowIndex(btn2)]))) {
      leftIndex++;
    }
    // While loop that checks the right side of the button being moved
    while((myGrid.getColumnIndex(btn2) + rightIndex < 10) && Jewels.getButtonColor(btn2).equals(Jewels.getButtonColor(sqr[myGrid.getColumnIndex(btn2) + rightIndex][myGrid.getRowIndex(btn2)]))) {
      rightIndex++;
    }
    // If statement that checks if there are three or more in a row. If so, mark with stars and slide every button above down to its correct place
    if ((leftIndex + rightIndex) - 1 >= 3) {
      for (int i = (myGrid.getColumnIndex(btn2) - leftIndex + 1); i < (myGrid.getColumnIndex(btn2)) + rightIndex; i++) {
        if(sqr[i][myGrid.getRowIndex(btn2)].getText() != "*"){
          counter++;
          System.out.println(counter);
        }
        sqr[i][myGrid.getRowIndex(btn2)].setText("*");
        // loop from bottom to top rows 
        for(int j = myGrid.getRowIndex(btn2); j >= 1; j--){
          
          Jewels.setButtonColor(Jewels.getButtonColor(sqr[i][j-1]), sqr[i][j]);
          
        }
        
        // randomize first row
        Color c = color[new Random().nextInt(sqrN)];
        Jewels.setButtonColor(c, sqr[i][0]);;
        
      }
    }
  }
  
  /**
   * Method to check if the columns should be replaced
   * @param btn2 the location of the button that is being swapped into place
   */
  public void checkVertical(Button btn2) {
    checkWin();
    
    // Fields to check the indeces used in the while loop
    int bottomIndex = 0;
    int upIndex = 0;
    // While loop that goes through the top of the button that sees if they are of the same color. Records the number of buttons of the same color
    // In the same column above
    while ((myGrid.getRowIndex(btn2) - upIndex >= 0) && Jewels.getButtonColor(btn2).equals(Jewels.getButtonColor(sqr[myGrid.getColumnIndex(btn2)][myGrid.getRowIndex(btn2) - upIndex]))) {
      upIndex++;
    }
    // While loop that goes through the bottom of the button that sees if they are the same color. Records number of buttons of same color in column below
    while ((myGrid.getRowIndex(btn2) + bottomIndex <= 7) && Jewels.getButtonColor(btn2).equals(Jewels.getButtonColor(sqr[myGrid.getColumnIndex(btn2)][myGrid.getRowIndex(btn2) + bottomIndex]))) {
      bottomIndex++;
    }
    // If statement that checks if there are three or more in a column
    if ((upIndex + bottomIndex) - 1 >= 3) {
      int height = (upIndex + bottomIndex) - 1;
      int j = myGrid.getColumnIndex(btn2);
      // For loop that marks the stars and substitutes the other boxes.
      for (int i = (myGrid.getRowIndex(btn2)) + bottomIndex - 1; i > (myGrid.getRowIndex(btn2)) - upIndex; i--) {
        
        // If statement that will draw stars
        if(sqr[myGrid.getColumnIndex(btn2)][i].getText() != "*"){
          this.counter++;
          System.out.println(counter);
          
        }
        sqr[myGrid.getColumnIndex(btn2)][i].setText("*");
        
        // If statement that checks for substitution 
        if(i-height < 0){
          Color c = color[new Random().nextInt(sqrN)];
          Jewels.setButtonColor(c, sqr[j][i]);
        }
        else{
          Jewels.setButtonColor(Jewels.getButtonColor(sqr[j][i-height]), sqr[j][i]);
        } 
      }
      // Randomize all the new column colors
      // For loop that goes through the boxes and sets the colors to the new one
      for(int k = 0; k <= height; k++) {
        Color c = color[new Random().nextInt(sqrN)];
        Jewels.setButtonColor(c, sqr[j][k]);
      }
    }
  }
  
  /**
   * Method to check how many steps you took to win
   */
  public void checkWin(){
    // If statement that sees if the number of stars you have is enough for a filled grid
    if(counter >= 10*8){
      System.out.println("You Won in " + steps + " steps");
    }
  }
  
  
  
  /**
   * Helper method to swap the buttons
   * @param btn1 the name of the first button I want to remember
   * @param btn2 the name of the second button I want to remember
   * @return boolean: if a button can be swapped or not
   */
  public boolean swapButton(Button btn1, Button btn2) {
    // Getting the indeces of each of the rows of the buttons
    int row1 = myGrid.getRowIndex(btn1);
    int row2 = myGrid.getRowIndex(btn2);
    // Getting the indeces of each of the columns of the buttons
    int col1 = myGrid.getColumnIndex(btn1);
    int col2 = myGrid.getColumnIndex(btn2);
    // If statement to see if the buttons are next to each other, therefore able to be swapped
    if((Math.abs(row1 - row2) == 1 && col1 == col2) || (Math.abs(col1 - col2) == 1 && row1 == row2)) {
      // Remembering the original color of the buttons
      Color rmbColor1 = getButtonColor(btn1);
      Color rmbColor2 = getButtonColor(btn2);
      // Swapping the colors
      setButtonColor(rmbColor1, btn2);
      setButtonColor(rmbColor2, btn1);
    }
    
    else {
      return false;
    }
    return true;
  }
  
  
  
  /**
   * Start method that creates the "Stage"
   * @param primaryStage the name of the Stage
   */
  
  public void start(Stage primaryStage) throws Exception{
    // Creating the grid pane and making the board 10x8
    this.myGrid = new GridPane();
    this.sqr = new Button[10][8];
    
    // for loop to go through each of the buttons (grids) of the board and filling them with a color
    for(int i = 0; i < 10; i++){
      for(int j = 0; j < 8; j++){
        sqr[i][j] = new Button("  ");
        sqr[i][j].setPrefSize(10,10);
        sqr[i][j].setBackground(new Background(new BackgroundFill(color[new Random().nextInt(sqrN)], null, null)));
        sqr[i][j].setBorder(new Border(new BorderStroke(null, null, rad, null)));
        
        // Lambda notation that does the commands for playing the game
        sqr[i][j].setOnAction(e -> {
          // Tell if the first button is able to be selected and get the color of that button in order to swap
          if(isClicked) {
            b1 = (Button)(e.getSource());
            rememberColor = getButtonColor(b1);
            setButtonColor(getButtonColor(b1).darker(), b1);
            isClicked = false;
          }
          // Tell if the second button is swappable with the first and see if they substitute when necessary
          else {
            b2 = (Button)(e.getSource());
            isClicked = true;
            setButtonColor(rememberColor, b1);
            swapButton(b1, b2);
            checkHorizontal(b2);
            checkVertical(b2);
          }
          
        });
        myGrid.add(sqr[i][j],i,j);
        myGrid.setMargin(sqr[i][j], inset);
      }
    }
    myGrid.setHgap(1);
    myGrid.setVgap(1);
    
    // Create the grid. Code made in class
    BorderPane borderPane = new BorderPane();
    borderPane.setCenter(myGrid);
    Scene scene = new Scene(borderPane);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  /**
   * The method to launch the program.
   * @param args The command line arguments. The arguments are passed on to the JavaFX application.
   */
  public static void main(String[] args) {
    Application.launch(args);
    
  }
  /** A static nested class that is an action listener that rotates the button clicked */
  private static class ProcessClick implements EventHandler<ActionEvent> {
    
    /** Respond to a button click
      * @param e the information about the action event 
      */
    public void handle(ActionEvent e) {
      Button b = (Button)e.getSource();
      System.out.println("You clicked Button 1");
    }
  } 
}


