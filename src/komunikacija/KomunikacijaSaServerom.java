/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikacija;

import domen.Nalog;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;


/**
 *
 * @author Miroslav
 */
public class KomunikacijaSaServerom {
    private Nalog ulogovani;
    
    private static KomunikacijaSaServerom instanca;
    Socket s;

    
    
    private KomunikacijaSaServerom() throws IOException {
        
       //     s= new Socket("localhost", 9000);
       
    }

    public static KomunikacijaSaServerom getInstanca() throws IOException {
        
        if(instanca == null)
            instanca = new KomunikacijaSaServerom();
        return instanca;
    }
    
     public void posaljiZahtev(TransferObjekatZahtev toz){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(toz);
        } catch (IOException ex) {
            Logger.getLogger(KomunikacijaSaServerom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public TransferObjekatOdgovor primiOdgovor(){
    
        TransferObjekatOdgovor too = new TransferObjekatOdgovor();
        
        try {
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            too = (TransferObjekatOdgovor) ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(KomunikacijaSaServerom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KomunikacijaSaServerom.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return too;
    }


    public void poveziSe(Socket s) {
this.s=s;
    }

    public Nalog getUlogovani() {
        return ulogovani;
    }

    public void setUlogovani(Nalog ulogovani) {
        this.ulogovani = ulogovani;
    }
    
    
}
