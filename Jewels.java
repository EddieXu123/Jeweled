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
import java.util.Scanner;
import java.util.InputMismatchException;
import java.lang.NumberFormatException;

public class Jewels extends Application {
  
  // Field to create the gridPane
  private GridPane myGrid;
  // Field that creates the different types of buttons used
  private static int sqrN;
  // Field that creates a 2D array of buttons
  private Button[][] sqr;
  // Field that sets each button to one of four colors
  private Color[] color = new Color[]{
    Color.PURPLE, Color.RED,Color.BLUE,Color.YELLOW, Color.ORANGE, Color.GREEN
  };
  // Field that creates an instance of a CornerRadii
  private static CornerRadii rad = new CornerRadii(20); // Gives radius of tile
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
  private static int height = 0;
  private static int width = 0;
  
  private int leftIndex = 0;
  private int rightIndex = 0;
  private int downIndex = 0;
  private int upIndex = 0;
  
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
    b.setBackground(new Background(new BackgroundFill(c, rad, null)));
  }
  
  /**
   * Method to check if result of swap produces 3 in a row horizontally
   * @param bt2n The button that is initially selected and then swapped with a neighboring button
   * @return int[]: The first index contains a 1 or 0, if the move was valid or not, the second
   * index contains the index of the left-most button in that row with the same color
   */
  public int[] checkHorizontal(Button btn2) {
    int[] result = new int[3];
    steps++;
    
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
    while((myGrid.getColumnIndex(btn2) + rightIndex < height) && Jewels.getButtonColor(btn2).equals(Jewels.getButtonColor(sqr[myGrid.getColumnIndex(btn2) + rightIndex][myGrid.getRowIndex(btn2)]))) {
      rightIndex++;
    }
    
    result[1] = leftIndex;
    result[2] = rightIndex;
    // If statement that checks if there are three or more in a row. If so, mark with stars and slide every button above down to its correct place
    if ((leftIndex + rightIndex) - 1 >= 3) {
      result[0] = 1;
    }
    else {
      result[0] = 0;
    }
    leftIndex = 0;
    rightIndex = 0;
    return result;
  }
  
  /**
   * Helper method to see if two buttons are neighbors of each other
   * @param b1, b2 The two buttons being tested
   * @return boolean If the two buttons are neighbors
   */
  public boolean isNeighbors(Button b1, Button b2) {
    // Two buttons are neighbors if:
    if ((Math.abs(myGrid.getRowIndex(b1) - myGrid.getRowIndex(b2)) == 1 // They are 1 row apart
       && myGrid.getColumnIndex(b1) == myGrid.getColumnIndex(b2)) // And in the same Column
          || // OR
        (Math.abs(myGrid.getColumnIndex(b1) - myGrid.getColumnIndex(b2)) == 1) // 1 Column apart
       && myGrid.getRowIndex(b1) == myGrid.getRowIndex(b2)) return true; // And in the same row
    
    return false;
  }
  
  /**
   * Method to check if the columns should be replaced
   * @param btn2 the location of the button that is being swapped into place
   * @return int[] An array containing 3 pieces of information, if the move was valid or not,
   * the bottom index of the column with the same color, and the top index of the column with the same color
   */
  public int[] checkVertical(Button btn2) {
    int[] result = new int[3];
    // While loop that goes through the top of the button that sees if they are of the same color. Records the number of buttons of the same color
    // In the same column above
    while ((myGrid.getRowIndex(btn2) - upIndex >= 0) && Jewels.getButtonColor(btn2).equals(Jewels.getButtonColor(sqr[myGrid.getColumnIndex(btn2)][myGrid.getRowIndex(btn2) - upIndex]))) {
      upIndex++;
    }
    // While loop that goes through the bottom of the button that sees if they are the same color. Records number of buttons of same color in column below
    while ((myGrid.getRowIndex(btn2) + downIndex <= width - 1) && Jewels.getButtonColor(btn2).equals(Jewels.getButtonColor(sqr[myGrid.getColumnIndex(btn2)][myGrid.getRowIndex(btn2) + downIndex]))) {
      downIndex++;
    }
    result[1] = downIndex;
    result[2] = upIndex;
    // If statement that checks if there are three or more in a column
    if ((upIndex + downIndex) - 1 >= 3) {
      result[0] = 1;
    }
    else {
      result[0] = 0;
    }
    
    upIndex = 0;
    downIndex = 0;
    return result;
  }
  
  /**
   * Method to mark tiles that have been validly swapped with a "*" symbol
   * @param Button the button that was initially selected on and swapped with a neighboring button
   */
  public void markTiles(Button button) {
    int[] vert = checkVertical(button);
    int[] horz = checkHorizontal(button);
    
    if (horz[0] == 1) {   
      for (int i = (myGrid.getColumnIndex(button) - horz[1] + 1); i < (myGrid.getColumnIndex(button)) + horz[2]; i++) {
        if(sqr[i][myGrid.getRowIndex(button)].getText() != "*"){
          counter++;
          if (counter == (height * width)) {
            System.out.println("You won in " + steps + " steps!");
            System.err.println(counter);
            System.err.println(height * width);
            System.exit(0);
          }
        }
        
        sqr[i][myGrid.getRowIndex(button)].setText("*");
        // loop from bottom to top rows 
        for(int j = myGrid.getRowIndex(button); j >= 1; j--){
          
          Jewels.setButtonColor(Jewels.getButtonColor(sqr[i][j-1]), sqr[i][j]);
          
        }
        
        // randomize first row
        Color c = color[new Random().nextInt(sqrN)];
        Jewels.setButtonColor(c, sqr[i][0]);;
      }
    }
    
    if (vert[0] == 1) {
      int colHeight = (vert[2] + vert[1]) - 1;
      int j = myGrid.getColumnIndex(button);
      // For loop that marks the stars and substitutes the other boxes.
      for (int i = (myGrid.getRowIndex(button)) + vert[1] - 1; i > (myGrid.getRowIndex(button)) - vert[2]; i--) {  
        // If statement that will draw stars
        if(sqr[myGrid.getColumnIndex(button)][i].getText() != "*"){
          this.counter++;
          if (counter == (height * width)) {
            System.err.println("You won in " + steps + " steps!");
            System.out.println(counter);
            System.err.println(height * width);
            System.exit(0);
          }
        }
        sqr[myGrid.getColumnIndex(button)][i].setText("*");
        
        // If statement that checks for substitution 
        if(i-colHeight < 0){
          Color c = color[new Random().nextInt(sqrN)];
          Jewels.setButtonColor(c, sqr[j][i]);
        }
        else{
          Jewels.setButtonColor(Jewels.getButtonColor(sqr[j][i-colHeight]), sqr[j][i]);
        }
      }
      // Randomize all the new column colors
      // For loop that goes through the boxes and sets the colors to the new one
      for(int k = 0; k <= colHeight; k++) {
        Color c = color[new Random().nextInt(sqrN)];
        Jewels.setButtonColor(c, sqr[j][k]);
      }
    }
    
    if (horz[0] == 1 || vert[0] == 1) {
      System.out.println("Nice work! You've marked " + counter + " of the " + height * width + " total tiles!");
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
      // System.out.println(myGrid.getRowIndex(btn1) + ", " + myGrid.getColumnIndex(btn1));
      // System.err.println(myGrid.getRowIndex(btn2) + ", " + myGrid.getColumnIndex(btn2));
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
    // Creating the grid pane and making the board height * width
    this.myGrid = new GridPane();
    this.sqr = new Button[height][width];
    
    // for loop to go through each of the buttons (grids) of the board and filling them with a color
    for(int i = 0; i < height; i++){
      for(int j = 0; j < width; j++){
        sqr[i][j] = new Button("  ");
        sqr[i][j].setPrefSize(10,10);
        sqr[i][j].setBackground(new Background(new BackgroundFill(color[new Random().nextInt(sqrN)], rad, null)));
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
            // If the second button was the first one, reset
            if (b2 == b1) {
              isClicked = true;
              setButtonColor(rememberColor, b1);
            }
            // Otherwise, the second button must be a neighbor and be a valid move in order to swap the two buttons
            else {
              isClicked = true;
              setButtonColor(rememberColor, b1);
              if (isNeighbors(b1, b2)) {
                // System.out.println("Hello");
                boolean neighbor = swapButton(b1, b2);
                System.out.println(neighbor);
                if (checkVertical(b2)[0] != 1 && checkHorizontal(b2)[0] != 1) {
                  swapButton(b1, b2); // swap back
                  System.err.println("Illegal move!");
                }
                markTiles(b2);
              }
            }
          }
          
        });
        myGrid.add(sqr[i][j],i,j);
        myGrid.setMargin(sqr[i][j], null);
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
   * @param args Height, Width, number of colors
   */
  public static void main(String[] args) throws InputMismatchException, NumberFormatException {
    // 4 Variables to keep track of if the inputs to run the program are valid
    boolean checkValid = true;
    boolean validWidth = true;
    boolean validHeight = true;
    boolean validColor = true;
    
    while (checkValid) {
      try {
        // Until we have a valid width entered, keep running this loop
        while (validWidth) {
          System.out.println("What is the preferred width of your board? (Please keep it between 8-20 for the best experience!)");
          Scanner scan2 = new Scanner(System.in);
          height = scan2.nextInt();
          if (height < 8 || height > 20) throw new InputMismatchException(); // Too large or small
          validWidth = false; // Exit this loop
        }
        // Until we have a valid width entered, keep running this loop
        while (validHeight) {
          System.out.println("What is the preferred height of your board? (Please keep it between 8-20 for the best experience!)");
          Scanner scan = new Scanner(System.in);
          width = scan.nextInt();
          if (width < 8 || width > 20) throw new InputMismatchException(); // Too large or small
          validHeight = false; // Exit loop
        }
        // Until we have a valid color # entered, keep running this loop
        while (validColor) {
          System.out.println("How many different tile colors would you like? (Please pick a number between 4-6)");
          Scanner scan3 = new Scanner(System.in);
          sqrN = scan3.nextInt();
          if (sqrN < 4 || sqrN > 6) throw new NumberFormatException(); // To large or small
          validColor = false; // Exit loop
        }
        checkValid = false; // Exit entire loop. Now, we have our valid 3 entries
      }
      
      // If there's an incorrect dimension given
      catch (InputMismatchException e) {
        System.out.println("Please enter a number between 8 and 20!");
      }
      
      // If there's an incorrect number of colors given
      catch (NumberFormatException n) {
        System.out.println("Please enter a number between 4 and 6!");
      }
    }
    
    // Launch the program
    Application.launch(args);
  }
  
  /** A static nested class that is an action listener that rotates the button clicked */
  private static class ProcessClick implements EventHandler<ActionEvent> {
    
    /** Respond to a button click
      * @param e the information about the action event 
      */
    public void handle(ActionEvent e) {
      Button b = (Button)e.getSource();
    }
  } 
}

