package org.joget.marketplace;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.datalist.model.DataList;
import org.joget.apps.datalist.model.DataListColumn;
import org.joget.apps.datalist.model.DataListColumnFormatDefault;
import org.joget.apps.datalist.service.DataListService;
import org.joget.commons.util.LogUtil;

public class TimeAgoDatalistFormatter extends DataListColumnFormatDefault {

    private final static String MESSAGE_PATH = "messages/TimeAgoDatalistFormatter";

    @Override
    public String getName() {
        return "Time Ago Datalist Formatter";
    }

    @Override
    public String getVersion() {
        return "7.0.3";
    }

    @Override
    public String getDescription() {
        // support i18n
        return AppPluginUtil.getMessage("org.joget.marketplace.TimeAgoDatalistFormatter.pluginDesc", getClassName(), MESSAGE_PATH);
    }

    public String getYear() {
        // support i18n
        return " " + AppPluginUtil.getMessage("org.joget.marketplace.TimeAgoDatalistFormatter.year(s)", getClassName(), MESSAGE_PATH) + " ";
    }

    public String getMonth() {
        // support i18n
        return " " + AppPluginUtil.getMessage("org.joget.marketplace.TimeAgoDatalistFormatter.month(s)", getClassName(), MESSAGE_PATH) + " ";
    }

    public String getDay() {
        // support i18n
        return " " + AppPluginUtil.getMessage("org.joget.marketplace.TimeAgoDatalistFormatter.day(s)", getClassName(), MESSAGE_PATH) + " ";
    }
    
    public String getHour() {
        // support i18n
        return " " + AppPluginUtil.getMessage("org.joget.marketplace.TimeAgoDatalistFormatter.hour(s)", getClassName(), MESSAGE_PATH) + " ";
    }

    public String getMinute() {
        // support i18n
        return " " + AppPluginUtil.getMessage("org.joget.marketplace.TimeAgoDatalistFormatter.minute(s)", getClassName(), MESSAGE_PATH) + " ";
    }  
    
    public String getTryDefaultFormatErrorMsg() {
        return AppPluginUtil.getMessage("org.joget.marketplace.TimeAgoDatalistFormatter.tryDefaultFormatErrorMsg", getClassName(), MESSAGE_PATH);
    } 
    
