package com.example.velocity.limit.services;

import org.json.JSONArray;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

public class VelocityLimitService {

    public static String readJsonToString() throws IOException {
        File file = ResourceUtils.getFile("classpath:static/input_data.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String json = "";
        try {
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(",");
                line = reader.readLine();
            }
            sb = sb.deleteCharAt(sb.length() - 1) ;
            json = "["+sb.toString()+"]";
        } finally {
            reader.close();
        }
        return json;
    }

    public static Boolean processJsonInformation(String jsonData) throws JSONException, ParseException, FileNotFoundException, UnsupportedEncodingException {
        JSONArray transactions = new JSONArray(jsonData);
        Map<Integer, String[]> dataLimit = new HashMap<>();
        String dataFinal = "";
        for (int i = 0; i < transactions.length(); i++) {
            JSONObject t = transactions.getJSONObject(i);
            int customerId = t.getInt("customer_id");
            String loadAmount = t.getString("load_amount");
            String transactionTime = t.getString("time");
            String dateParsed = getDayWeekYearValueFromDateTime(transactionTime);
            int transactionId = t.getInt("id");
            Double amount = convertAmount(loadAmount);
            Boolean status = true;
            String partsDate[] = dateParsed.split(",");
            if (dataLimit.containsKey(customerId)){
                String value = Arrays.toString(dataLimit.get(customerId));
                String strNew = value.replace("[", "").replace("]", "");
                String parts[] = strNew.split(",");
                Double amountData = Double.valueOf(parts[0]);
                int dayData = Integer.parseInt(parts[1].strip());
                int weekData = Integer.parseInt(parts[2].strip());
                int timeLoadWeekData = Integer.parseInt(parts[3].strip());
                Double sumAmount = amountData + amount;
                if (timeLoadWeekData <= 3){
                    status = false;
                } else if (sumAmount > 5000 && dayData == Integer.parseInt(partsDate[1])) {
                    status = false;
                    sumAmount =  amount;
                } else if(timeLoadWeekData < 3 && sumAmount > 20000){
                    status = false;
                }
                else {
                    status = true;
                }
                String[] dataArrayLimit;
                timeLoadWeekData = timeLoadWeekData + 1;
                dataArrayLimit = new String[]{String.valueOf(sumAmount),String.valueOf(dayData),String.valueOf(weekData),String.valueOf(timeLoadWeekData)};
                dataLimit.put(customerId, dataArrayLimit);
            }
            else{
                String[] dataArrayLimit;
                //Data for Array HashMap - Amount, Day, Week, TimeLoadWeek
                dataArrayLimit = new String[]{String.valueOf(amount),String.valueOf(partsDate[0]),String.valueOf(partsDate[1]),String.valueOf(1)};
                dataLimit.put(customerId, dataArrayLimit);
            }

            dataFinal = dataFinal.concat("{\"id\":"+String.valueOf(transactionId)+",\"customer_id\":\""+ String.valueOf(customerId)+"\",\"accepted\":" +String.valueOf(status)+"};");

        }
        String result = dataFinal;
        createFileOutput(result);
        return true;
    }

    public static Double convertAmount(String amountJson){
        Double finalAmount = 0.0;
        String strAmount = amountJson.replace("$", "");
        finalAmount = Double.parseDouble(strAmount);
        return finalAmount;
    }

    public static String getDayWeekYearValueFromDateTime(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = sdf.parse(dateTime);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);
        int week = c.get(Calendar.WEEK_OF_YEAR);
        int year = c.get(Calendar.YEAR);
        String finalDate = Integer.toString(day) + "," + Integer.toString(week) + "," + Integer.toString(year);
        return finalDate;
    }

    public static void createFileOutput(String data) throws FileNotFoundException, UnsupportedEncodingException {
        String parts[] = data.split(";");
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        for(String part: parts) {
            writer.println(part);
        }
        writer.close();
    }
}
