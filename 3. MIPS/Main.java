import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class Main {
    static String file = "assembly.txt";
    static int new_label_count = 1;
    static HashMap<String, Integer> mp = new HashMap<>();

    public static void main(String[] args) {
        int line_count = 0;
        String in = "";
        MIPS mips = new MIPS();

        changeForPushPop();
        scanForLabels();
        optimizeJump();
        scanForLabels();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while(true){
                in = reader.readLine();

                if(in == null) break;

                mips.generateMIPS(in, line_count);

                line_count++;
            }
            mips.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void changeForPushPop(){
        String in = "";
        String out = "";

        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while(true){
                in = reader.readLine();

                if(in == null) break;

                String[] arr = in.split(" ");

                if(arr[0].equals("push")){
                    out += "sw " + arr[1] + ", 0($sp)\n";
                    out += "addi $sp, $sp, -1\n";
                }else if(arr[0].equals("pop")){
                    out += "addi $sp, $sp, 1\n";
                    out += "lw " + arr[1] + ", 0($sp)\n";
                }else{
                    out += in;
                    out += "\n";
                }
            }

            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(out);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void scanForLabels(){
        int line_count = 0;
        String in = "";

        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while(true){
                in = reader.readLine();

                if(in == null) break;

                String[] arr = in.split(" ");

                if(arr[0].charAt(arr[0].length()-1) == ':'){
                    String label = arr[0].substring(0, arr[0].length()-1);
                    mp.put(label, line_count);
                }

                line_count++;
            }

            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void optimizeJump(){
        int line_count = 0;
        String out = "";
        String in = "";

        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while(true){
                in = reader.readLine();

                if(in == null) break;

                String[] arr = in.split(" ");

                if(arr[0].equals("beq") || (arr[0].startsWith("label") && arr[1].equals("beq"))){
                    String label = (arr[0].equals("beq"))? arr[3] : arr[4];
                    int beqIdx = (arr[0].equals("beq"))? 0 : 1;
                    int label_line = mp.get(label);
                    int jump = label_line-line_count-1;
                    if((jump < -8) || (jump > 7)){
                        if(arr[1].equals("beq")) out += arr[0] + " ";
                        out += "bneq " + arr[beqIdx+1] + " " + arr[beqIdx+2] + " " + "newLabel" + new_label_count + "\n";
                        out += "j " + arr[beqIdx+3] + "\n";
                        out += "newLabel" + new_label_count + ":\n";
                        new_label_count++;
                    }else{
                        out += in + "\n";
                    }
                }else if(arr[0].equals("bneq") || (arr[0].startsWith("label") && arr[1].equals("bneq"))){
                    String label = (arr[0].equals("bneq"))? arr[3] : arr[4];
                    int bneqIdx = (arr[0].equals("bneq"))? 0 : 1;
                    int label_line = mp.get(label);
                    int jump = label_line-line_count-1;
                    if((jump < -8) || (jump > 7)){
                        if(arr[1].equals("bneq")) out += arr[0] + " ";
                        out += "beq " + arr[bneqIdx+1] + " " + arr[bneqIdx+2] + " " + "newLabel" + new_label_count + "\n";
                        out += "j " + arr[bneqIdx+3] + "\n";
                        out += "newLabel" + new_label_count + ":\n";
                        new_label_count++;
                    }else{
                        out += in + "\n";
                    }
                }else{
                    out += in;
                    out += "\n";
                }

                line_count++;
            }

            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(out);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
