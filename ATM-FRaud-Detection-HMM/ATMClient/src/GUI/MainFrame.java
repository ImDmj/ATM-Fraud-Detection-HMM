package GUI;

import Server.ServerConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

public class MainFrame extends javax.swing.JFrame implements Runnable {
    //

    public static String RFID="";
    public static String Acc_no;
    public static byte[] data;
    public static String Old_Pin;
    public static String Bal;
    public static String EnteredAmt;
    public static String ServerIP = "127.0.0.1";
    public String op;
    //
    String NewPinMsg = "";
    String CashMsg = "";
    String Number = "";
    public String RFIDNumber = "";
    private Thread t;
    int flag = 0;
    private ServerConnection server = null;
    public static MainFrame mainobj = null;

    public MainFrame() {
        try {
            initComponents();
            mainobj = this;

            bleft1.setEnabled(false);
            bleft2.setEnabled(false);
            bright1.setEnabled(false);
            bright2.setEnabled(false);
            b0.setEnabled(false);
            b1.setEnabled(false);
            b2.setEnabled(false);
            b3.setEnabled(false);
            b4.setEnabled(false);
            b5.setEnabled(false);
            b6.setEnabled(false);
            b7.setEnabled(false);
            b8.setEnabled(false);
            b9.setEnabled(false);
            bcancel.setEnabled(true);
            bclear.setEnabled(true);
            benter.setEnabled(true);
            cashlabel.setVisible(false);
            txtpwd.setVisible(false);
            txtamount.setVisible(false);
            jDesktopPane1.setVisible(false);
            optionlb.setVisible(false);

            NewPinMsg = "\n"
                    + "\n                ENTER NEW"
                    + "\n               PIN NUMBER";

            //
            CashMsg = "\n"
                    + "\n           ENTER AMOUNT";
            //
            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/WelcomeMsg.gif")));
            optionlb.setVisible(true);
            //
            server = new ServerConnection("127.0.0.1");
            server.start();
            //
            //
            //UPS = new UPSInterface(this);
            //UPS.go();
            new ReadNFC(5);
            t = new Thread(this);
            t.start();

        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void run() {
        while (flag == 0) {
            System.out.println("Not Found");
            if (!ReadNFC.NFCCardTagID.equals("")) {
                RFIDNumber = (ReadNFC.NFCCardTagID);
                System.out.println("Done");
                ReadNFC.NFCCardTagID = "";
                optionlb.setVisible(false);
                System.out.println("Your card No :" + RFIDNumber);
                server.SendCommand("rfid " + server.getId() + " " + RFIDNumber);
                try {
                    CaptureDesktopScreen.captureScreen(RFIDNumber+ ".bmp");
                    RFID=RFIDNumber;
                    server.SendCommand("email " + server.getId() + " " + RFIDNumber);
                } catch (Exception ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

                t.stop();
                break;
            }

       // try{
         //       Thread.sleep(5000);
           //     RFIDNumber = "1234567890";
            //} catch (Exception err) {
             //   System.out.println("Error :" + err.toString());
            //}
        }


 }

    public void ChangeMsg(int flg) {
        this.flag = flg;
        if (this.flag == 0) {
            jDesktopPane1.setVisible(true);
            ThumbChkForm f = new ThumbChkForm(this);
            jDesktopPane1.add(f, javax.swing.JDesktopPane.CENTER_ALIGNMENT);
            f.show();
            //server.SendCommand("rfid " + server.getId() + " " + RFIDNumber);
        } else if (this.flag == 1) {
            b0.setEnabled(true);
            b1.setEnabled(true);
            b2.setEnabled(true);
            b3.setEnabled(true);
            b4.setEnabled(true);
            b5.setEnabled(true);
            b6.setEnabled(true);
            b7.setEnabled(true);
            b8.setEnabled(true);
            b9.setEnabled(true);
            bcancel.setEnabled(true);
            bclear.setEnabled(true);
            //
            txtpwd.setVisible(true);
            txtpwd.requestFocus(true);
            //
            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/PinMsg.gif")));
            optionlb.setVisible(true);
        } else if (this.flag == 2) {
            try {
                optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/PinErrorMsg.gif")));
                optionlb.setVisible(true);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (Exception ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            Reload();

        } else if (this.flag == 3) {
            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/OptionMsg.gif")));
            optionlb.setVisible(true);
            bleft1.setEnabled(true);
            bleft2.setEnabled(true);
            bright1.setEnabled(true);
            bright2.setEnabled(true);
        } else if (this.flag == 4) {
            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/BalanceMsg.gif")));
            optionlb.setVisible(true);
            txtamount.setVisible(true);
            txtamount.setText(Bal);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            txtamount.setText("");
            txtamount.setVisible(false);
            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/OptionMsg.gif")));
            optionlb.setVisible(true);
            bleft1.setEnabled(true);
            bleft2.setEnabled(true);
            bright1.setEnabled(true);
            bright2.setEnabled(true);
            this.flag = 3;
        } else if (this.flag == 5) {
            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/FlashMsg.gif")));
            optionlb.setVisible(true);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            txtpwd.setVisible(true);
            txtpwd.requestFocus(true);
        } else if (this.flag == 6) {
            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/CountCashMsg.gif")));
            optionlb.setVisible(true);
            int time = Integer.parseInt(EnteredAmt) / 100 * 1000 * 2;
            cashlabel.setVisible(true);
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            cashlabel.setVisible(false);
            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ThankMsg.gif")));
            optionlb.setVisible(true);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            Reload();
        } else if (this.flag == 7) {

            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/FlashErrorMsg.gif")));
            optionlb.setVisible(true);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            Reload();

        } else if (this.flag == 8) {

            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/PinChangeMsg.gif")));
            optionlb.setVisible(true);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            Reload();

        }
    }

    public void Reload() {
        this.flag = 0;
        this.RFIDNumber = "";
        bleft1.setEnabled(false);
        bleft2.setEnabled(false);
        bright1.setEnabled(false);
        bright2.setEnabled(false);
        b0.setEnabled(false);
        b1.setEnabled(false);
        b2.setEnabled(false);
        b3.setEnabled(false);
        b4.setEnabled(false);
        b5.setEnabled(false);
        b6.setEnabled(false);
        b7.setEnabled(false);
        b8.setEnabled(false);
        b9.setEnabled(false);
        bcancel.setEnabled(false);
        bclear.setEnabled(false);
        benter.setEnabled(false);
        cashlabel.setVisible(false);
        txtpwd.setVisible(false);
        txtamount.setVisible(false);
        //
        optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/WelcomeMsg.gif")));
        optionlb.setVisible(true);
        //
        //UPS = new UPSInterface(this);
        //UPS.go();
        t = new Thread(this);
        t.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtpwd = new javax.swing.JPasswordField();
        txtamount = new javax.swing.JTextField();
        optionlb = new javax.swing.JLabel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        bleft1 = new javax.swing.JButton();
        bleft2 = new javax.swing.JButton();
        bright1 = new javax.swing.JButton();
        bright2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cashlabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        b1 = new javax.swing.JButton();
        b2 = new javax.swing.JButton();
        b3 = new javax.swing.JButton();
        b4 = new javax.swing.JButton();
        b5 = new javax.swing.JButton();
        b6 = new javax.swing.JButton();
        b7 = new javax.swing.JButton();
        b8 = new javax.swing.JButton();
        b9 = new javax.swing.JButton();
        b0 = new javax.swing.JButton();
        bcancel = new javax.swing.JButton();
        bclear = new javax.swing.JButton();
        benter = new javax.swing.JButton();
        bexit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ATM CLIENT");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtpwd.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtpwd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtpwdKeyReleased(evt);
            }
        });
        getContentPane().add(txtpwd, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 270, 170, 30));

        txtamount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtamount.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtamount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtamountActionPerformed(evt);
            }
        });
        getContentPane().add(txtamount, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 270, 170, 30));

        optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/OptionMsg.gif"))); // NOI18N
        getContentPane().add(optionlb, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 186, 210, 140));
        getContentPane().add(jDesktopPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, 210, 160));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setEnabled(false);

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(0, 0, 0));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea1.setRows(5);
        jTextArea1.setEnabled(false);
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 210, 140));

        bleft1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/LArrow.png"))); // NOI18N
        bleft1.setAlignmentY(0.0F);
        bleft1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bleft1ActionPerformed(evt);
            }
        });
        getContentPane().add(bleft1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 29, 24));

        bleft2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/LArrow.png"))); // NOI18N
        bleft2.setAlignmentY(0.0F);
        bleft2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bleft2ActionPerformed(evt);
            }
        });
        getContentPane().add(bleft2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 265, 29, 25));

        bright1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/RArrow.png"))); // NOI18N
        bright1.setAlignmentY(0.0F);
        bright1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bright1ActionPerformed(evt);
            }
        });
        getContentPane().add(bright1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 210, 29, 25));

        bright2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/RArrow.png"))); // NOI18N
        bright2.setAlignmentY(0.0F);
        bright2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bright2ActionPerformed(evt);
            }
        });
        getContentPane().add(bright2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 265, 29, 25));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/number.jpg"))); // NOI18N
        jLabel2.setMaximumSize(new java.awt.Dimension(330, 202));
        jLabel2.setMinimumSize(new java.awt.Dimension(330, 202));
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(62, 339, 320, 190));

        cashlabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/atmcash.gif"))); // NOI18N
        getContentPane().add(cashlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(455, 270, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/atm.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        b1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/b1.png"))); // NOI18N
        b1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b1ActionPerformed(evt);
            }
        });
        getContentPane().add(b1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 60, 40));

        b2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/b2.png"))); // NOI18N
        b2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b2ActionPerformed(evt);
            }
        });
        getContentPane().add(b2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 350, 60, 40));

        b3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/b3.png"))); // NOI18N
        b3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b3ActionPerformed(evt);
            }
        });
        getContentPane().add(b3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 350, 60, 40));

        b4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/b4.png"))); // NOI18N
        b4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b4ActionPerformed(evt);
            }
        });
        getContentPane().add(b4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, 60, 40));

        b5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/b5.png"))); // NOI18N
        b5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b5ActionPerformed(evt);
            }
        });
        getContentPane().add(b5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 390, 60, 40));

        b6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/b6.png"))); // NOI18N
        b6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b6ActionPerformed(evt);
            }
        });
        getContentPane().add(b6, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 390, 60, 40));

        b7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/b7.png"))); // NOI18N
        b7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b7ActionPerformed(evt);
            }
        });
        getContentPane().add(b7, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 430, 60, 40));

        b8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/b8.png"))); // NOI18N
        b8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b8ActionPerformed(evt);
            }
        });
        getContentPane().add(b8, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 433, 60, 40));

        b9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/b9.png"))); // NOI18N
        b9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b9ActionPerformed(evt);
            }
        });
        getContentPane().add(b9, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 433, 60, 40));

        b0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/b0.png"))); // NOI18N
        b0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b0ActionPerformed(evt);
            }
        });
        getContentPane().add(b0, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 473, 60, 40));

        bcancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancel.png"))); // NOI18N
        bcancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcancelActionPerformed(evt);
            }
        });
        getContentPane().add(bcancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 350, 65, 39));

        bclear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/clear.png"))); // NOI18N
        bclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bclearActionPerformed(evt);
            }
        });
        getContentPane().add(bclear, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 390, 60, 41));

        benter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/enter.png"))); // NOI18N
        benter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                benterActionPerformed(evt);
            }
        });
        getContentPane().add(benter, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 433, 60, 41));

        bexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit.png"))); // NOI18N
        bexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bexitActionPerformed(evt);
            }
        });
        getContentPane().add(bexit, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 473, 60, 41));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b6ActionPerformed
        // TODO add your handling code here:
        if (flag == 1) {
            txtpwd.setText(txtpwd.getText().trim() + "6");
        } else if (flag == 3) {
            txtamount.setText(txtamount.getText().trim() + "6");
        }
    }//GEN-LAST:event_b6ActionPerformed

    private void bexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bexitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_bexitActionPerformed

    private void bclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bclearActionPerformed
        // TODO add your handling code here:
        if (flag == 1) {
            String pin = txtpwd.getText();
            txtpwd.setText(pin.substring(0, pin.length() - 1));
        } else if (flag == 3) {
            String amt = txtamount.getText();
            txtamount.setText(amt.substring(0, amt.length() - 1));
        }
    }//GEN-LAST:event_bclearActionPerformed

    private void b1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1ActionPerformed
        // TODO add your handling code here:
        if (flag == 1) {
            txtpwd.setText(txtpwd.getText().trim() + "1");
        } else if (flag == 3) {
            txtamount.setText(txtamount.getText().trim() + "1");
        }
    }//GEN-LAST:event_b1ActionPerformed

    private void b2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b2ActionPerformed
        // TODO add your handling code here:
        if (flag == 1) {
            txtpwd.setText(txtpwd.getText().trim() + "2");
        } else if (flag == 3) {
            txtamount.setText(txtamount.getText().trim() + "2");
        }
    }//GEN-LAST:event_b2ActionPerformed

    private void b3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b3ActionPerformed
        // TODO add your handling code here:
        if (flag == 1) {
            txtpwd.setText(txtpwd.getText().trim() + "3");
        } else if (flag == 3) {
            txtamount.setText(txtamount.getText().trim() + "3");
        }
    }//GEN-LAST:event_b3ActionPerformed

    private void b4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b4ActionPerformed
        // TODO add your handling code here:
        if (flag == 1) {
            txtpwd.setText(txtpwd.getText().trim() + "4");
        } else if (flag == 3) {
            txtamount.setText(txtamount.getText().trim() + "4");
        }
    }//GEN-LAST:event_b4ActionPerformed

    private void b5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b5ActionPerformed
        // TODO add your handling code here:
        if (flag == 1) {
            txtpwd.setText(txtpwd.getText().trim() + "5");
        } else if (flag == 3) {
            txtamount.setText(txtamount.getText().trim() + "5");
        }
    }//GEN-LAST:event_b5ActionPerformed

    private void b7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b7ActionPerformed
        // TODO add your handling code here:
        if (flag == 1) {
            txtpwd.setText(txtpwd.getText().trim() + "7");
        } else if (flag == 3) {
            txtamount.setText(txtamount.getText().trim() + "7");
        }
    }//GEN-LAST:event_b7ActionPerformed

    private void b8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b8ActionPerformed
        // TODO add your handling code here:
        if (flag == 1) {
            txtpwd.setText(txtpwd.getText().trim() + "8");
        } else if (flag == 3) {
            txtamount.setText(txtamount.getText().trim() + "8");
        }
    }//GEN-LAST:event_b8ActionPerformed

    private void b9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b9ActionPerformed
        // TODO add your handling code here:
        if (flag == 1) {
            txtpwd.setText(txtpwd.getText().trim() + "9");
        } else if (flag == 3) {
            txtamount.setText(txtamount.getText().trim() + "9");
        }
    }//GEN-LAST:event_b9ActionPerformed

    private void b0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b0ActionPerformed
        // TODO add your handling code here:
        if (flag == 1) {
            txtpwd.setText(txtpwd.getText().trim() + "0");
        } else if (flag == 3) {
            txtamount.setText(txtamount.getText().trim() + "0");
        }
    }//GEN-LAST:event_b0ActionPerformed

    private void bcancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcancelActionPerformed
        // TODO add your handling code here:
        Reload();
    }//GEN-LAST:event_bcancelActionPerformed

    private void benterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_benterActionPerformed
        // TODO add your handling code here:
        if (this.flag == 3) {
            //if (op.equals("amt")) 
            {
                EnteredAmt = txtamount.getText().trim();
                optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/WaitMsg.gif")));
                optionlb.setVisible(true);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                txtamount.setVisible(false);
                server.SendCommand("checkfraud " + server.getId() + " " + Acc_no + " " + EnteredAmt + " " + RFIDNumber);
            }
        } else if (this.flag == 5) {
            String SC = txtpwd.getText().trim();
            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/FlashCheckMsg.gif")));
            optionlb.setVisible(true);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            txtpwd.setVisible(false);
            server.SendCommand("checksecuritycode " + server.getId() + " " + Acc_no + " " + SC + " " + EnteredAmt);
        }
    }//GEN-LAST:event_benterActionPerformed

    private void txtpwdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpwdKeyReleased
        // TODO add your handling code here:
        if (txtpwd.getText().length() >= 4) {
            if (this.flag == 1) {
                try {
                    optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/PinCheckMsg.gif")));
                    optionlb.setVisible(true);
                    Old_Pin = txtpwd.getText().trim();
                    System.out.println("Pin Number :" + txtpwd.getText());
                    txtpwd.setVisible(false);
                    server.SendCommand("checkpin " + server.getId() + " " + Acc_no + " " + Old_Pin);
                    txtpwd.setText("");
                } catch (Exception ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (this.flag == 3) {
                String NewPin = txtpwd.getText().trim();
                txtpwd.setVisible(false);
                optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/WaitMsg.gif")));
                optionlb.setVisible(true);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                server.SendCommand("changepin " + server.getId() + " " + Acc_no + " " + Old_Pin + " " + NewPin);
                txtpwd.setText("");
            }
        }
    }//GEN-LAST:event_txtpwdKeyReleased

    private void txtamountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtamountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtamountActionPerformed

    private void bleft1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bleft1ActionPerformed
        // TODO add your handling code here:
        if (flag == 3) {
            op = "bal";
            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/WaitMsg.gif")));
            optionlb.setVisible(true);
            server.SendCommand("balance " + server.getId() + " " + Acc_no);
            bleft1.setEnabled(false);
            bleft2.setEnabled(false);
            bright1.setEnabled(false);
            bright2.setEnabled(false);
        }
    }//GEN-LAST:event_bleft1ActionPerformed

    private void bleft2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bleft2ActionPerformed
        // TODO add your handling code here:
        if (flag == 3) {
            op = "amt";
            //optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/CashMsg.gif")));
            optionlb.setVisible(false);
            jTextArea1.setText(CashMsg);
            txtamount.setVisible(true);
            txtamount.requestFocus(true);
            bleft1.setEnabled(false);
            bleft2.setEnabled(false);
            bright1.setEnabled(false);
            bright2.setEnabled(false);
        }
    }//GEN-LAST:event_bleft2ActionPerformed

    private void bright1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bright1ActionPerformed
        // TODO add your handling code here:
        if (flag == 3) {
            op = "pin";
            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/PinMsg.gif")));
            optionlb.setVisible(true);
            jTextArea1.setText(NewPinMsg);
            txtpwd.setVisible(true);
            txtpwd.requestFocus(true);
            bleft1.setEnabled(false);
            bleft2.setEnabled(false);
            bright1.setEnabled(false);
            bright2.setEnabled(false);
        }
    }//GEN-LAST:event_bright1ActionPerformed

    private void bright2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bright2ActionPerformed
        // TODO add your handling code here:
        if (flag == 3) {
            optionlb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/PrintMsg.gif")));
            optionlb.setVisible(true);
            bleft1.setEnabled(false);
            bleft2.setEnabled(false);
            bright1.setEnabled(false);
            bright2.setEnabled(false);
            server.SendCommand("print " + server.getId() + " " + Acc_no);
        }
    }//GEN-LAST:event_bright2ActionPerformed
    public static void ApplyTheme(int theme) {
        try {
            String[] lafs = {
                "de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaClassyLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaWhiteVisionLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaBlueLightLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaBlueMoonLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaBlueSteelLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaGreenDreamLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaMauveMetallicLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaSilverMoonLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaSkyMetallicLookAndFeel",
                "de.javasoft.plaf.synthetica.SyntheticaOrangeMetallicLookAndFeel"};

            System.out.println(System.getProperty("user.dir"));
            String[] li = {"Licensee=Jyloo Software", "LicenseRegistrationNumber=------", "Product=Synthetica", "LicenseType=For internal tests only", "ExpireDate=--.--.----", "MaxVersion=2.999.999"};

            UIManager.put(
                    "Synthetica.license.info", li);
            UIManager.put(
                    "Synthetica.license.key", "E1CBD033-B07718A2-1E181B5F-A78A6DFF-813D8FB4");

            UIManager.setLookAndFeel(
                    lafs[theme]);

        } catch (Exception ex) {
            System.out.println("Error :" + ex.toString());
        }

    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ApplyTheme(3);
                java.awt.Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                MainFrame lf = new MainFrame();
                java.awt.Dimension dialogsize = lf.getSize();
                lf.setLocation((screensize.width / 2) - (dialogsize.width / 2), (screensize.height / 2) - (dialogsize.height / 2));
                lf.show();
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b0;
    private javax.swing.JButton b1;
    private javax.swing.JButton b2;
    private javax.swing.JButton b3;
    private javax.swing.JButton b4;
    private javax.swing.JButton b5;
    private javax.swing.JButton b6;
    private javax.swing.JButton b7;
    private javax.swing.JButton b8;
    private javax.swing.JButton b9;
    private javax.swing.JButton bcancel;
    private javax.swing.JButton bclear;
    private javax.swing.JButton benter;
    private javax.swing.JButton bexit;
    private javax.swing.JButton bleft1;
    private javax.swing.JButton bleft2;
    private javax.swing.JButton bright1;
    private javax.swing.JButton bright2;
    private javax.swing.JLabel cashlabel;
    public static javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel optionlb;
    private javax.swing.JTextField txtamount;
    private javax.swing.JPasswordField txtpwd;
    // End of variables declaration//GEN-END:variables
}
