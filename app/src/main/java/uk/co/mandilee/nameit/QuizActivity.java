package uk.co.mandilee.nameit;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class QuizActivity extends AppCompatActivity {
    public static final int ANS1 = 1; // only first answer is correct
    public static final int ANS2 = 2; // first two answers are correct (checkbox only)
    public static final int ANS3 = 3; // first three answers are correct (checkbox only)
    public static final int ANS4 = 4; // all answers are correct (checkbox only)
    private static final String QUESTION_ARRAY = "questionArray",
            SCORE = "score",
            CURRENT_QUESTION = "currentQuestion",
            NUM_QUESTIONS = "numQuestions";
    private final List<Question> questions = new ArrayList<>();
    private RadioButton radioOptionA, radioOptionB, radioOptionC, radioOptionD;
    private EditText editTextAnswer;
    private CheckBox checkOptionA, checkOptionB, checkOptionC, checkOptionD;
    private ImageButton nextButton, prevButton;
    private Button submitButton;
    private int score = 0, currentQuestion = 0, numQuestions;
    private Question thisQuestion;
    private TextView questionNumber, tvGivenAnswer;
    private ImageView questionImage;
    private RadioGroup radioGroup;
    private LinearLayout checkBoxGroup, editTextGroup, llGivenAnswer;

    // Implementing Fisher–Yates shuffle
    private static void shuffleArray(int[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        setVariables();

        // recovering the instance state
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE);
            currentQuestion = savedInstanceState.getInt(CURRENT_QUESTION);
            numQuestions = savedInstanceState.getInt(NUM_QUESTIONS);

            for (int i = 0; i < numQuestions; i++) {
                Question q = (Question) savedInstanceState.getSerializable(QUESTION_ARRAY + i);
                questions.add(q);
            }
        } else {
            addAllQuestions();
        }
        thisQuestion = questions.get(currentQuestion);
        setQuestion();

        // next button is clicked, check question has been answered
        // if it has, move to next question
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if (checkScoreRadio() && checkScoreEditText() && checkScoreCheckBox()) {
                    currentQuestion++;
                    setQuestion();
                } else {
                    myToast(getString(R.string.missing_answers));
                }
            }
        });

        // prev button is clicked
        // move to previous question
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                currentQuestion--;
                setQuestion();
            }
        });

        // submit button clicked, check question has been answered
        // show Toast with score/
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkScoreRadio() || checkScoreEditText() || checkScoreCheckBox()) {
                    myToast(getString(R.string.you_got) + " " + score + " " + getString(R.string.out_of) + " " + numQuestions);
                    setQuestion();
                } else {
                    myToast(getString(R.string.missing_answers));
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        for (int i = 0; i < numQuestions; i++) {
            outState.putSerializable(QUESTION_ARRAY + i, questions.get(i));
        }
        outState.putInt(NUM_QUESTIONS, numQuestions);
        outState.putInt(SCORE, score);
        outState.putInt(CURRENT_QUESTION, currentQuestion);
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    /*
     * initialize all the variables here to keep it tidy
     */
    private void setVariables() {
        // Question Stuff
        questionImage = (ImageView) findViewById(R.id.question_image);
        questionNumber = (TextView) findViewById(R.id.question_number);
        llGivenAnswer = (LinearLayout) findViewById(R.id.answer_group);
        tvGivenAnswer = (TextView) findViewById(R.id.given_answer);

        // Answer Groups
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        checkBoxGroup = (LinearLayout) findViewById(R.id.checkbox_group);
        editTextGroup = (LinearLayout) findViewById(R.id.edit_text_group);

        // RadioButton options
        radioOptionA = (RadioButton) findViewById(R.id.radio_option_1);
        radioOptionB = (RadioButton) findViewById(R.id.radio_option_2);
        radioOptionC = (RadioButton) findViewById(R.id.radio_option_3);
        radioOptionD = (RadioButton) findViewById(R.id.radio_option_4);

        // CheckBox options
        checkOptionA = (CheckBox) findViewById(R.id.checkbox_option_1);
        checkOptionB = (CheckBox) findViewById(R.id.checkbox_option_2);
        checkOptionC = (CheckBox) findViewById(R.id.checkbox_option_3);
        checkOptionD = (CheckBox) findViewById(R.id.checkbox_option_4);

        // EditText answer
        editTextAnswer = (EditText) findViewById(R.id.editText);

        // ImageButtons
        prevButton = (ImageButton) findViewById(R.id.prev_button);
        nextButton = (ImageButton) findViewById(R.id.next_button);
        submitButton = (Button) findViewById(R.id.submit_button);
    }

    /*
     * set all the pieces for the current question
     */
    private void setQuestion() {
        thisQuestion = questions.get(currentQuestion);  // get the current question
        int[] answers = thisQuestion.getAnswerArray();  // get the answer array
        shuffleArray(answers);                          // shuffle the answers

        // hide all the answer types
        llGivenAnswer.setVisibility(View.GONE);
        checkBoxGroup.setVisibility(View.GONE);
        editTextGroup.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);

        // unset any checked options
        checkOptionA.setChecked(false);
        checkOptionB.setChecked(false);
        checkOptionC.setChecked(false);
        checkOptionD.setChecked(false);

        // same for radio
        radioGroup.clearCheck();

        // remove focus from edittext
        editTextAnswer.clearFocus();

        // set the text and image for the new question
        questionNumber.setText(getString(R.string.question) + " " + (currentQuestion + 1));
        questionImage.setImageResource(thisQuestion.getImageResId());

        // if it's been answered show whether or not they were correct
        if (!thisQuestion.getAnswerGiven().equals("")) {
            String correctAnswer = "";
            // Grab all the string answer(s)
            // switch fallthrough is intentional here!
            switch (thisQuestion.getCorrectAnswers()) {
                case ANS4:
                    correctAnswer += getString(thisQuestion.getAnswer4()) + ", ";
                case ANS3:
                    correctAnswer += getString(thisQuestion.getAnswer3()) + ", ";
                case ANS2:
                    correctAnswer += getString(thisQuestion.getAnswer2()) + ", ";
                case 1:
                    correctAnswer += getString(thisQuestion.getAnswer1()) + ", ";
            }
            correctAnswer = correctAnswer.substring(0, correctAnswer.length() - 2);

            // set the text to show whether or not they were right
            if (thisQuestion.isAnswerCorrect()) {
                tvGivenAnswer.setText(getString(R.string.you_were_right) + getString(R.string.its_a) + " " + correctAnswer);
            } else {
                tvGivenAnswer.setText(getString(R.string.you_were_wrong) + " " + thisQuestion.getAnswerGiven() +
                        getString(R.string.its_a) + " " + correctAnswer);
            }
            // and set the response visible
            llGivenAnswer.setVisibility(View.VISIBLE);

            // if it hasn't been answered and it's a radio
        } else if (thisQuestion.mRadioGroup != null) {
            // set the random answers and make visible
            radioOptionA.setText(answers[0]);
            radioOptionB.setText(answers[1]);
            radioOptionC.setText(answers[2]);
            radioOptionD.setText(answers[3]);
            radioGroup.setVisibility(View.VISIBLE);

            // if it hasn't been answered and it's a checkbox
        } else if (thisQuestion.mCheckBox != null) {
            // set the random answers and make visible
            checkOptionA.setText(answers[0]);
            checkOptionB.setText(answers[1]);
            checkOptionC.setText(answers[2]);
            checkOptionD.setText(answers[3]);
            checkBoxGroup.setVisibility(View.VISIBLE);

            // if it hasn't been answered and it's an edittext
        } else if (thisQuestion.mEditText != null) {
            // empty and make visible
            editTextAnswer.setText("");
            editTextGroup.setVisibility(View.VISIBLE);
        }

        showHideButtons();  // show or hide the buttons as appropriate

    }

    /*
     * check the score if currentQuestion is a RadioButton
     * @return boolean - whether or not it's been answered
     */
    private boolean checkScoreRadio() {
        if (thisQuestion.getRadioGroup() != null && thisQuestion.getAnswerGiven().equals("")) {
            if (radioGroup.getCheckedRadioButtonId() != -1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                thisQuestion.setAnswerGiven(radioButton.getText().toString());
                if (thisQuestion.getAnswerGiven().equals(getString(thisQuestion.getAnswer1()))) {
                    score++;
                    thisQuestion.setAnswerCorrect();
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /*
     * check the score if currentQuestion is EditText
     * @return boolean - whether or not it's been answered
     */
    private boolean checkScoreEditText() {
        if (thisQuestion.getEditText() != null) {
            String answer = editTextAnswer.getText().toString();
            if (answer.length() > 0) {
                thisQuestion.setAnswerGiven(answer);
                if (thisQuestion.getAnswerGiven().equalsIgnoreCase(getString(thisQuestion.getAnswer1()))) {
                    score++;
                    thisQuestion.setAnswerCorrect();
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /*
     * check the score if currentQuestion is a CheckBox
     * @return boolean - whether or not it's been answered
     */
    private boolean checkScoreCheckBox() {
        if (thisQuestion.getCheckBox() != null && thisQuestion.getAnswerGiven().equals("")) {
            String given = " ";
            double answers = 0;
            boolean wrong = false;

            if (checkOptionA.isChecked()) {
                given += checkOptionA.getText() + ", ";
                if (checkOptionA.getText().equals(getString(thisQuestion.getAnswer1())) ||
                        checkOptionA.getText().equals(getString(thisQuestion.getAnswer2()))) {
                    answers++;
                } else {
                    wrong = true;
                }
            }
            if (checkOptionB.isChecked()) {
                given += checkOptionB.getText() + ", ";
                if (checkOptionB.getText().equals(getString(thisQuestion.getAnswer1())) ||
                        checkOptionB.getText().equals(getString(thisQuestion.getAnswer2()))) {
                    answers++;
                } else {
                    wrong = true;
                }
            }
            if (checkOptionC.isChecked()) {
                given += checkOptionC.getText() + ", ";
                if (checkOptionC.getText().equals(getString(thisQuestion.getAnswer1())) ||
                        checkOptionC.getText().equals(getString(thisQuestion.getAnswer2()))) {
                    answers++;
                } else {
                    wrong = true;
                }
            }
            if (checkOptionD.isChecked()) {
                given += checkOptionD.getText() + ", ";
                if (checkOptionD.getText().equals(getString(thisQuestion.getAnswer1())) ||
                        checkOptionD.getText().equals(getString(thisQuestion.getAnswer2()))) {
                    answers++;
                } else {
                    wrong = true;
                }
            }

            if (answers == 0 && !wrong) {
                return false;
            }

            thisQuestion.setAnswerGiven(given.substring(0, given.length() - 2));
            if (answers == thisQuestion.getCorrectAnswers() && !wrong) {
                score++;
                thisQuestion.setAnswerCorrect();
            }
        }
        return true;
    }

    /*
     * short way to show a Toast
     * @param String - message to display
     */
    private void myToast(String message) {
        Toast.makeText(QuizActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /*
     * Add all the questions to an array here
     * for ease of viewing, first line consists of correct answers, second line (if any) is wrong answers
     */
    private void addAllQuestions() {
        questions.add(new Question(R.drawable.labrador, R.string.labrador, R.string.golden_lab,
                R.string.bull_mastiff, R.string.dalmatian, ANS2, checkBoxGroup));

        questions.add(new Question(R.drawable.american_eskimo, R.string.american_eskimo,
                R.string.alaskan_malamute, R.string.husky, R.string.king_charles, radioGroup));

        questions.add(new Question(R.drawable.bullmastiff, R.string.bull_mastiff,
                R.string.dalmatian, R.string.chihuahua, R.string.staffie, radioGroup));

        questions.add(new Question(R.drawable.boxer, R.string.boxer, editTextGroup));

        questions.add(new Question(R.drawable.alaskan_malamute, R.string.alaskan_malamute,
                R.string.american_eskimo, R.string.husky, R.string.chihuahua, radioGroup));

        questions.add(new Question(R.drawable.chowchow, R.string.chowchow,
                R.string.cairn_terrier, R.string.alaskan_malamute, R.string.poodle, radioGroup));

        questions.add(new Question(R.drawable.westie, R.string.westie, R.string.west_highland_terrier,
                R.string.cairn_terrier, R.string.chowchow, ANS1, checkBoxGroup));

        questions.add(new Question(R.drawable.king_charles, R.string.king_charles,
                R.string.cocker_spaniel, R.string.labradoodle, R.string.poodle, radioGroup));

        questions.add(new Question(R.drawable.weimaraner, R.string.weimaraner,
                R.string.greyhound, R.string.cairn_terrier, R.string.labrador, radioGroup));

        questions.add(new Question(R.drawable.whippet, R.string.whippet, editTextGroup));

        // store number of questions for later
        numQuestions = questions.size();
    }

    /*
     * Simply decides whether or not to hide each button based on current question number
     */
    private void showHideButtons() {
        // hide the previous and submit buttons if it's the first question
        if (currentQuestion == 0) {
            prevButton.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);

            // hide the next button and show the submit button if it's the last question
        } else if (currentQuestion == numQuestions - 1) {
            prevButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
            submitButton.setVisibility(View.VISIBLE);

            // otherwise show prev and next, hide submit
        } else {
            prevButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);
        }
    }
}
