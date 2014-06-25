
import java.net.*;
import java.nio.charset.Charset;
import java.io.*;

import org.json.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TocHw4
{
  public static void main(String[] args)
  {
   
    getUrlContents(args[0]);
    //System.out.println(output);
  }
 
  private static void getUrlContents(String theUrl)
  {
    try
    {
      // create a url object
      URL url = new URL(theUrl);
 
      // create a urlconnection object
      URLConnection urlConnection = url.openConnection();

      int max=0,min=999999999;
      System.out.println("Input:"+theUrl);
      JSONArray jsonRealPrice = new JSONArray(new JSONTokener(new InputStreamReader(urlConnection.getInputStream(),Charset.forName("UTF-8"))));
      
      
      int max_month=0;
      Pattern pattern=Pattern.compile(".*(路|街|大道)|^(路|街|大道).*巷");
      Map<String,Integer> linkmap = new LinkedHashMap<String,Integer>();
      Map<String,Integer> money = new LinkedHashMap<String,Integer>();
      Map<String,Integer> maxmap = new LinkedHashMap<String,Integer>();
      Map<String,Integer> minmap = new LinkedHashMap<String,Integer>();
      Map<String,Integer> month_count = new LinkedHashMap<String,Integer>();
      
      for(int x=0;x<jsonRealPrice.length();x++)
      {
        JSONObject obj = jsonRealPrice.getJSONObject(x);
        Matcher matcher =pattern.matcher(obj.getString("土地區段位置或建物區門牌"));
        while(matcher.find())
        {
          //String[] address=obj.getString("土地區段位置或建物區門牌").split("[路街]");
          //System.out.println();
          linkmap.put(matcher.group()+" "+obj.getInt("交易年月"),obj.getInt("總價元") );
          money.put(matcher.group()+" "+obj.getInt("交易年月")+obj.getInt("總價元"),obj.getInt("總價元") );
          //System.out.println(matcher.group());
          
        }
      }
      //initialize data
      for(Entry<String,Integer> entry:linkmap.entrySet()){
        String[] address=entry.getKey().split(" ");
        month_count.put(address[0],0);
        maxmap.put(address[0],0);
        minmap.put(address[0],0);
      }
      //output month count
      for(Entry<String,Integer> entry:linkmap.entrySet()){
        String[] address=entry.getKey().split(" ");
        month_count.put(address[0],month_count.get(address[0])+1);
      }
      //find max month
      for(Entry<String,Integer> entry:month_count.entrySet()){
          //System.out.print(entry.getKey()+" "+entry.getValue()+"\n");
          if(entry.getValue()>max_month)
            max_month=entry.getValue();
      }
      //System.out.println(max_month);
      //get which object has max month
      for(Entry<String,Integer> entry:month_count.entrySet()){
          //System.out.print(entry.getKey()+" "+entry.getValue()+"\n");
        if(entry.getValue()==max_month)
        {
          for(Entry<String,Integer> entry2:money.entrySet()){
              String[] address=entry2.getKey().split(" ");
              
              if(address[0].equals(entry.getKey()))
              {
                //System.out.println(address[0]+" "+entry.getKey()+"/");
                if(max<entry2.getValue())
                  max=entry2.getValue();
                if(min>entry2.getValue())
                  min=entry2.getValue();
              }
            }
            
          System.out.println(entry.getKey()+", 最高成交價:"+max+", 最低成交價:"+min);
          max=0;min=999999999;
        }
      }
      
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
 
}