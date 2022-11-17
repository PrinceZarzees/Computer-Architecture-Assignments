public enum RegCode {
    $zero("f"),
    $t0("8"),
    $t1("9"),
    $t2("a"),
    $t3("b"),
    $t4("c"),
    $sp("d");

    private String code;

    RegCode(String s){
        this.code = s;
    }

    public String getCode() { return this.code; }
}
