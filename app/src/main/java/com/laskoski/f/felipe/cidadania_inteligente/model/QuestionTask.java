package com.laskoski.f.felipe.cidadania_inteligente.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by Felipe on 11/25/2017.
 */

public class QuestionTask extends Task {
    private String question;

    /**
     *
     * @param title - Title of the task
     * @param question
     * @param answers
     * @param timeToAnswer
     * @param correctAnswer - Answer choice that is correct, starting by 1.
     */
    public QuestionTask(String title, String question, ArrayList<String> answers, Integer timeToAnswer, Integer correctAnswer) {
        this.question = question;

        if(answers.size() < 2 || answers.size() > 6 || (correctAnswer < 2 || correctAnswer > 6))
            throw new InvalidParameterException("Number of answers must be between 2 and 6!");
        if(correctAnswer > answers.size() )
            throw new InvalidParameterException("Correct Answer doesn't exist!");

        this.answers = answers;
        this.timeToAnswer = timeToAnswer;
        this.correctAnswer = correctAnswer;
        this.title = title;
    }

    private ArrayList<String> answers;
    private Integer timeToAnswer;
    private String title;
    private Integer correctAnswer;

    /**
     *
     * @param title - Title of the task
     * @param question
     * @param answers
     * @param correctAnswer - Answer choice that is correct, starting by 1.
     */
    public QuestionTask(String title, String question, ArrayList<String> answers, Integer correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.timeToAnswer = Task.TIMER_OFF;
        this.title = title;
    }

    public QuestionTask(){

    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String question) {
        this.title = title;
    }

    @Override
    public String getType() {
        return "Pergunta";
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public Integer getTimeToAnswer() {
        return timeToAnswer;
    }

    public void setTimeToAnswer(Integer timeToAnswer) {
        this.timeToAnswer = timeToAnswer;
    }

    public Integer getCorrectAnswer() {return correctAnswer;    }

    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
