import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Knapsack {

    public final static int BUDGET = 100; // change to set the budget throughout the whole program

    // class used to represent an Item and its name, price, & value
    public static class Item {
        String name;
        int price;
        int value;

        // Item constructor initializes based on name, price, & value
        public Item(String name, int price, int value) {
            this.name = name;
            this.price = price;
            this.value = value;
        }

        // toString that allows for easy printing
        @Override
        public String toString() {
            return name + " price: " + price + " value: " + value;
        }
    }

    /****************************************************************
     *                       max Method
     * @param item // item we are currently considering
     * @param value1 // value of the cell of the previous row at currentColumn - item.price column
     * @param value2 // value of the previous row at the current column
     * - method adds the current item's price being considered to the value1 and compares it to the value2
     */
    public static int max(Item item, int value1, int value2) {
        if(item.value + value1 > value2)
            return item.value + value1;
        else
            return value2;
    }

    /****************************************************************
     *                       fillTable Method
     * @param c // our table represented as a 2D int array
     * @param rows // number of matrix rows
     * @param itemsList // list of items to be considered
     * - method goes through and fills our table to with the
     * corresponding values in a dynamic programming fashion
     */
    public static void fillTable(int[][] c, int rows, List<Item> itemsList) {

        // fills out first column of the table with 0 values for price 0
        for(int i = 0; i < rows; i++)
            c[i][0] = 0;

        // fills out first row of the table with values corresponding to the first item in the list
        for(int i = 0; i < BUDGET + 1; i++){
            // item price is <= i we store the price else cell equals 0
            if(itemsList.get(0).price <= i)
                c[0][i] = itemsList.get(0).price;
            else
                c[0][i] = 0;
        }

        // nested for loops goes through every cell in the table except for row 0 and column 0 which we previously initialized
        for(int i = 1; i < rows; i++) {
            for(int j = 1; j < BUDGET + 1; j++) {
                int itemPrice = itemsList.get(i).price;
                /* if item price > j then set current cell equal to previous row's cell at current column
                 * else set it equal to the max of the ( item.price + ( the cell of previous row at column that's j - item.price ) )
                 * compared to the previous row's cell at the current column
                 */
                if(itemPrice > j){
                    c[i][j] = c[i-1][j];
                } else {
                    c[i][j] = max(itemsList.get(i), c[i-1][j-itemPrice], c[i-1][j]);
                }
            }
        }
        //printTable(c, rows, BUDGET + 1);
    }

    /****************************************************************
     *                       traceBack Method
     * @param c // our table represented as a 2D int array
     * @param rows // number of matrix rows
     * @param itemsList // list of items to be considered
     * - method starts at c[rows-1][BUDGET] and traces back the items that were
     *   used in the table to get the optimal solution and prints their names
     */
    public static void traceBack(int[][] c, int rows, List<Item> itemsList) {
        int i = rows - 1;
        int j = BUDGET;

        while(c[i][j] != 0){
            //if current cell at previous row at current column != current cell then current item is used and go one row up and item.price columns left
            //else the previous row at current column is same value so then current item was not used so go there
            if (c[i][j] != c[i - 1][j]) {
                System.out.println(itemsList.get(i).name);
                j -= itemsList.get(i).price;
                i--;
            } else {
                i--;
                System.out.println(itemsList.get(i).name);
            }
        }
    }

    public static void main(String[] args) {

        String line = "";
        List<Item> itemList = new ArrayList<>();

        // BufferedReader to read in items info
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(args[0]));
            fileReader.readLine(); //to skip first line
            // reads in all the item lines to determine the amount of items
            while((line = fileReader.readLine()) != null){
                String[] queryArray = line.split(",");
                // dynamically adds items to the list
                itemList.add(new Item(queryArray[0], Integer.parseInt(queryArray[1]), Integer.parseInt(queryArray[2])));
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // initialize the dynamic programming table matrix
        int[][] c = new int[itemList.size() + 1][BUDGET + 1];

        // if there are no items return
        if(itemList.isEmpty()){
            System.out.println("ERROR: No items in the list\n");
            System.exit(-1);
        }

        // fills in the table with values
        fillTable(c, itemList.size(), itemList);

        // prints out the optimal value
        System.out.println("The optimal solution: " + c[itemList.size()-1][BUDGET]);

        // traces through table to find used items and prints them
        traceBack(c, itemList.size(), itemList);

        /*
        // prints out all items in the list for debugging
        for(Item i : itemList){
            System.out.println(i);
        }
        System.out.println(itemList.size() + " items in list");
         */
    }

    // prints out the table (debug function)
    public static void printTable(int[][] c, int rows, int columns){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                System.out.print(c[i][j] + " ");
            }
            System.out.println();
        }
    }

}
