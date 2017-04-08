package uk.co.mandilee.nameit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private RadioButton answer1a, answer1b, answer1c;
    private EditText answer2;
    private RadioButton answer3a, answer3b, answer3c;
    private CheckBox answer4a, answer4b, answer4c;
    private Button submitButton;

    private int correct = 0, num_questions = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setVariables();

        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;
                if (checkQuestion1() && checkQuestion2() && checkQuestion3() && checkQuestion4()) {
                    message = getString(R.string.you_got) + " " + correct + " " + getString(R.string.out_of) + " " + num_questions + " " + getString(R.string.right);
                } else {
                    message = getString((R.string.missing_answers));
                }
                Toast.makeText(QuizActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setVariables() {
        // question 1
        answer1a = (RadioButton) findViewById(R.id.answer1a);
        answer1b = (RadioButton) findViewById(R.id.answer1b);
        answer1c = (RadioButton) findViewById(R.id.answer1c);

        // question2
        answer2 = (EditText) findViewById(R.id.answer2);

        // question 3
        answer3a = (RadioButton) findViewById(R.id.answer3a);
        answer3b = (RadioButton) findViewById(R.id.answer3b);
        answer3c = (RadioButton) findViewById(R.id.answer3c);

        // question 4
        answer4a = (CheckBox) findViewById(R.id.answer4a);
        answer4b = (CheckBox) findViewById(R.id.answer4b);
        answer4c = (CheckBox) findViewById(R.id.answer4c);
    }

    private boolean checkQuestion1() {
        num_questions += 1; // count number of questions
        if (answer1a.isChecked()) {
            correct += 1;
            return true;
        } else if (answer1b.isChecked() || answer1c.isChecked()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkQuestion2() {
        num_questions += 1; // count number of questions
        String userAnswer = String.valueOf(answer2.getText());
        if (userAnswer.length() > 0) {
            if (userAnswer.equals(getString(R.string.question_2_answer))) {
                correct += 1;
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean checkQuestion3() {
        num_questions += 1; // count number of questions
        if (answer3a.isChecked()) {
            correct += 1;
            return true;
        } else if (answer3b.isChecked() || answer3c.isChecked()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkQuestion4() {
        num_questions += 1; // count number of questions
        if (answer4a.isChecked() && answer4c.isChecked() && !answer4b.isChecked()) {
            correct += 1;
        }
        if (answer4a.isChecked() || answer4b.isChecked() || answer4c.isChecked()) {
            return true;
        } else {
            return false;
        }
    }
}
