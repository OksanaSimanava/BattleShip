import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UserResults extends JPanel {

    private JLabel winCount;
    private JLabel loseCount;
    private JLabel zeroCount;
    private Player player;


    public void createPanel(MainWindow frame, Player player) {
        this.player = player;
        this.setBorder(new EmptyBorder(5, 5, 5, 5));

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = 1;
        gbc.ipady = 1;

        this.setLayout(gbl);

        JLabel lblWin = new JLabel();
        lblWin.setText("Won:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbl.setConstraints(lblWin, gbc);
        this.add(lblWin);

        winCount = new JLabel();
        winCount.setText(Integer.toString(this.player.getWonCount()));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbl.setConstraints(winCount, gbc);
        this.add(winCount);

        JLabel lblLose = new JLabel();
        lblLose.setText("Lost:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbl.setConstraints(lblLose, gbc);
        this.add(lblLose);

        loseCount = new JLabel();
        loseCount.setText(Integer.toString(this.player.getLoseCount()));
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbl.setConstraints(loseCount, gbc);
        this.add(loseCount);

        JLabel lblZero = new JLabel();
        lblZero.setText("Not complited:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbl.setConstraints(lblZero, gbc);
        this.add(lblZero);

        zeroCount = new JLabel();
        zeroCount.setText(Integer.toString(this.player.getZeroCount()));
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbl.setConstraints(zeroCount, gbc);
        this.add(zeroCount);

        JButton btnNewGame = new JButton();
        btnNewGame.setText("New game");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbl.setConstraints(btnNewGame, gbc);
        this.add(btnNewGame);

        //btnNewGame.addActionListener(arg0 -> frame.newGameAction(0));
        btnNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.newGameAction(0);
			}
		}); 

        JButton btnLogOut = new JButton();
        btnLogOut.setText("Log out");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbl.setConstraints(btnLogOut, gbc);
        this.add(btnLogOut);

        //btnLogOut.addActionListener(arg0 -> frame.logOutAction());
        btnLogOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.logOutAction();
			}
		}); 

        this.setVisible(true);
    }

    public void  updatePanel() {
        winCount.setText(Integer.toString(this.player.getWonCount()));
        loseCount.setText(Integer.toString(this.player.getLoseCount()));
        zeroCount.setText(Integer.toString(this.player.getZeroCount()));
    }

}
