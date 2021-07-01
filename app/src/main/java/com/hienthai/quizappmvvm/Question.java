package com.hienthai.quizappmvvm;

import com.google.firebase.firestore.DocumentId;

public class Question {

    @DocumentId
    private String questionId;
    private String question, answer, optionA, optionB, optionC;
    private long timer;

    public Question() {
    }

    public Question(String questionId, String question, String answer, String optionA, String optionB, String optionC, long timer) {
        this.questionId = questionId;
        this.question = question;
        this.answer = answer;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.timer = timer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }
}
