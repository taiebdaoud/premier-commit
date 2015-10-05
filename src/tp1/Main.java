package tp1;

import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jberger.jsonparsingexample.json.FielReader;

/**
 *
 * @author Taieb
 */
public class Main {

    public static void main(String[] args) throws IOException {
        
        double coutAdul, coutEnf, Tarif =0.0 , Total , PrixParEleve;
        
        String json = FielReader.loadFileIntoString("JSON/jsonFile.json", "UTF-8");
        JSONObject ecole = JSONObject.fromObject(json);
        
        int NombreEleve = ecole.getInt("nombre_eleves");
        int age = ecole.getInt("age");
        
        JSONArray array = ecole.getJSONArray("activites");
        
        
        for(int i = 0 ; i < array.size() ; i++){
            JSONObject obj = array.getJSONObject(i);
            
            // recuperation des informations du fichier Json
            String desc = obj.getString("description");
            
            String PrixEnf = obj.getString("prix_unitaire_enfant");
            String SE = PrixEnf.substring(0,PrixEnf.length()-1).trim();
            double PrixEnfInt = Double.parseDouble(SE);
            
            String PrixAdul = obj.getString("prix_unitaire_adulte");
            String SP = PrixAdul.substring(0,PrixAdul.length()-1).trim();
            double PrixAdulInt = Double.parseDouble(SP);
            
            int Par = obj.getInt("nombre_parents_accompagnateurs");  
            int Trans = obj.getInt("transport");
            int Dis = obj.getInt("distance");
            String date = obj.getString("date");
            
            // le montant du prix d'entrée pour l'ensemble des enfants
            coutEnf = NombreEleve * PrixEnfInt;
            
            // le montant du prix d'entrée pour l'ensemble des adultes
            coutAdul = (Par+1) * PrixAdulInt;
            
            // Calcul du Transport
            if(Trans == 2){
                int NombreAuto;
                int NombrePer;
                
                NombrePer = NombreEleve +Par + 1;
                if(NombrePer <= 30){ // definir le nombre des autobus
                    NombreAuto = 1;
                } else {
                    NombreAuto = (NombrePer/30) +1 ;
                }
                if(Dis < 40){ // definir le cout du transport par Km * nombre des Auto
                    Tarif = (4*Dis)*NombreAuto;
                } else if (Dis >= 40){ 
                    Tarif = (4.95*Dis)*NombreAuto;
                }
 
                if (age <= 5){ // < 5 ans 10% de rabais
                    Tarif = Tarif-(Tarif*0.1);
                }
            } else if(Trans == 1) {
                if(age <= 5){
                    Tarif = 0.0;
                } else if( age < 17) {
                    Tarif = 4.5 * NombreEleve;
                } else {
                    Tarif = 6.5 * NombreEleve;
                }
            } else{
                Tarif = 0.0;
            }
            Total = Tarif + coutEnf + coutAdul;
            PrixParEleve = (Total/NombreEleve) + 9;
            
            // Format the price
            DecimalFormat format = new DecimalFormat();
            format.setMinimumFractionDigits(2);
            String PrixFinal = format.format(PrixParEleve);
            
            System.out.println("description : " + desc);
            System.out.println("prix_par_eleve : " + PrixFinal + " $" + "\n"); 
            
        }   
    }       
}
