package uk.co.mandilee.nameit;

import android.os.Parcel;
import android.os.Parcelable;

class Question implements Parcelable {

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }

        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }
    };
    private int mImageResId;
    private int mAnswer1;
    private int mAnswer2;
    private int mAnswer3;
    private int mAnswer4;
    private int mCorrectAnswers;
    private String mAnswerGiven = "";
    private boolean mAnswerCorrect = false;
    private int mQuestionType;

    Question(int imageResId, int answer1, int answer2, int answer3, int answer4, int correctAnswers, int questionType) {
        mImageResId = imageResId;
        mAnswer1 = answer1;
        mAnswer2 = answer2;
        mAnswer3 = answer3;
        mAnswer4 = answer4;
        mCorrectAnswers = correctAnswers;
        mQuestionType = questionType;
    }

    Question(int imageResId, int answer1, int answer2, int answer3, int answer4, int questionType) {
        mImageResId = imageResId;
        mAnswer1 = answer1;
        mAnswer2 = answer2;
        mAnswer3 = answer3;
        mAnswer4 = answer4;
        mCorrectAnswers = QuizActivity.ANS1;
        mQuestionType = questionType;
    }

    Question(int imageResId, int answer1, int questionType) {
        mImageResId = imageResId;
        mAnswer1 = answer1;
        mCorrectAnswers = QuizActivity.ANS1;
        mQuestionType = questionType;
    }

    private Question(Parcel parcel) {
        mImageResId = parcel.readInt();
        mAnswer1 = parcel.readInt();
        mAnswer2 = parcel.readInt();
        mAnswer3 = parcel.readInt();
        mAnswer4 = parcel.readInt();
        mCorrectAnswers = parcel.readInt();
        mAnswerGiven = parcel.readString();
        mAnswerCorrect = Boolean.valueOf(parcel.readString());
        mQuestionType = parcel.readInt();

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

    int getQuestionType() {
        return mQuestionType;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mImageResId);
        dest.writeInt(mAnswer1);
        dest.writeInt(mAnswer2);
        dest.writeInt(mAnswer3);
        dest.writeInt(mAnswer4);
        dest.writeInt(mCorrectAnswers);
        dest.writeString(mAnswerGiven);
        dest.writeString(String.valueOf(mAnswerCorrect));
        dest.writeInt(mQuestionType);

    }
}
