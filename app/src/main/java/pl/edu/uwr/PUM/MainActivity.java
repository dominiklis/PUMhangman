package pl.edu.uwr.PUM;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView _titleTextView;
    private TextView _triedLettersTextView;
    private TextView _wordToGuessTextView;
    private TextView _triesLeftTextView;

    private EditText _letterInput;

    private Button _sendLetterButton;

    private ArrayList<String> _words;

    private String _wordToGuess;
    private char[] _wordToGuessCharArray;
    private String _triedLetters;

    private int triesLimit = 5;
    private int triesLeft = 5;

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

        _words = new ArrayList<>();
        _words.add("accountant");
        _words.add("central");
        _words.add("professional");
        _words.add("neighbourhood");
    }
}