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
public class ModelTabeleDodajUcesce extends AbstractTableModel{
      ArrayList<Vozac> lista;

    public ModelTabeleDodajUcesce(ArrayList<Vozac> lista) {
        this.lista = lista;
    }

    @Override
    public int getRowCount() {
if(lista==null) return 0;
        return lista.size();
    }

    @Override
    public int getColumnCount() {
return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
Vozac v=lista.get(rowIndex);
switch(columnIndex){
    
    case 0: return v.getImePrezime();
    case 1: return v.getTim().getNaziv();
    
    default: return "n/a"; 
}
    }

    @Override
    public String getColumnName(int column) {
switch(column){
    case 0: return "Vozač";
    case 1: return "Tim";
    
    default: return "n/a"; 
}
    }

    public Vozac getVozac(int red) {
return lista.get(red);
    }
    public void obrisi(int red){
    lista.remove(red);
    fireTableDataChanged();
    }
}
