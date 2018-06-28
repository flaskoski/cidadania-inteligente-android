package com.laskoski.f.felipe.cidadania_inteligente.model;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by Felipe on 11/25/2017.
 */

public class QuestionTask extends AbstractTask {
    private String question;

    /**
     *
     * @param title - Title of the task
     * @param question
     * @param answers
     * @param timeToAnswer
     * @param correctAnswer - Answer choice that is correct, starting by 1.
     */
    public QuestionTask(String title, String question, List<String> answers, Integer correctAnswer, Integer timeToAnswer) {
        this.question = question;

        checkIfParametersAreValid(title, question, answers, correctAnswer);

        this.answers = answers;
        this.timeToAnswer = timeToAnswer;
        this.correctAnswer = correctAnswer;
        this.title = title;
    }

    private List<String> answers;
    private Integer timeToAnswer;
    private Integer correctAnswer;

    public Boolean getAnsweredCorrectly() {
        return answeredCorrectly;
    }

    public void setAnsweredCorrectly(Boolean answeredCorrectly) {
        this.answeredCorrectly = answeredCorrectly;
    }

    private Boolean answeredCorrectly;
    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    private List<String> getAnswers(List<String> answers){
        return answers;
    }

    /**
     *
     * @param title - Title of the task
     * @param question
     * @param answers
     * @param correctAnswer - Answer choice that is correct, starting by 1.
     */
    public QuestionTask(String title, String question, List<String> answers, Integer correctAnswer) {
        checkIfParametersAreValid(title, question, answers, correctAnswer);

        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.timeToAnswer = AbstractTask.TIMER_OFF;
        this.title = title;
    }

    public QuestionTask(){

    }

    private void checkIfParametersAreValid(String title, String question, List<String> answers, Integer correctAnswer){
        if(answers.size() < 2 || answers.size() > 6 || (correctAnswer < 1 || correctAnswer > 6))
            throw new InvalidParameterException("Number of answers must be between 2 and 6!");
        if(correctAnswer > answers.size() )
            throw new InvalidParameterException("Correct Answer doesn't exist!");

    }
    @Override
    public String getType() {
        return "Pergunta";
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
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
