import java.util.List;

/**
        *       There are six types of' lines': 1) Function declaration line
        *                                      2) Variable declaration line
        *                                      3) Variable assignment line
        *                                      4) Function call : h(x, y);
 *                                             5) Function call and assignment;
 *                                             6) The while loop
*/

/**
 * TODO: Type checking of assignment and function call.
 */
 public class Compiler {

    public static void compile(String code) throws Exception{
        List<String> lines = RegexParser.parseIntoLines(code);
        SymbolTable currentScope = new SymbolTable(null);
        boolean flag = false;
        for(String line : lines ) {
            if(line.equals("{")) {
                if(!flag)
                currentScope = currentScope.enter_scope();
                else
                    flag = false;
            }
            else if(line.equals("}")) {
                SymbolTable parentScope = currentScope.exit_scope();
                System.out.print(currentScope);
                System.out.println("-----------------------");
                currentScope = parentScope;
            }
            else {
                Pair<Integer, String[]> p = RegexParser.classifyLine(line);
                String[] codeLine = p.getRight();
                switch(p.getLeft()) {
                    case 1:
                        if(codeLine.length % 2 == 0) {
                            currentScope.insert(codeLine[1], Kind.getKind(codeLine[0]), Type.getType("fun"));
                            flag = true;
                            currentScope = currentScope.enter_scope();
                            switch(codeLine.length) {
                                case 6: currentScope.insert(codeLine[5], Kind.getKind(codeLine[4]), Type.getType("par"));
                                case 4: currentScope.insert(codeLine[3], Kind.getKind(codeLine[2]), Type.getType("par")); break;
                                case 2: break;
                                default:
                                    throw new Exception("Line type 1. codeLine length isn't correct. Can have a" +
                                            " Maximum of two arguments");
                            }
                        }
                        else {
                            throw new Exception("Parser couldn't parse");
                        }
                        break;
                    case 2:
                        // Check if the variable is already defined
                        currentScope.insert(codeLine[1], Kind.getKind(codeLine[0]), Type.getType("var"));
                        break;
                    case 3:
                        // Check if the variable is already defined
                        currentScope.lookup(codeLine[0]);
                        break;
                    case 5:
                    case 4:
                        for(int i =0; i < codeLine.length; i++)
                            currentScope.lookup(codeLine[i]);
                        break;
                    case 6:
                        switch(codeLine.length) {
                            case 4:
                            case 2:
                                currentScope.lookup(codeLine[1]);
                                break;
                            default:
                                throw new Exception("Error in while statement");
                        }
                    default:
                        break;
                }
            }
        }
        SymbolTable parentScope = currentScope.exit_scope();
        System.out.print(currentScope);
        System.out.println("-----------------------");
        currentScope = parentScope;
        assert currentScope == null;
    }

    public static void main(String[] args) throws Exception{
        Compiler.compile(" int g; int c; void f() {    { int     yolester; }  while( c > 5) { int x; x = 2; int c; c = 212; } {int pome; pome = 9876;}    int x;          int y;         x = 7;          y = 5;  } void h(int x, int y) {    { int z; }      int mn; } int hfd; hfd = h(g,c); ");
    }
}
