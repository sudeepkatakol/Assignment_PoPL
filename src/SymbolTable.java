import java.util.HashMap;
import java.util.Formatter;
import java.util.Map;

/**
 * A variable should not be declared more than once in the same scope.
 * A variable should not be used before being declared.
 * The type of the left-hand side of an assignment should match the type of the right-hand side.
 * Methods should be called with the right number and types of arguments
 **/
public class SymbolTable {

    private HashMap<String, Pair<Type, Kind>> currentScope;
    //private List<SymbolTable> childScopes;
    private SymbolTable parentScope;

    /*inserts an entry for x in the current scope if it is already not defined in
    it. Throws an error if the variable name is already present in the most recent scope.*/
    public void insert(String name, Kind kind, Type type) throws Exception {
        if (currentScope.containsKey(name)) throw new Exception("Identifier already present in currentscope");
        try {
            currentScope.put(name, new Pair<>(type, kind));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public SymbolTable(SymbolTable parentScope) {
        this.parentScope = parentScope;
        //this.childScopes = new ArrayList<>();
        this.currentScope = new HashMap<>();
    }

    /*returns the most recent definition of name by searching the tree from leaf to root. If no match is found it throws an error.*/
    public SymbolTable lookup(String name) throws Exception {


        if (currentScope.containsKey(name)) {
            return this;
        } else if (parentScope != null) {
            return parentScope.lookup(name);
        } else throw new Exception("Identifer not found in any scope!");

    }

    /* Generates a new level of nesting by creating a symbol table for the new scope. */
    public SymbolTable enter_scope() {
        SymbolTable child = new SymbolTable(this);
        //childScopes.add(child);
        return child;
    }

    /*remove symbol table entries for the current scope and move back to the enclosing
      scope in the symbol table tree. */
    public SymbolTable exit_scope() {
        //parentScope.killChild(this);
        return parentScope;
    }

    /*public List<SymbolTable> getChildScopes() {
        return this.childScopes;
    }
    */
    /* public void killChild(SymbolTable child) {
        this.childScopes.remove(child);
    }
    */
    @Override
    public String toString() {
        StringBuilder table = new StringBuilder();
        Formatter formatter = new Formatter(table);
        for (Map.Entry<String, Pair<Type, Kind>> p : currentScope.entrySet()) {
            formatter.format("%1$10s %2$4s %3$6s \n", p.getKey(), p.getValue().getLeft(), p.getValue().getRight());
        }
        table.append("\n");
        if (parentScope == null)
            return table.toString();
        return table.toString() + parentScope.toString();
    }
}