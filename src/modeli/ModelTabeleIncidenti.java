/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeli;

import domen.IzvestajIncidenta;
import domen.Vozac;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import kontroler.KontrolerKI;

/**
 *
 * @author Ivan
 */
public class ModelTabeleIncidenti extends AbstractTableModel{
ArrayList<IzvestajIncidenta> lista=new ArrayList<>();
ArrayList<Vozac> vozaci=new ArrayList<Vozac>();
KontrolerKI kontroler=new KontrolerKI();
    public ModelTabeleIncidenti(ArrayList<IzvestajIncidenta> lista) {
        this.lista=lista;
vozaci=kontroler.vratiVozace();
    }

    @Override
    public int getRowCount() {
return lista.size();
    }

    @Override
    public int getColumnCount() {
return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
IzvestajIncidenta ii=lista.get(rowIndex);
switch(columnIndex){
    case 0: return vratiVozaca(ii.getVozacID());
    case 1: return ii.getIncident();
    case 2: return ii.getKrug()+"";
    case 3: return ii.getKazna();
    default: return "n/a";
}
    }

    @Override
    public String getColumnName(int column) {
switch(column){
    case 0: return "Vozac";
    case 1: return "Incident";
    case 2: return "Krug";
    case 3: return "Kazna";
    default: return "n/a";
}

    }

    public ArrayList<IzvestajIncidenta> getLista() {
return lista;
    }

    private String  vratiVozaca(int vozacID) {
        for (Vozac vozac : vozaci) {
            if(vozac.getVozacID()==vozacID)return vozac.getImePrezime();
        }
        return "n/a";
    }
    
    
    
}
