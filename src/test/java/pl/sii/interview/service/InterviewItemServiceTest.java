package pl.sii.interview.service;

import org.junit.jupiter.api.Test;
import pl.sii.interview.demo.model.InterviewItem;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InterviewItemServiceTest {

    @Test
    void embeddedClasses_creation() {
        Object interviewQuestion = null;
        Object interviewAnswer = null;

        /* <ANSWER> */
            interviewQuestion = new InterviewItem.InterviewQuestion();
            interviewAnswer = new InterviewItem(null, null, null).new InterviewAnswer();
        /* </ANSWER> */

        assertEquals(interviewQuestion.getClass(), InterviewItem.InterviewQuestion.class);
        assertEquals(interviewAnswer.getClass(), InterviewItem.InterviewAnswer.class);
    }

    final InterviewItem[] interviewItemsArray = new InterviewItem[]{
            InterviewItem.builder().question("Abc ?").answer("Xyz").build(),
            InterviewItem.builder().question("Abc ?").answer("Good").build(),
            InterviewItem.builder().question("Abc ?").answer("Good").build(),
            InterviewItem.builder().question("Abc ??").answer("Maybe C?").build(),
            InterviewItem.builder().question("Abc ?").answer(null).build(),
            InterviewItem.builder().question("Is this real?").answer("Maybe Yes").build(),
            InterviewItem.builder().question("Is this real?").answer("Maybe No").build(),
    };

    @Test
    void arrayItems_filtering() {
        List<InterviewItem> resultList = emptyList();

        /* TODO create a list containing all InterviewItem objects from the interviewItemsArray, for which
           - the answer property contains 'y' character
           - the question property ends with a single '?' character and
        */

        /* <ANSWER> */
        resultList = stream(interviewItemsArray)
                .filter(item -> Optional.ofNullable(item.getAnswer()).map(answer -> answer.contains("y")).orElse(false))
                .filter(item -> item.getQuestion().endsWith("?"))
                .filter(item -> !item.getQuestion().endsWith("??"))
                .collect(toList());
        /* </ANSWER> */

        assertThat(resultList).hasSize(3);
        assertThat(resultList).contains(InterviewItem.builder().question("Abc ?").answer("Xyz").build());
        assertThat(resultList).contains(InterviewItem.builder().question("Is this real?").answer("Maybe Yes").build());
        assertThat(resultList).contains(InterviewItem.builder().question("Is this real?").answer("Maybe No").build());
    }

    @Test
    void arrayItems_toMap_uniqueEntries() {
        Map<String, ? extends Collection<String>> resultMap = emptyMap();

        /* TODO from the interviewItemsArray create a map {key: question, answers: [answer1, answer2]}
           - questions should be unique
           - answers to the questions should be unique
        */

        /* <ANSWER> */
        resultMap = stream(interviewItemsArray)
                .collect(groupingBy(
                        InterviewItem::getQuestion,
                        mapping(InterviewItem::getAnswer, toSet())
                ));
        /* </ANSWER> */

        assertThat(resultMap).hasSize(3);
        assertThat(resultMap).containsKeys("Abc ?", "Abc ??", "Is this real?");
        assertThat(resultMap.get("Abc ?"))
                .hasSize(3)
                .contains("Xyz", "Good", null);
        assertThat(resultMap.get("Abc ??"))
                .hasSize(1)
                .contains("Maybe C?");
        assertThat(resultMap.get("Is this real?"))
                .hasSize(2)
                .contains("Maybe Yes", "Maybe No");
    }

}
