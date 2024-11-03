public class CombinedErrors {

    private int unusedVariable = 41; // PMD: This variable is declared but never used.

    public CombinedErrors() {
        // Intentionally left blank.
    }

    public int getNameLength() {
        String name = null; 
        return name.length();  // FindBugs: Potential null dereference.
    }

    public String[] getArray() {
    return new String[] {};
    }

    public static void main(String[] args){
        System.out.println("Hello, World!"); // Checkstyle: Missing space after 'args'.

        CombinedErrors errors = new CombinedErrors();
        System.out.println(errors.getNameLength());
    }

    public void potentialNullDereference(String input) {
        String local = null;
        if (input.isEmpty()) {
            local = "Test";
        }
        System.out.println(local.length()); // potential null dereference here
    }
  

}
