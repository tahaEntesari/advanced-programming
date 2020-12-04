package GUI.Panels;

import Configurations.GUIConfig;
import GUI.Mapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import GUI.UtilityFunctions.StringToJLabel;

import static LoggingModule.LoggingClass.logUser;

public class GameLogInPanel {
    private JPanel logInPanel;
    private JPanel signUpPanel;
    private JPanel mainPanel;
    private Mapper mapper;

    public GameLogInPanel(int xdim, int ydim) {
        this.mapper = Mapper.getInstance();
        logInPanel = new JPanel();
        signUpPanel = new JPanel();
        signUpPanel.setPreferredSize(new Dimension(xdim / 2 - 4, ydim));
        logInPanel.setPreferredSize(new Dimension(xdim / 2 - 4, ydim));
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(signUpPanel, BorderLayout.EAST);
        mainPanel.add(logInPanel, BorderLayout.WEST);
        completeSignUpPanel();
        completeLogInPanel();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void completeSignUpPanel() {
        Border innerBorder = BorderFactory.createTitledBorder("Sign Up");
        Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        this.signUpPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        JLabel username = new JLabel("Username: ");
        JLabel password = new JLabel("Password: ");
        JLabel reenterPassword = new JLabel("Re-enter password: ");
//        JLabel messageToShow = new JLabel("Enter a username and password to create an account");
        JLabel messageToShow = new JLabel("");

        JTextField usernameText = new JTextField(15);
        JPasswordField passwordText = new JPasswordField(15);
        JPasswordField reenterPasswordText = new JPasswordField(15);
        reenterPasswordText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Key entered: Enter! Sign up");
                signUp(usernameText, passwordText, reenterPasswordText, messageToShow);
            }
        });

        JButton sigUpButton = new JButton("Sign up!");
        sigUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Button pressed! Sign up");
                signUp(usernameText, passwordText, reenterPasswordText, messageToShow);
            }
        });

        this.signUpPanel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.NONE;
        //////////////// First Row //////////////////////
        addComponent(this.signUpPanel, gc, 0, 0, 1, 1, 1, username);
        addComponent(this.signUpPanel, gc, 1, 0, 1, 1, 0, usernameText);

        //////////////// Second Row //////////////////////
        addComponent(this.signUpPanel, gc, 0, 1, 1, 1, 1, password);
        addComponent(this.signUpPanel, gc, 1, 1, 1, 1, 0, passwordText);

        //////////////// Third Row //////////////////////
        addComponent(this.signUpPanel, gc, 0, 2, 1, 1, 1, reenterPassword);
        addComponent(this.signUpPanel, gc, 1, 2, 1, 1, 0, reenterPasswordText);

        //////////////// Fourth Row //////////////////////
        addComponent(this.signUpPanel, gc, 0, 3, 1, 1, 1, sigUpButton);

        //////////////// Fifth Row //////////////////////
        addComponent(this.signUpPanel, gc, 0, 4, 1, 1, 2, messageToShow);
    }

    private void completeLogInPanel() {
        Border innerBorder = BorderFactory.createTitledBorder("Log In");
        Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        this.logInPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        JLabel username = new JLabel("Username: ");
        JLabel password = new JLabel("Password: ");

//        JLabel messageToShow = new JLabel("Enter your credentials to enter the game.");
        JLabel messageToShow = new JLabel("");

        JTextField usernameText = new JTextField(15);
        JPasswordField passwordText = new JPasswordField(15);

        usernameText.setText("tt");
        passwordText.setText("Taha1234");


        passwordText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Key entered: enter! log in");
                logIn(usernameText, passwordText, messageToShow);
            }
        });

        JButton logInButton = new JButton("Log in!");
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Button pressed! log in");
                logIn(usernameText, passwordText, messageToShow);

            }
        });

        this.logInPanel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.NONE;
        //////////////// First Row //////////////////////
        addComponent(this.logInPanel, gc, 0, 0, 1, 1, 2, username);
        addComponent(this.logInPanel, gc, 1, 0, 1, 1, 2, usernameText);

        //////////////// Second Row //////////////////////
        addComponent(this.logInPanel, gc, 0, 2, 2, 1, 2, password);
        addComponent(this.logInPanel, gc, 1, 2, 2, 1, 2, passwordText);

        //////////////// Third Row //////////////////////
        addComponent(this.logInPanel, gc, 0, 4, 1, 1, 0, logInButton);

        //////////////// Fourth Row //////////////////////
        addComponent(this.logInPanel, gc, 0, 6, 1, 1, 2, messageToShow);


    }

    private void addComponent(JPanel panelToAddTo, GridBagConstraints gc, int gridx, int gridy, int gridHeight,
                              int gridWidth, int position, Component component) {
        gc.gridheight = gridHeight;
        gc.gridwidth = gridWidth;
        gc.gridx = gridx;
        gc.gridy = gridy;
        switch (position) {
            case 0:
                gc.anchor = GridBagConstraints.LINE_START;
                break;
            case 1:
                gc.anchor = GridBagConstraints.LINE_END;
                break;
            case 2:
                gc.anchor = GridBagConstraints.CENTER;
                break;
        }
        panelToAddTo.add(component, gc);
    }

    private void signUp(JTextField usernameText, JPasswordField passwordText, JPasswordField reenterPasswordText,
                        JLabel messageToShow) {
        String enteredUsername = usernameText.getText();
        String enteredPassword = new String(passwordText.getPassword());
        String reEnteredPassword = new String(reenterPasswordText.getPassword());
        String sigUpResult;
        if (!enteredPassword.equals(reEnteredPassword)) {
            sigUpResult = "Passwords are not a match!";
            messageToShow.setText(sigUpResult);
            System.out.println(sigUpResult);
        } else {
            sigUpResult = mapper.userSignUp(enteredUsername, enteredPassword);
            sigUpResult = StringToJLabel.correctFormat(sigUpResult);
            messageToShow.setText(sigUpResult);
            System.out.println(sigUpResult);
            mainPanel.repaint();
            mainPanel.revalidate();
        }
    }

    private void logIn(JTextField usernameText, JPasswordField passwordText, JLabel messageToShow) {
        String enteredUsername = usernameText.getText();
        String enteredPassword = new String(passwordText.getPassword());
        boolean logInResult;
        String message = "";
        try {
             logInResult = mapper.userLogIn(enteredUsername, enteredPassword);
             if(logInResult){
                 message += "Log in successful";
             } else {
                 message += "Log in unsuccessful";
             }
        } catch (Exception e){
            message += "Log in unsuccessful";
        }
        messageToShow.setText(message);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

}
