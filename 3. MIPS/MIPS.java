import java.io.BufferedWriter;
import java.io.FileWriter;

public class MIPS {
    String file = "mips.txt";
    BufferedWriter writer;

    public MIPS(){
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("v2.0 raw\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateMIPS(String code, int line_count){
        String[] arr = code.split(" ");

        if(arr.length != 2) RSIFormat(arr, line_count);
        else JFormat(arr, line_count);
    }

    /**
     *  R -> add $t1, $t2, $zero
     *  S -> sll $t4, $t3, 3
     *  I -> sw $t0, 5($t2), addi $t3, $zero, $t0
     * */
    public void RSIFormat(String[] arr, int line_count){
        int idx = 0;
        String opcode = "";
        String reg1 = "";
        String reg2 = "";
        String fourth = "";

        if(arr[0].charAt(arr[0].length()-1) == ':') idx = 1;

        if(arr.length <= idx){

        }
        else if(arr[idx].equals("add")||arr[idx].equals("sub")||arr[idx].equals("and")||arr[idx].equals("or")||arr[idx].equals("nor")){
            opcode = OpCode.valueOf(arr[idx]).getCode();
            reg1 = RegCode.valueOf(getReg(arr[idx+2])).getCode();
            reg2 = RegCode.valueOf(getReg(arr[idx+3])).getCode();
            fourth = RegCode.valueOf(getReg(arr[idx+1])).getCode();
        }else if(arr[idx].equals("bneq")||arr[idx].equals("beq")){
            opcode = OpCode.valueOf(arr[idx]).getCode();
            reg1 = RegCode.valueOf(getReg(arr[idx+1])).getCode();
            reg2 = RegCode.valueOf(getReg(arr[idx+2])).getCode();
            fourth = Integer.toHexString(Main.mp.get(arr[idx+3])-line_count-1);
            if (fourth.length()>1)
                fourth=fourth.charAt(fourth.length()-1)+"";
        }else if(arr[idx].equals("addi")||arr[idx].equals("subi")||arr[idx].equals("andi")||arr[idx].equals("ori")||arr[idx].equals("sll")||arr[idx].equals("srl")){
            opcode = OpCode.valueOf(arr[idx]).getCode();
            reg1 = RegCode.valueOf(getReg(arr[idx+2])).getCode();
            reg2 = RegCode.valueOf(getReg(arr[idx+1])).getCode();
            fourth = Integer.toHexString(Integer.parseInt(arr[idx+3]));
            if (fourth.length()>1)
                fourth=fourth.charAt(fourth.length()-1)+"";
        }else{
            opcode = OpCode.valueOf(arr[idx]).getCode();
            reg2 = RegCode.valueOf(getReg(arr[idx+1])).getCode();
            String[] ar = arr[idx+2].split("\\s*[()]\\s*");
            reg1 = RegCode.valueOf(getReg(ar[1])).getCode();
            fourth = Integer.toHexString(Integer.parseInt(ar[0]));
        }

        try {
            writer.write(opcode + reg1 + reg2 + fourth + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void JFormat(String[] arr, int line_count){
        String opcode = OpCode.valueOf(arr[0]).getCode();
        String address = "";
        int n = Main.mp.get(arr[1]);
        if(n > 15) address = Integer.toHexString(n);
        else address = "0" + Integer.toHexString(n);
        String fourth = Integer.toHexString(0);

        try{
            writer.write(opcode + address + fourth + "\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getReg(String op){
        if(op.charAt(op.length()-1) != ',') return op;
        return op.substring(0, op.length()-1);
    }

    public String intToBinary(String num){
        String fourth = Integer.toString(Integer.parseInt(num), 2);
        if(fourth.length() != 4){
            String prefix = new String(new char[4-fourth.length()]).replace("\0", "0");
            fourth = prefix + fourth;
        }
        System.out.println(fourth);

        return fourth;
    }

    public void close() {
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
