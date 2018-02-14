package com.codetreatise.service;

import com.codetreatise.bean.Agent;
import com.codetreatise.bean.Annonce;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class PrintReport {
    //public class PrintReport extends JFrame{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void showReport(Annonce annonce) throws JRException, ClassNotFoundException, SQLException {

        InputStream reportSrcFile = getClass().getResourceAsStream("/jasper/immo_fly-1.1.jrxml");

        // First, compile jrxml file.
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        // Fields for report
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("titre", annonce.getTitre());
        parameters.put("description", annonce.getDescription());
        parameters.put("prix", annonce.getPrix());
        parameters.put("coordLigne1", annonce.getAgent().getCoordonnees().getLine1forPdf());
        parameters.put("coordLigne2", annonce.getAgent().getCoordonnees().getLine2forPdf());
        parameters.put("coordLigne3", annonce.getAgent().getCoordonnees().getLine3forPdf());
        parameters.put("coordLigne4", annonce.getAgent().getCoordonnees().getLine4forPdf());
;

        ByteArrayInputStream bis = new ByteArrayInputStream(annonce.getPhoto1());
        BufferedImage read = null;
        try {
            read = ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        parameters.put("photo1", read);

        bis = new ByteArrayInputStream(annonce.getPhoto2());
        read = null;
        try {
            read = ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        parameters.put("photo2", read);

        bis = new ByteArrayInputStream(annonce.getPhoto3());
        read = null;
        try {
            read = ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        parameters.put("photo3", read);

        bis = new ByteArrayInputStream(annonce.getPhoto4());
        read = null;
        try {
            read = ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        parameters.put("photo4", read);

        bis = new ByteArrayInputStream(annonce.getCover());
        read = null;
        try {
            read = ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        parameters.put("cover", read);

        bis = new ByteArrayInputStream(annonce.getAgent().getLogo());
        read = null;
        try {
            read = ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        parameters.put("logo", read);


        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list.add(parameters);

        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, null, beanColDataSource);

        OutputStream output = null;
        try {
            output = new FileOutputStream(new File("/home/axel/JasperReport.pdf"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JasperExportManager.exportReportToPdfStream(print, output);

        System.out.print("Done! in /home/axel/JasperReport.pdf");
    }


        /* JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
       this.add(viewer);
        this.setSize(700, 500);
        this.setVisible(true);*/

    public void showReport(Agent agent){
        //TODO everything
    }

}
