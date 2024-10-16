package g56583.esi.atl.model;


import g56583.esi.atl.model.validator.Validator;
import g56583.esi.atl.model.validator.ValidatorFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A class responsible for reading problem data from a CSV file.
 * This class parses lines of a CSV file to create Problem objects with associated difficulty, luck,
 * code, and validators.
 */
public class CSVReader {
    /**
     * Reads problems from a specified CSV file path and constructs a list of Problem objects.
     * Each line in the CSV file represents a problem with its attributes: difficulty, luck, code, and validators.
     *
     * @param filepath The path to the CSV file containing problem data.
     * @return A list of Problem objects read from the CSV file.
     */
    public List<Problem> readProblems(String filepath)  {
        List<Problem> problems = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] values = line.split(",");
                int difficulty = Integer.parseInt(values[1]);
                int luck = Integer.parseInt(values[2]);
                Code secretCode = new Code(values[3]);
                List<Validator> validators = getValidatorsForProblem(values);
                problems.add(new Problem(secretCode, validators, difficulty, luck));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return problems;
    }

    /**
     * Constructs a list of Validators based on the provided values from a CSV file line.
     *
     * @param values An array of strings representing the attributes of a problem, including validators.
     * @return A list of Validator objects for the problem.
     */
    private List<Validator> getValidatorsForProblem(String[] values) {
        List<Validator> validators = new ArrayList<>();
        for (int i = 4; i < values.length; i++) {
            int validatorNo = Integer.parseInt(values[i]);
            Validator validator = ValidatorFactory.getValidator(validatorNo);
            validators.add(validator);
        }
        return validators;
    }
}
