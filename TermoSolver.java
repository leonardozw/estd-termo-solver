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

    private List<String> wordList;
    private String pickedWord;
    private String hiddenWord;
    private String inputWord;

    public TermoSolver(List<String> wordList) {
        this.wordList = wordList;
        this.pickedWord = pickRandomWord();
        this.hiddenWord = hideWord();
        this.inputWord = "";
    }

    private String pickRandomWord() {
        Random random = new Random();
        int size = wordList.size();
        return wordList.get(random.nextInt(size));
    }

    private String hideWord() {
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

    public void print(){
        for(String w : wordList){
            System.out.println(w);
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
        String path = "filteredWords.txt";
        Scanner sc = new Scanner(System.in);
        int tries = 0;
        
        List<String> wordsList = readFromFile(path);

        TermoSolver termoSolver = new TermoSolver(wordsList);

        termoSolver.print();

        System.err.println("Palavra: " + termoSolver.getHiddenWord());
        System.out.println(termoSolver.getPickedWord());

        while(tries < 6 && !termoSolver.isSolved()){
            String guess = sc.nextLine();
            termoSolver.setInputWord(guess);
            System.out.println("> " + termoSolver.makeGuess(guess));
            tries++;
        }
        System.out.println("A palavra correta era: " + termoSolver.getPickedWord());
        
    }

    public static List<String> readFromFile(String path){
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String row;
            while ((row = br.readLine()) != null) {
                String[] wordsInRow = row.split("\\s+");
                words.addAll(Arrays.asList(wordsInRow));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}