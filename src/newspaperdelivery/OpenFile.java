/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package newspaperdelivery;

import java.awt.Desktop;
import static java.awt.SystemColor.desktop;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author kedarkanel
 */


public class OpenFile {

    public void open(String filePath){       
        if (!Desktop.isDesktopSupported()) {
            System.err.println("Desktop not supported");
            // use alternative (Runtime.exec)
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.EDIT)) {
            System.err.println("EDIT not supported");
            // use alternative (Runtime.exec)
            return;
        }

        try {
            desktop.open(new File(filePath));
    //Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + OpeningFile.outputFilePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
