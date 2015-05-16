package com.crossover.techtrial.component;

import com.crossover.techtrial.model.*;
import com.crossover.techtrial.repository.GradesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * This component implements test examination process business logic and holds process current state.
 */
@Component
@Scope("session")
public class SubmissionStateHolder implements Serializable
{
    private final Logger logger = LoggerFactory.getLogger(SubmissionStateHolder.class);

    @Autowired
    private GradesRepository gradesRepository;

    private Date started;
    private Date finished;
    private long maxTime;

    private User user;
    private TestExam exam;

    private Map<Integer, Question> orderedQuestions = new HashMap<>();
    private Map<Integer, Integer[]> examResult = new HashMap<>();

    private int currentQuestion = 0;

    public SubmissionStateHolder() {
    }

    /**
     * Start test process
     * @param exam test exam
     * @param user user that try to pass test
     * @return <code>true</code> when test process started properly, <code>false</code> otherwise (wrong input params, user is already passed the test etc.)
     */
    @Transactional
    public boolean startExam(TestExam exam, User user)
    {
        if ((exam == null) || (user == null) || (gradesRepository.findByExamAndUser(exam, user) != null)) return false;

        clear();

        this.exam = exam;
        this.user = user;

        int counter=0;

        for (Question question : exam.getQuestions())
        {
            orderedQuestions.put(counter++, question);
        }

        started = new Date();
        maxTime = started.getTime() + exam.getDuration() * 1000;

        return true;
    }

    /**
     * Stop test process, calculate results and store it in database.
     */
    public void stopExam()
    {
        if ((exam == null) || (user == null)) return;

        finished = new Date();

        // calculate results
        int totalScore = 0;

        int qCounter = 0;

        for (Question question : exam.getQuestions())
        {
            Integer[] questionResults = examResult.get(qCounter++);

            if (questionResults == null) continue;

            if (question.getMultiple())
            {
                Map<Integer, Answer> orderedAnswers = new HashMap<>();

                int aCounter = 0;
                for (Answer answer : question.getAnswers())
                {
                    orderedAnswers.put(aCounter++, answer);
                }

                boolean isCorrect = true;

                // check that only all answer results are valid
                for (Integer questionResult : questionResults)
                {
                    Answer selectedAnswer = orderedAnswers.remove(questionResult);
                    isCorrect = (selectedAnswer != null) && selectedAnswer.getValid();
                    if (!isCorrect) break;
                }

                if (!isCorrect) continue;

                // check that only wrong answers remain
                for (Integer answer : orderedAnswers.keySet())
                {
                    isCorrect = !orderedAnswers.get(answer).getValid();
                    if (!isCorrect) break;
                }

                if (!isCorrect) continue;
            }
            else
            {
                if ((questionResults == null) || questionResults.length != 1) continue;
                    else if (!question.getAnswers().get(questionResults[0]).getValid()) continue;
            }

            totalScore++;
        }

        gradesRepository.saveAndFlush(new Grade(user, exam, started, finished, totalScore, totalScore >= exam.getPassScore()));

        clear();
    }

    /**
     * Get current question number
     * @return current question number
     */
    public int getCurrentQuestion() {
        return currentQuestion;
    }

    /**
     * Get total questions number
     * @return total questions number
     */
    public int getTotalQuestions() {
        return orderedQuestions.size();
    }

    /**
     * Get test expiration time
     * @return test expiration time
     */
    public long getMaxTime() {
        return maxTime;
    }

    /**
     * Get current test exam
     * @return current test exam object
     */
    public TestExam getExam() {
        return exam;
    }

    /**
     * Get user that tries to pass test
     * @return user object
     */
    public User getUser() {
        return user;
    }

    /**
     * Check if test time is expired.
     * @return <code>true</code> when test time is expired, <code>false</code> otherwise
     */
    public boolean verifySubmissionFinalTime()
    {
        if (isEmpty()) return false;

        if ((finished != null) || maxTime < System.currentTimeMillis())
        {
            if (finished == null)
            {
                stopExam();
            }

            clear();

            return false;
        }

        return true;
    }

    /**
     * Submit answer(s) on current question and go to next question.
     * @param answers answer choice indexes
     * @return next question object or <code>null</code> when no more questions are available
     */
    public Question submitAnswer(Integer[] answers)
    {
        if (exam == null) return null;

        if (currentQuestion == orderedQuestions.size()) return null;

        examResult.remove(currentQuestion);

        if ((answers != null) && (answers.length > 0))
        {
            examResult.put(currentQuestion, answers);
        }

        return orderedQuestions.get(++currentQuestion);
    }

    /**
     * Go to custom question
     * @param num index of question
     * @return question object or <code>null</code> when question with such index is unavailable
     */
    public Question gotoQuestion(int num)
    {
        if ((exam == null) || (num < 0) || (num >= orderedQuestions.size()))
        {
            return null;
        }

        return orderedQuestions.get(currentQuestion = num);
    }

    /**
     * Clear current state
     */
    public void clear()
    {
        exam = null;
        user = null;
        started = null;
        finished = null;
        maxTime = 0;

        examResult.clear();
        orderedQuestions.clear();

        currentQuestion = 0;
    }

    /**
     * Check if ptest process is started.
     * @return <code>true</code> when process is active and <code>false</code> otherwise
     */
    public boolean isEmpty()
    {
        return exam == null;
    }

    /**
     * Get list of not answered questions.
     * @return list of not answered questions.
     */
    public List<Question> getMissedQuestions()
    {
        if (isEmpty()) return null;

        List<Question> questions = new ArrayList<>(exam.getQuestions().size());


        for (int counter=0; counter < exam.getQuestions().size(); counter++)
        {
            if (!examResult.containsKey(counter)) questions.add(exam.getQuestions().get(counter));
        }

        return questions;
    }
}
