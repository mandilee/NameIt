package uk.co.mandilee.nameit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
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
    private static final int ANS1 = 1;
    private static final int ANS2 = 2;
    private static final int ANS3 = 4;
    private static final int AN$4 = 7;

    private RadioButton radioOptionA, radioOptionB, radioOptionC, radioOptionD;
    private EditText editTextAnswer;
    private CheckBox checkOptionA, checkOptionB, checkOptionC, checkOptionD;
    private ImageButton nextButton, prevButton, submitButton;

    private List<Integer> answers = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();
    private int score = 0, currentQuestion = 0, numQuestions;
    private Question thisQuestion;

    private TextView questionNumber, tvGivenAnswer;
    private ImageView questionImage;
    private RadioGroup radioGroup;
    private LinearLayout checkBoxGroup, editTextGroup, llGivenAnswer;

    // Implementing Fisher–Yates shuffle
    static void shuffleArray(int[] ar) {
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
        addAllQuestions();
        thisQuestion = questions.get(currentQuestion);
        setQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean goToNextQuestion = true;
                if (!checkScoreRadio() || !checkScoreEditText() || !checkScoreCheckBox()) {
                    goToNextQuestion = false;
                }

                if (goToNextQuestion) {
                    if ((currentQuestion + 1) == numQuestions) {
                        myToast(getString(R.string.you_got) + " " + score + " " + getString(R.string.out_of) + " " + numQuestions);
                        setQuestion();
                    } else {
                        currentQuestion++;
                        setQuestion();
                    }
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion <= 0) {
                    myToast(getString(R.string.no_more_questions));
                } else {
                    currentQuestion--;
                }
                setQuestion();
            }
        });

    }

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
    }

    private void setQuestion() {
        thisQuestion = questions.get(currentQuestion);
        int[] answers = thisQuestion.getAnswerArray();
        shuffleArray(answers);

        questionNumber.setText(getString(R.string.question) + " " + (currentQuestion + 1));
        questionImage.setImageResource(thisQuestion.getImageResId());

        checkOptionA.setChecked(false);
        checkOptionB.setChecked(false);
        checkOptionC.setChecked(false);
        checkOptionD.setChecked(false);
        radioOptionA.setChecked(false);
        radioOptionB.setChecked(false);
        radioOptionC.setChecked(false);
        radioOptionD.setChecked(false);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        llGivenAnswer.setVisibility(View.GONE);
        checkBoxGroup.setVisibility(View.GONE);
        editTextGroup.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);
        if (!thisQuestion.getAnswerGiven().equals("")) {
            if (thisQuestion.isAnswerCorrect()) {
                tvGivenAnswer.setText(getString(R.string.you_were_right) + getString(R.string.its_a) + " " + getString(thisQuestion.getAnswer1()));
            } else {
                String correctAnswer = "";
                switch (thisQuestion.getCorrectAnswers()) {
                    case 4:
                        correctAnswer += getString(thisQuestion.getAnswer4()) + ", ";
                    case 3:
                        correctAnswer += getString(thisQuestion.getAnswer3()) + ", ";
                    case 2:
                        correctAnswer += getString(thisQuestion.getAnswer2()) + ", ";
                    case 1:
                        correctAnswer += getString(thisQuestion.getAnswer1()) + ", ";
                }
                tvGivenAnswer.setText(getString(R.string.you_were_wrong) + " " + thisQuestion.getAnswerGiven() +
                        getString(R.string.its_a) + " " + correctAnswer.substring(0, correctAnswer.length() - 2));
            }
            llGivenAnswer.setVisibility(View.VISIBLE);

        } else if (thisQuestion.mRadioGroup != null) {
            radioOptionA.setText(answers[0]);
            radioOptionB.setText(answers[1]);
            radioOptionC.setText(answers[2]);
            radioOptionD.setText(answers[3]);
            setRadioButtons(thisQuestion.mAnswerGiven.equals(""));

            radioGroup.setVisibility(View.VISIBLE);

        } else if (thisQuestion.mCheckBox != null) {
            checkOptionA.setText(answers[0]);
            checkOptionB.setText(answers[2]);
            checkOptionC.setText(answers[2]);
            checkOptionD.setText(answers[3]);
            setCheckboxes(thisQuestion.mAnswerGiven.equals(""));

            checkBoxGroup.setVisibility(View.VISIBLE);

        } else if (thisQuestion.mEditText != null) {
            if (thisQuestion.mAnswerGiven.equals("")) {
                editTextAnswer.setText("");
                setEditText(true);
            } else {
                editTextAnswer.setText(thisQuestion.getEditTextAnswer());
                setEditText(false);
            }

            editTextGroup.setVisibility(View.VISIBLE);

        }

    }

    private boolean checkScoreRadio() {
        if (thisQuestion.getRadioGroup() != null && thisQuestion.getAnswerGiven().equals("")) {
            if (radioGroup.getCheckedRadioButtonId() != -1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                thisQuestion.setAnswerGiven(radioButton.getText().toString());
                if (thisQuestion.getAnswerGiven().equals(getString(thisQuestion.getAnswer1()))) {
                    score++;
                    thisQuestion.setAnswerCorrect(true);
                }
            } else {
                myToast("You haven\'t answered this question");
                return false;
            }
        }
        return true;
    }

    private void myToast(String message) {
        Toast.makeText(QuizActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean checkScoreEditText() {
        if (thisQuestion.getEditText() != null && !thisQuestion.getAnswerGiven().equals("")) {
            if (editTextAnswer.getText().toString().equals("")) {
                thisQuestion.setAnswerGiven(editTextAnswer.getText().toString());
                if (thisQuestion.getAnswerGiven().equalsIgnoreCase(getString(thisQuestion.getAnswer1()))) {
                    score++;
                    thisQuestion.setAnswerCorrect(true);
                }
            } else {
                myToast("You haven\'t answered this question");
                return false;
            }
        }
        return true;
    }

    private boolean checkScoreCheckBox() {
        if (thisQuestion.getCheckBox() != null && thisQuestion.getAnswerGiven().equals("")) {
            String given = " ";
            double answers = 0;
            boolean wrong = false;

            if (checkOptionA.isChecked()) {
                given += checkOptionA.getText() + ", ";
                if (checkOptionA.getText().equals(getString(thisQuestion.getAnswer1())) ||
                        checkOptionA.getText().equals(getString(thisQuestion.getAnswer1()))) {
                    answers++;
                } else {
                    wrong = true;
                }
            }
            if (checkOptionB.isChecked()) {
                given += checkOptionB.getText() + ", ";
                if (checkOptionB.getText().equals(getString(thisQuestion.getAnswer1())) ||
                        checkOptionB.getText().equals(getString(thisQuestion.getAnswer1()))) {
                    answers++;
                } else {
                    wrong = true;
                }
            }
            if (checkOptionC.isChecked()) {
                given += checkOptionC.getText() + ", ";
                if (checkOptionC.getText().equals(getString(thisQuestion.getAnswer1())) ||
                        checkOptionC.getText().equals(getString(thisQuestion.getAnswer1()))) {
                    answers++;
                } else {
                    wrong = true;
                }
            }
            if (checkOptionD.isChecked()) {
                given += checkOptionD.getText() + ", ";
                if (checkOptionD.getText().equals(getString(thisQuestion.getAnswer1())) ||
                        checkOptionD.getText().equals(getString(thisQuestion.getAnswer1()))) {
                    answers++;
                } else {
                    wrong = true;
                }
            }

            if (answers == 0 && wrong == false) {
                myToast("You haven\'t answered this question");
                return false;
            }

            thisQuestion.setAnswerGiven(given.substring(0, given.length() - 2));
            if (answers == thisQuestion.getCorrectAnswers() && wrong == false) {
                score++;
                thisQuestion.setAnswerCorrect(true);
            }
        }
        return true;
    }

    private void addAllQuestions() {
        questions.add(new Question(R.drawable.labrador, R.string.labrador, R.string.golden_lab,
                R.string.bull_mastiff, R.string.dalmatian, 2, checkBoxGroup));

        questions.add(new Question(R.drawable.westie, R.string.westie, R.string.cairn_terrier,
                R.string.alaskan_malamute, R.string.chowchow, ANS1, radioGroup));

        questions.add(new Question(R.drawable.boxer, R.string.boxer, ANS1, editTextGroup));

        questions.add(new Question(R.drawable.alaskan_malamute, R.string.alaskan_malamute, R.string.american_eskimo,
                R.string.husky, R.string.chihuahua, ANS1, radioGroup));

        questions.add(new Question(R.drawable.chowchow, R.string.chowchow, R.string.cairn_terrier,
                R.string.alaskan_malamute, R.string.poodle, ANS1, radioGroup));


        numQuestions = questions.size();
    }

    private void setCheckboxes(boolean enabled) {
        checkOptionA.setEnabled(enabled);
        checkOptionB.setEnabled(enabled);
        checkOptionC.setEnabled(enabled);
        checkOptionD.setEnabled(enabled);
    }

    private void setRadioButtons(boolean enabled) {
        radioOptionA.setEnabled(enabled);
        radioOptionB.setEnabled(enabled);
        radioOptionC.setEnabled(enabled);
        radioOptionD.setEnabled(enabled);
    }

    private void setEditText(boolean enabled) {
        editTextAnswer.setEnabled(enabled);
    }
}
