/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeli;

import domen.Tim;
import domen.Vozac;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import kontroler.KontrolerKI;

/**
 *
 * @author Ivan
 */
public class ModelTabeleTim extends AbstractTableModel{
ArrayList<Tim> lista;
ArrayList<Vozac> vozaci;
KontrolerKI kontrolerKI;

    public ModelTabeleTim(ArrayList<Tim> lista) {
        this.lista = lista;
        vozaci=new ArrayList<>();
        kontrolerKI=new KontrolerKI();
    }

    @Override
    public int getRowCount() {
        if(lista==null) return 0;
return lista.size();
    }

    @Override
    public int getColumnCount() {
return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
Tim t=lista.get(rowIndex);
switch(columnIndex){
    case 0: return t.getNaziv();
    case 1: return t.getDrzava();
    case 2: return vratiPoene(t)+"";
    default: return "n/a"; 
}
    }

    @Override
    public String getColumnName(int column) {
switch(column){
    case 0: return "Naziv";
    case 1: return "Država";
    case 2: return "Poeni";
    default: return "n/a"; 
}
    }

    public Tim getTim(int red) {
return lista.get(red);
    }

    private int vratiPoene(Tim t) {
vozaci=kontrolerKI.vratiVozace();
int poeni=0;
        for (Vozac vozac : vozaci) {
            if(vozac.getTim().getTimID()==t.getTimID())poeni+=vozac.getPoeni();
        }
        return poeni;
    }
    
}
