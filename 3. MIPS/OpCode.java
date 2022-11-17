public enum OpCode {
    or("0"),
    add("1"),
    sub("2"),
    bneq("3"),
    beq("4"),
    sw("5"),
    j("6"),
    subi("7"),
    lw("8"),
    and("9"),
    srl("a"),
    addi("b"),
    ori("c"),
    nor("d"),
    andi("e"),
    sll("f");

    private String code;

    OpCode(String s) {
        this.code = s;
    }

    public String getCode() { return this.code; }
}
