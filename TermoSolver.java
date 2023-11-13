import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TermoSolver {
    private String[] wordList;
    private String pickedWord;
    private String hiddenWord;

    public TermoSolver(String[] wordList) {
        this.wordList = wordList;
        this.pickedWord = pickRandomWord();
        this.hiddenWord = initializeHiddenWord();
    }

    private String pickRandomWord() {
        Random random = new Random();
        return wordList[random.nextInt(wordList.length)];
    }

    private String initializeHiddenWord() {
        char[] hidden = new char[pickedWord.length()];
        Arrays.fill(hidden, '*');
        return new String(hidden);
    }

    public Result makeGuess(String guess) {
        char[] hiddenWordArray = hiddenWord.toCharArray();
        List<Character> presentLetters = new ArrayList<>();
        List<Character> notPresentLetters = new ArrayList<>();

        for (int i = 0; i < pickedWord.length(); i++) {
            if (pickedWord.charAt(i) == guess.charAt(i)) {
                hiddenWordArray[i] = guess.charAt(i);
            } else if (pickedWord.contains(String.valueOf(guess.charAt(i)))) {
                presentLetters.add(guess.charAt(i));
            } else {
                notPresentLetters.add(guess.charAt(i));
            }
        }

        hiddenWord = new String(hiddenWordArray);
        return new Result(hiddenWord, presentLetters, notPresentLetters);
    }

    public void applyLengthFilter() {
        List<String> temp = new ArrayList<>();
        for (String s : wordList) {
            if (s.length() == pickedWord.length()) {
                temp.add(s);
            }
        }
        String[] tempArr = temp.toArray(new String[0]);
        wordList = tempArr;
    }

    public void filterWords(Result result) {
        List<String> filteredWords = new ArrayList<>();

        for (String word : wordList) {
            if (matchesResult(word, result)) {
                filteredWords.add(word);
            }
        }
        String[] temp = filteredWords.toArray(new String[0]);
        wordList = temp;
    }

    private boolean matchesResult(String word, Result result) {
        if (!result.getWord().equals(word)) {
            return false;
        }
    
        // Check for matching characters at other positions
        for (char presentChar : result.getPresentChars()) {
            if (result.getWord().indexOf(presentChar) == -1) {
                return false;
            }
        }
    
        // Check for characters that should not be present
        for (char notPresentChar : result.getNotPresentChars()) {
            if (word.indexOf(notPresentChar) != -1) {
                return false;
            }
        }
    
        return true;
    }

    public void printWordList() {
        for (String s : wordList) {
            System.out.println(s);
        }
    }

    public String makeFirstGuess() {
        applyLengthFilter();
        return wordList[0];
    }

    public boolean isEmpty(){
        return wordList.length == 0;
    }

    public boolean isSolved() {
        return hiddenWord.equals(pickedWord);
    }

    public String getHiddenWord() {
        return hiddenWord;
    }

    public String getPickedWord() {
        return pickedWord;
    }

    public static void main(String[] args) {
        String path = "words.txt";
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

        String[] wordList = words.toArray(new String[0]);

        TermoSolver termoSolver = new TermoSolver(wordList);

        String firstGuess = termoSolver.makeFirstGuess();

        System.out.println("Palavra original: " + termoSolver.getPickedWord());

        System.err.println("Palavra escodinda: " + termoSolver.getHiddenWord());

        System.out.println("Primeira tentativa: " + firstGuess);

        Result result = termoSolver.makeGuess(firstGuess);

        System.out.println("Resultado primeira tentativa: " + result);

        System.out.println(termoSolver.isEmpty());

        termoSolver.filterWords(result);

        System.out.println(termoSolver.isEmpty());

    }
}

class Result {
    private String word;
    private List<Character> presentChars;
    private List<Character> notPresentChars;

    public Result(String word, List<Character> presentChars, List<Character> notPresentChars) {
        this.word = word;
        this.presentChars = presentChars;
        this.notPresentChars = notPresentChars;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<Character> getPresentChars() {
        return presentChars;
    }

    public void setPresentChars(List<Character> presentChars) {
        this.presentChars = presentChars;
    }

    public List<Character> getNotPresentChars() {
        return notPresentChars;
    }

    public void setNotPresentChars(List<Character> notPresentChars) {
        this.notPresentChars = notPresentChars;
    }

    @Override
    public String toString() {
        return "Result [word=" + word + ", presentChars=" + presentChars + ", notPresentChars=" + notPresentChars + "]";
    }
}