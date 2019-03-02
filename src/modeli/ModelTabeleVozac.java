/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeli;

import domen.Vozac;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ivan
 */
public class ModelTabeleVozac extends AbstractTableModel{
    ArrayList<Vozac> lista;

    public ModelTabeleVozac(ArrayList<Vozac> lista) {
        this.lista = lista;
    }

    @Override
    public int getRowCount() {
if(lista==null) return 0;
        return lista.size();
    }

    @Override
    public int getColumnCount() {
return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
Vozac v=lista.get(rowIndex);
switch(columnIndex){
    case 0: return rowIndex+1;
    case 1: return v.getImePrezime();
    case 2: return v.getTim().getNaziv();
    case 3: return v.getPoeni();
    default: return "n/a"; 
}
    }

    @Override
    public String getColumnName(int column) {
switch(column){
    case 0: return "Rang";
    case 1: return "Vozač";
    case 2: return "Tim";
    case 3: return "Poeni";
    default: return "n/a"; 
}
    }

    public Vozac getVozac(int red) {
return lista.get(red);
    }
    
}
