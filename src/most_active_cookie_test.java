import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.Test;
import org.junit.runner.*;


public class most_active_cookie_test {


    @Test
    public void testValidate(){ 
        most_active_cookie test = new most_active_cookie();
        //correct format test
        assertTrue("Date not validated correctly",test.validate("2018-12-09T14:19:00+00:00"));
        //incorrect format tests
        assertTrue("Date not validated correctly",!test.validate("201-12-09T14:19:00+00:00"));
        assertTrue("Date not validated correctly",!test.validate("2018-12-09T24:19:00+00:00"));
        assertTrue("Date not validated correctly",!test.validate("2018-12-09T23:19:00+25:00"));
    }

    @Test
    public void testConvertDate(){
        most_active_cookie test = new most_active_cookie();
        //tests to check if dates are correctly converted to UTC
        assertEquals("Date not converted correctly","2018-12-09",test.convertDate("2018-12-09T14:19:00+00:00"));
        assertEquals("Date not converted correctly","2018-12-08",test.convertDate("2018-12-08T14:19:00+00:00"));
        assertEquals("Date not converted correctly","2018-12-08",test.convertDate("2018-12-09T01:00:00+05:30"));
    }

    @Test
    public void testCreateDate(){
        most_active_cookie test = new most_active_cookie();
        Scanner sc = new Scanner("cookie,timestamp\n"+
        "AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00\n"+
        "SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00\n"+
        "5UAVanZf6UtGyKVS,2018-12-09T07:25:00+00:00\n"+
        "5UAVanZf6UtGyKVS,2018-12-09T09:25:00+00:00\n");
        try{
            test.createDate("2018-12-09", sc);
        }catch(IOException e){
            fail("Data Parsed Wrongly");
        }
        HashMap<String,Integer> map = test.getMap();
        assertTrue("HashMap not populated correctly",1==map.get("AtY0laUfhglK3lC7"));
        assertTrue("HashMap not populated correctly",1==map.get("SAZuXPGUrfbcn5UA"));
        assertTrue("HashMap not populated correctly",2==map.get("5UAVanZf6UtGyKVS"));
        assertTrue("HashMap has wrong amount of elements",3==map.size());
    }

    @Test
    public void testCalculateDate(){
        most_active_cookie test = new most_active_cookie();
        Scanner sc = new Scanner("cookie,timestamp\n"+
        "AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00\n"+
        "SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00\n"+
        "5UAVanZf6UtGyKVS,2018-12-09T07:25:00+00:00\n"+
        "5UAVanZf6UtGyKVS,2018-12-09T09:25:00+00:00\n");
        try{
            test.createDate("2018-12-09", sc);
        }catch(IOException e){
        }
        ArrayList<String> output = test.calculateDate();
        ArrayList<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("5UAVanZf6UtGyKVS");
        assertEquals("Calculated output is incorrect",expectedOutput,output);
    }
}
