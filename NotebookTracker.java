//imports
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


class NotebookTracker{

// times cross functions
  public static boolean timesCross(int sPeriod1, int ePeriod1, int sPeriod2, int ePeriod2){
    if (sPeriod1 < sPeriod2 && sPeriod2 < ePeriod1){
      return true;
    }
    else if  (sPeriod2 < sPeriod1 && sPeriod1 <ePeriod2){
      return true;
    }
    else if (sPeriod1 >= sPeriod2 && ePeriod1 <= ePeriod2){
      return true;
    }
    else if (sPeriod2 >= sPeriod1 && ePeriod2 <= ePeriod1){
      return true;
    }
    else {
      return false;
    }
  }

  public static boolean timesCrossLate(int sPeriod1, int ePeriod1, int sPeriod2, int ePeriod2){
    if (sPeriod1 > ePeriod1 && sPeriod2 > ePeriod2){
      ePeriod1 = ePeriod1 + 24;
      ePeriod2 = ePeriod2 + 24;
      return timesCross(sPeriod1, ePeriod1, sPeriod2, ePeriod2);
    }
    else if (sPeriod2 > ePeriod2){
      ePeriod2 = ePeriod2 + 24;
      return timesCross(sPeriod1, ePeriod1, sPeriod2, ePeriod2);
    }
    else if (sPeriod1 > ePeriod1){
      ePeriod1 = ePeriod1 + 24;
      return timesCross(sPeriod1, ePeriod1, sPeriod2, ePeriod2);
    }
    else{
      return timesCross(sPeriod1, ePeriod1, sPeriod2, ePeriod2);
    }
  }

//reading file log
  public static boolean ReadingFileAndManipulation(int number, int start, int end, int setNUM, int numberOP) throws FileNotFoundException{
    File text = new File("access-log-"+setNUM+".txt");
    Scanner scnr = new Scanner(text);

//Moving line by line or .txt to array
    String[] names = new String [number];
    int lineNumber = 0;
    while(scnr.hasNextLine()){
      String line = scnr.nextLine();
       names[lineNumber] = line;
       lineNumber++;
      }    

//splitting start end and name of persons element of array
    for (int i=0; i < number; i++){
      String[] parts = names[i].split(" "); //parts[0] = names
      int start1 = Integer.parseInt(parts[1]); //start
      int end2 = Integer.parseInt(parts[2]); //end
      if (timesCrossLate(start, end, start1, end2) == true){ 
        System.out.println(parts[0]+" might have the notebook.");
        numberOP +=1; //number of people that might have
      }
    }
    setNUM +=1; // additional access log files increment
    File f = new File("access-log-"+setNUM+".txt");
    if(f.exists() && !f.isDirectory()) { 
//check if there is any additional files
      try {
        ReadingFileAndManipulation(number, start, end, setNUM, numberOP);
      }
      catch (Exception e) { //else quit
        e.getStackTrace();
      }
  }
  System.out.println("Number of staff who might have the notebook: "+numberOP); //final number of people that might have
  scnr.close();
  return true;
  }


//request for cross staff
  public static int getCrossingStaff(int start, int end)throws FileNotFoundException {
//variable declaration
    String name;//name of person
    int sPeriod;//start period
    int ePeriod;//end period
    int NStaff;//number of staff
    int numberC = 0;//number of people that might have notebook
//check if file exist so dont need to manually type
    File f = new File("access-log-1.txt");
    if(f.exists() && !f.isDirectory()) { 
      int count = 0;
      try {
        Scanner sc = new Scanner(f);
        while(sc.hasNextLine()) {
          sc.nextLine();
          count++;
        }
        sc.close();// close scanner
      } catch (Exception e) {
        e.getStackTrace();
      }
      ReadingFileAndManipulation(count, start, end, 1,0);
    }
    else{
    Scanner myObj = new Scanner(System.in);
    System.out.println("Enter the number of staff in the lab:");
    NStaff = Integer.parseInt(myObj.nextLine());
      for (int i = 0; i < (NStaff); i++) 
      {
        System.out.println("Enter the staff member's name:"); 
        name = myObj.nextLine();
        System.out.println("Enter the entry time:");
        sPeriod = Integer.parseInt(myObj.nextLine());
        System.out.println("Enter the exit time:");
        ePeriod = Integer.parseInt(myObj.nextLine());
        boolean b0 = timesCross(start, end, sPeriod, ePeriod);
        if (b0 == true){
          System.out.println(name+" might have the notebook.");
          numberC += 1;//number of people that might have notebook increment
        }
        else {
          System.out.println(name+" will not have the notebook.");
        }
      }

      System.out.println("Number of staff who might have the notebook: "+ numberC); //number of people that have notebook
      myObj.close();
    }
    return numberC;
  }


//main function
  public static void main(String[] args) throws FileNotFoundException {
    int eLost;//start lost
    int nLost;//end lost
//object scanner set
    Scanner myObj = new Scanner(System.in);
    System.out.println("What is the earliest the notebook could have been lost?");
    eLost = Integer.parseInt(myObj.nextLine());//change string to integer

    System.out.println("When did you notice the notebook was missing?");

    nLost = Integer.parseInt(myObj.nextLine());//change string to integer
    NotebookTracker.getCrossingStaff(eLost, nLost);
    myObj.close();
  }
}