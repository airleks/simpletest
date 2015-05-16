package com.crossover.techtrial.component;

import com.crossover.techtrial.model.Answer;
import com.crossover.techtrial.model.Question;
import com.crossover.techtrial.model.TestExam;
import com.crossover.techtrial.model.User;
import com.crossover.techtrial.repository.AnswersRepository;
import com.crossover.techtrial.repository.ExamsRepository;
import com.crossover.techtrial.repository.QuestionsRepository;
import com.crossover.techtrial.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/**
 * Generate test data.
 */
@Component
@Profile("development")
public class Bootstrap implements InitializingBean
{
    private final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ExamsRepository examsRepository;

    @Autowired
    QuestionsRepository questionsRepository;

    @Autowired
    AnswersRepository answersRepository;

    private final Random random = new Random();

    @Override
    @Transactional()
    public void afterPropertiesSet() throws Exception {
        logger.info("Bootstrap data uploading started...");

        createUsers();
        createExams();
        logger.info("...Bootstrap data uploading complete");
    }

    private void createUsers()
    {
        if (usersRepository.count() > 0) return;

        for (int i=1; i<=5; i++)
        {
            usersRepository.saveAndFlush(new User("user" + i, "User " + i, "password"));
        }
    }

    private void createExams()
    {
        if (examsRepository.count() > 0) return;

        int total = 1 + random.nextInt(15);

        for (int i=1; i<=total; i++)
        {
            int totalQuestions = 1 + random.nextInt(10);

            TestExam exam = examsRepository.saveAndFlush(new TestExam("Test " + i, "Description " + i, totalQuestions/2, totalQuestions, random.nextInt(20) + 15));
            fillWithQuestions(exam, totalQuestions);
        }
    }

    private void fillWithQuestions(TestExam exam, int total)
    {
        for (int i=1; i<=total; i++)
        {
            Question question = questionsRepository.saveAndFlush(new Question(exam, "Topic " + i, "Content " + i, random.nextBoolean(), i));
            fillWithAnswers(question);
        }
    }

    private void fillWithAnswers(Question question)
    {
        int total = 2 + random.nextInt(5);

        for (int i=1; i<=total; i++)
        {
            answersRepository.saveAndFlush(new Answer(question, "Topic " + i, i, i == 1));
        }
    }
}
