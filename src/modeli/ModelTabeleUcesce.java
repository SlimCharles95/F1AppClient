/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeli;

import domen.Trka;
import domen.Ucesce;
import domen.Vozac;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import komunikacija.KomunikacijaSaServerom;

/**
 *
 * @author Ivan
 */
public class ModelTabeleUcesce extends AbstractTableModel{

    ArrayList<Ucesce> lista;
    private boolean promenljivo=false;
    private Trka t=null;

    public ModelTabeleUcesce() {
        lista=new ArrayList<>();
    }

    public ModelTabeleUcesce(ArrayList<Ucesce> lista) {
        this.lista = lista;
    }
    
    @Override
    public int getRowCount() {
return lista.size();
    }

    @Override
    public int getColumnCount() {
return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
Ucesce u=lista.get(rowIndex);
switch(columnIndex){
    case 0: return u.getVozac().getImePrezime();
    case 1: return u.getPlasman();
    case 2: return u.getUkupnoVreme();
    case 3: return u.getZaostatakZaPrvim();
    case 4: return u.getPocetnaPozicija()+"";
    case 5: return u.getBrojPitStopa()+"";
    default: return "n/a";
}
    }

    @Override
    public String getColumnName(int column) {
switch(column){
    case 0: return "Vozač";
    case 1: return "Plasman";
    case 2: return "Ukupno vreme";
    case 3: return "Zaostatak";
    case 4: return "Početna pozicija";
    case 5: return "Pit stopa";
    default: return "n/a";
}
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
if(isPromenljivo()){
if(columnIndex==0)return false;
return true;
}
    return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Ucesce u = lista.get(rowIndex);

        switch (columnIndex) {
            case 1:
                String plasmanStr = (String) aValue;
                u.setPlasman(plasmanStr);
                 try{
                     int plasman=Integer.parseInt(plasmanStr);
                     if(plasman>lista.size()||plasman<1){
                         
                             u.setPlasman("n/a");
                             JOptionPane.showMessageDialog(null, "Niste uneli odgovarajucu plasman (<0 ili veci od broja ucesnika)!");
                             return;
                     }
                     for (Ucesce ucesce : lista) {
                         int pomocna=0;
                         try{pomocna=Integer.parseInt(ucesce.getPlasman());
                         } catch ( NumberFormatException ex) {
                         }
                         
                             if(pomocna==plasman && (ucesce!=u)){
                                 u.setPlasman("n/a");
                                 JOptionPane.showMessageDialog(null, "Niste uneli odgovarajucu plasman (dva ista plasmana)!");
                                return;
                             }
                         }
                     u.setPlasman(plasman+"");
                     
                        } catch ( NumberFormatException ex) {
                 if(plasmanStr.toUpperCase().equals("DNF")){
                             u.setPlasman(plasmanStr.toUpperCase());
                 }else
                 { u.setPlasman("n/a");
                            JOptionPane.showMessageDialog(null, "Niste uneli odgovarajuci plasman!");
                 }  
                 }
                
                fireTableCellUpdated(rowIndex, 1);
                break;
            case 2:
                String ukupno = (String) aValue;
                u.setUkupnoVreme(ukupno);
                fireTableCellUpdated(rowIndex, 2);
                break;
            case 3:
                String zaostatak = (String) aValue;
                u.setZaostatakZaPrvim(zaostatak);
                fireTableCellUpdated(rowIndex, 3);
                break;
            case 4:
                String pocetna = (String) aValue;
                
                 try{
                     int pocPozicija=Integer.parseInt(pocetna);
                     if(pocPozicija>lista.size()||pocPozicija<1){
                                                     u.setPocetnaPozicija(0);
                                                     JOptionPane.showMessageDialog(null, "Niste uneli odgovarajucu pocetnu poziciju!");
                     return;
                     }
                     for (Ucesce ucesce : lista) {
                             if((ucesce.getPocetnaPozicija()==pocPozicija)){
                                 u.setPocetnaPozicija(0);
                                 JOptionPane.showMessageDialog(null, "Niste uneli odgovarajucu pocetnu poziciju (dve iste )!");
                                return;
                             }
                     }
                     u.setPocetnaPozicija(pocPozicija);
                     
                        } catch ( NumberFormatException ex) {
                            u.setPocetnaPozicija(0);
                            JOptionPane.showMessageDialog(null, "Niste uneli broj!");
                     }
                
                fireTableCellUpdated(rowIndex, 4);
                break;
            case 5:
                String pit = (String) aValue;
                try{
                int pitStop=Integer.parseInt(pit);
                if(pitStop<0){
                                                     u.setBrojPitStopa(0);
                                                     JOptionPane.showMessageDialog(null, "Niste uneli odgovarajuci broj pit stopa!");
                     }
                u.setBrojPitStopa(pitStop);
                }
                catch ( NumberFormatException ex) {
                    u.setBrojPitStopa(0);
                    JOptionPane.showMessageDialog(null, "Niste uneli broj!");
                     }
                fireTableCellUpdated(rowIndex, 5);
                break;
            
        }
    
    }
    

    public void dodaj(Vozac v) throws IOException {
Ucesce u=new Ucesce(null, v, "n/a", "n/a", "n/a", 0, 0, "",  KomunikacijaSaServerom.getInstanca().getUlogovani(),null);
lista.add(u);
fireTableDataChanged();
    }

    public void obrisi(int red) {
        lista.remove(red);
        fireTableDataChanged();
    }

    public ArrayList<Ucesce> getLista() {
return lista;
    }

    public boolean isPromenljivo() {
        return promenljivo;
    }

    public void setPromenljivo(boolean promenljivo) {
        this.promenljivo = promenljivo;
    }

    public void setT(Trka t) {
        this.t = t;
    }

    public Trka getT() {
        return t;
    }
    public boolean daLiJeSveUneto(){
    for (Ucesce ucesce : lista) {
                             if((ucesce.getPlasman().equals("n/a"))){
                                return false;
                             }
                     }
    return true;
    }

    public Ucesce getRed(int red) {
return lista.get(red);
    }
}
