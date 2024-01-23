package com.chatty.utils.check;

import com.chatty.constants.Nickname;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckUtils {

    public static List<String> createNicknameProblem(String answer){
        List<String> problem = new ArrayList<>();
        List<String> nicknames = Nickname.value;
        for (int i = 0; i < 4; i++) {
            String selectedValue;
            while(true){
                selectedValue = nicknames.get((int) (Math.random()*99));
                if(nicknames.contains(selectedValue)){
                    break;
                }
            }
            problem.add(selectedValue);
        }

        problem.add(answer);

        return shuffleNicknames(problem);
    }

    private static List<String> shuffleNicknames(List<String> nicknames){
        Collections.shuffle(nicknames);
        return nicknames;
    }
}
