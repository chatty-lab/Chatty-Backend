package com.chatty.utils.check;

import com.chatty.constants.Nickname;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CheckUtils {

    public static List<String> createNicknameProblem(String answer){
        List<String> problems = new ArrayList<>();
        List<String> nicknames = Nickname.value;
        problems.add(answer);
        for (int i = 0; i < 4; i++) {
            while(true){
                String selectedValue = nicknames.get(new Random().nextInt(10));
                if(!nicknames.contains(selectedValue)){
                    problems.add(selectedValue);
                    break;
                }
            }
        }

        return shuffleProblem(problems);
    }

    public static List<String> createBirthProblem(LocalDate answer){
        List<String> problems = new ArrayList<>();
        int year = answer.getYear();
        
        String problem = String.valueOf(year);
        problems.add(String.valueOf(year));

        for (int i = 0; i < 4; i++) {
            while(true){
                String selectedValue = problem.substring(0,problem.length()-1) + new Random().nextInt(10);
                if(!problems.contains(selectedValue)){
                    problems.add(selectedValue);
                    break;
                }
            }
        }

        return shuffleProblem(problems);
    }

    private static List<String> shuffleProblem(List<String> problems){
        Collections.shuffle(problems);
        return problems;
    }
}
