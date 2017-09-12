import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Check for syntactic correctness;
 * **/


class RegexParser {

    /**
     * Parse C statements of the following type:
     * 1) Split string into lines at the following breakpoints: a) '{' b) '}' c) ;
     * 2) There are six types of' lines': 1) Function declaration line
     *                                     2) Variable declaration line
     *                                     3) Variable assignment line
     *                                     4) Function call
     *                                     5) Variable assignment with function call.
     *                                     6) While loop (With only one conditional statement, No && or || etc.)
     * Example: void f() {
     * int x;
     * int y;
     * x = 7;
     * y = 5;
     * }
     *
     * Note this parser cant parse something like a int x = 6;
     */

    public static List<String> parseIntoLines(String x) {
        x = x.replace('\n', ' ');
        x = x.replace("{", "{ \n");
        x = x.replace("}", "} \n");
        x = x.replace("=", " = ");
        x = x.replace("=  =", "==");
        x = x.replace("<", " < ");
        x = x.replace(">", " > ");
        x = x.replace("(", " ( ");
        x = x.replace(")", " ) ");
        String[] lines = x.split("(?=[{}])");
        List<String> lines2 = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim();
            for (String line : lines[i].split("[;\n]"))
                lines2.add(line.trim());
        }
        return lines2;
    }

    public static Pair<Integer, String[]> classifyLine(String y) throws Exception {
        if (y.contains("(")) {
            assert y.contains(")");

            Pattern p = Pattern.compile("\\(\\s*[\\w]*\\s*[\\w]*(([,<>]|==)\\s*[\\w]*\\s*[\\w]*\\s*)?\\)");

            String z2 = y.split("\\(\\s*[\\w]*\\s*[\\w]*(([,<>]|==)\\s*[\\w]*\\s*[\\w]*\\s*)?\\)")[0];

            ArrayList<String> tokens = new ArrayList<>();

            int left = 0;
            String[] tokens1;
            if (z2.contains("=")) {
                String[] t = z2.split("=");
                left = 5;
                tokens.add(t[0].trim());
                tokens.add(t[1].trim());
            } else {
                tokens1 = z2.split(" ");
                if (tokens1.length == 1) {
                    if (!tokens1[0].trim().equals("while")) {
                        //String[] ret = {tokens1[0], tokens};
                        left = 4;
                    }
                    else {
                        left = 6;
                    }
                    tokens.add(tokens1[0]);

                } else if (tokens1.length == 2) {
                    left = 1;
                    tokens.add(tokens1[0]);
                    tokens.add(tokens1[1]);
                } else {
                    throw new Exception("Syntax error with " + y);
                }
            }
            Matcher m = p.matcher(y);

            if (m.find()) {
                String z = m.group();
                tokens1 = z.split("\\(\\s*|\\s*\\)|\\s*,\\s*|\\s+");
                if (tokens1.length > 1) {
                    for (int i = 1; i < tokens1.length; i++) tokens.add(tokens1[i]);
                }
            } else
                throw new Exception("Syntax error with " + y);
            String[] ret = new String[tokens.size()];
            return new Pair<>(left, tokens.toArray(ret));

        } else {
            String[] tokens = y.split("\\s+");
            if (tokens.length == 3) {
                for (int i = 0; i < 3; i++) tokens[i] = tokens[i].trim();
                return new Pair<>(3, tokens);
            } else if (tokens.length == 2) {
                for (int i = 0; i < 2; i++) tokens[i] = tokens[i].trim();
                return new Pair<>(2, tokens);
            } else {
                throw new Exception("Syntax error with " + y);
            }
        }

    }
    /*
    public static void test(String y) {
        Pattern p = Pattern.compile("\\([\\w]*\\s*[\\w]*(,\\s*[\\w]*\\s*[\\w]*)?\\)");
        Matcher m = p.matcher(y);
        if (m.find()) {
            String z = m.group();
            String[] tokens = z.split("\\(\\s*|\\s*\\)|\\s*,\\s*|\\s+");
            System.out.println(tokens.length);
            for (String line : tokens) {
                System.out.println(line);
            }
        }
        //String[] x = y.split("\\([\\w]*\\s*[\\w]*(,\\s*[\\w]*\\s*[\\w]*)?\\)");
        //for(String z: x) System.out.println(z);
    }
    */

    public static void main(String[] args) throws Exception {
        List<String> lines = parseIntoLines("int g; void f() {    { int     y; }      int x;  while( x == 0  ) { int x; int y; x = -231; }   int y;         x = 7;          y = 5;      g(x, y); } void g(int x, int y) {    { int z; }      int x; }");
        //List<String> lines = parseIntoLines("while(x > 22)");
        //List<String> lines = parseIntoLines("void f(int x)");
        for (String line : lines) {
            if (line.equals("{") | line.equals("}")) continue;
            Pair<Integer, String[]> p = classifyLine(line);
            System.out.print(p.getLeft() + ":  ");
            String[] parsed = p.getRight();
            for (String x : parsed)
                System.out.print(x + " ");
            System.out.println();
        }

        /*String[] testcases = {"void f   (int     x, int y)",
        "void   f   (abc)",
        "void    f()",
                "  void   g  (  )",
        "  void   f  (abc, def   )",
        "void f   (abc,      def)"};
        */
    }

}
