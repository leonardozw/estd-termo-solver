import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TermoSolver {

    public static final String RESET = "\033[0m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";

    private String[] wordList;
    private String pickedWord;
    private String hiddenWord;
    private String inputWord;

    public TermoSolver(String[] wordList) {
        this.wordList = wordList;
        this.pickedWord = pickRandomWord();
        this.hiddenWord = initializeHiddenWord();
        this.inputWord = "";
    }

    private String pickRandomWord() {
        applyLengthFilter();
        Random random = new Random();
        return wordList[random.nextInt(wordList.length)];
    }

    private String initializeHiddenWord() {
        char[] hidden = new char[pickedWord.length()];
        Arrays.fill(hidden, '*');
        return new String(hidden);
    }

    public String makeGuess(String guess) {
        StringBuilder b = new StringBuilder();
        for(int i = 0; i < guess.length(); i++){
            char c = guess.charAt(i);
            if(pickedWord.charAt(i) == c){
                b.append(GREEN + c + RESET);
            }else if(pickedWord.contains(Character.toString(c))){
                b.append(YELLOW + c + RESET);
            }else{
                b.append(c);
            }
        }
        return b.toString();
    }

    public void applyLengthFilter() {
        List<String> temp = new ArrayList<>();
        for (String s : wordList) {
            if (s.length() == 5) {
                temp.add(s);
            }
        }
        String[] tempArr = temp.toArray(new String[0]);
        wordList = tempArr;
    }

    public void printWordList() {
        for (String s : wordList) {
            System.out.println(s);
        }
    }

    public boolean isSolved() {
        return inputWord.equals(pickedWord);
    }

    public String getHiddenWord() {
        return hiddenWord;
    }

    public String getPickedWord() {
        return pickedWord;
    }

    public void setInputWord(String inputWord) {
        this.inputWord = inputWord;
    }

    public static void main(String[] args) {
        String path = "words.txt";
        List<String> words = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        int tries = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String row;

            while ((row = br.readLine()) != null) {
                String[] wordsInRow = row.split("\\s+");
                words.addAll(Arrays.asList(wordsInRow));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] wordList = words.toArray(new String[0]);

        TermoSolver termoSolver = new TermoSolver(wordList);

        System.err.println("Palavra: " + termoSolver.getHiddenWord());

        while(tries > 6 || !termoSolver.isSolved()){
            String guess = sc.nextLine();
            termoSolver.setInputWord(guess);
            System.out.println("> " + termoSolver.makeGuess(guess));
        }
        System.out.println("A palavra correta era: " + termoSolver.getPickedWord());
        
    }
}