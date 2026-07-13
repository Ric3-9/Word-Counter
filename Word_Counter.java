import java.util.*;
import java.io.*;
import java.util.stream.Collectors;
public class Word_Counter {
    /* Capitalizes input text (personal preference) */public static String titleCase(String wrd) {
        StringBuilder cap = new StringBuilder();
        boolean toCap = true;
        for (char c : wrd.toCharArray()) {
            if (!Character.isLetter(c)) {
                toCap = false;
                cap.append(c);
            } else if (toCap) {
                cap.append(Character.toUpperCase(c));
                toCap = false;
            } else cap.append(Character.toLowerCase(c));
        }
        return cap.toString();
    }
    public static void main(String[] args) {
        String line;
        int lineCount = 0;
        StringBuilder alphanumeric = new StringBuilder();
        Map<String, Integer> sM = new TreeMap<>();
        Scanner a = new Scanner(System.in);
        System.out.print("Input File Path: ");
        /* Select file to read */ String filePath = a.nextLine();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                /* prints text file */System.out.println(line);
                lineCount++;
                /* replaces non-alphanumeric characters, reinstates contractions */
                alphanumeric.append(line.replaceAll("[^\\p{Alnum}\\p{IsLatin}]+", " ").replaceAll(" re ", "'re ")
                        .replaceAll(" ve ", "'ve ").replaceAll("n t ", "n't ").replaceAll(" m ", "'m ")
                        .replaceAll(" ll ", "'ll ").replaceAll(" s ", "'s ")
                        .replaceAll(" ([A-Z]) ([A-Z]) ([A-Z]) ", " $1.$2.$3. ")
                        .replaceAll(" ([a-z]) ([a-z]) ", " $1.$2. ")).append(" ");
            }
        }catch (IOException e) {
            System.err.println("Error reading file: "+ e.getMessage());
        }
        int count = 0;
        /* capitalization phase */String[] alphaArray = alphanumeric.toString().split("\\s+");
        for (String word : alphaArray) {
            if (word.isBlank()) continue;
            sM.put(titleCase(word), sM.getOrDefault(titleCase(word), 0) + 1);
            count++;
        }
        String wS = "---------";
        System.out.println("\n"+ wS.repeat(5) +"\n");
        System.out.println("The Total Word Count: " + count);
        System.out.println("Unique Word Occurrences: " + sM.size());
        System.out.println("The Total Line Count: " + lineCount);
        System.out.print("\nDo you want the words shown by frequency(f) or alphabetically(a)? ");
        /* determines the order the words are output */String order = a.nextLine();
        /* separates outputs */wS = "\n"+wS;
        switch (order.toLowerCase()) {
            case "frequency", "f":
                System.out.println("Printed by Frequency:"+wS);
                Map<String, Integer> freq = sM.entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new));
                for (String x : freq.keySet()) {
                    System.out.println(x + " (" + (freq.get(x) > 1 ? freq.get(x) + " times" : "once") + ")"+ wS);
                }
                break;
            /* alphabetically */default:
                System.out.println("Printed Alphabetically:"+ wS);
                for (Map.Entry<String, Integer> entry : sM.entrySet()) {
                    System.out.println(entry.getKey() + " (" + (entry.getValue() > 1 ? entry.getValue() + " times" : "once") + ")"+ wS);
                }
        }
        a.close();
    }
}
