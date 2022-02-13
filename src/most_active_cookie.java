import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class most_active_cookie{
    
    private HashMap<String, Integer> dayCount;
    private int max=0;
    public most_active_cookie(){
        dayCount = new HashMap<>();
    }
    HashMap<String,Integer> getMap(){
        return dayCount;
    }

    //Regex to check if entered date is correct
    private static Pattern DATE_PATTERN = Pattern.compile(
        "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$" 
        + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
        + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$" 
        + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");
    //Regex to check if entered time is correct
    private static Pattern TIME_PATTERN = Pattern.compile(
        "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])\\+([0-1][0-9]|2[0-3]):([0-5][0-9])$");

    //adds all cookies for specific day to a hashmap to keep count
    void createDate(String date, Scanner sc) throws IOException{ 
        sc.useDelimiter(",");
        boolean found = false;
        while(sc.hasNext()){
            String cookie = sc.next();
            String currDate = sc.nextLine().substring(1);
            //remove the first line of csv 
            if(cookie.contains("cookie") && currDate.contains("timestamp")){
                continue;
            }
            //validate that the date and time entered is correct
            if(!validate(currDate)){
                throw new IOException("Improperly formatted file, please check timestamps");
            }
            //convert date to useable format
            currDate=convertDate(currDate);

            //logic to see if appropriate cookie is found
            if(currDate.equals(date)){
                found=true;
                dayCount.put(cookie,dayCount.getOrDefault(cookie, 0)+1);
                if(dayCount.get(cookie)>max){
                    max=dayCount.get(cookie);
                }
            }else{
                if(found){
                    return;
                }
            }
        }
    }
    //function to validate the format of date and time
    boolean validate(String currDate) {

        String date = currDate.split("T")[0];
        String time = currDate.split("T")[1];

        //validate date using regex pattern generated above
        if(!DATE_PATTERN.matcher(date).matches()){
            return false;
        }

        //validate time using regex pattern generated above
        if(!TIME_PATTERN.matcher(time).matches()){
            return false;
        }

        return true;
    }

    //function to convert date to UTC format
    String convertDate(String currDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        Date result = null;
        try{
            result =  df.parse(currDate);
        }catch(ParseException e){
        }
        result.toInstant();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(result);
        return (calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH) + 1)+"-"+String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));
    }

    //return most frequently found cookies for the specific day
    ArrayList<String> calculateDate(){
        ArrayList<String> output = new ArrayList<>();
        for (Map.Entry<String,Integer> mapElement : dayCount.entrySet()) {
            if((int)mapElement.getValue()==max){
                output.add((String)mapElement.getKey());
            }
        }
        return output;
    }
    //parse in data from command line and validate
    public static void main(String args[]){
        if(args.length<3){
            System.out.println("Not Enough arguments");
            return;
        }
        Scanner sc;
        try{
            sc = new Scanner(new File(args[0]));
        }catch(FileNotFoundException e){
            System.out.println("Can't find specified file");
            return;
        }
        most_active_cookie currData = new most_active_cookie();
        if(args[1].equals("-d")){
            if(2>=args.length){
                System.out.println("Please enter a valid date");
                return;
            }
            String date = args[2];
            if(!DATE_PATTERN.matcher(date).matches()){
                System.out.println("Incorrect date format");
                return;
            }
            try{
                currData.createDate(date,sc);
            }catch(IOException e){
                System.out.println(e);
                return;
            }
            ArrayList<String> output=currData.calculateDate();
            for(String x:output){
                System.out.println(x);
            }
        }
        sc.close();
    }
}

