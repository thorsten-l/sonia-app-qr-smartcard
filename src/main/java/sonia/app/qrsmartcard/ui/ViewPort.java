/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonia.app.qrsmartcard.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sonia.app.qrsmartcard.qrclient.QrResponse;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@ostfalia.de>
 */
public class ViewPort extends javax.swing.JPanel
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    ViewPort.class.getName());
  
  private final static int VP_WIDTH = 800;

  private final static int VP_HEIGHT = 480;

  private int state = 0;

  private QrResponse reponse;

  private final Font bigFont = new Font("Arial", Font.BOLD, 72);
  private final Font midFont = new Font("Arial", Font.BOLD, 48);
  private final Font smallFont = new Font("Arial", Font.BOLD, 24);

  private final static ViewPort SINGLETON = new ViewPort();

  private ViewPort()
  {

  }

  public static ViewPort getInstance()
  {
    return SINGLETON;
  }
  
  @Override
  public void paint(Graphics g)
  {
    super.paint(g); //To change body of generated methods, choose Tools | Templates.
    Graphics2D g2d = (Graphics2D) g;

    g2d.setColor(new java.awt.Color(117, 192, 255));
    g2d.fillRect(0, 0, VP_WIDTH, VP_HEIGHT);
    g2d.setColor(Color.BLACK);
    g2d.setFont(bigFont);

    switch (state)
    {
      case 0:
        g2d.drawString("Karte auflegen", 60, 280);
        break;

      case 1:
        g2d.drawString("Unbekannte Karte", 40, 280);
        break;

      case 2:
        byte[] imageData = Base64.getDecoder().decode(reponse.getJpegPhoto());
        g2d.setFont(midFont);
        g2d.drawString(reponse.getGivenName(), 380, 80);
        g2d.drawString(reponse.getSn(), 380, 140);
        
        g2d.setFont(smallFont);
        g2d.drawString( reponse.getMail(), 380, 240);
        g2d.drawString("Typ: " + reponse.getEmployeeType(), 380, 280);
       
        if ( "s".equalsIgnoreCase(reponse.getEmployeeType()))
        {
          g2d.drawString("Matrikel-Nr: " + reponse.getSoniaStudentNumber(), 380, 320);
        }
        
        {
          try
          {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(
              imageData));
            LOGGER.debug( "image width = " + image.getWidth() + ", height=" + image.getHeight());
            g2d.drawImage( image, null, 0, 0);
          }
          catch (IOException ex)
          {
            LOGGER.error("ERROR loading image");
            //
          }
        }

        break;
        
      case 3:
        g2d.drawString("Lese Karte...", 40, 280);
        break;

      case 4:
        g2d.drawString("Shutdown...", 40, 280);
        break;

      default:
    }
  }

  public static void setState(int state)
  {
    SINGLETON.state = state;
    SINGLETON.refresh();
  }

  public static void setQrResponse(QrResponse response)
  {
    SINGLETON.reponse = response;
    setState(2);
  }

  private void refresh()
  {
    new Thread()
    {
      @Override
      public void run()
      {
        SINGLETON.repaint();
      }
    }.start();
  }
}
