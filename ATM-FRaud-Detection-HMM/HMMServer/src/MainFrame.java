import javax.swing.UIManager;

public class MainFrame extends javax.swing.JFrame {

    private MyServer server = null;
    FileServer FileServerObj;
    Thread th = null;
    public MainFrame() {
        initComponents();
    }

    public void StartServer() {
        if (server == null) {
            server = new MyServer(this);
            server.startServer();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HMM Server");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("HMM Server Log :");

        jMenu1.setText("Server");

        jMenuItem1.setText("Start Server");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);
        jMenu1.add(jSeparator1);

        jMenuItem2.setText("Stop Server");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("User");

        jMenuItem3.setText("Create New Account");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("System");

        jMenuItem4.setText("Exit");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 415, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        java.awt.Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        NewUser lf = new NewUser();
        java.awt.Dimension dialogsize = lf.getSize();
        lf.setLocation((screensize.width / 2) - (dialogsize.width / 2), (screensize.height / 2) - (dialogsize.height / 2));
        lf.show();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        StartServer();
        FileServerObj = new FileServer();
        th = new Thread(FileServerObj);
        th.start();
        jTextArea1.append("Server Started....\n");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        th.stop();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

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

            public void run() {
                ApplyTheme(6);
                java.awt.Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                MainFrame lf = new MainFrame();
                lf.setSize((screensize.width), (screensize.height));
                lf.show();
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    public javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
