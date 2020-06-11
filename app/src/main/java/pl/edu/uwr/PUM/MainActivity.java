package pl.edu.uwr.PUM;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private TextView mTriedLettersTextView;
    private TextView mWordToGuessTextView;
    private TextView mTriesLeftTextView;

    private EditText mLetterInput;

    private Button mSendLetterButton;
    private Button mRestartButton;
    private Button mNewGameButton;

    private ArrayList<Word> mWords;

    private Word mCurrentWord;
    private String mWordToGuess;
    private char[] mWordToGuessCharArray;
    private String mTriedLetters;
    private char[] mFirstAndLastLetter = new char[2];

    private final int mTriesLimit = 5;
    private int mTriesLeft = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitleTextView = findViewById(R.id.title_text);
        mTriedLettersTextView = findViewById(R.id.tried_letters);
        mWordToGuessTextView = findViewById(R.id.word_to_guess);
        mTriesLeftTextView = findViewById(R.id.tries_left);

        mLetterInput = findViewById(R.id.type_letter_input);

        mSendLetterButton = findViewById(R.id.send_letter_button);
        mRestartButton = findViewById(R.id.restart_button);
        mNewGameButton = findViewById(R.id.new_game_button);

        mWords = new ArrayList<>();
        mWords.add(new Word(R.string.word1));
        mWords.add(new Word(R.string.word2));
        mWords.add(new Word(R.string.word3));
        mWords.add(new Word(R.string.word4));

        mSendLetterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLetterInput.getText().length() > 0) {
                    ShowLetter(mLetterInput.getText().charAt(0));
                }
                mLetterInput.getText().clear();
            }
        });

        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restart();
            }
        });

        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewGame();
            }
        });

        StartGame();
    }

    void StartGame() {
        SetWord();
        ShowWord();
        SetUpGame();
    }
    void Restart() {
        SetWord(mCurrentWord, false);
        ShowWord();
        SetUpGame();
    }

    void NewGame() {
        SetWord(mCurrentWord, true);
        ShowWord();
        SetUpGame();
    }

    void SetWord() {
        // wybiera losowe słowo z listy
        Random random = new Random();
        mCurrentWord = mWords.get(random.nextInt(mWords.size()));
        mWordToGuess = getResources().getString(mCurrentWord.getWordId());
        mWordToGuessCharArray = mWordToGuess.toCharArray();

        // ukrywa litery z wylosowanego słowa oprócz pierwszej i ostatniej
        for (int i = 0; i < mWordToGuessCharArray.length; i++) {
            if (mWordToGuessCharArray[i] != mWordToGuessCharArray[0] && mWordToGuessCharArray[i] != mWordToGuessCharArray[mWordToGuessCharArray.length - 1]) {
                mWordToGuessCharArray[i] = '_';
            }
        }

        mFirstAndLastLetter[0] = mWordToGuessCharArray[0];
        mFirstAndLastLetter[1] = mWordToGuessCharArray[mWordToGuessCharArray.length - 1];
    }

    // gdy gracz kliknie restart to ponownie ustawi te same słowo, gdy new game to wylosuje inne niż te w poprzedniej grze
    void SetWord(Word word, boolean findOther) {
        Random random = new Random();

        if (findOther) {
            Word otherWord = mWords.get(random.nextInt(mWords.size()));
            while (word == otherWord) {
                otherWord = mWords.get(random.nextInt(mWords.size()));
            }
            word = otherWord;
        }

        mCurrentWord = word;
        mWordToGuess = getResources().getString(mCurrentWord.getWordId());
        mWordToGuessCharArray = mWordToGuess.toCharArray();

        for (int i = 0; i < mWordToGuessCharArray.length; i++) {
            if (mWordToGuessCharArray[i] != mWordToGuessCharArray[0] && mWordToGuessCharArray[i] != mWordToGuessCharArray[mWordToGuessCharArray.length - 1]) {
                mWordToGuessCharArray[i] = '_';
            }
        }

        mFirstAndLastLetter[0] = mWordToGuessCharArray[0];
        mFirstAndLastLetter[1] = mWordToGuessCharArray[mWordToGuessCharArray.length - 1];
    }

    void ShowWord() {
        mWordToGuessTextView.setText(new String(mWordToGuessCharArray));
    }

    void SetUpGame() {
        mTriedLetters = " ";
        ShowTriedLetters();
        mTriesLeft = mTriesLimit;
        ShowTriesLeft();
        mSendLetterButton.setEnabled(true);
        mTitleTextView.setText(R.string.title_text);
    }

    void ShowTriedLetters() {
        mTriedLettersTextView.setText(mTriedLetters);
    }

    void ShowTriesLeft() {
        mTriesLeftTextView.setText(new String(mTriesLeft + "/" + mTriesLimit));
    }

    void ShowLetter(char l) {
        // sprawdza czy użytkownik wprowadził literę
        if (!Character.isLetter(l)) {
            Toast.makeText(MainActivity.this, R.string.not_a_letter_info, Toast.LENGTH_SHORT).show();
            return;
        }

        // zmiana na małą literę
        l = Character.toLowerCase(l);

        // gdy gracz wprowadzi odsłoniętą od początku literę
        if (l == mFirstAndLastLetter[0] || l == mFirstAndLastLetter[1]) {
            Toast.makeText(MainActivity.this, R.string.first_or_last_letter_info, Toast.LENGTH_SHORT).show();
        }
        // informacja gdy gracz wprowadzi literę, która została już odsłonięta
        else if (mTriedLetters.indexOf(l) >= 0) {
            Toast.makeText(MainActivity.this, R.string.already_typed_info, Toast.LENGTH_SHORT).show();
        }
        else {
            // dodaje na listę użytych liter
            mTriedLetters += l + " ";
            ShowTriedLetters();

            // gdy litery nie ma w zgadywanym słowie odejmuje 1 od liczby pozostałych prób
            if (mWordToGuess.indexOf(l) == -1) {
                mTriesLeft -= 1;
                UpdateTries();
            }

            // gdy jest to odsłania jej wszystkie wystąpienia
            else {
                for (int i = 1; i < mWordToGuess.length() - 1; i++) {
                    if (l == mWordToGuess.charAt(i)) {
                        mWordToGuessCharArray[i] = l;
                    }
                }

                // gdy w słowie nie ma już liter do odslonięcia
                if (!(new String(mWordToGuessCharArray).contains("_"))) {
                    GameOver(getResources().getString(R.string.you_won_text));
                }

                ShowWord();
            }
        }
    }

    void UpdateTries() {
        // gdy gracz wyczerpał próby
        if (mTriesLeft <= 0) {
            GameOver(getResources().getString(R.string.game_over_text));
        }

        ShowTriesLeft();
    }

    void GameOver(String info) {
        Toast.makeText(MainActivity.this, info, Toast.LENGTH_LONG).show();
        mSendLetterButton.setEnabled(false);
        mTitleTextView.setText(info);
    }
}