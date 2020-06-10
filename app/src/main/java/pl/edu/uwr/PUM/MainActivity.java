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

    private ArrayList<String> mWords;

    private String mWordToGuess;
    private char[] mWordToGuessCharArray;
    private String mTriedLetters;

    private int mTriesLimit = 5;
    private int mTriesLeft = 5;

    private final String mTitle = "HANGMAN";
    private final String mNotALetterInfo = "it's not a letter";
    private final String mAlreadyTypedInfo = "already typed";
    private final String mWinInfo = "YOU WON!";
    private final String mLoseInfo = "GAME OVER!";

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
        mWords.add("accountant");
        mWords.add("central");
        mWords.add("professional");
        mWords.add("neighbourhood");

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
        SetWord(mWordToGuess, false);
        ShowWord();
        SetUpGame();
    }

    void NewGame() {
        SetWord(mWordToGuess, true);
        ShowWord();
        SetUpGame();
    }

    void SetWord() {
        // wybiera losowe słowo z listy
        Random random = new Random();
        mWordToGuess = mWords.get(random.nextInt(mWords.size()));
        mWordToGuessCharArray = mWordToGuess.toCharArray();

        // ukrywa litery z wylosowanego słowa oprócz pierwszej i ostatniej
        for (int i = 0; i < mWordToGuessCharArray.length; i++) {
            if (mWordToGuessCharArray[i] != mWordToGuessCharArray[0] && mWordToGuessCharArray[i] != mWordToGuessCharArray[mWordToGuessCharArray.length - 1]) {
                mWordToGuessCharArray[i] = '_';
            }
        }
    }

    // gdy gracz kliknie restart to ponownie ustawi te same słowo, gdy new game to wylosuje inne niż te w poprzedniej grze
    void SetWord(String word, boolean findOther) {
        Random random = new Random();

        if (findOther) {
            String otherWord = mWords.get(random.nextInt(mWords.size()));
            while (word.equals(otherWord)) {
                otherWord = mWords.get(random.nextInt(mWords.size()));
            }
            word = otherWord;
        }
        mWordToGuess = word;
        mWordToGuessCharArray = mWordToGuess.toCharArray();

        for (int i = 0; i < mWordToGuessCharArray.length; i++) {
            if (mWordToGuessCharArray[i] != mWordToGuessCharArray[0] && mWordToGuessCharArray[i] != mWordToGuessCharArray[mWordToGuessCharArray.length - 1]) {
                mWordToGuessCharArray[i] = '_';
            }
        }
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
        mTitleTextView.setText(mTitle);
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
            Toast.makeText(MainActivity.this, mNotALetterInfo, Toast.LENGTH_SHORT).show();
            return;
        }

        // zmiana na małą literę
        l = Character.toLowerCase(l);

        // informacja gdy gracz wprowadzi literę, która została już odsłonięta
        if (mTriedLetters.indexOf(l) >= 0) {
            Toast.makeText(MainActivity.this, mAlreadyTypedInfo, Toast.LENGTH_SHORT).show();
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
                    GameOver(mWinInfo);
                }

                ShowWord();
            }
        }
    }

    void UpdateTries() {
        // gdy gracz wyczerpał próby
        if (mTriesLeft <= 0) {
            GameOver(mLoseInfo);
        }

        ShowTriesLeft();
    }

    void GameOver(String info) {
        Toast.makeText(MainActivity.this, info, Toast.LENGTH_LONG).show();
        mSendLetterButton.setEnabled(false);
        mTitleTextView.setText(info);
    }
}