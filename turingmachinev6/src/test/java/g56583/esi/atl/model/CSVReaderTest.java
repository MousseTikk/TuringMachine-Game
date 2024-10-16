package g56583.esi.atl.model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CSVReaderTest {

    @Test
    void csvReaderShouldReadProblemsCorrectly() {
        CSVReader reader = new CSVReader();
        String csvFilePath = "src/main/resources/known_problems.csv";
        List<Problem> problems = reader.readProblems(csvFilePath);

        assertFalse(problems.isEmpty(), "The list of problems should not be empty.");

        Problem firstProblem = problems.get(0);
        assertEquals("241", firstProblem.getCode().getCode());
        assertEquals(4, firstProblem.getValidators().size());

    }


}