    @Override
    public String getLabel() {
        // support i18n
        return AppPluginUtil.getMessage("org.joget.marketplace.TimeAgoDatalistFormatter.pluginLabel", getClassName(), MESSAGE_PATH);
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClassName(), "/properties/TimeAgoDatalistFormatter.json", null, true, MESSAGE_PATH);
    }

    // Check different Date Formats
    public String checkDateFormat(String date) {

        // Store different Date Formats
        String[] dateFormats = { "yyyy-MM-dd", "MMMMMMMMM dd, yyyy", "dd-MM-yyyy" };
        String additionalFormats = getPropertyString("dateFormat");

        if (!additionalFormats.equals("")) {
            ArrayList<String> timeFormatsList = new ArrayList<>(Arrays.asList(dateFormats));
            String[] additionalFormatsArray = additionalFormats.split("\\s*,\\s*");
            ArrayList<String> additionalFormatsList = new ArrayList<>(Arrays.asList(additionalFormatsArray));
            timeFormatsList.addAll(additionalFormatsList);
            dateFormats = timeFormatsList.toArray(new String[0]);
        }

        // Store formatted date
        String formattedDate = "";
                   
        // Loop through different Date Formats to find which matches the input date format
        for (String dateFormat : dateFormats) {

            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat); // Take in the current dateFormat to be checked
            sdf.setLenient(false); // Set a strict format checking
            SimpleDateFormat finalDateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format date to yyyy-MM-dd format

            try { // If can parse, the input format is the same as the current 
                
                // Break the loop once found matching dateFormat
                Date unformattedDate = sdf.parse(date);
                formattedDate = finalDateFormat.format(unformattedDate);
                break;

            } catch (ParseException e) {
                // Continue to check for other formats if
                // input format does not match current format
            }
        }
        return formattedDate;
    }
    
    // Check different Time Formats
    public String checkTimeFormat(String time) {

         // Store different Time Formats
        String[] timeFormats = { "hh:mm a", "hh:mma", "h:mm a", "h:mma" };
        String additionalFormats = getPropertyString("timeFormat");

        if(!additionalFormats.equals("")){
            ArrayList<String> timeFormatsList = new ArrayList<>(Arrays.asList(timeFormats));
            String[] additionalFormatsArray = additionalFormats.split("\\s*,\\s*");
            ArrayList<String> additionalFormatsList = new ArrayList<>(Arrays.asList(additionalFormatsArray));
            timeFormatsList.addAll(additionalFormatsList);
            timeFormats = timeFormatsList.toArray(new String[0]);
        }

        // Store formatted time
        String formattedTime = "";
        
        if (!"".equals(checkDateFormat(time))) {
            formattedTime = "";
            
        } else {

            // Loop through different Time Formats to find which matches the input time format
            for (String timeFormat : timeFormats) {

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(timeFormat); // Take in the current timeFormat to be checked

                try { // If can parse, the input format is the same as the current timeFormat

                    // Break the loop once found matching timeFormat
                    LocalTime unformattedTime = LocalTime.parse(time.toUpperCase(), dtf);
                    DateTimeFormatter finalTimeFormat = DateTimeFormatter.ofPattern("hh:mm a");
                    formattedTime = unformattedTime.format(finalTimeFormat);
                    break;

                } catch (DateTimeParseException e) {
                    // Continue to check for other formats if
                    // input format does not match current format
                }
            }
        } 
        return formattedTime;
    }

     // Check different DateTime Formats
     public String checkDateTimeFormat(String dateTime) {

        String[] dateTimeFormats = { "yyyy-MM-dd hh:mm a", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mma", "yyyy-MM-dd'T'HH:mm:ss.SSSSSS", "yyyy-MM-dd'T'HH:mm:ss",  "dd-MM-yyyy hh:mm a", "dd-MM-yyyy hh:mma", "dd-MM-yyyy HH:mma", "dd-MM-yyyy HH:mm"};
        String additionalFormats = getPropertyString("dateFormat");
  
        if (!additionalFormats.equals("")) {
            ArrayList<String> timeFormatsList = new ArrayList<>(Arrays.asList(dateTimeFormats));
            String[] additionalFormatsArray = additionalFormats.split("\\s*,\\s*");
            ArrayList<String> additionalFormatsList = new ArrayList<>(Arrays.asList(additionalFormatsArray));
            timeFormatsList.addAll(additionalFormatsList);
            dateTimeFormats = timeFormatsList.toArray(new String[0]);
        }

        LocalDateTime formattedDateTime = null;
        String formattedDate = "";
        String formattedTime = "";
        for (String format : dateTimeFormats) {
            try {
                DateTimeFormatter f = new DateTimeFormatterBuilder().parseCaseInsensitive()
                .append(DateTimeFormatter.ofPattern(format)).toFormatter();
                // Attempt to parse the input string using the current format
                formattedDateTime = LocalDateTime.parse(dateTime, f);

                break; // If parsing succeeds, exit the loop
            } catch (DateTimeParseException e) {
                // Continue to check for other formats if
                // input format does not match current format
            }
        }

        if (formattedDateTime != null) {
            // Parsing successful, separate date and time components
            String date = formattedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = formattedDateTime.format(DateTimeFormatter.ofPattern("hh:mma"));

            formattedDate = checkDateFormat(date);
            boolean isValidTime = ("validTimeInputs".equals(checkTimeValidity(time)));

            if (isValidTime) {
                formattedTime = checkTimeFormat(time); // Format date to hh:mm a format
            }
        }
            
        return formattedDate + "//" + formattedTime;
    }
    
    // Split the time and reform into "hh:mm" format
    public String splitTime(String time) {
        
        // Split time into hours and minutes
        String[] hour_minute = time.split(":");
        String hour = String.format("%02d", Integer.valueOf(hour_minute[0]));

        if (hour_minute[1].toUpperCase().contains("AM") || hour_minute[1].toUpperCase().contains("PM")) {
            
            if (hour_minute[1].contains(" ")) {
                String[] minute_indicator = hour_minute[1].split(" ");
                String minute = minute_indicator[0];
                time = hour + ":" + minute;
                
            } else {
                String[] minute_indicator = hour_minute[1].split("[aApP]");
                String minute = minute_indicator[0];
                time = hour + ":" + minute;
            }
            
        }
        return time;
    }
    
    // Check Time Validity
    public String checkTimeValidity(String formattedTime) {
       
        // Check validity
        if ("".equals(checkTimeFormat(formattedTime))) {
            return "";
            
        } else {
            String time = splitTime(formattedTime);
            
            // Conditions
            Boolean is12Hours = LocalTime.parse(time).getHour() >= 0 && LocalTime.parse(time).getHour() <= 12;
            Boolean is24Hours = LocalTime.parse(time).getHour() > 12 && LocalTime.parse(time).getHour() < 24;
            Boolean isWithin60Minutes = LocalTime.parse(time).getMinute() >= 0 && LocalTime.parse(time).getMinute() < 60;
            Boolean hasTimeIndicator = formattedTime.toUpperCase().contains("AM") || formattedTime.toUpperCase().contains("PM");
            
            if (is12Hours && hasTimeIndicator)  { // Valid
                if (isWithin60Minutes) { // Valid
                    return "validTimeInputs"; // Valid time
                }
            } else if (is24Hours && !hasTimeIndicator) { // Valid
                if (isWithin60Minutes) { // Valid
                    return "validTimeInputs"; // Valid time
                }
            }
            
            return time; // Return the original time if conditions aren't met
        }
    }
    
    // Check if input is a date or time
    public String checkDateOrTime(String input1, String input2) {
        
        String diff = "";
        
        // Conditions
        Boolean equalDateInputs = !"".equals(checkDateFormat(input1)) && !"".equals(checkDateFormat(input2));
        Boolean validTimeInputs = ("validTimeInputs".equals(checkTimeValidity(input1)) && "validTimeInputs".equals(checkTimeValidity(input2)));
        Boolean equalDateTimeInputs = !"//".equals(checkDateTimeFormat(input1)) && !"//".equals(checkDateTimeFormat(input2));

        if (equalDateInputs) {
            
            // If parsing succeeded in checkDateFormat(), the input is a date
            String formattedColumnDate = checkDateFormat(input1); // Format date to yyyy-MM-dd format
            String formattedTargetDate = checkDateFormat(input2); // Format date to yyyy-MM-dd format

            // Parse
            LocalDate date1 = LocalDate.parse(formattedColumnDate); // First input date
            LocalDate date2 = LocalDate.parse(formattedTargetDate); // Second input date

            // Find difference between date1 and date2
            diff = getDateDiff(date1, date2);

            // if is datetime, split the date and append time difference
            if (equalDateTimeInputs) {
    
                String formattedColumnDateTime = checkDateTimeFormat(input1); // get datetime
                String formattedTargetDateTime = checkDateTimeFormat(input2); // get datetime

                // Split the string into two parts using the delimiter "//"
                String[] separatedColumnDateTime = formattedColumnDateTime.split("//");
                String[] separatedTargetDateTime = formattedTargetDateTime.split("//");

                String separatedColumnTime = new String();
                String separatedTargetTime = new String();

                // Check if the split produced exactly two parts and only get time value
                if (separatedColumnDateTime.length == 2) {
                    separatedColumnTime = separatedColumnDateTime[1];
                }
                if (separatedTargetDateTime.length == 2) {
                    separatedTargetTime = separatedTargetDateTime[1];
                }

                // Parse
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
                LocalTime time1 = LocalTime.parse(separatedColumnTime, dtf); // First input time
                LocalTime time2 = LocalTime.parse(separatedTargetTime, dtf); // Second input time

                // Find difference between time1 and time2, if 0 day(s), replace instead of append
                if(diff.equals("0 day(s) ")){
                    diff = getTimeDiff(time1, time2);
                } else{
                    diff += getTimeDiff(time1, time2);
                }
            }
            
        } else if (validTimeInputs) {
            
            // If parsing succeeded in checkTimeFormat(), the input is a time
            String formattedColumnTime = checkTimeFormat(input1); // Format date to hh:mm a format
            String formattedTargetTime = checkTimeFormat(input2); // Format date to hh:mm a format

            // Parse
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
            LocalTime time1 = LocalTime.parse(formattedColumnTime, dtf); // First input time
            LocalTime time2 = LocalTime.parse(formattedTargetTime, dtf); // Second input time

            // Find difference between time1 and time2
            diff = getTimeDiff(time1, time2);
            
        } else if (!validTimeInputs) {
            diff = "invalidTimeInputs";
            
        }

         // Format final datetime output based on user configuration
        String dateOutputFormat = getPropertyString("dateOutputFormat");
        String inclDateOutputFormat = getPropertyString("inclDateOutputFormat");
        if (!dateOutputFormat.equals("") && !dateOutputFormat.isEmpty()){
            diff = formatDateTimeOutput(diff, dateOutputFormat, inclDateOutputFormat);
        }
        return diff;
    }
    
    // Calculate difference between date1 and date2
    public String getDateDiff(LocalDate date1, LocalDate date2) {
        
        Period dateDiff = Period.between(date1, date2);
        String diff;
        
        if (Math.abs(dateDiff.getYears()) > 0) {
            diff = Math.abs(dateDiff.getYears()) + getYear() + Math.abs(dateDiff.getMonths()) +
                    getMonth() + Math.abs(dateDiff.getDays()) + getDay();
        } else if (Math.abs(dateDiff.getMonths()) > 0) {
            diff = Math.abs(dateDiff.getMonths()) + getMonth() + Math.abs(dateDiff.getDays()) + getDay();
        } else {
            diff = Math.abs(dateDiff.getDays()) + getDay();
        }
        return diff;
    }
    
    // Calculate difference between time1 and time2
    public String getTimeDiff(LocalTime time1, LocalTime time2) {
        
        Duration timeDiff = Duration.between(time1, time2);
        String diff;

        if (Math.abs(timeDiff.toHours()) > 0) {
            diff = Math.abs(timeDiff.toHours()) + getHour() + Math.abs(timeDiff.toMinutes() % 60) + getMinute();
        } else {
            diff = Math.abs(timeDiff.toMinutes() % 60) + getMinute();
        }
        return diff;
    }

     // Format final datetime output based on user configuration
     public String formatDateTimeOutput(String time, String dateOutputFormat, String inclDateOutputFormat) {
        String finalOutput = "";
        // split multi select box value
        String[] parts = dateOutputFormat.split(";");
        
        String regex;
        for (String part : parts) {
            switch (part) {
                case "year":
                    if(inclDateOutputFormat.equals("true")) {
                        regex = "\\b(\\d+\\s+year\\(s\\))";
                    } else {
                        regex = "\\b(\\d+)\\s+year\\(s\\)";
                    }
                    break;
                case "month":
                    if(inclDateOutputFormat.equals("true")) {
                        regex = "\\b(\\d+\\s+month\\(s\\))";
                    } else {
                        regex = "\\b(\\d+)\\s+month\\(s\\)";
                    }
                    break;
                case "day":
                    if(inclDateOutputFormat.equals("true")) {
                        regex = "\\b(\\d+\\s+day\\(s\\))";
                    } else {
                        regex = "\\b(\\d+)\\s+day\\(s\\)";
                    }
                    break;
                case "hour":
                    if(inclDateOutputFormat.equals("true")) {
                        regex = "\\b(\\d+\\s+hour\\(s\\))";
                    } else {
                        regex = "\\b(\\d+)\\s+hour\\(s\\)";
                    }
                    break;
                case "minute":
                    if(inclDateOutputFormat.equals("true")) {
                        regex = "\\b(\\d+\\s+minute\\(s\\))";
                    } else {
                        regex = "\\b(\\d+)\\s+minute\\(s\\)";
                    }
                    break;
                default:
                    return time; // If desired unit is not recognized, return input as is
            }

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(time);
            
            if (matcher.find()) {
                finalOutput += matcher.group(1) + " ";
            } else {
                finalOutput += ""; // If no match found, return an empty string
            }
        }
        return finalOutput;
    }
    
    @Override
    public String format(DataList dataList, DataListColumn column, Object row, Object value) {
        String result = (String) value;
        String duration = getPropertyString("duration");

        switch (duration) {
            case "today":
                String columnStr = result;
                String todayStr = LocalDateTime.now().toString();

                if (!"invalidTimeInputs".equals(checkDateOrTime(columnStr, todayStr))) {
                    return checkDateOrTime(columnStr, todayStr);
                } else {
                    LogUtil.info(getClass().getName(), getTryDefaultFormatErrorMsg() + columnStr + ", " + todayStr);
                    return columnStr;
                }

            case "anotherDate":
                columnStr = result;
                String targetStr = getPropertyString("targetDate");
                targetStr = (String) DataListService.evaluateColumnValueFromRow(row, targetStr);
                
                // Outputs
                if (!"invalidTimeInputs".equals(checkDateOrTime(columnStr, targetStr))) {
                    return checkDateOrTime(columnStr, targetStr);
                } else {
                    LogUtil.info(getClass().getName(), getTryDefaultFormatErrorMsg() + columnStr + ", " + targetStr);
                    return columnStr;
                }

            case "twoDates":
                // Get input From Date
                String fromStr = getPropertyString("fromDate");
                fromStr = (String) DataListService.evaluateColumnValueFromRow(row, fromStr);
                
                // Get input To Date
                String toStr = getPropertyString("toDate");
                toStr = (String) DataListService.evaluateColumnValueFromRow(row, toStr);
                
                // Outputs
                if (!"invalidTimeInputs".equals(checkDateOrTime(fromStr, toStr))) {
                    return checkDateOrTime(fromStr, toStr);
                } else {
                    LogUtil.info(getClass().getName(), getTryDefaultFormatErrorMsg() + fromStr + ", " + toStr);
                    return result;
                }
        }
        return result;
    }
}