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

    private TextView _titleTextView;
    private TextView _triedLettersTextView;
    private TextView _wordToGuessTextView;
    private TextView _triesLeftTextView;

    private EditText _letterInput;

    private Button _sendLetterButton;
    private Button _restartButton;
    private Button _newGameButton;

    private ArrayList<String> _words;

    private String _wordToGuess;
    private char[] _wordToGuessCharArray;
    private String _triedLetters;

    private int triesLimit = 5;
    private int triesLeft = 5;

    private final String _title = "HANGMAN";
    private final String _notALetterInfo = "it's not a letter";
    private final String _alreadyTypedInfo = "already typed";
    private final String _winInfo = "YOU WON!";
    private final String _loseInfo = "GAME OVER!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _titleTextView = findViewById(R.id.title_text);
        _triedLettersTextView = findViewById(R.id.tried_letters);
        _wordToGuessTextView = findViewById(R.id.word_to_guess);
        _triesLeftTextView = findViewById(R.id.tries_left);

        _letterInput = findViewById(R.id.type_letter_input);

        _sendLetterButton = findViewById(R.id.send_letter_button);
        _restartButton = findViewById(R.id.restart_button);
        _newGameButton = findViewById(R.id.new_game_button);

        _words = new ArrayList<>();
        _words.add("accountant");
        _words.add("central");
        _words.add("professional");
        _words.add("neighbourhood");

        _sendLetterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_letterInput.getText().length() > 0) {
                    ShowLetter(_letterInput.getText().charAt(0));
                }
                _letterInput.getText().clear();
            }
        });

        _restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restart();
            }
        });

        _newGameButton.setOnClickListener(new View.OnClickListener() {
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
        SetWord(_wordToGuess, false);
        ShowWord();
        SetUpGame();
    }

    void NewGame() {
        SetWord(_wordToGuess, true);
        ShowWord();
        SetUpGame();
    }

    void SetWord() {
        // wybiera losowe słowo z listy
        Random random = new Random();
        _wordToGuess = _words.get(random.nextInt(_words.size()));
        _wordToGuessCharArray = _wordToGuess.toCharArray();

        // ukrywa litery z wylosowanego słowa oprócz pierwszej i ostatniej
        for (int i = 0; i < _wordToGuessCharArray.length; i++) {
            if (_wordToGuessCharArray[i] != _wordToGuessCharArray[0] && _wordToGuessCharArray[i] != _wordToGuessCharArray[_wordToGuessCharArray.length - 1]) {
                _wordToGuessCharArray[i] = '_';
            }
        }
    }

    // gdy gracz kliknie restart to ponownie ustawi te same słowo, gdy new game to wylosuje inne niż te w poprzedniej grze
    void SetWord(String word, boolean findOther) {
        Random random = new Random();

        if (findOther) {
            String otherWord = _words.get(random.nextInt(_words.size()));
            while (word.equals(otherWord)) {
                otherWord = _words.get(random.nextInt(_words.size()));
            }
            word = otherWord;
        }
        _wordToGuess = word;
        _wordToGuessCharArray = _wordToGuess.toCharArray();

        for (int i = 0; i < _wordToGuessCharArray.length; i++) {
            if (_wordToGuessCharArray[i] != _wordToGuessCharArray[0] && _wordToGuessCharArray[i] != _wordToGuessCharArray[_wordToGuessCharArray.length - 1]) {
                _wordToGuessCharArray[i] = '_';
            }
        }
    }

    void ShowWord() {
        _wordToGuessTextView.setText(new String(_wordToGuessCharArray));
    }

    void SetUpGame() {
        _triedLetters = " ";
        ShowTriedLetters();
        triesLeft = triesLimit;
        ShowTriesLeft();
        _sendLetterButton.setEnabled(true);
        _titleTextView.setText(_title);
    }

    void ShowTriedLetters() {
        _triedLettersTextView.setText(_triedLetters);
    }

    void ShowTriesLeft() {
        _triesLeftTextView.setText(new String(triesLeft + "/" + triesLimit));
    }

    void ShowLetter(char l) {
        // sprawdza czy użytkownik wprowadził literę
        if (!Character.isLetter(l)) {
            Toast.makeText(MainActivity.this, _notALetterInfo, Toast.LENGTH_SHORT).show();
            return;
        }

        // zmiana na małą literę
        l = Character.toLowerCase(l);

        // informacja gdy gracz wprowadzi literę, która została już odsłonięta
        if (_triedLetters.indexOf(l) >= 0) {
            Toast.makeText(MainActivity.this, _alreadyTypedInfo, Toast.LENGTH_SHORT).show();
        }
        else {
            // dodaje na listę użytych liter
            _triedLetters += l + " ";
            ShowTriedLetters();

            // gdy litery nie ma w zgadywanym słowie odejmuje 1 od liczby pozostałych prób
            if (_wordToGuess.indexOf(l) == -1) {
                triesLeft -= 1;
                UpdateTries();
            }

            // gdy jest to odsłania jej wszystkie wystąpienia
            else {
                for (int i = 1; i < _wordToGuess.length() - 1; i++) {
                    if (l == _wordToGuess.charAt(i)) {
                        _wordToGuessCharArray[i] = l;
                    }
                }

                // gdy w słowie nie ma już liter do odslonięcia
                if (!(new String(_wordToGuessCharArray).contains("_"))) {
                    GameOver(_winInfo);
                }

                ShowWord();
            }
        }
    }

    void UpdateTries() {
        // gdy gracz wyczerpał próby
        if (triesLeft <= 0) {
            GameOver(_loseInfo);
        }

        ShowTriesLeft();
    }

    void GameOver(String info) {
        Toast.makeText(MainActivity.this, info, Toast.LENGTH_LONG).show();
        _sendLetterButton.setEnabled(false);
        _titleTextView.setText(info);
    }
}