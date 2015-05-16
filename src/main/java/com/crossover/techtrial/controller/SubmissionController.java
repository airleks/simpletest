package com.crossover.techtrial.controller;

import com.crossover.techtrial.component.SubmissionStateHolder;
import com.crossover.techtrial.model.Question;
import com.crossover.techtrial.model.TestExam;
import com.crossover.techtrial.model.User;
import com.crossover.techtrial.repository.ExamsRepository;
import com.crossover.techtrial.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This controller provides test process support. It covers question and test end page.
 */
@Controller
@Scope("session")
public class SubmissionController implements Serializable
{
    @Autowired
    SubmissionStateHolder submissionStateHolder;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ExamsRepository examsRepository;


    /**
     * Submit test exam pass process: validate user and start test process
     * @param examId test exam id
     * @param username user name
     * @param password user pasword
     * @param model view model
     * @return view name
     */
    @RequestMapping(value = "/exam/{examId}", method = RequestMethod.POST)
    private String startExam(@PathVariable("examId") Long examId,
                             @RequestParam(value = "username", required = true) String username,
                             @RequestParam(value = "password", required = true) String password,
                             Model model)
    {
        TestExam exam = examsRepository.findOne(examId);

        if (exam == null)
        {
            return "exams";
        }

        User user = usersRepository.findByUsernameAndPassword(username, password);

        if (user == null)
        {
            model.addAttribute("exam", exam);
            model.addAttribute("error", "Invalid credentials");
            return "exam";
        }

        return submissionStateHolder.startExam(exam, user) ? "redirect:/submission?q=1" : "redirect:/exam/" + examId;
    }

    /**
     * Supports navigation to custom page and submit of answers of current question (depends on 'q' parameter exsistance).
     * @param questionNum question index
     * @param answers selected answer indexes
     * @param model view model
     * @return view name
     */
    @RequestMapping(value = "/submission")
    private String getQuestion(@RequestParam(value = "q", required = false) Integer questionNum,
                               @RequestParam(value = "answers", required = false) Integer[] answers, Model model)
    {
        if (!submissionStateHolder.verifySubmissionFinalTime()) return "redirect:/submission/send";

        Question question;

        if (questionNum != null)
        {
            if ((question = submissionStateHolder.gotoQuestion(questionNum-1)) == null)
            {
                return "redirect:/submission/send";
            }
        }
        else if ((question = submissionStateHolder.submitAnswer(answers)) == null)
        {
            return "redirect:/submission/send";
        }

        model.addAttribute("exam", submissionStateHolder.getExam());
        model.addAttribute("user", submissionStateHolder.getUser());
        model.addAttribute("question", question);
        model.addAttribute("current", submissionStateHolder.getCurrentQuestion() + 1);
        model.addAttribute("total", submissionStateHolder.getTotalQuestions());
        model.addAttribute("time_left", (submissionStateHolder.getMaxTime() - System.currentTimeMillis()) / 1000);

        return "question";
    }

    /**
     * Check if current test process is expired.
     * @return empty json when test process is still active and {"expired":"true"} otherwise.
     */
    @RequestMapping(value = "/submission/check", method = RequestMethod.GET, produces = "application/json")
    private @ResponseBody
    Map checkTimeLeft()
    {
        Map<String, String> result = new HashMap<>();

        if (!submissionStateHolder.verifySubmissionFinalTime())
        {
            result.put("expired", "true");
        }

        return result;
    }

    /**
     * Navigate to test end page. If there is no active process at the moment, redirect to homepage.
     * @param model test model
     * @return view name
     */
    @RequestMapping(value = "/submission/send", method = RequestMethod.GET)
    private String examResults(Model model)
    {
        if (submissionStateHolder.isEmpty())
        {
            return "redirect:/";
        }

        model.addAttribute("exam", submissionStateHolder.getExam());
        model.addAttribute("missed_questions", submissionStateHolder.getMissedQuestions());

        return "submit_exam";
    }

    /**
     * Submit test exam results. Stop test process, calculate results and return to homepage.
     * @return homepage view name
     */
    @RequestMapping(value = "/submission/send", method = RequestMethod.POST)
    private String stopExam()
    {
        submissionStateHolder.stopExam();
        return "redirect:/";
    }
}
