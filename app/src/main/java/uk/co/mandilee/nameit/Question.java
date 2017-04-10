package uk.co.mandilee.nameit;

import android.widget.LinearLayout;
import android.widget.RadioGroup;

class Question {

    LinearLayout mCheckBox, mEditText;
    RadioGroup mRadioGroup;
    private int mImageResId;
    private int mAnswer1;
    private int mAnswer2;
    private int mAnswer3;
    private int mAnswer4;
    private int mCorrectAnswers;
    private String mAnswerGiven = "";
    private boolean mAnswerCorrect = false;

    Question(int imageResId, int answer1, int answer2, int answer3, int answer4, int correctAnswers, LinearLayout checkBox) {
        mImageResId = imageResId;
        mAnswer1 = answer1;
        mAnswer2 = answer2;
        mAnswer3 = answer3;
        mAnswer4 = answer4;
        mCorrectAnswers = correctAnswers;
        mCheckBox = checkBox;
    }

    Question(int imageResId, int answer1, int answer2, int answer3, int answer4, RadioGroup radioGroup) {
        mImageResId = imageResId;
        mAnswer1 = answer1;
        mAnswer2 = answer2;
        mAnswer3 = answer3;
        mAnswer4 = answer4;
        mCorrectAnswers = QuizActivity.ANS1;
        mRadioGroup = radioGroup;
    }

    Question(int imageResId, int answer1, LinearLayout editText) {
        mImageResId = imageResId;
        mAnswer1 = answer1;
        mCorrectAnswers = QuizActivity.ANS1;
        mEditText = editText;
    }

    int getImageResId() {
        return mImageResId;
    }

    int getAnswer1() {
        return mAnswer1;
    }

    int getAnswer2() {
        return mAnswer2;
    }

    int getAnswer3() {
        return mAnswer3;
    }

    int getAnswer4() {
        return mAnswer4;
    }

    int getCorrectAnswers() {
        return mCorrectAnswers;
    }

    String getAnswerGiven() {
        return mAnswerGiven;
    }

    void setAnswerGiven(String answerGiven) {
        mAnswerGiven = answerGiven;
    }

    LinearLayout getCheckBox() {
        return mCheckBox;
    }

    LinearLayout getEditText() {
        return mEditText;
    }

    RadioGroup getRadioGroup() {
        return mRadioGroup;
    }

    boolean isAnswerCorrect() {
        return mAnswerCorrect;
    }

    void setAnswerCorrect() {
        mAnswerCorrect = true;
    }

    int[] getAnswerArray() {
        return new int[]{mAnswer1, mAnswer2, mAnswer3, mAnswer4};
    }
}
