package uk.co.mandilee.nameit;

import android.widget.LinearLayout;
import android.widget.RadioGroup;

class Question {

    int mImageResId, mFunFactResId;
    int mAnswer1, mAnswer2, mAnswer3, mAnswer4;
    int mCorrectAnswers;
    String mAnswerGiven = "";
    LinearLayout mCheckBox, mEditText;
    RadioGroup mRadioGroup;
    boolean mAnswerCorrect = false;
    String mEditTextAnswer;

    public Question(int imageResId, int answer1, int answer2, int answer3, int answer4, int correctAnswers, LinearLayout checkBox) {
        mImageResId = imageResId;
        mAnswer1 = answer1;
        mAnswer2 = answer2;
        mAnswer3 = answer3;
        mAnswer4 = answer4;
        mCorrectAnswers = correctAnswers;
        mCheckBox = checkBox;
    }

    public Question(int imageResId, int answer1, int answer2, int answer3, int answer4, int correctAnswer, RadioGroup radioGroup) {
        mImageResId = imageResId;
        mAnswer1 = answer1;
        mAnswer2 = answer2;
        mAnswer3 = answer3;
        mAnswer4 = answer4;
        mCorrectAnswers = correctAnswer;
        mRadioGroup = radioGroup;
    }

    public Question(int imageResId, int answer1, int correctAnswer, LinearLayout editText) {
        mImageResId = imageResId;
        mAnswer1 = answer1;
        mCorrectAnswers = correctAnswer;
        mEditText = editText;
    }

    public int getImageResId() {
        return mImageResId;
    }

    public int getFunFactResId() {
        return mFunFactResId;
    }

    public int getAnswer1() {
        return mAnswer1;
    }

    public int getAnswer2() {
        return mAnswer2;
    }

    public int getAnswer3() {
        return mAnswer3;
    }

    public int getAnswer4() {
        return mAnswer4;
    }

    public int getCorrectAnswers() {
        return mCorrectAnswers;
    }

    public String getAnswerGiven() {
        return mAnswerGiven;
    }

    public void setAnswerGiven(String answerGiven) {
        mAnswerGiven = answerGiven;
    }

    public LinearLayout getCheckBox() {
        return mCheckBox;
    }

    public LinearLayout getEditText() {
        return mEditText;
    }

    public RadioGroup getRadioGroup() {
        return mRadioGroup;
    }

    public boolean isAnswerCorrect() {
        return mAnswerCorrect;
    }

    public void setAnswerCorrect(boolean answerCorrect) {
        mAnswerCorrect = answerCorrect;
    }

    public String getEditTextAnswer() {
        return mEditTextAnswer;
    }

    public void setEditTextAnswer(String editTextAnswer) {
        mEditTextAnswer = editTextAnswer;
    }

    public int[] getAnswerArray() {
        return new int[]{mAnswer1, mAnswer2, mAnswer3, mAnswer4};
    }
}
