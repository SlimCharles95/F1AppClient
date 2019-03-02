/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeli;

import domen.Trka;
import domen.Ucesce;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import kontroler.KontrolerKI;

/**
 *
 * @author Ivan
 */
public class ModelTabeleTrke extends AbstractTableModel{
ArrayList<Trka> lista;
private KontrolerKI kontrolerKI;
    public ModelTabeleTrke(ArrayList<Trka> lista) {
        this.lista=lista;
        kontrolerKI=new KontrolerKI();
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
        Trka t=lista.get(rowIndex);
        SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy");
        switch(columnIndex){
            case 0:return t.getDrzava();
            case 1: return t.getNazivStaze();
            case 2: return sdf.format(t.getDatumOdrzavanja());
            case 3: return vratiPobednika(t);
            default: return "n/a";
        }
    }

    @Override
    public String getColumnName(int column) {
  switch(column){
            case 0:return "Država";
            case 1: return "Naziv staze";
            case 2: return "Datum održavanja";
            case 3: return "Pobednik";
            default: return "n/a";
        }
    }

    public ArrayList<Trka> getLista() {
        return lista;
    }
    public void obrisi(int red){
    lista.remove(red);
    fireTableDataChanged();
    }
public Trka getTrka(int red){
return lista.get(red);
}
    private String vratiPobednika(Trka t) {
      ArrayList<Ucesce> ucesca=kontrolerKI.vratiUcesca(t);
      if(!(ucesca==null)){
        for (Ucesce ucesce : ucesca) {
         if(ucesce.getPlasman().equals("1")) return ucesce.getVozac().getImePrezime();
        }
      }
        return "n/a";
    }
    
    
}
