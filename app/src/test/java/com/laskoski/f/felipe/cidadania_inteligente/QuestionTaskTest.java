package com.laskoski.f.felipe.cidadania_inteligente;

import android.util.Log;

import com.laskoski.f.felipe.cidadania_inteligente.model.QuestionTask;

import org.junit.Before;
import org.junit.Test;

import java.io.Console;
import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class QuestionTaskTest {
    ArrayList<String> answers;

    @Before
    public void setup(){
        answers = new ArrayList<>();
        answers.add("ans1");answers.add("ans2"); answers.add("ans3");
    }
    @Test(expected = InvalidParameterException.class)
    public void parameterCorrectAnswerTooHigh() throws Exception {
        new QuestionTask("test title", "question test", answers, 4);
    }
    @Test(expected = InvalidParameterException.class)
    public void parameterCorrectAnswerTooLow() throws Exception {
        new QuestionTask("test title", "question test", answers, 0);
    }
    @Test(expected = InvalidParameterException.class)
    public void parameterAnswersTooBig() throws Exception {
        answers.addAll(Arrays.asList("ans4", "ans5", "ans6", "ans7"));
        new QuestionTask("test title", "question test", answers, 2);
    }
    @Test(expected = InvalidParameterException.class)
    public void parameterAnswersTooSmall() throws Exception {
        answers.clear();
        answers.add("ansOnly");
        new QuestionTask("test title", "question test", answers, 1);
    }

}