package Hangman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class hangman {

    public static void main(String[]args) throws FileNotFoundException {
        String word = pickWord();
        startGUI(word);
    }
    public static void startGUI(String selectedWord){

        final int[] remainingGuesses = {7};
        List incorrectGuesses = new List();
        List usedLetters = new List();
        final int[] points = {0};

        // Creates a frame
        JFrame f = new JFrame("Hangman");
        f.setLayout(new FlowLayout());
        f.setSize(1200, 450);
        f.setResizable(false);

        JPanel panel1 = new JPanel(); // Label panel
        JPanel panel2 = new JPanel(); // Guessed letters panel
        JPanel panel3 = new JPanel(); // Guessing panel
        JPanel panel4 = new JPanel(); // Guesses remaining and incorrect guesses panel

        panel1.setPreferredSize(new Dimension(1150,100));
        panel2.setPreferredSize(new Dimension(1150,100));
        panel3.setPreferredSize(new Dimension(1150,100));
        panel4.setPreferredSize(new Dimension(1150,75));

        f.add(panel1);
        f.add(panel2);
        f.add(panel3);
        f.add(panel4);

        // PANEL 1
        JLabel title = new JLabel();
        title.setText("Hangman");
        title.setFont(new Font("Serif", Font.BOLD, 60));
        panel1.add(title);


        // PANEL 2
        JTextArea[] wordLetterCount = new JTextArea[10];

        for (int i = 0; i < (selectedWord.length()); i++) {
            wordLetterCount[i] = new JTextArea(1, 2);
            wordLetterCount[i].setFont(new Font("Segoe Script", Font.BOLD, 50));
            wordLetterCount[i].setEditable(false);
            wordLetterCount[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel2.add(wordLetterCount[i]);
        }


        // PANEL 3
        panel3.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel guessLabel = new JLabel();
        guessLabel.setText("      Guess Letter: ");
        guessLabel.setFont(new Font("Serif", Font.BOLD, 30));
        panel3.add(guessLabel);

        JTextField guessLetterTextField = new JTextField();
        guessLetterTextField.setPreferredSize(new Dimension(75,75));
        guessLetterTextField.setFont(new Font("Segoe Script", Font.BOLD, 50));
        guessLetterTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel3.add(guessLetterTextField);

        panel3.add(new JLabel("     "));

        JButton submitButton = new JButton();
        submitButton.setText("Submit Guess");
        submitButton.setFont(new Font("Serif", Font.BOLD, 30));

        panel3.add(submitButton);


        // PANEL 4
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.PAGE_AXIS));

        JLabel guessesRemainingLabel = new JLabel();
        guessesRemainingLabel.setText("           Guesses remaining: " + remainingGuesses[0]);
        guessesRemainingLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JLabel incorrectGuessesLabel = new JLabel();
        incorrectGuessesLabel.setText("           Incorrect Guesses: " + Arrays.toString(incorrectGuesses.getItems()));
        incorrectGuessesLabel.setFont(new Font("Serif", Font.BOLD, 20));

        panel4.add(guessesRemainingLabel);
        panel4.add(incorrectGuessesLabel);


        // ACTIONS

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainingGuesses[0] > 0){

                    String letter = guessLetterTextField.getText().toUpperCase();

                    if (isValid(letter)){
                        if (!Arrays.toString(usedLetters.getItems()).contains(letter)){
                            if (selectedWord.contains(letter)){
                                ArrayList<Integer> letterLocations = new ArrayList<>();
                                for(int i = 0; i < selectedWord.length(); i++){
                                    if(String.valueOf(selectedWord.charAt(i)).equals(letter)){
                                        letterLocations.add(i);
                                    }
                                }
                                for(Integer i : letterLocations){
                                    wordLetterCount[i].setText(" " + letter);
                                }
                                usedLetters.add(letter);
                                points[0] = points[0] + letterLocations.size();
                            }
                            else {
                                usedLetters.add(letter);
                                remainingGuesses[0] -= 1;
                                guessesRemainingLabel.setText("           Guesses remaining: " + remainingGuesses[0]);
                                incorrectGuesses.add(letter);
                                incorrectGuessesLabel.setText("           Incorrect Guesses: " + Arrays.toString(incorrectGuesses.getItems()));
                            }
                        }
                        else {
                            System.out.println("LETTER ALREADY USED!");
                            JOptionPane.showMessageDialog(null,
                                    "ERROR: This letter has already been used.",
                                    "Error",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    else {
                        System.out.println("ERROR");
                        JOptionPane.showMessageDialog(null,
                                "ERROR: Input can only be a letter.",
                                "Input Error",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                }
                else {
                    System.out.println("GAME OVER");
                }

                System.out.println("Points: " + points[0]);

                if (points[0] == selectedWord.length()){
                    System.out.println("YOU WIN!!!!!");
                    JOptionPane.showMessageDialog(null,
                            "You have guessed the word correctly.",
                            "YOU WIN!",
                            JOptionPane.INFORMATION_MESSAGE);
                    System.exit(1);
                }

                if (remainingGuesses[0] == 0){
                    System.out.println("GAME OVER - YOU HAVE LOST");
                    JOptionPane.showMessageDialog(null,
                            "GAME OVER - YOU HAVE LOST. The correct word was: " + selectedWord,
                            "GAME OVER",
                            JOptionPane.INFORMATION_MESSAGE);
                    System.exit(1);
                }
            }
        });

        // Makes the window/frame visible to keep the GUI running
        f.setVisible(true);
    }

    public static String pickWord() throws FileNotFoundException {
        List wordsList = new List();
        File file = new File(System.getProperty("user.dir") + "\\words.txt");
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine())
            wordsList.add(sc.nextLine());

        Random random = new Random();
        int value = random.nextInt(10 + 1) + 1;

        System.out.println("Word chosen: " + wordsList.getItem(value).toUpperCase());

        return wordsList.getItem(value).toUpperCase();
    }

    public static boolean isValid(String input){
        return input != null && input.length() == 1 && Character.isLetter(input.charAt(0));
    }
}
