/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontroler;

import domen.IzvestajIncidenta;
import domen.Nalog;
import domen.Tim;
import domen.Trka;
import domen.Ucesce;
import domen.Vozac;
import forme.IzvestajIncidentaForma;
import forme.LoginForma;
import forme.NalogDodajForma;
import forme.TimDodajForma;
import forme.TimoviForma;
import forme.TrkaPrikazForma;
import forme.TrkeForma;
import forme.UcesceForma;
import forme.VozacDodajForma;
import forme.VozaciForma;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.RowFilter;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import komunikacija.KomunikacijaSaServerom;
import modeli.ModelTabeleDodajUcesce;
import modeli.ModelTabeleIncidenti;
import modeli.ModelTabeleTim;
import modeli.ModelTabeleTrke;
import modeli.ModelTabeleUcesce;
import modeli.ModelTabeleVozac;
import transfer.Operacije;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;

/**
 *
 * @author Ivan
 */
public class KontrolerKI {
     private int brojac=3;
      public int getBrojac() {
        return brojac;
    }

    public void setBrojac(int brojac) {
        this.brojac = brojac;
    }

    public void prijaviKorisnika(LoginForma rootPane,JPasswordField txtPassword,JTextField txtUsername,JButton btnPrijaviSe){
     try{
         String username = txtUsername.getText();
           String password = txtPassword.getText();
           Nalog nalog=new Nalog( username, password);
           TransferObjekatZahtev toz=new TransferObjekatZahtev();
           toz.setOperacija(Operacije.LOGIN);
           toz.setParametar(nalog);
           KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
           TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
           Nalog ulogovani=(Nalog) too.getRezultat();
           if(ulogovani!=null){
               JOptionPane.showMessageDialog(rootPane, "Uspesno ste prijavljeni!");
               TrkeForma tf=new TrkeForma();
               KomunikacijaSaServerom.getInstanca().setUlogovani(ulogovani);
               tf.setVisible(true);
               rootPane.setVisible(false);
           }else{
               if(getBrojac()>1)JOptionPane.showMessageDialog(rootPane, "Pogresno uneti podaci! Imate jos "+(getBrojac()-1)+" pokusaja!");
               else JOptionPane.showMessageDialog(rootPane, "Pogresno uneti podaci! Nemate vise pokusaja ");
               setBrojac((getBrojac()-1));
               if(getBrojac()==0) btnPrijaviSe.setEnabled(false);
               return;
           }
       } catch (IOException ex) {
           JOptionPane.showMessageDialog(rootPane, "Server nije pokrenut");
           Logger.getLogger(LoginForma.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    public void zapamtiNalog(NalogDodajForma rootPane,JPasswordField txtPassword,JPasswordField txtPasswordPonovo,JTextField txtUsername){
         try {
             String username = txtUsername.getText();
             String password = txtPassword.getText();
             String passPonovo= txtPasswordPonovo.getText();
              if(username.isEmpty()||password.isEmpty()|| passPonovo.isEmpty()){
     JOptionPane.showMessageDialog(rootPane, "Niste uneli sve podatke!");
        return;
    }
             if(!(password.equals(passPonovo))){
                 JOptionPane.showMessageDialog(rootPane, "Ne poklapaju se šifre!");
                 return;
             }
             Nalog nalog=new Nalog( username, password);
             TransferObjekatZahtev toz=new TransferObjekatZahtev();
             toz.setOperacija(Operacije.ZAPAMTI_NALOG);
             toz.setParametar(nalog);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz); 
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
             boolean uspesno=(boolean) too.getRezultat();
             if(uspesno){
             JOptionPane.showMessageDialog(rootPane, "Sistem je zapamtio nalog!");
           rootPane.dispose();
             return;
             }else{
                 JOptionPane.showMessageDialog(rootPane, "Sistem ne može da zapamti nalog!");
                 return;
             }
         } catch (IOException ex) {
             JOptionPane.showMessageDialog(rootPane, "Server nije pokrenut");
             Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
   
    public void zapamtiTim(TimDodajForma aThis, JTextField txtNaziv, JTextField txtDrzava, JTextField txtSefTima, JTextField txtSasija, JTextField txtMotor, JTextField txtPrvoUcesce) {
try{
    
    if(txtNaziv.getText().isEmpty()||txtDrzava.getText().isEmpty()||txtMotor.getText().isEmpty()||txtSasija.getText().isEmpty()||txtSefTima.getText().isEmpty()|| txtPrvoUcesce.getText().isEmpty()){
     JOptionPane.showMessageDialog(aThis, "Niste uneli sve podatke!");
        return;
    }
    String naziv=txtNaziv.getText();
    String drzava=txtDrzava.getText();
    String sefTima=txtSefTima.getText();
    String sasija=txtSasija.getText();
    String motor=txtMotor.getText();
    String prvoUcesce=txtPrvoUcesce.getText();
    int ucesce=0;
    try{
        ucesce=Integer.parseInt(prvoUcesce);
    } catch ( NumberFormatException ex) {
        JOptionPane.showMessageDialog(aThis, "Niste uneli godinu!");
        return;
    }
    if(ucesce>2018 || ucesce<1949){
        JOptionPane.showMessageDialog(aThis, "Netacna godina (<1949 ili >2018)!");
        return;
    }
    Tim tim=new Tim(-1, naziv, drzava, sefTima, sasija, motor, ucesce);
    TransferObjekatZahtev toz=new TransferObjekatZahtev();
    toz.setOperacija(Operacije.ZAPAMTI_TIM);
    toz.setParametar(tim);
    KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
    TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
    boolean uspesno=(boolean) too.getRezultat();
    if(uspesno){
        JOptionPane.showMessageDialog(aThis, "Sistem je zapamtio tim!");
        
        aThis.dispose();
        return;
    }else{
        JOptionPane.showMessageDialog(aThis, "Sistem ne može da zapamti tim!");
        return;
    }
} catch ( IOException ex) {
     JOptionPane.showMessageDialog(aThis, "Server nije pokrenut");
    Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null,ex);
         }
    }

   

    public void srediTabeluTim(TimoviForma aThis,JTable tabelaTimovi) {
    try {
             TransferObjekatZahtev toz=new TransferObjekatZahtev();
             toz.setOperacija(Operacije.VRATI_TIMOVE);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
             ArrayList<Tim> lista=(ArrayList<Tim>) too.getRezultat();
             ModelTabeleTim mtt=new ModelTabeleTim(lista);
             tabelaTimovi.setModel(mtt);
              TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(tabelaTimovi.getModel());
        tabelaTimovi.setRowSorter(rowSorter);
         } catch (IOException ex) {
             JOptionPane.showMessageDialog(aThis, "Server nije pokrenut");
             Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    public void prikaziTim(int red, JTextField txtDrzava, JTextField txtNaziv, JTextField txtMotor, JTextField txtSasija, JTextField txtPrvoUcesce, JTextField txtSefTima, JButton btnDodajTim,JTable tabelaTim) {
ModelTabeleTim mtt=(ModelTabeleTim) tabelaTim.getModel();
Tim t=mtt.getTim(red);
txtDrzava.setText(t.getDrzava());
txtDrzava.setEditable(false);
txtNaziv.setText(t.getNaziv());
txtNaziv.setEditable(false);
txtSefTima.setText(t.getSefTima());
txtSefTima.setEditable(false);
txtSasija.setText(t.getSasija());
txtSasija.setEditable(false);
txtMotor.setText(t.getMotor());
txtMotor.setEditable(false);
txtPrvoUcesce.setText(t.getPrvoUcesce()+"");
txtPrvoUcesce.setEditable(false);
btnDodajTim.setVisible(false);
    }

    public void srediTabeluVozaci(VozaciForma aThis, JTable tabelaVozaci) {
        
             ArrayList<Vozac> lista=vratiVozace(); 
             ModelTabeleVozac mtv=new ModelTabeleVozac(lista);
             tabelaVozaci.setModel(mtv);
         
    }

    public void srediKomboTim(VozacDodajForma aThis, JComboBox cmbTim) {
         try {
             TransferObjekatZahtev toz=new TransferObjekatZahtev();
             toz.setOperacija(Operacije.VRATI_TIMOVE);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
             ArrayList<Tim> lista=(ArrayList<Tim>) too.getRezultat();
             cmbTim.removeAllItems();
             for (Tim tim : lista) {
              cmbTim.addItem(tim);
             }
         } catch (IOException ex) {
             JOptionPane.showMessageDialog(aThis, "Server nije pokrenut");
             Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    public void zapamtiVozaca(VozacDodajForma aThis, JTextField txtImePrezime, JTextField txtDržava, JTextField txtDatumRodjenja, JComboBox cmbTim) {
        
         try {
             
             if(txtImePrezime.getText().isEmpty()||txtDržava.getText().isEmpty()||txtDatumRodjenja.getText().isEmpty()){
                 JOptionPane.showMessageDialog(aThis, "Niste uneli sve podatke!");
                 return;
             }
             SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy");
             String imePrezime=txtImePrezime.getText();
             Tim tim=(Tim) cmbTim.getSelectedItem();
             String drzava=txtDržava.getText();
             String datum=txtDatumRodjenja.getText();
             Date datumRodjenja=null;
             try {
                datumRodjenja= sdf.parse(datum);
             } catch (ParseException ex) {
                 JOptionPane.showMessageDialog(aThis, "Niste uneli godinu u pravom formatu!");
                 return;
             }
             Vozac vozac=new Vozac(-1, imePrezime, drzava, datumRodjenja, 0, 0, 0, 0, 0, tim);
             TransferObjekatZahtev toz=new TransferObjekatZahtev();
             toz.setOperacija(Operacije.ZAPAMTI_VOZACA);
             toz.setParametar(vozac);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
             boolean uspesno=(boolean) too.getRezultat();
             if(uspesno){
                 JOptionPane.showMessageDialog(aThis, "Sistem je zapamtio vozača!");
                 
                 aThis.dispose();
                 return;
             }else{
                 JOptionPane.showMessageDialog(aThis, "Sistem ne može da zapamti vozača!");
                 return;
             }
             
         } catch (IOException ex) {
             JOptionPane.showMessageDialog(aThis, "Server nije pokrenut");
             Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null,ex);
         }
         
    }
   
    

    public void prikaziVozaca(int red, VozacDodajForma aThis, JTextField txtImePrezime, JTextField txtDržava, JTextField txtDatumRodjenja, JComboBox cmbTim, JTextField txtBrPobeda, JTextField txtBrPodijumi, JTextField txtBrPoena, JTextField txtBrojPolPozicija, JTable tabelaVozaci, JPanel panelStatistika,JButton btnDodajVozaca) {
SimpleDateFormat sdf2=new SimpleDateFormat("dd.MM.yyyy");
panelStatistika.setVisible(true);
aThis.setTitle("Formula 1 - Prikaz vozača");
btnDodajVozaca.setVisible(false);
ModelTabeleVozac mtv=(ModelTabeleVozac) tabelaVozaci.getModel();
Vozac v=mtv.getVozac(red);
txtDržava.setText(v.getDrzava());
txtDržava.setEditable(false);
txtImePrezime.setText(v.getImePrezime());
txtImePrezime.setEditable(false);
txtDatumRodjenja.setText(sdf2.format(v.getDatumRodjenja()));
txtDatumRodjenja.setEditable(false);
txtBrPobeda.setText(v.getPobede()+"");
txtBrPobeda.setEditable(false);
txtBrPodijumi.setText(v.getPodijumi()+"");
txtBrPodijumi.setEditable(false);
txtBrPoena.setText(v.getPoeni()+"");
txtBrPoena.setEditable(false);
txtBrojPolPozicija.setText(v.getPolPozicije()+"");
txtBrojPolPozicija.setEditable(false);
cmbTim.removeAllItems();
cmbTim.addItem(v.getTim());
cmbTim.setEditable(false);
aThis.pack();

    }

    public void srediTabeluTrke(TrkeForma aThis, JTable tabelaTrke) {
         try {
             TransferObjekatZahtev toz=new TransferObjekatZahtev();
             toz.setOperacija(Operacije.VRATI_TRKE);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
             ArrayList<Trka> lista=(ArrayList<Trka>) too.getRezultat();
             ModelTabeleTrke mtt=new ModelTabeleTrke(lista);
             tabelaTrke.setModel(mtt);
             
         } catch (IOException ex) {
             JOptionPane.showMessageDialog(aThis, "Server nije pokrenut");
             Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    public void pripremiTabeluTrkeZaSort(TrkeForma aThis, JTable tabelaTrke, JTextField txtFilter) {
  TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(tabelaTrke.getModel());
        tabelaTrke.setRowSorter(rowSorter);

        txtFilter.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                String filter = txtFilter.getText();

                if (filter.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + filter));
                }
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                String text = txtFilter.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    public ArrayList<Ucesce> vratiUcesca(Trka t) {
        ArrayList<Ucesce> lista=null; 
        try {
             TransferObjekatZahtev toz=new TransferObjekatZahtev();
             toz.setOperacija(Operacije.VRATI_UCESCA);
             toz.setParametar(t);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
              lista=(ArrayList<Ucesce>) too.getRezultat();
                     } catch (IOException ex) {
                         Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null, ex);
         }
         return lista;
    }

    public void obrisiTrku(TrkeForma aThis, int red, JTable tabelaTrke) {
         try {
             ModelTabeleTrke mtt=(ModelTabeleTrke) tabelaTrke.getModel();
             Trka t=mtt.getTrka(red);
             TransferObjekatZahtev toz=new TransferObjekatZahtev();
             ArrayList<Ucesce> ucesca=vratiUcesca(t);
             ArrayList<Ucesce> ucescaObrisana=srediObrisanaUcesca(ucesca);
             toz.setOperacija(Operacije.OBRISI_TRKU);
             toz.setParametar(t);
             toz.setParametar2(ucescaObrisana);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
             boolean uspesno=(boolean) too.getRezultat();
             if(uspesno){
                 mtt.obrisi(red);
                 JOptionPane.showMessageDialog(aThis, "Sistem je obrisao trku!");
                 return;
             }else{
                 JOptionPane.showMessageDialog(aThis, "Sistem ne može da obriše vozača!");
                 return;
             }
         } catch (IOException ex) {
             JOptionPane.showMessageDialog(aThis, "Server nije pokrenut");
             Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null, ex);
         }
             
    }

    public ArrayList<Vozac> vratiVozace() {
        ArrayList<Vozac> lista=null;
        try {
             TransferObjekatZahtev toz=new TransferObjekatZahtev();
             toz.setOperacija(Operacije.VRATI_VOZACE);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
             lista=(ArrayList<Vozac>) too.getRezultat();
         } catch (IOException ex) {
              JOptionPane.showMessageDialog(null, "Server nije pokrenut");
             Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null, ex);
         }
        return lista;
    }

   

    public void srediTabeluUcesce(JTable tabelaTrke,Trka trka) {
try {
             TransferObjekatZahtev toz=new TransferObjekatZahtev();
             toz.setParametar(trka);
             toz.setOperacija(Operacije.VRATI_UCESCA);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
             ArrayList<Ucesce> lista=(ArrayList<Ucesce>) too.getRezultat();
             ModelTabeleUcesce mtdu=new ModelTabeleUcesce(lista);
             tabelaTrke.setModel(mtdu);
         } catch (IOException ex) {
             JOptionPane.showMessageDialog(null, "Server nije pokrenut");
             Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    public void srediTabeluDodajUcesce(UcesceForma aThis, JTable tabelaVozaci, ArrayList<Ucesce> listaUcesca) {
 try {
             TransferObjekatZahtev toz=new TransferObjekatZahtev();
             toz.setOperacija(Operacije.VRATI_VOZACE);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
             ArrayList<Vozac> lista=(ArrayList<Vozac>) too.getRezultat();
             ArrayList<Vozac> listaPomocna=new ArrayList<>();
             for (Vozac vozac : lista) {
                 boolean dodati=true;
                 for (Ucesce ucesce : listaUcesca) {
                     if(ucesce.getVozac().getVozacID()==vozac.getVozacID())dodati=false;
                 }
                 if(dodati)listaPomocna.add(vozac);
     }
             
         
     
             ModelTabeleDodajUcesce mtdu=new ModelTabeleDodajUcesce(listaPomocna);
             tabelaVozaci.setModel(mtdu);
                       if(listaPomocna.size()==0)   JOptionPane.showMessageDialog(aThis, "Uneta su sva učešća");

         } catch (IOException ex) {
             JOptionPane.showMessageDialog(aThis, "Server nije pokrenut");
             Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    public void srediFormuDodajTrku(JButton btnPrikaziIzveštaje, JButton btnKreirajIzveštaj) {
btnKreirajIzveštaj.setVisible(false);
btnPrikaziIzveštaje.setVisible(false);
    }

    public void srediFormuPrikziTrku(int red, JTable tabelaTrke,JTable tabelaUcesce, JTextField txtBrKruga, JTextField txtDatum, JTextField txtDrzava, JTextField txtDuzinaKruga, JTextField txtKapacitet, JTextField txtNaziv, JTextField txtStaza, JButton btnSacuvajTrku, JButton btnObrisiUcesce, JButton btnDodajUcesca, JButton btnKreirajIzveštaj) {
ModelTabeleTrke mtt=(ModelTabeleTrke) tabelaTrke.getModel();
Trka t=mtt.getTrka(red);
btnDodajUcesca.setVisible(false);
btnObrisiUcesce.setVisible(false);
btnKreirajIzveštaj.setVisible(false);
btnSacuvajTrku.setVisible(false);
SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy");
txtBrKruga.setText(t.getBrojKrugova()+"");
txtBrKruga.setEditable(false);
txtDatum.setText(sdf.format(t.getDatumOdrzavanja()));
txtDatum.setEditable(false);
txtDrzava.setText(t.getDrzava());
txtDrzava.setEditable(false);
txtDuzinaKruga.setText(t.getDuzinaKruga()+"");
txtDuzinaKruga.setEditable(false);
txtKapacitet.setText(t.getKapacitet()+"");
txtKapacitet.setEditable(false);
txtNaziv.setText(t.getNazivTrke());
txtNaziv.setEditable(false);
txtStaza.setText(t.getNazivStaze());
txtStaza.setEditable(false);
srediTabeluUcesce(tabelaUcesce, t);
    }

    public void srediFormuIzmeniTrku(TrkaPrikazForma forma,int red, JTable tabelaTrke,JTable tabelaUcesce, JTextField txtBrKruga, JTextField txtDatum, JTextField txtDrzava, JTextField txtDuzinaKruga, JTextField txtKapacitet, JTextField txtNaziv, JTextField txtStaza, JButton btnSacuvajTrku, JButton btnObrisiUcesce, JButton btnDodajUcesca, JButton btnPrikaziIzveštaje) throws Exception {
ModelTabeleTrke mtt=(ModelTabeleTrke) tabelaTrke.getModel();
Trka t=mtt.getTrka(red);
btnDodajUcesca.setVisible(false);
btnObrisiUcesce.setVisible(false);
btnPrikaziIzveštaje.setVisible(false);
btnSacuvajTrku.setText("Sačuvaj izmene");
SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy");

txtBrKruga.setText(t.getBrojKrugova()+"");
txtBrKruga.setEditable(false);
txtDatum.setText(sdf.format(t.getDatumOdrzavanja()));
txtDatum.setEditable(false);
txtDrzava.setText(t.getDrzava());
txtDrzava.setEditable(false);
txtDuzinaKruga.setText(t.getDuzinaKruga()+"");
txtDuzinaKruga.setEditable(false);
txtKapacitet.setText(t.getKapacitet()+"");
txtKapacitet.setEditable(false);
txtNaziv.setText(t.getNazivTrke());
txtNaziv.setEditable(false);
txtStaza.setText(t.getNazivStaze());
txtStaza.setEditable(false);
srediTabeluUcesce(tabelaUcesce, t);
ModelTabeleUcesce mtu=(ModelTabeleUcesce) tabelaUcesce.getModel();
mtu.setPromenljivo(true);
mtu.setT(t);
boolean uneto=mtu.daLiJeSveUneto();
if(uneto){
 
throw new Exception();   

}
    }

    public void zapamtiTrku(TrkaPrikazForma aThis, JTable tabelaUcesca, JTextField txtBrKruga, JTextField txtDatum, JTextField txtDrzava, JTextField txtDuzinaKruga, JTextField txtKapacitet, JTextField txtNaziv, JTextField txtStaza, JButton btnSacuvajTrku) {
         try {
             String brKruga=txtBrKruga.getText();
             String datum=txtDatum.getText();
             String drzava=txtDrzava.getText();
             String duzinaKruga=txtDuzinaKruga.getText();
             String kapacitet=txtKapacitet.getText();
             String nazivTrke=txtNaziv.getText();
             String nazivStaze=txtStaza.getText();
             if(brKruga.isEmpty()||datum.isEmpty()||drzava.isEmpty()||duzinaKruga.isEmpty()||kapacitet.isEmpty()||nazivStaze.isEmpty()||nazivTrke.isEmpty()){
                 JOptionPane.showMessageDialog(aThis, "Niste uneli sve vrednosti!");
                 return;
             }
             SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy");
             Date dat=null;
             int brKrugova=0;
             int kapacitetTribina=0;
             double duzinaKrug=0;
             try {
                 dat=sdf.parse(datum);
             } catch (ParseException ex) {
                 JOptionPane.showMessageDialog(aThis, "Niste uneli datum u pravom formatu!");
                 return;
             }
             Date danas=new Date();
             if(dat.before(danas)){
             JOptionPane.showMessageDialog(aThis, "Datum mora biti posle današnjeg!");
                 return;
             }
             try{
                 brKrugova=Integer.parseInt(brKruga);
                 if(brKrugova<0) {JOptionPane.showMessageDialog(aThis, "Broj krugova ne sme biti negativan");
        return;}
             } catch ( NumberFormatException ex) {
                 JOptionPane.showMessageDialog(aThis, "Niste uneli broj krugova u pravom formatu!");
                 return;
             }
             try{
                 kapacitetTribina=Integer.parseInt(kapacitet);
                  if(kapacitetTribina<0) {JOptionPane.showMessageDialog(aThis, "Kapacitet ne sme biti negativan");
        return;}
             } catch ( NumberFormatException ex) {
                 JOptionPane.showMessageDialog(aThis, "Niste uneli kapacitet u pravom formatu!");
                 return;
             }
             try{
                 duzinaKrug=Double.parseDouble(duzinaKruga);
                  if(duzinaKrug<0) {JOptionPane.showMessageDialog(aThis, "Duzina krugaa ne sme biti negativana");
        return;}
             } catch ( NumberFormatException ex) {
                 JOptionPane.showMessageDialog(aThis, "Niste uneli duzinu kruga u pravom formatu!");
                 return;
             }
             ModelTabeleUcesce mtu=(ModelTabeleUcesce) tabelaUcesca.getModel();
             ArrayList<Ucesce> ucesca=mtu.getLista();
             if(ucesca.isEmpty()){
                 JOptionPane.showMessageDialog(aThis, "Niste uneli učešće!Unesite bar jedno!");
                 return;
             }
             
             Trka t=new Trka(-1, nazivTrke, drzava, nazivStaze, dat, brKrugova, duzinaKrug, kapacitetTribina);
             
             
             if(btnSacuvajTrku.getText().equals("Sačuvaj trku")){
             TransferObjekatZahtev toz=new TransferObjekatZahtev();
             toz.setOperacija(Operacije.ZAPAMTI_TRKU);
             toz.setParametar(t);
             toz.setParametar2(ucesca);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
             boolean uspesno=(boolean) too.getRezultat();
             if(uspesno){
                 JOptionPane.showMessageDialog(aThis, "Sistem je zapamtio trku!");
                 TrkeForma tf=new TrkeForma();
                 tf.setVisible(true);
                 aThis.dispose();
                 return;
             }else{
                 JOptionPane.showMessageDialog(aThis, "Sistem ne može da zapamti trku!");
                 return;
             }
             }else{
                boolean uneto= mtu.daLiJeSveUneto();
                if(!uneto){
                 JOptionPane.showMessageDialog(aThis, "Niste uneli sve rezultate");
                 return;
                }
                 ArrayList<Ucesce> unetaUcesca=srediUnetaUcesca(ucesca);
             TransferObjekatZahtev toz=new TransferObjekatZahtev();
             toz.setOperacija(Operacije.IZMENI_TRKU);
             toz.setParametar(t);
             toz.setParametar2(unetaUcesca);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
             boolean uspesno=(boolean) too.getRezultat();
             if(uspesno){
                 JOptionPane.showMessageDialog(aThis, "Sistem je izmenio trku!");
                 TrkeForma tf=new TrkeForma();
                 tf.setVisible(true);
                 aThis.dispose();
                 return;
             }else{
                 JOptionPane.showMessageDialog(aThis, "Sistem ne može da izmeni trku!");
                 return;
             }
             }
         } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Server nije pokrenut");
             Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null,ex);
         }
    }

    private ArrayList<Ucesce> srediUnetaUcesca(ArrayList<Ucesce> ucesca) {
        ArrayList<Ucesce> sredjena=new ArrayList<>();
        for (Ucesce ucesce : ucesca) {
            int poeni=0;
            int pobede=0;
            int podijumi=0;
            int polPozicije=0;
            int plasman=0;
           
             try{
                 plasman=Integer.parseInt(ucesce.getPlasman());
             } catch ( NumberFormatException ex) {
                 
             }
             if(plasman==1){
             pobede=1;
             podijumi=1;
             poeni=10;
             }
             if(plasman==2){
             podijumi=1;
             poeni=7;
             }
             if(plasman==3){
             podijumi=1;
             poeni=5;
             }
             if(plasman==4){
             poeni=3;
             
             }
             if(plasman==5){
             poeni=1;
             }
            if(ucesce.getPocetnaPozicija()==1)polPozicije=1;
            Vozac v=ucesce.getVozac();
            v.setPoeni((poeni+ucesce.getVozac().getPoeni()));
            v.setPobede((pobede+ucesce.getVozac().getPobede()));
            v.setPodijumi((podijumi+ucesce.getVozac().getPodijumi()));
            v.setPolPozicije((polPozicije+ucesce.getVozac().getPolPozicije()));
           ucesce.setVozac(v);
            sredjena.add(ucesce);
        }
        return sredjena;
    }

    private ArrayList<Ucesce> srediObrisanaUcesca(ArrayList<Ucesce> ucesca) {
        ArrayList<Ucesce> sredjena=new ArrayList<>();
        for (Ucesce ucesce : ucesca) {
            int poeni=0;
            int pobede=0;
            int podijumi=0;
            int polPozicije=0;
            int plasman=0;
           
             try{
                 plasman=Integer.parseInt(ucesce.getPlasman());
             } catch ( NumberFormatException ex) {
                 
             }
             if(plasman==1){
             pobede=-1;
             podijumi=-1;
             poeni=-10;
             }
             if(plasman==2){
             podijumi=-1;
             poeni=-7;
             }
             if(plasman==3){
             podijumi=-1;
             poeni=-5;
             }
             if(plasman==4){
             poeni=-3;
             
             }
             if(plasman==5){
             poeni=-1;
             }
            if(ucesce.getPocetnaPozicija()==1)polPozicije=1;
            ucesce.getVozac().setPoeni((poeni+ucesce.getVozac().getPoeni()));
            ucesce.getVozac().setPobede((pobede+ucesce.getVozac().getPobede()));
            ucesce.getVozac().setPodijumi((podijumi+ucesce.getVozac().getPodijumi()));
            ucesce.getVozac().setPolPozicije((polPozicije+ucesce.getVozac().getPolPozicije()));
           // ucesce.setInt(ucesce.getVozac().getVozacID());
            sredjena.add(ucesce);
        }
        return sredjena;
    }

    public void srediTabeluIncidenti(JTable tabelaIncidenti,Trka t) {
         try {
             TransferObjekatZahtev toz=new TransferObjekatZahtev();
             toz.setParametar(t);
             toz.setOperacija(Operacije.VRATI_IZVESTAJE);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
             ArrayList<IzvestajIncidenta> lista=(ArrayList<IzvestajIncidenta>) too.getRezultat();
             ModelTabeleIncidenti mti=new ModelTabeleIncidenti(lista);
             tabelaIncidenti.setModel(mti);
         } catch (IOException ex) {
             Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null, ex);
         }
             
    }

    public void zapamtiIzvestaj(Ucesce u, JTextField txtIncident, JTextField txtKazna, JTextField txtKrug, IzvestajIncidentaForma aThis) {
             
         try {
             String incident=txtIncident.getText();
             String kazna=txtKazna.getText();
             String krug=txtKrug.getText();
             if(incident.isEmpty()||kazna.isEmpty()||krug.isEmpty()){
               JOptionPane.showMessageDialog(aThis, "Niste uneli sve vrednosti!");
                 return;
             }
             int brKruga=0;
             try{
                 brKruga=Integer.parseInt(krug);
               
             } catch ( NumberFormatException ex) {
                 JOptionPane.showMessageDialog(aThis, "Niste uneli krug kao broj!");
                 return;
             }
             if(brKruga<0||(brKruga>(u.getTrka().getBrojKrugova()))){
              JOptionPane.showMessageDialog(aThis, "Niste uneli odgovarajuci krug !");
                 return;
             }
             IzvestajIncidenta ii=new IzvestajIncidenta(u.getVozac().getVozacID(), u.getTrka().getTrkaID(), -1, incident, kazna, brKruga, "");
            TransferObjekatZahtev toz=new TransferObjekatZahtev();
             toz.setOperacija(Operacije.ZAPAMTI_IZVESTAJ);
             toz.setParametar(ii);
             KomunikacijaSaServerom.getInstanca().posaljiZahtev(toz);
             TransferObjekatOdgovor too=KomunikacijaSaServerom.getInstanca().primiOdgovor();
             boolean uspesno=(boolean) too.getRezultat();
             if(uspesno){
                 JOptionPane.showMessageDialog(aThis, "Sistem je zapamtio izvestaj incidenta!");
                 aThis.dispose();
                 return;
             }else{
                 JOptionPane.showMessageDialog(aThis, "Sistem ne može da zapamti izvestaj incidenta!");
                 return;
             }
         } catch (IOException ex) {
                              JOptionPane.showMessageDialog(aThis, "Sistem ne može da zapamti vozača!");
             Logger.getLogger(KontrolerKI.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    }

  

