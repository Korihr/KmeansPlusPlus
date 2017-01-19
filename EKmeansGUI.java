package com.mycompany.kmeans;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import static java.lang.Math.abs;
import java.util.ArrayList;

public class EKmeansGUI {

    private class DoubleKmeansExt extends DoubleKmeans {
        public DoubleKmeansExt(double[][] centroids, double[][] points, DoubleDistanceFunction doubleDistanceFunction) {
            super(centroids, points, doubleDistanceFunction);
        }
    }

    private static final int X = 0;
    private static final int Y = 1;

    private static final int RESOLUTION = 300;
    private final JToolBar toolBar;
    private final JTextField kTextField;
    private final JPanel canvaPanel;
    private JCheckBox delCheckBox;

    private double[][] centroids = null;
    private double[][] pointsMas = null;
    private ArrayList<Point> points = null;  
    private Point p;
    
    private DoubleKmeansExt eKmeans = null;
    private KmeansPlusPlus kmeansPP = null;

    public EKmeansGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(RESOLUTION + 100, RESOLUTION + 100));
        frame.setPreferredSize(new Dimension(RESOLUTION * 2, RESOLUTION * 2));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        frame.setContentPane(contentPanel);

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        contentPanel.add(toolBar, BorderLayout.NORTH);
        
        contentPanel.setCursor(new java.awt.Cursor(java.awt.Cursor
                .CROSSHAIR_CURSOR));
         
        JLabel delLabel = new JLabel("Delete:");
        toolBar.add(delLabel);
        delCheckBox = new JCheckBox("");
        toolBar.add(delCheckBox);

        JLabel kLabel = new JLabel("k:");
        toolBar.add(kLabel);
        kTextField = new JTextField("3");
        toolBar.add(kTextField);
        
        JButton runButton = new JButton();
        runButton.setAction(new AbstractAction(" Run ") {
            public void actionPerformed(ActionEvent ae) {
                run();
            }
        });
        toolBar.add(runButton);
        
        canvaPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                EKmeansGUI.this.paint(g, getWidth(), getHeight());
            }
        };
        contentPanel.add(canvaPanel, BorderLayout.CENTER);
        
        points = new ArrayList<Point>();       
         contentPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p = evt.getPoint();
                if(delCheckBox.isSelected()) {
                    delete();
                }
                else {
                    points.add(p);
                    run(); 
                }
            }
        });
        frame.pack();
        frame.setVisible(true);
    }
    
    private void delete() {
        for(int i = 0; i < points.size(); i++) {
            if((abs(points.get(i).getX() - p.getX())) < 5 &&
               (abs(points.get(i).getY() - p.getY())) < 5) {
                points.remove(i);
            }
        }
        run();
    }
    
    private void run()
    {
        int k = Integer.parseInt(kTextField.getText());
        if(points.size() < k) {
            canvaPanel.repaint();
            return;
        }
        centroids = new double[k][2];
        kmeansPP = new KmeansPlusPlus(points, centroids, k);
        kmeansPP.run();
        pointsMas = new double[points.size()][2];
        for(int i = 0; i < points.size(); i++) {
            pointsMas[i][X] = points.get(i).getX()/canvaPanel.getWidth();
            pointsMas[i][Y] = points.get(i).getY()/canvaPanel.getHeight();
        }
         for(int i = 0; i < k; i++) {
            centroids[i][X] = centroids[i][X] / canvaPanel.getWidth();
            centroids[i][Y] = centroids[i][Y] / canvaPanel.getHeight(); 
         }
        eKmeans = new DoubleKmeansExt(centroids, pointsMas,
                                     DoubleKmeans.EUCLIDEAN_DISTANCE_FUNCTION);
        eKmeans.run();
        canvaPanel.repaint();
    }
    
    private void paint(Graphics g, int width, int height) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        for (int i = 0; i < points.size(); i++) {
            g.drawRect((int)points.get(i).getX() - 3,
                       (int)points.get(i).getY() - 33, 4, 4);
        }       
        if (eKmeans == null) {
            return;
        }
        if(centroids.length <= points.size()) {
            g.setColor(Color.GREEN);
            for (double[] centroid : centroids) {
                g.drawRect((int)(centroid[X] * canvaPanel.getWidth()) - 2,
                    (int)(centroid[Y] * canvaPanel.getHeight()) - 32, 4, 4);
            }
        }
    }
    
    public static void main(String[] args) {
        new EKmeansGUI();
    }
}
