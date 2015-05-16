package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Grade;
import com.crossover.techtrial.model.TestExam;
import com.crossover.techtrial.repository.ExamsRepository;
import com.crossover.techtrial.repository.GradesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This controller serves for static pages such as homepage and test exam description page.
 */
@Controller
public class ExamsController
{
    @Autowired
    private ExamsRepository examsRepository;

    @Autowired
    private GradesRepository gradesRepository;

    /**
     * Homepage support. Lists all available test exams with user grades information.
     * @param model view model
     * @return view name
     */
    @RequestMapping("/")
    private String list(Model model)
    {
        Collection<TestExam> exams = examsRepository.findAll();
        model.addAttribute("exams", exams);

        Map<Long, List<Grade>> grades = new HashMap<>();

        for (TestExam exam : exams)
        {
            grades.put(exam.getId(), gradesRepository.findByExamOrderByStartedDesc(exam));
        }

        model.addAttribute("grades", grades);

        return "exams";
    }

    /**
     * Test exam description page support.
     * @param id test exam id
     * @param model view model
     * @return view name
     */
    @RequestMapping(value = "/exam/{id}", method = RequestMethod.GET)
    private String list(@PathVariable("id") Long id, Model model)
    {
        model.addAttribute("exam", examsRepository.findOne(id));
        return "exam";
    }
}
