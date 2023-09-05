package fr.rades.template.infrastructure.interfaces;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface TraiteurDate {

     final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

     public default Date stringEnDate(String dateString) throws ParseException {
         try {
             return simpleDateFormat.parse(dateString);
         }
         catch (ParseException parseException) {
             return null;
         }
     }
}