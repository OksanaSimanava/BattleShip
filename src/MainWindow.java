import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("all")
public class MainWindow {

	private JFrame frame;
	final JPanel loginPanel;
	private Field fieldPanel;
	private UserResults userResultsPanel;
	final JTextField textField;
	final JPasswordField passwordField;
	JButton buttonSignUp;
	JButton buttonSignIn;
	private String login, password;
	private String salt;

	private Player player;

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		salt = salt();
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		createDB();

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 1000, 400); // 600 250
		frame.setResizable(false);

		loginPanel = new JPanel();
		loginPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.ipadx = 10;
		gbc.ipady = 10;
		gbc.anchor = GridBagConstraints.WEST;
		loginPanel.setLayout(gbl);

		JLabel lblLogin = new JLabel("EnterLogin");
		gbc.gridx = 0;
		gbc.gridy = 0;
		loginPanel.add(lblLogin, gbc);

		JLabel lblPassword = new JLabel("EnterPassword");
		gbc.gridx = 0;
		gbc.gridy = 1;
		loginPanel.add(lblPassword, gbc);

		textField = new JTextField(10);
		lblLogin.setLabelFor(textField);
		gbc.gridx = 2;
		gbc.gridy = 0;
		loginPanel.add(textField, gbc);

		passwordField = new JPasswordField(10);
		lblPassword.setLabelFor(passwordField);
		gbc.gridx = 2;
		gbc.gridy = 1;
		loginPanel.add(passwordField, gbc);

		buttonSignUp = new JButton("Sign Up");
		buttonSignUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				login = textField.getText();
				password = String.valueOf(passwordField.getPassword());
				registration(salt, login, password);
			}
		}); 
		
		
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		loginPanel.add(buttonSignUp, gbc);

		buttonSignIn = new JButton("Sign In");
		buttonSignIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				login = textField.getText();
				password = String.valueOf(passwordField.getPassword());
				int playerID = validation(salt, login, password);
				if (playerID != -1) {
					player = new Player(playerID);
					startUserSession(player);
			}
			}
		});     
		
		gbc.gridx = 2;
		gbc.gridy = 3;
		loginPanel.add(buttonSignIn, gbc);

		frame.add(loginPanel);
		frame.setVisible(true);
	}

	public static String salt() {
		int n = 26;
		ArrayList<Character> arr = new ArrayList<>();
		char a = 'a', b = 'A', c = '0';
		for (int i = 0; i < n; i++) {
			arr.add(a++);
			arr.add(b++);
			if (i < 10)
				arr.add(c++);
		}
		Collections.shuffle(arr);
		return arr.toString().replaceAll(", |\\[|\\]", "").substring(0, 10);
	}

	public static void registration(String salt, String login, String password) {

		if (login.length() < 4) {
			JOptionPane.showMessageDialog(null,
					"Login name is too short(should be at least 4 symbols)",
					"Registration error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (password.length() < 4) {
			JOptionPane.showMessageDialog(null,
					"Password is too short(should be at least 4 symbols)",
					"Registration error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Connection cn;
		try {
			cn = DriverManager.getConnection("jdbc:mysql://localhost/BD",
					"root", "root");
			Statement st = cn.createStatement();
			int intRole = 1;

			String passSalt = null;
			try {
				passSalt = Sha1Encryption.encryptPassword((Sha1Encryption
						.encryptPassword(salt) + Sha1Encryption
						.encryptPassword(password + salt)));
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			String queruRegister = "insert into users"
					+ "(name, password, salt, role) values(" + "'" + login
					+ "'" + "," + "'" + passSalt + "'" + "," + "'" + salt + "'"
					+ "," + "'" + intRole + "')";

			try {
				st.execute(queruRegister);
				JOptionPane.showMessageDialog(null, "User " + login
						+ " was registered. Now you can enter the game.",
						"Successful registration", 1);

			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Mistake", "Login is busy",
						1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int validation(String salt, String inText, String pass) {
		Connection cn;
		try {
			cn = DriverManager.getConnection("jdbc:mysql://localhost", "root",
					"root");
			Statement st = cn.createStatement();
			st.executeUpdate("use BD");

			ResultSet rs = st
					.executeQuery("select name, password, user_id from users where name = '"
							+ inText + "'");

			// String getLogin = null;
			String passFromDB = null;
			int id = 0;
			while (rs.next()) {
				// getLogin = rs.getString("name");
				passFromDB = rs.getString("password");
				id = rs.getInt("user_id");
			}

			String inPassSalt = null;
			try {
				inPassSalt = Sha1Encryption.encryptPassword((Sha1Encryption
						.encryptPassword(salt) + Sha1Encryption
						.encryptPassword(pass + salt)));
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			//assert inPassSalt != null;
			if(inPassSalt != null){
				if (inPassSalt.equals(passFromDB)) {
					return id;
	
				} else {
					JOptionPane.showMessageDialog(null,
							"Login or password is wrong", "Mistake",
							JOptionPane.WARNING_MESSAGE);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static void createDB() {
		Connection cn;
		Statement st;

		try {
			String queryCreateUser = "create table users(user_id int auto_increment, "
					+ "name varchar(60) not null unique, "
					+ "password varchar(60) not null, "
					+ "salt varchar(10) not null,"
					+ "role tinyint not null,"
					+ "begin datetime,"
					+ "end datetime,"
					+ "gamesWon int default 0,"
					+ "gamesLose int default 0,"
					+ "gamesZero int default 0," + "primary key(user_id))";
			
			String queruCreateGames = "create table games(game_id int auto_increment, "
					+ "user_id_1 int, "
					+ "user_id_2 int, "
					+ "result tinyint(1),"
					+ "field1 varchar(3),"
					+ "field2 varchar(3),"
					+ "begin datetime,"
					+ "end datetime,"
					+ "primary key(game_id),"
					+ "foreign key(user_id_1) references users(user_id) on delete cascade,"
					+ "foreign key(user_id_2) references users(user_id) on delete cascade)";

			String queruRegisterBot = "insert into users"
					+ "(name, password, salt, role) values('bot',"
					+ "'null', '0','0')";

			cn = DriverManager.getConnection("jdbc:mysql://localhost", "root",
					"root");
			st = cn.createStatement();
			try {
				st.executeUpdate("drop database BD");
			} catch (SQLException se) {
				System.out.println("Database doesn't exist");
			}
			try {
				st.executeUpdate("create database BD");
				st.executeUpdate("use BD");
				st.executeUpdate(queryCreateUser);
				st.executeUpdate(queruCreateGames);
				st.executeUpdate(queruRegisterBot);
			} catch (SQLException se) {
				se.printStackTrace();
				return;
			}

		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	public void startUserSession(Player player) {
		loginPanel.setVisible(false);
		fieldPanel = new Field(this);
		fieldPanel.createField();

		userResultsPanel = new UserResults();
		userResultsPanel.createPanel(this, player);

		frame.add(fieldPanel, BorderLayout.CENTER);
		frame.add(userResultsPanel, BorderLayout.EAST);
	}

	public void newGameAction(int status) {
		player.updateStatCount(status);
		frame.remove(fieldPanel);
		fieldPanel = new Field(this);
		fieldPanel.createField();
		frame.add(fieldPanel, BorderLayout.CENTER);
		userResultsPanel.updatePanel();
	}

	public void logOutAction() {
		player.updateStatCount(0);
		frame.remove(fieldPanel);
		frame.remove(userResultsPanel);
		fieldPanel = null;
		userResultsPanel = null;
		loginPanel.setVisible(true);
	}
}
