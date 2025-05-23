import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Main {
    public static Scanner sc = new Scanner(System.in);
    public static Map<Character, String> morseMap = new HashMap<>() {{
        // Letters A-Z
        put('A', ".-");    put('B', "-...");  put('C', "-.-.");
        put('D', "-..");   put('E', ".");     put('F', "..-.");
        put('G', "--.");   put('H', "....");  put('I', "..");
        put('J', ".---");  put('K', "-.-");   put('L', ".-..");
        put('M', "--");    put('N', "-.");    put('O', "---");
        put('P', ".--.");  put('Q', "--.-");  put('R', ".-.");
        put('S', "...");   put('T', "-");     put('U', "..-");
        put('V', "...-");  put('W', ".--");   put('X', "-..-");
        put('Y', "-.--");  put('Z', "--..");

        // Numbers 0-9
        put('0', "-----"); put('1', ".----"); put('2', "..---");
        put('3', "...--"); put('4', "....-"); put('5', ".....");
        put('6', "-...."); put('7', "--..."); put('8', "---..");
        put('9', "----.");

        // Special Characters
        put('.', ".-.-.-");  put(',', "--..--");  put('?', "..--..");
        put('\'', ".----."); put('!', "-.-.--");  put('/', "-..-.");
        put('(', "-.--.");   put(')', "-.--.-");  put('&', ".-...");
        put(':', "---...");  put(';', "-.-.-.");  put('=', "-...-");
        put('+', ".-.-.");   put('-', "-....-");  put('_', "..--.-");
        put('"', ".-..-.");  put('$', "...-..-"); put('@', ".--.-.");
    }};

    // Mapping letters to Morse code


    //---------------//
    // Main Function //
    //---------------//

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n******************************");
            System.out.println("File Encryption and Decryption");
            System.out.println("******************************\n");

            System.out.println("Choose one of the following options:");
            System.out.println("1.Text");
            System.out.println("2.File");
            System.out.println("3.Exit");
            int choice = 0;
            try{
                choice = sc.nextInt();
                if (choice != 1 && choice != 2 && choice != 3) {
                    System.out.println("Please enter a valid option");
                    continue;
                }
                if (choice == 3) {
                    System.out.println("Exiting...");
                    return;
                }
                typesCipher(choice);
            }
            catch (Exception e) {
                System.out.println("Please enter a valid option");
                sc.nextLine();
            }
        }
    }

    //-------------------------------------//
    // Functions:

    //Menu
    public static void displayMenu() {
        System.out.println("Choose one of the following options:");
        System.out.println("1.Ceaser Cipher.");
        System.out.println("2.AES Cipher.");
        System.out.println("3.Atbash Cipher.");
        System.out.println("4.Morse code Cipher.");
        System.out.println("Your choice:");
    }

    // Valid
    public static String validcChoice(String choice) {
        String regex = "^[1-4]$";
        Pattern ptrn = Pattern.compile(regex);

        while (!ptrn.matcher(choice).matches()) {
            System.out.print("Please enter a number between 1 and 4: ");
            sc.nextLine();
            choice = sc.next();
        }
        return choice;
    }

    // // // // // // // // // // //
    // Cipher Functions://
    // // // // // // // // // // //

    // Caesar Cipher -----------------------------------------------//
    public static String caesarCipher(int key, String text, boolean cipherType) {
        StringBuilder cipher = new StringBuilder();
        if(cipherType) {
            for (int i = 0; i < text.length(); i++) {
                cipher.append((char) (text.charAt(i) + key));
            }
        }
        else {
            for (int i = 0; i < text.length(); i++) {
                cipher.append((char) (text.charAt(i) - key));
            }
        }
        return cipher.toString();
    }

    // ---------------------------------------------------------------//

    public static String aesCipher(String key, String text, boolean cipherType) throws Exception {
        String return_string = "";
        if (cipherType) {
            // Create a SecretKeySpec object using the provided key and the AES algorithm
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");

            // Get an instance of the Cipher class for AES encryption
            Cipher cipher = Cipher.getInstance("AES");

            // Initialize the Cipher in ENCRYPT_MODE with the secret key
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            // Perform the encryption on the input text and get the encrypted bytes
            byte[] encryptedBytes = cipher.doFinal(text.getBytes());

            // Encode the encrypted bytes to a Base64 string for easy storage/transmission
            return_string = Base64.getEncoder().encodeToString(encryptedBytes);
        }
        else {
            // Create a SecretKeySpec object using the provided key and the AES algorithm
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");

            // Get an instance of the Cipher class for AES decryption
            Cipher cipher = Cipher.getInstance("AES");

            // Initialize the Cipher in DECRYPT_MODE with the secret key
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            // Decode the Base64 encoded input text to get the encrypted bytes
            byte[] encryptedBytes = Base64.getDecoder().decode(text);

            // Perform the decryption on the encrypted bytes and get the original bytes
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Convert the decrypted bytes back to a string
            return_string = new String(decryptedBytes);
        }
        return return_string;
    }

    public static String atBashCipher(String text) {
        String alphabetsLower = "abcdefghijklmnopqrstuvwxyz";
        String alphabetsUpper = alphabetsLower.toUpperCase();
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            if(Character.isLetter(text.charAt(i))) {
                if(Character.isUpperCase(text.charAt(i))) {
                    result += alphabetsUpper.charAt('Z' - (text.charAt(i) - 'A') - 'A');
                }
                else {
                    result += alphabetsLower.charAt('z' - (text.charAt(i) - 'a') - 'a');
                }
            }
            else {
                result += text.charAt(i);
            }
        }
        return result;
    }

    public static char morseToChar(String morse) {
        for (Map.Entry<Character, String> entry : morseMap.entrySet()) {
            if (entry.getValue().equals(morse)) {
                morse = entry.getKey().toString();
                break;  // Stop once we find the first match
            }
        }
        return morse.charAt(0);
    }

    public static String morseCode(String text, boolean cipherType) {
        String result = "";
        if(cipherType) {
            text = text.toUpperCase();
            int space = 0;
            for (int i = 0; i < text.length(); i++) {
                if (!Character.isSpaceChar(text.charAt(i))) {
                    result += morseMap.get(text.charAt(i)) + ' ';
                    space = 0;
                } else {
                    space++;
                    if (space <= 1)
                        result += "   ";
                }
            }
        }
        else {
            int space = 0;
            String word = "";
            for (int i = 0; i < text.length(); i++) {
                if (!Character.isSpaceChar(text.charAt(i))) {
                    if(space <= 2 && space != 0)
                        space = 0;
                    if(space >= 3){
                        result += ' ';
                        space = 0;
                    }
                    word += text.charAt(i);
                }
                else{
                    space++;
                    if(!word.isEmpty() && space != 0){
                        result += morseToChar(word); //func;
                        word = "";
                    }
                }
            }
            if(!word.isEmpty()){
                result += morseToChar(word);
            }
        }
        return result;
    }

    // ---------------------------------------------------------------//

    // // // // // // // // // // //
    // File Functions://
    // // // // // // // // // // //

    // Fetch file data
    public static String[] fetchFileData(File file) {
        String[] lines = new String[0];
        try {
            Scanner scanner = new Scanner(file);

            List<String> linesList = new ArrayList<String>();
            while (scanner.hasNextLine()) {
                linesList.add(scanner.nextLine());
            }
            lines = linesList.toArray(new String[0]);
            return lines;
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }
        return lines;
    }

    public static void caesarCipherFile(int key, File file, boolean cipherType) {
        String[] text = fetchFileData(file);
        List<String> result = new ArrayList<>();
        for (String line : text) {
            result.add(caesarCipher(key, line, cipherType));
        }
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            for (String line : result) {
                fileWriter.write(line);
                fileWriter.newLine();
            }
            fileWriter.close();
            System.out.println("File modified successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void aesCipherFile(String key, File file, boolean cipherType) throws Exception {
        String[] text = fetchFileData(file);
        List<String> result = new ArrayList<>();
        for (String line : text) {
            result.add(aesCipher(key, line, cipherType));
        }
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            for (String line : result) {
                fileWriter.write(line);
                fileWriter.newLine();
            }
            fileWriter.close();
            System.out.println("File modified successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void atbashCipherFile(File file) {
        String[] text = fetchFileData(file);
        List<String> result = new ArrayList<>();
        for (String line : text) {
            result.add(atBashCipher(line));
        }
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            for (String line : result) {
                fileWriter.write(line);
                fileWriter.newLine();
            }
            fileWriter.close();
            System.out.println("File modified successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void morseCodeFile(File file, boolean cipherType) {
        String[] text = fetchFileData(file);
        List<String> result = new ArrayList<>();
        for (String line : text) {
            result.add(morseCode(line, cipherType));
        }
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            for (String line : result) {
                fileWriter.write(line.toLowerCase());
                fileWriter.newLine();
            }
            fileWriter.close();
            System.out.println("File modified successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------//
    // Cipher Types for Text //
    public static void typesCipher(int value) {
        displayMenu();
        String choice = sc.next();
        choice = validcChoice(choice);
        System.out.println("(1) Encrypt or (2) Decrypt:");
        boolean cipherType;
        while (true) {
            char ch = sc.next().charAt(0);
            if (ch == '1') {
                cipherType = true;
                break;
            } else if (ch == '2') {
                cipherType = false;
                break;
            } else {
                System.out.println("Please enter a valid option");
                System.out.println("(1) Encrypt or (2) Decrypt:");
            }
        }

        // TEXT ------------------->>
        if (value == 1) {
            System.out.println("Enter the text:");
            sc.nextLine();
            String text = sc.nextLine();

            switch (choice) {
                case "1": {
                    System.out.println("Enter the number key for the cipher:");
                    int key = sc.nextInt();
                    System.out.println("Text: " + caesarCipher(key, text, cipherType));
                    break;
                }
                case "2": {
                    System.out.println("Enter the key for AES cipher:");
                    String scrtKey = sc.nextLine();
                    while(scrtKey.length() != 16){
                        System.out.println("Please enter a valid key contain 16 characters (16 byte for each):");
                        scrtKey = sc.nextLine();
                    }
                    try {
                        System.out.println("Text: " + aesCipher(scrtKey, text, cipherType));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "3": {
                    System.out.println("Text: " + atBashCipher(text));
                    break;
                }
                case "4": {
                    System.out.println("Text: " + morseCode(text, cipherType).toLowerCase());
                    break;
                }
            }
        }

        // FILE  ------------------->>
        else if (value == 2) {
            sc.nextLine();
            System.out.println("Enter the file name:");
            File file = new File(sc.nextLine());
            if (file.exists()) {
                switch (choice) {
                    case "1": {
                        System.out.println("Enter the number key for the cipher:");
                        int key = sc.nextInt();
                        caesarCipherFile(key, file, cipherType);
                        break;
                    }
                    case "2": {
                        System.out.println("Enter the key for AES cipher contain 16 or 24 or 32 characters :");
                        String scrtKey = sc.nextLine();
                        while(scrtKey.length() != 16 && scrtKey.length() != 24 && scrtKey.length() != 32){
                            System.out.println("Please enter a valid key contain 16 characters (1 byte for each char):");
                            scrtKey = sc.nextLine();
                        }
                        try {
                            aesCipherFile(scrtKey, file, cipherType);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                    case "3": {
                        atbashCipherFile(file);
                        break;
                    }
                    case "4": {
                        morseCodeFile(file, cipherType);
                        break;
                    }
                }
            }
            else {
                System.out.println("File does not exist!");
                return;
            }
        }
    }
}